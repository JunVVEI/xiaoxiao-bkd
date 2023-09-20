package com.xiaoxiao.user.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiaoxiao.baseservice.rpc.model.response.CaptchaResponse;
import com.xiaoxiao.baseservice.rpc.model.response.SendMailResponse;
import com.xiaoxiao.baseservice.rpc.model.resquest.CaptchaRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.cache.RedisService;
import com.xiaoxiao.common.constant.XiaoXiaoConst;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.user.config.WeChatConfig;
import com.xiaoxiao.user.mapper.UserMapper;
import com.xiaoxiao.user.model.dto.LoginDTO;
import com.xiaoxiao.user.model.dto.UserRegistrationDTO;
import com.xiaoxiao.user.model.dto.WeChatLoginDTO;
import com.xiaoxiao.user.model.dto.WeChatSession;
import com.xiaoxiao.user.model.entity.User;
import com.xiaoxiao.user.service.IdentityService;
import com.xiaoxiao.user.service.RpcService;
import com.xiaoxiao.user.service.UserService;
import com.xiaoxiao.user.utils.JwtUtil;
import com.xiaoxiao.user.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjw
 * @since 2022-11-19 11:11:38
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     * JWT过期时间 1天
     */
    // private final static Long JWT_EXPIRE = 30 * 24 * 60 * 60 * 1000L;
    private final static Long JWT_VALIDITY_DURATION = 24 * 60 * 60 * 1000L;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;

    @Resource
    private RpcService rpcService;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WeChatConfig weChatConfig;

    @Resource
    private IdentityService identityService;

    @Override
    public boolean login(LoginDTO loginDTO, HttpServletResponse response) {
        LoginDTO.checkIsValid(loginDTO);

        // 查询用户信息
        User validUser = userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("user_name", loginDTO.getUserName())
                        .eq("is_delete", XiaoXiaoConstEnum.UN_DELETE)
        );
        // 密码验证
        AssertUtil.isTrue(
                ObjectUtil.isNotEmpty(validUser) && PasswordUtils.matches(loginDTO.getPassword(), validUser.getPassword()),
                "用户名或密码错误"
        );

        String jwt = generateJwt(validUser);

        // 返回前端
        response.setHeader("Authorization", XiaoXiaoConst.BEARER + jwt);
        return true;
    }

    @Override
    public boolean logout() {
        CommonUser commonUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(commonUser);
        renewUserVersion(commonUser.getUid(), commonUser.getVersion());
        return true;
    }

    private CommonUser prepareCommonUser(User user) {
        return CommonUser.builder()
                .uid(user.getId())
                .openid(user.getOpenid())
                .unionid(user.getUnionid())
                .username(user.getUserName())
                .avatar(user.getAvatar())
                .version(user.getVersion())
                .build();
    }

    @Override
    public void renewUserVersion(Long uid, String version) {
        // TODO: 加锁
        try {
            User user = userMapper.selectById(uid);
            AssertUtil.isTrue(
                    ObjectUtil.equals(user.getVersion(), version),
                    StatusCode.BIZ_ERROR
            );

            String newVersion = UUID.randomUUID().toString().replaceAll("-", "");
            int res = userMapper.update(
                    null,
                    new UpdateWrapper<User>()
                            .eq("id", uid)
                            .set("version", newVersion)
            );
            AssertUtil.isTrue(0 < res, StatusCode.BIZ_ERROR);

            // TODO:后面规范化
            Map<String, String> map = new HashMap<>(8);
            map.put("version", newVersion);

            redisService.set(
                    XiaoXiaoConst.COMMON_USER_BLACKLIST + uid,
                    JSONUtil.toJsonStr(map),
                    JWT_VALIDITY_DURATION,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            log.error("更新账号版本异常 {} {}", uid, version, e);
            throw e;
        }
    }

    @Override
    public String sendEmailCode(User user) {
        // 检查是否已注册
        User selectEmail = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail, user.getEmail()));
        AssertUtil.isTrue(ObjectUtil.isNull(selectEmail), "该邮箱已注册账号");
        // 获取验证码
        CaptchaResponse captchaResponse = rpcService.captchaCode(user.getEmail());
        String verCode = captchaResponse.getVerCode();
        // 设置发送邮件的内容
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setTargetMail(user.getEmail());
        sendMailRequest.setTitle("用户注册");
        sendMailRequest.setMsg("验证码：" + verCode);
        SendMailResponse sendMailResponse = rpcService.sendMail(sendMailRequest);
        if (!sendMailResponse.getIsSuccess()) {
            throw new ApiException(StatusCode.BIZ_ERROR);
        }
        return captchaResponse.getKey();
    }

    @Override
    public boolean register(UserRegistrationDTO userRegistrationDTO) {
        UserRegistrationDTO.checkIsValid(userRegistrationDTO);

        // 检查是否已注册
        User selectEmail = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getEmail, userRegistrationDTO.getEmail()));
        AssertUtil.isTrue(ObjectUtil.isNull(selectEmail), "该邮箱已注册账号");
        User selectUserName = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUserName, userRegistrationDTO.getUserName()));
        AssertUtil.isTrue(ObjectUtil.isNull(selectUserName), "该用户名已被注册");

        // 校验验证码
        CaptchaRequest captchaRequest = new CaptchaRequest();
        captchaRequest.setKey(userRegistrationDTO.getEmail());
        captchaRequest.setUserCode(userRegistrationDTO.getUserCode());
        CaptchaResponse verified = rpcService.verify(captchaRequest);
        if (!verified.isSuccess()) {
            return false;
        }
        User user = UserRegistrationDTO.prepareXiaoXiaoUser(userRegistrationDTO);
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean weChatLogin(WeChatLoginDTO weChatLoginDTO, HttpServletResponse response) {
        AssertUtil.isTrue(StringUtils.isNotBlank(weChatLoginDTO.getCode()), StatusCode.VALIDATE_FAILED);
        WeChatSession weChatSession = getWeChatSession(weChatLoginDTO.getCode());
        String openid = weChatSession.getOpenid();
        if (StringUtils.isBlank(openid)) {
            log.error("获取openid失败 {}", weChatLoginDTO);
            return false;
        }
        AssertUtil.isTrue(StrUtil.isNotBlank(openid), StatusCode.VALIDATE_FAILED);

        User wxUser = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));
        if (wxUser == null) {
            return false;
        }

        try {
            // 兼容历史数据，如果用户之前没有保存unionid则保存
            if (StringUtils.isBlank(wxUser.getUnionid()) && StringUtils.isNoneBlank(weChatSession.getUnionid())) {
                wxUser.setUnionid(weChatSession.getUnionid());
                userMapper.updateById(wxUser);
            }
        } catch (Exception e) {
            log.error(" 兼容历史数据，如果用户之前没有保存unionid则保存异常", e);
        }

        // 生成JWT
        String jwt = generateJwt(wxUser);
        // 返回前端
        response.setHeader("Authorization", XiaoXiaoConst.BEARER + jwt);
        return true;
    }

    @Override
    public boolean weChatRegister(WeChatLoginDTO weChatLoginDTO, HttpServletResponse response) {
        AssertUtil.isTrue(StringUtils.isNotBlank(weChatLoginDTO.getCode()), StatusCode.VALIDATE_FAILED);
        AssertUtil.isTrue(StringUtils.isNotBlank(weChatLoginDTO.getUserName()), StatusCode.VALIDATE_FAILED);
        AssertUtil.isTrue(StringUtils.isNotBlank(weChatLoginDTO.getAvatar()), StatusCode.VALIDATE_FAILED);

        WeChatSession weChatSession = getWeChatSession(weChatLoginDTO.getCode());
        String openid = weChatSession.getOpenid();
        if (StringUtils.isBlank(openid)) {
            log.error("获取openid失败{}", weChatLoginDTO);
            return false;
        }
        AssertUtil.isTrue(StrUtil.isNotBlank(openid), StatusCode.VALIDATE_FAILED);

        User wxUser = userMapper.selectOne(new QueryWrapper<User>().eq("openid", openid));
        AssertUtil.isTrue(Objects.isNull(wxUser), "微信账户已注册");

        wxUser = new User();
        wxUser.setUserName(weChatLoginDTO.getUserName());
        wxUser.setAvatar(weChatLoginDTO.getAvatar());
        wxUser.setIsDelete(XiaoXiaoConstEnum.UN_DELETE.getVal());

        wxUser.setOpenid(openid);
        // 兼容逻辑，可能unionid为null
        wxUser.setUnionid(weChatSession.getUnionid());
        AssertUtil.isTrue(userMapper.insert(wxUser) == 1, "保存用户失败");

        // TODO： 生成默认的匿名, 后续需要改成异步
        identityService.addIdentity(wxUser.getId());

        // 生成JWT
        String jwt = generateJwt(wxUser);
        // 返回前端
        response.setHeader("Authorization", XiaoXiaoConst.BEARER + jwt);
        return true;
    }

    private WeChatSession getWeChatSession(String code) {
        String url = weChatConfig.getJscode2sessionUrl()
                + "?appid=" + weChatConfig.getAppid()
                + "&secret=" + weChatConfig.getSecret()
                + "&js_code=" + code
                + "&grant_type=authorization_code";
        String result = restTemplate.getForObject(url, String.class);
        log.info("获取WeChatSession结果：{}", result);
        WeChatSession weChatSession = JSONUtil.toBean(result, WeChatSession.class);
        if (StrUtil.isNotBlank(weChatSession.getErrcode())) {
            log.error("获取WeChatSession异常 {}", weChatSession);
        }
        return weChatSession;
    }

    private static final String JWT_EXPIRED = "jwt:expired:";

    @Override
    public boolean invalidateJWT(Long uid) {
        redisService.set(
                JWT_EXPIRED + uid,
                String.valueOf(System.currentTimeMillis()),
                JWT_VALIDITY_DURATION + 3 * 1000L,
                TimeUnit.MILLISECONDS
        );
        return true;
    }

    private String generateJwt(User user) {
        CommonUser userDTO = prepareCommonUser(user);
        // 生成JWT
        return JwtUtil.createJWT(
                String.valueOf(userDTO.getUid()),
                BeanUtil.beanToMap(userDTO),
                JWT_VALIDITY_DURATION
        );
    }

}
