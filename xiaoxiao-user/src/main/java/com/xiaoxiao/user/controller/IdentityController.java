package com.xiaoxiao.user.controller;

import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.user.model.dto.IdentityDTO;
import com.xiaoxiao.user.model.vo.IdentityVO;
import com.xiaoxiao.user.service.IdentityService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户身份表 前端控制器
 * </p>
 *
 * @author junwei
 * @since 2022-11-20 03:36:59
 */
@RestController
@RequestMapping("/user/identity")
public class IdentityController {
    @Resource
    private IdentityService identityService;

    /**
     * 获取用户匿名身份列表
     */
    @GetMapping("/listIdentity")
    @XiaoXiaoResponseBody
    public List<IdentityVO> listIdentity() {
        return identityService.listCurrentUserIdentities();
    }

    /**
     * 获取当前用户匿名信息
     */
    @GetMapping("/getCurrentUserIdentityInfo")
    @XiaoXiaoResponseBody
    public IdentityVO getCurrentUserIdentityInfo() {
        return identityService.getCurrentUserIdentityInfo();
    }


    /**
     * 根据identityId查看匿名身份的信息
     */
    @GetMapping("/getIdentity")
    @XiaoXiaoResponseBody
    public IdentityVO queryIdentity(Long identityId) {
        return identityService.getIdentity(identityId);
    }

    /**
     * 新增匿名身份
     */
    @PostMapping("/addIdentity")
    @XiaoXiaoResponseBody
    public IdentityVO add() {
        throw new ApiException("功能上不支持");
    }

    /**
     * 修改匿名身份
     */
    @PostMapping("/updateIdentity")
    @XiaoXiaoResponseBody
    public boolean edit(@RequestBody IdentityDTO identityDTO) {
        return identityService.updateIdentity(identityDTO);
    }


    /**
     * 修改昵称
     */
    @PostMapping("/updateName")
    @XiaoXiaoResponseBody
    public boolean updateNickName(@RequestBody IdentityDTO identityDTO) {
        return identityService.updateName(identityDTO);
    }

    /**
     * 获取匿名身份头像库
     */
    @GetMapping("/getIdentityAvatarLibrary")
    @XiaoXiaoResponseBody
    public List<String> getIdentityAvatarLibrary() {
        return identityService.getIdentityAvatarLibrary();
    }
}

