package com.xiaoxiao.user.service.serviceImpl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.config.IdentityConfig;
import com.xiaoxiao.user.mapper.IdentityMapper;
import com.xiaoxiao.user.model.dto.IdentityDTO;
import com.xiaoxiao.user.model.dto.UserDTO;
import com.xiaoxiao.user.model.entity.Identity;
import com.xiaoxiao.user.model.vo.IdentityVO;
import com.xiaoxiao.user.service.FollowService;
import com.xiaoxiao.user.service.IdentityService;
import com.xiaoxiao.user.service.RpcService;
import com.xiaoxiao.user.utils.RandomNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户身份表 服务实现类
 * </p>
 *
 * @author junwei
 * @since 2022-11-20 03:36:59
 */
@Service
@Slf4j
public class IdentityServiceImpl implements IdentityService {

    @Resource
    private IdentityMapper identityMapper;

    @Resource
    private RpcService rpcService;

    @Resource
    private FollowService followService;

    @Resource
    private IdentityConfig identityConfig;

    private final Random random = new Random();

    @Override
    public List<IdentityVO> listCurrentUserIdentities() {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();
        List<Identity> identities = identityMapper.selectList(
                new LambdaQueryWrapper<Identity>()
                        .eq(Identity::getUid, uid)
                        .eq(Identity::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );

        return identities.stream().map(IdentityVO::prepareIdentityVo).collect(Collectors.toList());
    }

    @Override
    public IdentityVO getIdentity(Long identityId) {
        Identity identity = identityMapper.selectById(identityId);
        log.info("identity的信息为:{}", identity);
        return IdentityVO.prepareIdentityVo(identity);
    }

    @Override
    public Identity addIdentity(Long uid) {
        if (uid == null) {
            return null;
        }
        // 查询身份表查看该用户是否已经生成过匿名身份
        Identity one = identityMapper.selectOne(
                new LambdaQueryWrapper<Identity>().eq(Identity::getUid, uid)
        );

        if (Objects.nonNull(one)) {
            return one;
        }

        // 设置随机昵称和随机头像
        Identity identity = new Identity();
        identity.setUid(uid);
        identity.setName(this.getRandomName());
        identity.setAvatar(getRandomAvatar());

        identityMapper.insert(identity);
        return identity;
    }

    private String getRandomAvatar() {
        int i = random.nextInt(identityConfig.getIdentityAvatarLibrary().size());
        return identityConfig.getIdentityAvatarLibrary().get(i);
    }

    @Override
    public boolean updateIdentity(IdentityDTO identityDTO) {
        IdentityDTO.checkIsValid(identityDTO);
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        Identity identity = identityMapper.selectById(identityDTO.getIdentityId());
        if (Objects.nonNull(identity) && Objects.equals(identity.getUid(), currentUser.getUid())) {
            identity.setAvatar(identityDTO.getAvatar());
            return identityMapper.updateById(identity) != 0;
        }
        return false;
    }

    @Override
    public boolean updateName(IdentityDTO identityDTO) {
        IdentityDTO.checkIsValid(identityDTO);
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        //获取当前时间戳的毫秒数
        long now = System.currentTimeMillis();
        //获取上一次更新昵称的时间
        Identity one = identityMapper.selectOne(
                new LambdaQueryWrapper<Identity>()
                        .eq(Identity::getId, identityDTO.getIdentityId())
                        .eq(Identity::getUid, currentUser.getUid())
        );
        if (Objects.isNull(one.getChangeTime()) || (now - one.getChangeTime().getTime()) / (1000 * 60 * 60 * 24) >= 30) {
            if (this.checkNickNameUnique(identityDTO.getName())) {
                Identity identity = new Identity();
                identity.setId(identityDTO.getIdentityId());
                identity.setName(identityDTO.getName());
                identity.setChangeTime(new Timestamp(now));
                int count = identityMapper.updateById(identity);
                return count > 0;
            } else {
                log.info("id为 {} 的匿名身份所更改的昵称 {} 重复!", identityDTO.getIdentityId(), identityDTO.getName());
                return false;
            }
        }
        return false;
    }

    private String getRandomName() {
        String nickName;
        for (int i = 0; i < 10; ++i) {
            nickName = RandomNameUtil.randomName(true, 4);
            if (this.checkNickNameUnique(nickName)) {
                return nickName;
            }
        }
        throw new ApiException(StatusCode.BIZ_ERROR);
    }

    private boolean checkNickNameUnique(String nickName) {
        Long count;
        count = identityMapper.selectCount(
                new LambdaQueryWrapper<Identity>()
                        .eq(Identity::getName, nickName)
        );
        return count == 0;
    }

    @Override
    public IdentityVO getCurrentUserIdentityInfo() {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();
        List<Identity> identities = identityMapper.selectList(
                new LambdaQueryWrapper<Identity>()
                        .eq(Identity::getUid, uid)
                        .eq(Identity::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        Identity identity = CollectionUtil.getFirst(identities);
        if (identity == null) {
            identity = addIdentity(uid);
        }

        IdentityVO identityVO = new IdentityVO();
        identityVO.setIdentityId(identity.getId());
        identityVO.setUid(identity.getUid());
        identityVO.setName(identity.getName());
        identityVO.setAvatar(identity.getAvatar());
        identityVO.setPostCount(rpcService.getUserIdentityPostCount(identity.getId()));
        identityVO.setLikeCount(rpcService.getUserIdentityLikeCount(identity.getId()));

        UserDTO userDTO = new UserDTO();
        userDTO.setIdentityId(identity.getId());
        userDTO.setType(XiaoXiaoConstEnum.ANONYMOUS_IDENTITY.getVal());
        identityVO.setFollowerCount(Math.toIntExact(followService.getFollowerCount(userDTO)));

        return identityVO;
    }

    @Override
    public List<String> getIdentityAvatarLibrary() {
        return identityConfig.getIdentityAvatarLibrary();
    }
}
