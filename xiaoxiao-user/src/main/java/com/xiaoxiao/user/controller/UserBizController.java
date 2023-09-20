package com.xiaoxiao.user.controller;

import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.user.model.dto.UserDTO;
import com.xiaoxiao.user.model.vo.CurrentUserInfoVO;
import com.xiaoxiao.user.model.vo.FollowUserVO;
import com.xiaoxiao.user.model.vo.UserPostOptionVO;
import com.xiaoxiao.user.model.vo.UserPublicInfoVO;
import com.xiaoxiao.user.service.FollowService;
import com.xiaoxiao.user.service.UserBizService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户业务controller，区别于UserController
 * </p>
 *
 * @author Junwei
 * @since 2023/2/14
 */
@RestController
@RequestMapping("/user/biz")
public class UserBizController {

    @Resource
    private FollowService followService;

    @Resource
    private UserBizService userBizService;

    /**
     * 用户关注的人列表
     */
    @GetMapping("/getUserFollowing")
    @XiaoXiaoResponseBody
    public List<FollowUserVO> getUserFollowing(UserDTO userDTO) {
        return followService.getUserFollowing(userDTO);
    }

    /**
     * 关注用户的人列表,如果identityId为空则查询的是真实身份，反之则为匿名身份
     */
    @GetMapping("/getUserFollowers")
    @XiaoXiaoResponseBody
    public List<FollowUserVO> getUserFollowers(UserDTO userDTO) {
        return followService.getUserFollowers(userDTO);
    }

    /**
     * 新增关注
     */
    @PostMapping("/follow")
    @XiaoXiaoResponseBody
    public Boolean follow(@RequestBody UserDTO userDTO) {
        return followService.follow(userDTO);
    }

    /**
     * 取消关注
     */
    @PostMapping("/unfollow")
    @XiaoXiaoResponseBody
    public Boolean unfollow(@RequestBody UserDTO userDTO) {
        return followService.unfollow(userDTO);
    }

    /**
     * 查询用户的基础公开信息
     */
    @PostMapping("/getUserPublicInfo")
    @XiaoXiaoResponseBody
    public UserPublicInfoVO getUserPublicInfo(@RequestBody UserDTO userDTO) {
        return userBizService.getUserPublicInfo(userDTO);
    }

    /**
     * 获取当前用户的信息
     */
    @GetMapping("/getCurrentUserInfo")
    @XiaoXiaoResponseBody
    public CurrentUserInfoVO getCurrentUserInfo() {
        return userBizService.getCurrentUserInfo();
    }

    /**
     * 用户发布时的可使用身份选项
     */
    @PostMapping("/getUserPostOptions")
    @XiaoXiaoResponseBody
    public UserPostOptionVO getUserPostOptions() {
        return userBizService.getUserPostOptions();
    }

}
