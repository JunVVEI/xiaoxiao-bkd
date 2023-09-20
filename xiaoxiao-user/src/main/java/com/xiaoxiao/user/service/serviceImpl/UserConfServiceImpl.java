package com.xiaoxiao.user.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.mapper.UserConfMapper;
import com.xiaoxiao.user.model.dto.UserConfDTO;
import com.xiaoxiao.user.model.entity.UserConf;
import com.xiaoxiao.user.model.enums.UserConfEnum;
import com.xiaoxiao.user.model.vo.UserConfVO;
import com.xiaoxiao.user.service.UserConfService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chenjunwei
 * @since 2023-08-17 10:57:05
 */
@Service
public class UserConfServiceImpl implements UserConfService {

    @Resource
    private UserConfMapper userConfMapper;

    @Override
    public boolean setConf(UserConfDTO userConfDTO) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        UserConf userConf = userConfMapper.selectOne(
                new LambdaQueryWrapper<UserConf>()
                        .eq(UserConf::getUid, uid)
                        .eq(UserConf::getConfKey, userConfDTO.getKey())
        );

        if (userConf == null) {
            UserConf newUserConf = new UserConf();
            newUserConf.setUid(uid);
            newUserConf.setConfKey(userConfDTO.getKey());
            newUserConf.setConfValue(userConfDTO.getValue());
            newUserConf.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            userConfMapper.insert(newUserConf);
            return true;
        }

        userConf.setConfValue(userConf.getConfValue());
        userConf.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userConfMapper.updateById(userConf);
        return true;
    }

    @Override
    public boolean toggleConf(String key) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        UserConf userConf = userConfMapper.selectOne(
                new LambdaQueryWrapper<UserConf>()
                        .eq(UserConf::getUid, uid)
                        .eq(UserConf::getConfKey, key)
        );

        if (userConf == null) {
            userConf = new UserConf();
            userConf.setUid(uid);
            userConf.setConfKey(key);
            userConf.setConfValue(Objects.equals(UserConfEnum.getDefaultValue(key), "0") ? "1" : "0");
            userConf.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            userConfMapper.insert(userConf);

            return true;
        }

        userConf.setConfValue(Objects.equals(userConf.getConfValue(), "0") ? "1" : "0");
        userConf.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        userConfMapper.updateById(userConf);
        return true;
    }

    @Override
    public List<UserConfVO> getUserConf(Long uid) {
        List<UserConf> exixtConfList = userConfMapper.selectList(
                new LambdaQueryWrapper<UserConf>()
                        .eq(UserConf::getUid, uid)
        );
        Map<String, String> exixtConfMap = exixtConfList.stream()
                .collect(Collectors.toMap(UserConf::getConfKey, UserConf::getConfValue));

        List<String> keyList = UserConfEnum.getKeyList();

        ArrayList<UserConfVO> userConfVOList = new ArrayList<>();
        for (String key : keyList) {
            UserConfVO userConfVO = new UserConfVO();
            userConfVO.setKey(key);
            userConfVO.setShowContent(UserConfEnum.getShowContent(key));

            String value = exixtConfMap.getOrDefault(key, UserConfEnum.getDefaultValue(key));
            userConfVO.setValue(value);

            userConfVOList.add(userConfVO);
        }

        return userConfVOList;
    }

    @Override
    public List<Long> getActivateConfKeyUserIds(String key) {
        List<UserConf> confList = userConfMapper.selectList(
                new LambdaQueryWrapper<UserConf>()
                        .eq(UserConf::getConfKey, key)
                        .ne(UserConf::getConfValue, UserConfEnum.getDefaultValue(key))
        );

        return confList.stream().map(UserConf::getUid).collect(Collectors.toList());
    }
}
