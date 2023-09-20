package com.xiaoxiao.taskCourier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskCommentDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskCommentQueryDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskQueryDTO;
import com.xiaoxiao.taskCourier.model.enums.RewardTaskStatusEnum;
import com.xiaoxiao.taskCourier.model.vo.RewardTaskCommentVO;
import com.xiaoxiao.taskCourier.model.vo.RewardTaskVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author junwei
 * @since 2023-08-08 11:05:50
 */
public interface RewardTaskService {
    RewardTaskVO postTask(RewardTaskDTO rewardTaskDTO);

    IPage<RewardTaskVO> listQuery(RewardTaskQueryDTO rewardTaskQueryDTO);

    IPage<RewardTaskVO> myTasks(RewardTaskQueryDTO rewardTaskQueryDTO);

    RewardTaskVO getTask(Long taskId);

    Boolean updateStatus(Long taskId, RewardTaskStatusEnum rewardTaskStatusEnum);

    IPage<RewardTaskCommentVO> commentListQuery(RewardTaskCommentQueryDTO rewardTaskCommentQueryDTO);

    RewardTaskCommentVO addComment(RewardTaskCommentDTO rewardTaskCommentDTO);

    Boolean delComment(Long commentId);


}
