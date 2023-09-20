package com.xiaoxiao.user.controller;

import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.user.model.vo.UserConfVO;
import com.xiaoxiao.user.service.UserConfService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author chenjunwei
 * @since 2023-08-17 10:57:05
 */
@RestController
@RequestMapping("/user/userConf")
public class UserConfController {
    @Resource
    private UserConfService userConfService;

    /**
     * 根据设置key，切换设置的value状态
     */
    @PostMapping("/toggleConf")
    @XiaoXiaoResponseBody
    public boolean toggleConf(@RequestParam String key) {
        return userConfService.toggleConf(key);
    }

    /**
     * 获取用户设置列表
     */
    @GetMapping("/getUserConf")
    @XiaoXiaoResponseBody
    public List<UserConfVO> getUserConf() {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        return userConfService.getUserConf(currentUser.getUid());
    }
}

