package com.xiaoxiao.taskCourier.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskCommentDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskCommentQueryDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskQueryDTO;
import com.xiaoxiao.taskCourier.model.enums.RewardTaskStatusEnum;
import com.xiaoxiao.taskCourier.model.vo.RewardTaskCommentVO;
import com.xiaoxiao.taskCourier.model.vo.RewardTaskVO;
import com.xiaoxiao.taskCourier.service.RewardTaskCommentService;
import com.xiaoxiao.taskCourier.service.RewardTaskService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author junwei
 * @since 2023-08-08 11:05:50
 */
@RestController
@RequestMapping("/taskCourier/rewardTask")
public class RewardTaskController {
    @Resource
    private RewardTaskService rewardTaskService;

    @PostMapping("postTask")
    @XiaoXiaoResponseBody
    public RewardTaskVO postTask(@RequestBody RewardTaskDTO rewardTaskDTO) {
        return rewardTaskService.postTask(rewardTaskDTO);
    }

    @PostMapping("listQuery")
    @XiaoXiaoResponseBody
    public IPage<RewardTaskVO> listQuery(@RequestBody RewardTaskQueryDTO rewardTaskQueryDTO) {
        return rewardTaskService.listQuery(rewardTaskQueryDTO);
    }

    @PostMapping("myTasks")
    @XiaoXiaoResponseBody
    public IPage<RewardTaskVO> myTasks(@RequestBody RewardTaskQueryDTO rewardTaskQueryDTO) {
        return rewardTaskService.myTasks(rewardTaskQueryDTO);
    }

    @PostMapping("getTask")
    @XiaoXiaoResponseBody
    public RewardTaskVO getTask(Long taskId) {
        return rewardTaskService.getTask(taskId);
    }


    @PostMapping("delTask")
    @XiaoXiaoResponseBody
    public Boolean delTask(Long taskId) {
        return rewardTaskService.updateStatus(taskId, RewardTaskStatusEnum.DELETED);
    }

    @PostMapping("accept")
    @XiaoXiaoResponseBody
    public Boolean accept(Long taskId) {
        return rewardTaskService.updateStatus(taskId, RewardTaskStatusEnum.ORDER_ACCEPTED);
    }

    @PostMapping("unAccept")
    @XiaoXiaoResponseBody
    public Boolean unAccept(Long taskId) {
        return rewardTaskService.updateStatus(taskId, RewardTaskStatusEnum.WAITING_FOR_ACCEPTANCE);
    }

    @PostMapping("commentListQuery")
    @XiaoXiaoResponseBody
    public IPage<RewardTaskCommentVO> commentListQuery(
            @RequestBody RewardTaskCommentQueryDTO rewardTaskCommentQueryDTO
    ) {
        return rewardTaskService.commentListQuery(rewardTaskCommentQueryDTO);
    }

    @PostMapping("addComment")
    @XiaoXiaoResponseBody
    public RewardTaskCommentVO addComment(@RequestBody RewardTaskCommentDTO rewardTaskCommentDTO) {
        return rewardTaskService.addComment(rewardTaskCommentDTO);
    }

    @PostMapping("delComment")
    @XiaoXiaoResponseBody
    public Boolean delComment(Long commentId) {
        return rewardTaskService.delComment(commentId);
    }


}

