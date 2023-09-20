package com.xiaoxiao.toolbag.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoxiao.toolbag.config.ToolBagConfig;
import com.xiaoxiao.toolbag.mapper.FreeRoomMapper;
import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.entity.FreeRoom;
import com.xiaoxiao.toolbag.service.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.tomcat.jni.Time.sleep;

/**
 * @author yaoyao
 * @Description
 * @create 2023-08-19 0:36
 */
@Slf4j
@Component
@EnableScheduling
public class TaskUtil {
    @Resource
    private FreeRoomMapper freeRoomMapper;

    @Resource
    private ToolBagConfig toolbagConfig;

    @Resource
    private RpcService rpcService;

    private static final String[] classRoom = {"W133f0ea0000WJ", "W133f0ea0000WH", "W133f0ea0000WI", "3", "8", "22", "67", "69"};
    private static final String[] zyxjs = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15"};
    private static final String[] days = {"1", "2", "3", "4", "5", "6", "7"};
    private static final String url = "https://jwxt.scau.edu.cn/resService/jwxtpt/v1/xsd/xsdjsjygl_info/searchFreeRoomList?resourceCode=XSMH070201&apiCode=jw.xsd.xsdInfo.controller.XsdJsjyglController.searchFreeRoomList";

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Scheduled(cron = "0 0 14 * * *")
    public void syncFreeRoom() throws JSONException {
        if (Objects.equals(toolbagConfig.getFreeRoomTaskSwitch(), 0)) {
            return;
        }

        String termStartTime = toolbagConfig.getTermStartTime();
        DateTime start = DateUtil.parse(termStartTime);
        DateTime now = DateUtil.date();
        int currentWeek = (int) (DateUtil.between(start, now, DateUnit.WEEK) + 1);
        int interval = (int) (now.getTime() - start.getTime());
        if (interval < 0) {
            currentWeek = 0;
        }
        int cnt = 0;
        EducationSystemLoginDTO educationSystemLoginDTO = new EducationSystemLoginDTO();
        educationSystemLoginDTO.setStudentNo("学号");
        educationSystemLoginDTO.setPassword("密码");
        headers = JWXTUtil.getInstance().solveCode(educationSystemLoginDTO);
        int errorCnt = 0;
        while (cnt <= 1) {
            for (String day : days) {
                List<FreeRoom> insertList = new ArrayList<>();
                for (String courseTime : zyxjs) {
                    for (String building : classRoom) {
                        JSONObject requestBody = new JSONObject();
                        requestBody.put("jczy013id", "2023-2024-1");
                        requestBody.put("pkgl002id", "W134afdb0002bJ");
                        requestBody.put("jczy008id", building);
                        requestBody.put("pkzc", currentWeek);
                        requestBody.put("pkxq", day);
                        requestBody.put("pkjc", courseTime);
                        requestBody.put("page", new JSONObject()
                                .put("pageIndex", 1)
                                .put("pageSize", 100)
                                .put("orderBy", "[{\"field\":\"bh\",\"sortType\":\"asc\"}]")
                                .put("conditions", "QZDATASOFTJddJJVIJY29uZGl0aW9uR3JvdXAlMjIlM0ElNUIlN0IlMjJsaW5rJTIyJTNBJTIyYW5kJTIyJTJDJTIyY29uZGl0aW9uJTIyJTNBJTVCJTVEJTdEyTTECTTE"));
                        int loop = 3;
                        while (loop > 0 && errorCnt <= 100) {
                            try {
                                HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
                                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                                String responseData = responseEntity.getBody();
                                JSONObject data = new JSONObject(responseData);
                                JSONArray items = data.getJSONObject("data").getJSONArray("items");
                                for (int i = 0; i < items.length(); i++) {
                                    FreeRoom freeRoom = new FreeRoom();
                                    JSONObject item = items.getJSONObject(i);
                                    String jsName = item.getString("js_name");
                                    String yxzw = item.getString("yxzw");
                                    String kszw = item.getString("kszw");
                                    freeRoom.setWeek(currentWeek);
                                    freeRoom.setDay(Integer.valueOf(day));
                                    freeRoom.setBuildingId(building);
                                    freeRoom.setClassroom(jsName);
                                    freeRoom.setCourseTime(courseTime);
                                    freeRoom.setSeatNumber(Integer.valueOf(yxzw));
                                    freeRoom.setExamSeatNumber(Integer.valueOf(kszw));
                                    insertList.add(freeRoom);
                                }
                                break;
                            } catch (Exception e) {
                                sleep(5000);
                                errorCnt++;
                                loop--;
                                log.warn(e.toString());
                            }
                        }
                    }
                }
                if (insertList.size() != 0) {
                    QueryWrapper<FreeRoom> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(FreeRoom::getWeek, currentWeek);
                    List<List<FreeRoom>> list = ListUtils.partition(insertList, 1000);
                    list.forEach(freeRooms -> freeRoomMapper.insertMany(freeRooms));
                } else {
                    log.error("free room error");
                    rpcService.asyncSendErrorMail("空教室爬取失败", "空教室爬取失败");
                    return;
                }
                if (errorCnt >= 100) {
                    rpcService.asyncSendErrorMail("空教室爬取错误", "many request error");
                }
            }
            currentWeek++;
            cnt++;
        }
    }

}
