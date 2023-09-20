package com.xiaoxiao.taskCourier.service.serviceImpl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.taskCourier.config.TaskCourierConfig;
import com.xiaoxiao.taskCourier.mapper.RewardTaskCommentMapper;
import com.xiaoxiao.taskCourier.mapper.RewardTaskMapper;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskCommentDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskCommentQueryDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskDTO;
import com.xiaoxiao.taskCourier.model.dto.RewardTaskQueryDTO;
import com.xiaoxiao.taskCourier.model.entity.RewardTask;
import com.xiaoxiao.taskCourier.model.entity.RewardTaskComment;
import com.xiaoxiao.taskCourier.model.enums.RewardTaskStatusEnum;
import com.xiaoxiao.taskCourier.model.enums.RewardTaskTypeEnum;
import com.xiaoxiao.taskCourier.model.vo.RewardTaskCommentVO;
import com.xiaoxiao.taskCourier.model.vo.RewardTaskVO;
import com.xiaoxiao.taskCourier.service.RewardTaskService;
import com.xiaoxiao.taskCourier.service.RpcService;
import com.xiaoxiao.taskCourier.util.PageUtil;
import com.xiaoxiao.user.rpc.model.response.IdentityInfoResponse;
import com.xiaoxiao.user.rpc.model.response.UserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author junwei
 * @since 2023-08-08 11:05:50
 */
@Service
@Slf4j
public class RewardTaskServiceImpl implements RewardTaskService {

    @Resource
    private RewardTaskMapper rewardTaskMapper;

    @Resource
    private RewardTaskCommentMapper rewardTaskCommentMapper;

    @Resource
    private RpcService rpcService;

    @Override
    public RewardTaskVO postTask(RewardTaskDTO rewardTaskDTO) {
        RewardTaskDTO.checkIsValid(rewardTaskDTO);

        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        RewardTask rewardTask = new RewardTask();
        rewardTask.setUid(currentUser.getUid());
        rewardTask.setContent(rewardTaskDTO.getContent());
        rewardTask.setMediaPath(rewardTaskDTO.getMediaPath());
        rewardTask.setBounty(rewardTaskDTO.getBounty());
        rewardTask.setContact(rewardTaskDTO.getContact());
        rewardTask.setStatus(RewardTaskStatusEnum.WAITING_FOR_ACCEPTANCE.getStatus());
        rewardTask.setCreateTime(new Timestamp(System.currentTimeMillis()));
        rewardTask.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        rewardTask.setType(
                Objects.isNull(rewardTaskDTO.getType()) ? RewardTaskTypeEnum.REWARD_TASK.getType() : rewardTaskDTO.getType()
        );

        rewardTaskMapper.insert(rewardTask);
        CompletableFuture.runAsync(() -> syncToWxGroups(rewardTask));
        return prepareRewardTaskVO(rewardTask);
    }

    private static final String WX_BOT_SEND_MSG_API = "http://localhost:8590/wechat_bot/send_message";
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String SCHEMA_LINK_API = "https://api.weixin.qq.com/wxa/generatescheme";

    @Resource
    private TaskCourierConfig taskCourierConfig;

    public void syncToWxGroups(RewardTask rewardTask) {
        String link = getSchemaLink(rewardTask.getId().toString());
        String pattern = "";
        if (Objects.equals(rewardTask.getType(), RewardTaskTypeEnum.REWARD_TASK.getType())) {
            pattern = "赏金：%s元\n" +
                    "悬赏内容：%s\n" +
                    "链接：%s";
        }

        if (Objects.equals(rewardTask.getType(), RewardTaskTypeEnum.SECOND_HAND.getType())) {
            pattern = "售价：%s元\n" +
                    "商品详情：%s\n" +
                    "链接：%s";
        }

        StringBuilder simpleContent = new StringBuilder();
        for (int i = 0; i < 20 && i < rewardTask.getContent().length(); i++) {
            simpleContent.append(rewardTask.getContent().charAt(i));
        }
        simpleContent.append("...");
        String text = String.format(
                pattern,
                rewardTask.getBounty(),
                simpleContent,
                link
        );
        String[] groupList = taskCourierConfig.getGroups().split(",");
        sendToWxGroup(groupList, text);
    }

    private String getSchemaLink(String id) {
        String url = SCHEMA_LINK_API + "?access_token=" + rpcService.getMiniAppAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode requestBody = objectMapper.createObjectNode().set("jump_wxa", objectMapper.createObjectNode()
                .put("path", "/pages/help/index")
                .put("query", "taskId=" + id));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        JSON body = JSONUtil.parse(responseBody);
        String link = (String) body.getByPath("openlink");
        log.info("get schema link succeed {}", link);
        return link;
    }


    private void sendToWxGroup(String[] groupNames, String text) {
        String url = WX_BOT_SEND_MSG_API;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode groupNameArray = objectMapper.createArrayNode();
        for (String groupName : groupNames) {
            groupNameArray.add(groupName);
        }
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("object_type", 1);
        requestBody.set("group_name", groupNameArray);
        requestBody.put("text", text);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    @Override
    public IPage<RewardTaskVO> listQuery(RewardTaskQueryDTO rewardTaskQueryDTO) {
        Integer type = Objects.isNull(rewardTaskQueryDTO.getType()) ? RewardTaskTypeEnum.REWARD_TASK.getType() : rewardTaskQueryDTO.getType();

        Page<RewardTask> rewardTaskPage = rewardTaskMapper.selectPage(
                new Page<>(rewardTaskQueryDTO.getCurrentPage(), rewardTaskQueryDTO.getPageSize()),
                new LambdaQueryWrapper<RewardTask>()
                        .eq(RewardTask::getType, type)
                        .ne(RewardTask::getStatus, RewardTaskStatusEnum.DELETED.getStatus())
                        .orderByDesc(RewardTask::getCreateTime)
        );
        return PageUtil.convertPage(rewardTaskPage, prepareRewardTaskVO(rewardTaskPage.getRecords()));
    }

    @Override
    public IPage<RewardTaskVO> myTasks(RewardTaskQueryDTO rewardTaskQueryDTO) {
        Integer type = Objects.isNull(rewardTaskQueryDTO.getType()) ? RewardTaskTypeEnum.REWARD_TASK.getType() : rewardTaskQueryDTO.getType();

        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        Page<RewardTask> rewardTaskPage = rewardTaskMapper.selectPage(
                new Page<>(rewardTaskQueryDTO.getCurrentPage(), rewardTaskQueryDTO.getPageSize()),
                new LambdaQueryWrapper<RewardTask>()
                        .eq(RewardTask::getType, type)
                        .eq(RewardTask::getUid, currentUser.getUid())
                        .ne(RewardTask::getStatus, RewardTaskStatusEnum.DELETED.getStatus())
        );
        return PageUtil.convertPage(rewardTaskPage, prepareRewardTaskVO(rewardTaskPage.getRecords()));
    }

    @Override
    public RewardTaskVO getTask(Long taskId) {
        RewardTask rewardTask = rewardTaskMapper.selectById(taskId);
        if (!Objects.equals(rewardTask.getStatus(), RewardTaskStatusEnum.DELETED.getStatus())) {
            return prepareRewardTaskVO(rewardTask);
        }
        return null;
    }

    private RewardTaskVO prepareRewardTaskVO(RewardTask rewardTask) {
        if (rewardTask == null) {
            return null;
        }
        return CollectionUtil.getFirst(prepareRewardTaskVO(Collections.singletonList(rewardTask)));
    }

    private List<RewardTaskVO> prepareRewardTaskVO(List<RewardTask> rewardTaskList) {
        List<UserInfoResponse> userNameAndAvatar = rpcService.getUserNameAndAvatar(
                rewardTaskList.stream()
                        .map(RewardTask::getUid)
                        .distinct()
                        .collect(Collectors.toList())
        );
        Map<Long, UserInfoResponse> userNameAndAvatarMap = userNameAndAvatar.stream()
                .collect(
                        Collectors.toMap(
                                UserInfoResponse::getUid,
                                Function.identity(),
                                (a, b) -> a
                        )
                );

        return rewardTaskList.stream().map(item -> {
                    RewardTaskVO rewardTaskVO = new RewardTaskVO();
                    rewardTaskVO.setId(item.getId());
                    rewardTaskVO.setUid(item.getUid());
                    rewardTaskVO.setContent(item.getContent());
                    rewardTaskVO.setBounty(item.getBounty());
                    rewardTaskVO.setContact(item.getContact());
                    rewardTaskVO.setMediaPath(item.getMediaPath());
                    rewardTaskVO.setStatus(item.getStatus());

                    String statusName = "";
                    RewardTaskStatusEnum rewardTaskStatusEnum = RewardTaskStatusEnum.getRewardTaskStatusEnum(item.getStatus());
                    if (Objects.equals(item.getType(), RewardTaskTypeEnum.REWARD_TASK.getType())) {
                        statusName = rewardTaskStatusEnum.getStatusName();
                    }
                    if (Objects.equals(item.getType(), RewardTaskTypeEnum.SECOND_HAND.getType())) {
                        statusName = rewardTaskStatusEnum.getSecondHandStatusName();
                    }
                    rewardTaskVO.setStatusName(statusName);

                    UserInfoResponse userInfoResponse = userNameAndAvatarMap.getOrDefault(
                            item.getUid(),
                            new UserInfoResponse()
                    );
                    rewardTaskVO.setUserName(userInfoResponse.getName());
                    rewardTaskVO.setUserAvatar(userInfoResponse.getAvatar());

                    CommonUser currentUser = UserContext.getCurrentUser();
                    if (CommonUser.isLogInUser(currentUser)) {
                        rewardTaskVO.setIsCreator(Objects.equals(item.getUid(), currentUser.getUid()));
                    }

                    Long commentCount = rewardTaskCommentMapper.selectCount(
                            new LambdaQueryWrapper<RewardTaskComment>()
                                    .eq(RewardTaskComment::getRewardTaskId, item.getId())
                                    .eq(RewardTaskComment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
                    );
                    rewardTaskVO.setCommentCount(Objects.isNull(commentCount) ? 0 : commentCount);
                    rewardTaskVO.setCreateTime(item.getCreateTime());
                    rewardTaskVO.setType(item.getType());
                    return rewardTaskVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean updateStatus(Long taskId, RewardTaskStatusEnum rewardTaskStatusEnum) {
        RewardTask rewardTask = rewardTaskMapper.selectById(taskId);
        if (Objects.isNull(rewardTask)) {
            return true;
        }
        rewardTask.setStatus(rewardTaskStatusEnum.getStatus());
        rewardTaskMapper.updateById(rewardTask);
        return true;
    }

    @Override
    public IPage<RewardTaskCommentVO> commentListQuery(RewardTaskCommentQueryDTO rewardTaskCommentQueryDTO) {
        Page<RewardTaskComment> rewardTaskCommentPage = rewardTaskCommentMapper.selectPage(
                new Page<>(rewardTaskCommentQueryDTO.getCurrentPage(), rewardTaskCommentQueryDTO.getPageSize()),
                new LambdaQueryWrapper<RewardTaskComment>()
                        .eq(RewardTaskComment::getRewardTaskId, rewardTaskCommentQueryDTO.getTaskId())
                        .isNull(RewardTaskComment::getParentId)
                        .eq(RewardTaskComment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );

        List<RewardTaskCommentVO> rewardTaskCommentVOS = prepareRewardTaskCommentVO(
                rewardTaskCommentPage.getRecords()
        );

        for (RewardTaskCommentVO rewardTaskCommentVO : rewardTaskCommentVOS) {
            RewardTaskComment child = rewardTaskCommentMapper.selectOne(
                    new LambdaQueryWrapper<RewardTaskComment>()
                            .eq(RewardTaskComment::getParentId, rewardTaskCommentVO.getCommentId())
                            .eq(RewardTaskComment::getRewardTaskId, rewardTaskCommentVO.getRewardTaskId())
                            .eq(RewardTaskComment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
            );
            if (Objects.nonNull(child)) {
                rewardTaskCommentVO.setReply(prepareRewardTaskCommentVO(child));
            }
        }
        return PageUtil.convertPage(rewardTaskCommentPage, rewardTaskCommentVOS);
    }

    @Override
    public RewardTaskCommentVO addComment(RewardTaskCommentDTO rewardTaskCommentDTO) {
        RewardTaskCommentDTO.checkIdValid(rewardTaskCommentDTO);

        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        RewardTaskComment rewardTaskComment = new RewardTaskComment();
        rewardTaskComment.setRewardTaskId(rewardTaskCommentDTO.getRewardTaskId());

        if (Objects.nonNull(rewardTaskCommentDTO.getParentId())) {
            replyCheck(currentUser, rewardTaskCommentDTO);
            rewardTaskCommentDTO.setParentId(rewardTaskCommentDTO.getParentId());
        }

        rewardTaskComment.setUid(currentUser.getUid());
        rewardTaskComment.setIdentityId(rewardTaskCommentDTO.getIdentityId());
        rewardTaskComment.setContent(rewardTaskCommentDTO.getContent());
        rewardTaskComment.setIsDelete(XiaoXiaoConstEnum.UN_DELETE.getVal());
        rewardTaskComment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        rewardTaskComment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        rewardTaskCommentMapper.insert(rewardTaskComment);
        return prepareRewardTaskCommentVO(rewardTaskComment);
    }

    public void replyCheck(
            CommonUser currentUser,
            RewardTaskCommentDTO rewardTaskCommentDTO
    ) {
        RewardTask rewardTask = rewardTaskMapper.selectById(rewardTaskCommentDTO.getRewardTaskId());
        AssertUtil.isTrue(Objects.nonNull(rewardTask), StatusCode.BIZ_ERROR);
        AssertUtil.isTrue(
                Objects.equals(rewardTask.getUid(), currentUser.getUid()),
                StatusCode.BIZ_ERROR
        );

        Long commentCount = rewardTaskCommentMapper.selectCount(
                new LambdaQueryWrapper<RewardTaskComment>()
                        .eq(RewardTaskComment::getParentId, rewardTaskCommentDTO.getParentId())
                        .eq(RewardTaskComment::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal())
        );
        AssertUtil.isTrue(
                Objects.isNull(commentCount) || commentCount == 0,
                StatusCode.BIZ_ERROR
        );
    }

    public RewardTaskCommentVO prepareRewardTaskCommentVO(RewardTaskComment rewardTaskComment) {
        return CollectionUtil.getFirst(prepareRewardTaskCommentVO(Collections.singletonList(rewardTaskComment)));
    }

    public List<RewardTaskCommentVO> prepareRewardTaskCommentVO(List<RewardTaskComment> rewardTaskCommentList) {
        List<UserInfoResponse> userNameAndAvatar = rpcService.getUserNameAndAvatar(
                rewardTaskCommentList.stream()
                        .filter(item -> !isAnonymously(item))
                        .map(RewardTaskComment::getUid)
                        .distinct()
                        .collect(Collectors.toList())
        );
        Map<Long, UserInfoResponse> userNameAndAvatarMap = userNameAndAvatar.stream()
                .collect(Collectors.toMap(UserInfoResponse::getUid, Function.identity(), (a, b) -> a));


        List<IdentityInfoResponse> anonymousNameAndAvatar = rpcService.getAnonymousNameAndAvatar(
                rewardTaskCommentList.stream()
                        .filter(this::isAnonymously)
                        .map(RewardTaskComment::getIdentityId)
                        .distinct()
                        .collect(Collectors.toList())
        );
        Map<Long, IdentityInfoResponse> anonymousNameAndAvatarMap = anonymousNameAndAvatar.stream()
                .collect(Collectors.toMap(IdentityInfoResponse::getIdentityId, Function.identity(), (a, b) -> a));

        CommonUser currentUser = UserContext.getCurrentUser();

        return rewardTaskCommentList.stream().map(item -> {
                    RewardTaskCommentVO rewardTaskCommentVO = new RewardTaskCommentVO();
                    rewardTaskCommentVO.setCommentId(item.getId());
                    rewardTaskCommentVO.setRewardTaskId(item.getRewardTaskId());
                    rewardTaskCommentVO.setContent(item.getContent());
                    rewardTaskCommentVO.setCreateTime(item.getCreateTime());

                    if (isAnonymously(item)) {
                        IdentityInfoResponse identityInfoResponse = anonymousNameAndAvatarMap.getOrDefault(
                                item.getIdentityId(),
                                new IdentityInfoResponse()
                        );
                        rewardTaskCommentVO.setUserName(identityInfoResponse.getName());
                        rewardTaskCommentVO.setUserAvatar(identityInfoResponse.getAvatar());
                        rewardTaskCommentVO.setUid(null);
                        rewardTaskCommentVO.setIdentityId(item.getIdentityId());
                    } else {
                        UserInfoResponse userInfoResponse = userNameAndAvatarMap.getOrDefault(
                                item.getUid(),
                                new UserInfoResponse()
                        );
                        rewardTaskCommentVO.setUserName(userInfoResponse.getName());
                        rewardTaskCommentVO.setUserAvatar(userInfoResponse.getAvatar());
                        rewardTaskCommentVO.setUid(item.getUid());
                        rewardTaskCommentVO.setIdentityId(null);
                    }

                    if (CommonUser.isLogInUser(currentUser)) {
                        rewardTaskCommentVO.setIsCreator(Objects.equals(item.getUid(), currentUser.getUid()));
                    }
                    return rewardTaskCommentVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean delComment(Long commentId) {
        RewardTaskComment rewardTaskComment = rewardTaskCommentMapper.selectById(commentId);
        if (Objects.isNull(rewardTaskComment)) {
            return true;
        }
        rewardTaskComment.setIsDelete(XiaoXiaoConstEnum.DELETED.getVal());
        rewardTaskCommentMapper.updateById(rewardTaskComment);
        return true;
    }

    private boolean isAnonymously(RewardTaskComment rewardTaskComment) {
        if (Objects.nonNull(rewardTaskComment.getUid()) && Objects.nonNull(rewardTaskComment.getIdentityId())) {
            return true;
        }

        if (Objects.nonNull(rewardTaskComment.getUid())) {
            return false;
        }

        throw new ApiException(StatusCode.BIZ_ERROR);
    }
}
