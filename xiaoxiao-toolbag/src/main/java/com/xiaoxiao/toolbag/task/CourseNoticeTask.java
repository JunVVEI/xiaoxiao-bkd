package com.xiaoxiao.toolbag.task;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xiaoxiao.baseservice.rpc.model.WeChatInformTypeEnum;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.toolbag.config.ToolBagConfig;
import com.xiaoxiao.toolbag.config.threadPool.ThreadPoolConfig;
import com.xiaoxiao.toolbag.model.entity.CourseInfo;
import com.xiaoxiao.toolbag.service.CourseInfoService;
import com.xiaoxiao.toolbag.service.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@EnableScheduling
public class CourseNoticeTask {

    @Resource
    private ToolBagConfig toolbagConfig;

    @Resource
    private CourseInfoService courseInfoService;

    @Resource
    private RpcService rpcService;

    @Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Scheduled(cron = "0 30 22 * * ?", zone = "Asia/Shanghai")
    public void sendCourseTomorrow() {
        log.info("开始执行明日课表推送定时任务");
        if (Objects.equals(toolbagConfig.getCourseNoticeTaskSwitch(), 0)) {
            return;
        }

        // 获取接收课程通知的用户的uid
        List<Long> userIds = rpcService.getActivateConfKeyUserIds("RECEIVE_COURSE");
        log.info("明日课程通知用户 {}", userIds);
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }

        List<CourseInfo> courseInfos = getCourseInfos(userIds);

        int currentWeek = (int) courseInfoService.getWeek().getCurrentWeek();
        int sendDay = LocalDate.now(ZoneId.of("Asia/Shanghai")).getDayOfWeek().getValue() + 1;
        if (sendDay == 7) {
            sendDay = 1;
            currentWeek += 1;
        }

        Map<Long, List<String>> msgMap = new HashMap<>();

        for (CourseInfo courseInfo : courseInfos) {
            String json = courseInfo.getDetail();
            List<String> msgList = new ArrayList<>();
            msgList.add("----您的明日课程如下----");

            // 不需要关心什么错误  有错误直接跳过就是了  表示json有问题解析错误 让他崩。。。
            try {
                JsonArray weeks = JsonParser.parseString(json).getAsJsonArray();
                for (int i = 0; i < weeks.size(); i++) {

                    JsonObject weekData = weeks.get(i).getAsJsonObject();
                    String week = weekData.get("week").getAsString();

                    if (week.equals(String.valueOf(currentWeek))) {

                        JsonObject daysCourses = weekData.getAsJsonObject("daysCourses");
                        String dayKey = String.valueOf(sendDay);

                        if (daysCourses.has(dayKey)) {
                            JsonArray courses = daysCourses.getAsJsonArray(dayKey);
                            for (JsonElement courseJsonElement : courses) {
                                JsonObject courseJsonObject = courseJsonElement.getAsJsonObject();

                                String classRoom = getStringOrDefault(courseJsonObject.get("js_name"), "");
                                String startTime = getStringOrDefault(courseJsonObject.get("djkssj"), "");
                                String endTime = getStringOrDefault(courseJsonObject.get("djjssj"), "");
                                String courseName = getStringOrDefault(courseJsonObject.get("kc_name"), "未知课程");

                                if (StringUtils.isNotBlank(classRoom)) {
                                    classRoom = "<" + classRoom + ">";
                                } else {
                                    classRoom = "";
                                }

                                String time;
                                if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                                    time = startTime + "-" + endTime + " ";
                                } else {
                                    time = getCourseTime(courseJsonObject.get("pksjshow"));
                                }

                                String msg_one = classRoom + time + " " + courseName;
                                msgList.add(msg_one);
                            }
                        }
                        break;
                    }
                }

                // 只有一条数据的话，表示明天没有课程，不推送
                if (1 < msgList.size()) {
                    // 对齐
                    if (msgList.size() % 2 == 1) {
                        msgList.add("------------------------");
                    }
                    msgMap.put(Long.valueOf(courseInfo.getUid()), msgList);
                }
            } catch (Exception e) {
                log.error(
                        "解析用户明日课程异常 week {} day {} data {}",
                        currentWeek,
                        sendDay,
                        JSONUtil.toJsonStr(courseInfo),
                        e
                );
            }

        }

        asyncSendCourseNotice(msgMap);

    }


    private List<CourseInfo> getCourseInfos(List<Long> userIds) {
        QueryWrapper<CourseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(CourseInfo::getTerm, toolbagConfig.getCurrentTerm())
                .in(CourseInfo::getUid, userIds)
                .eq(CourseInfo::getIsDelete, XiaoXiaoConstEnum.UN_DELETE.getVal());

        return courseInfoService.list(queryWrapper);
    }

    private void asyncSendCourseNotice(Map<Long, List<String>> msgMap) {
        msgMap.forEach((userid, courseMsg) -> {
            CompletableFuture.runAsync(
                    () -> {
                        try {
                            ListIterator<String> stringListIterator = courseMsg.listIterator();
                            while (stringListIterator.hasNext()) {
                                SendWeChatInformRequest sendWeChatInformRequest = new SendWeChatInformRequest();
                                sendWeChatInformRequest.setUid(userid);
                                sendWeChatInformRequest.setWeChatInformType(
                                        WeChatInformTypeEnum.POST_COURSE.getType()
                                );
                                sendWeChatInformRequest.setWeChatInformTypeString(
                                        stringListIterator.next()
                                );
                                sendWeChatInformRequest.setPagePath("/pages/bag/class/index");
                                sendWeChatInformRequest.setContent(stringListIterator.next());
                                rpcService.sendWeChatInform(sendWeChatInformRequest);
                            }
                        } catch (Exception e) {
                            log.error("明日课程通知失败 {} {}", userid, courseMsg, e);
                        }
                    },
                    threadPoolTaskExecutor
            );
        });
    }

    private String getStringOrDefault(JsonElement jsonElement, String defaultString) {
        try {
            return jsonElement.getAsString();
        } catch (Exception e) {
            return defaultString;
        }
    }


    private final static String[] TIME_SLOTS = {
            "第1节 08:00 - 08:40",
            "第2节 08:45 - 09:25",
            "第3节 09:55 - 10:35",
            "第4节 10:40 - 11:20",
            "第5节 11:25 - 12:05",
            "第6节 12:40 - 13:20",
            "第7节 13:25 - 14:05",
            "第8节 14:30 - 15:10",
            "第9节 15:15 - 15:55",
            "第10节 16:25 - 17:05",
            "第11节 17:10 - 17:50",
            "第12节 17:55 - 18:35",
            "第13节 19:30 - 20:10",
            "第14节 20:15 - 20:55",
            "第15节 21:00 - 21:40"
    };

    private String getCourseTime(JsonElement jsonElement) {
        try {
            String input = jsonElement.getAsString();
            input = input.substring(input.indexOf(" ") + 1).replace("节", "");

            String[] inputParts = input.split("-");
            int start = Integer.parseInt(inputParts[0]);
            int end = Integer.parseInt(inputParts[1]);

            String startTime = "";
            String endTime = "";
            for (int i = start - 1; i < end; i++) {
                String[] parts = TIME_SLOTS[i].split(" ");
                if (startTime.equals("")) {
                    startTime = parts[1];
                }
                endTime = parts[3];
            }

            return startTime + "-" + endTime;
        } catch (Exception e) {
            return "";
        }

    }
}
