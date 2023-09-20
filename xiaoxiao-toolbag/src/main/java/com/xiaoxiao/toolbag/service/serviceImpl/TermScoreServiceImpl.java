package com.xiaoxiao.toolbag.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.constant.XiaoXiaoConstEnum;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.toolbag.model.bo.score.ScoreDetail;
import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.entity.TermScore;
import com.xiaoxiao.toolbag.mapper.TermScoreMapper;
import com.xiaoxiao.toolbag.model.vo.ScoreVO;
import com.xiaoxiao.toolbag.service.TermScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.toolbag.util.JWXTUtil;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yaoyao
 * @since 2023-08-26 02:27:07
 */
@Service
public class TermScoreServiceImpl extends ServiceImpl<TermScoreMapper, TermScore> implements TermScoreService {

    final RestTemplate restTemplate = new RestTemplate();

    private Double getDoubleForString(JsonObject jsonObject, String name) {
        if (jsonObject.get(name).isJsonNull()) {
            return 0.0;
        } else {
            String string = jsonObject.get(name).getAsString();
            return Double.parseDouble(string);
        }
    }

    @Override
    public ScoreVO pullTermScore(EducationSystemLoginDTO educationSystemLoginDTO) {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();
        HttpHeaders headers = JWXTUtil.getInstance().solveCode(educationSystemLoginDTO);
        JsonObject requestBody = new JsonObject();
        JsonObject page = new JsonObject();
        page.addProperty("pageIndex", 1);
        page.addProperty("pageSize", 200);
        page.addProperty("orderBy", "[{\"field\":\"jczy013id\",\"sortType\":\"asc\"}]");
        page.addProperty("conditions", "QZDATASOFTJddJJVIJY29uZGl0aW9uR3JvdXAlMjIlM0ElNUIlN0IlMjJsaW5rJTIyJTNBJTIyYW5kJTIyJTJDJTIyY29uZGl0aW9uJTIyJTNBJTVCJTVEJTdEyTTECTTE");

        requestBody.add("page", page);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        JsonArray items = null;
        int count = -1;
        int loop = 3;
        while (loop > 0) {
            try {
                String url = "https://jwxt.scau.edu.cn/resService/jwxtpt/v1/xsd/cjgl_xsxdsq/hnnydxFindKccjList?resourceCode=XSMH0507&apiCode=jw.xsd.xsdInfo.controller.CjglKccjckController.hnnydxFindKccjList";
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                String responseData = responseEntity.getBody();
                JsonObject data = new Gson().fromJson(responseData, JsonObject.class);
                items = data.getAsJsonObject("data").getAsJsonArray("items");
                count = data.getAsJsonObject("data").get("rowCount").getAsInt();
                break;
            } catch (Exception e) {
                loop--;
            }
        }
        if (Objects.isNull(items) && count == -1) {
            throw new ApiException(StatusCode.FAILED);
        }
        List<ScoreDetail> list = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            ScoreDetail scoreDetail = new ScoreDetail();
            JsonObject item = items.get(i).getAsJsonObject();
            String kcname = item.get("kcname").getAsString();
            Double gpa = item.get("jd").getAsDouble();
            Double usualScore = getDoubleForString(item, "cjxm1");
            Double finishScore = getDoubleForString(item, "zcjname1");
            Double examScore = getDoubleForString(item, "cjxm4");
            Double courseScore = item.get("xf").getAsDouble();
            String term = item.get("xnxq").getAsString();
            scoreDetail.setTerm(term);
            scoreDetail.setExamScore(examScore);
            scoreDetail.setCourseScore(courseScore);
            scoreDetail.setFinishScore(finishScore);
            scoreDetail.setUsualScore(usualScore);
            scoreDetail.setGPA(gpa);
            scoreDetail.setCourseName(kcname);
            list.add(scoreDetail);
        }
        //删除旧数据
        UpdateWrapper<TermScore> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(TermScore::getUserid, uid).set(TermScore::getIsDelete, XiaoXiaoConstEnum.DELETED.getVal());
        this.update(wrapper);


        Map<String, List<ScoreDetail>> termScoreMap = list.stream()
                .collect(Collectors.groupingBy(ScoreDetail::getTerm, TreeMap::new, Collectors.toList()));

        ScoreVO scoreVO = new ScoreVO();
        scoreVO.setScoreMap(termScoreMap);
        String json = new Gson().toJson(scoreVO);
        TermScore termScore = new TermScore();
        termScore.setScoreJson(json);
        termScore.setUserid(uid);
        this.save(termScore);
        return scoreVO;
    }
}
