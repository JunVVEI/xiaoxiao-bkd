package com.xiaoxiao.toolbag.service.serviceImpl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.common.util.AssertUtil;
import com.xiaoxiao.toolbag.config.ToolBagConfig;
import com.xiaoxiao.toolbag.config.threadPool.ThreadPoolConfig;
import com.xiaoxiao.toolbag.mapper.CourseInfoMapper;
import com.xiaoxiao.toolbag.model.bo.course.*;
import com.xiaoxiao.toolbag.model.dto.course.EducationSystemLoginDTO;
import com.xiaoxiao.toolbag.model.dto.course.TermQueryDTO;
import com.xiaoxiao.toolbag.model.entity.CourseInfo;
import com.xiaoxiao.toolbag.model.vo.CourseVO;
import com.xiaoxiao.toolbag.model.vo.CurrentWeekVO;
import com.xiaoxiao.toolbag.service.CourseInfoService;
import com.xiaoxiao.toolbag.util.JWXTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zjh
 * @since 2022-11-22 01:16:43
 */
@Service
@Slf4j
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoMapper, CourseInfo> implements CourseInfoService {

    @javax.annotation.Resource
    private ToolBagConfig toolbagConfig;

    @javax.annotation.Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @javax.annotation.Resource
    private CourseInfoMapper courseInfoMapper;

    /**
     * 从教务系统中拉取本学期课表数据并保存
     */
    @Override
    public boolean pullAndSaveCourse(EducationSystemLoginDTO educationSystemLoginDTO) {
        HttpHeaders httpHeaders = JWXTUtil.getInstance().solveCode(educationSystemLoginDTO);
        // 拉取课表
        List<EducationSystemTermsCoursesParse> courseList = pullCourse(httpHeaders);
        // 保存课表
        if (!saveCourse(educationSystemLoginDTO, courseList)) {
            log.error("保存课表异常 请求参数 {} 课表 {}", educationSystemLoginDTO, courseList);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }
        return true;
    }

    /**
     * 从教务系统中拉取课表数据
     */
    private List<EducationSystemTermsCoursesParse> pullCourse(HttpHeaders headers) {
        // 获取课表备注需要参数:jczy013id
        // 获取课表需要两个参数:jczy013id和pkgl002id

        //参数map
        Map<String, String> paramMap = new HashMap<>(4);

        // 获取参数jczy013id(对应当前学期)
        List<String> terms = getTermList(headers, "{\"JwPublicXnxq\": {}}");
        String jczy013id = terms.get(0);

        // 获取参数pkgl002id需要jczy013id
        paramMap.put("jczy013id", jczy013id);

        // 获取参数pkgl002id
        String pkgl002id = getpkgl002id(headers, JSONUtil.toJsonStr(paramMap));

        // 获取多学期课表
        // 添加请求参数pkgl002id
        paramMap.put("pkgl002id", pkgl002id);

        // 大学4年 至多8个学期
        List<EducationSystemTermsCoursesParse> termsCoursesList = new CopyOnWriteArrayList<>();

        CountDownLatch countDownLatch = new CountDownLatch(terms.size());
        for (String term : terms) {
            CompletableFuture.runAsync(
                    () -> {
                        log.info("开始获取课程 {}", term);
                        // 新建一个map 防止并发修改
                        Map<String, String> termParamMap = new HashMap<>(4);
                        // 这里是浅拷贝，由于参数都是不可变string类型，故实际上没有影响
                        termParamMap.putAll(paramMap);
                        // 替换学期参数
                        termParamMap.put("jczy013id", term);
                        String jsonStr = JSONUtil.toJsonStr(termParamMap);
                        // 获取课表备注
                        String[] notes = getNotes(headers, jsonStr);
                        // 获取课表
                        List<EducationSystemCourse> courseData = getCourseData(headers, jsonStr);
                        List<EducationSystemWeeksCourses> educationSystemWeeksCourses = paresCourseInfo(courseData);
                        log.info("获取课程结果 {} {} {}", term, educationSystemWeeksCourses, notes);

                        // 课表内容为空
                        if (courseData.size() == 0) {
                            return;
                        }
                        //将解析好的课程数据添加到list中
                        termsCoursesList.add(new EducationSystemTermsCoursesParse(term, educationSystemWeeksCourses, notes));
                    },
                    threadPoolTaskExecutor
            ).whenComplete(
                    (result, ex) -> {
                        if (Objects.nonNull(ex)) {
                            log.error("获取课程异常 {} {}", term, headers, ex);
                        }
                        countDownLatch.countDown();
                    }
            );
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("多线程获取课表异常", e);
            throw new RuntimeException(e);
        }

        return termsCoursesList;
    }

    /**
     * 获取课表备注
     */
    private String[] getNotes(HttpHeaders headers, String jsonStr) {
        String url = "https://jwxt.scau.edu.cn/resService/jwxtpt/v1/xsd/xsdqxxkb_info/findXskbBzShowApSz?resourceCode=XSMH0701&apiCode=jw.xsd.xsdInfo.controller.XsdQxxkbController.findXskbBzShowApSz";
        ResponseEntity<String> res = new RestTemplate().exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(jsonStr, headers),
                String.class
        );
        EducationSystemResponse<String> courseNote = JSONUtil.toBean(
                res.getBody(), new TypeReference<EducationSystemResponse<String>>() {
                }, false
        );
        if (courseNote == null) {
            log.error("getNotes():获取课程备注失败 jsonStr为 {}", jsonStr);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }

        return courseNote.getData().split(" ;");
    }

    /**
     * 获取某一学期的课表内容
     */
    private List<EducationSystemCourse> getCourseData(HttpHeaders headers, String jsonStr) {
        String url = "https://jwxt.scau.edu.cn/resService/jwxtpt/v1/xsd/xsdqxxkb_info/searchOneXskbList?resourceCode=XSMH0701&apiCode=jw.xsd.xsdInfo.controller.XsdQxxkbController.searchOneXskbList";
        ResponseEntity<String> res = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(jsonStr, headers),
                String.class
        );
        EducationSystemResponse<List<EducationSystemCourse>> courseResponse = JSONUtil.toBean(
                res.getBody(),
                new TypeReference<EducationSystemResponse<List<EducationSystemCourse>>>() {
                }, false
        );
        if (courseResponse == null || CollectionUtil.isEmpty(courseResponse.getData())) {
            log.error("getCourseData():获取课表内容为空 jsonStr为 {} 响应{}", jsonStr, courseResponse);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }

        return courseResponse.getData();
    }

    /**
     * 获取学期列表,从当前学期开始存储之前的学期
     */
    private List<String> getTermList(HttpHeaders headers, String jsonStr) {

        String url = "https://jwxt.scau.edu.cn/resService/jwxtpt/v1/jczy/jwPublic_dic/findPublicDic?resourceCode=JCZY0101&apiCode=jwPublic.controller.JwPublicController.findPublicDic";
        ResponseEntity<String> res = new RestTemplate().exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(jsonStr, headers),
                String.class
        );
        EducationSystemResponse<EducationSystemJczy013id> courseResponse = JSONUtil.toBean(
                res.getBody(),
                new TypeReference<EducationSystemResponse<EducationSystemJczy013id>>() {
                }, false
        );
        if (courseResponse == null) {
            log.error("getTermList():获取参数jczy013id失败 jsonStr为 {}", jsonStr);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }

        //教务系统的学年学期业务字典
        List<EducationSystemJczy013id.JwPublicXnxq> xnxqs = courseResponse.getData().getXnxq();
        int xnxqSize = xnxqs.size();
        EducationSystemJczy013id.JwPublicXnxq xnxq = null;
        for (int i = 0; i < xnxqSize; i++) {
            xnxq = xnxqs.get(i);
            //遍历到当前学期
            // if ("1".equals(xnxq.getCode())) {
            if (Objects.equals(toolbagConfig.getCurrentTerm(), xnxq.getJczy013id())) {
                // 大学4年 8个学期,从当前学期开始存储之前的学期
                return xnxqs.stream()
                        .skip(i)
                        .limit(8)
                        .map(EducationSystemJczy013id.JwPublicXnxq::getJczy013id)
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    /**
     * 获取参数pkgl002id
     */
    private String getpkgl002id(HttpHeaders headers, String jsonStr) {
        String url = "https://jwxt.scau.edu.cn/resService/jwxtpt/v1/xsd/xsdjsjygl_info/searchPkgl002List?resourceCode=XSMH0702&apiCode=jw.xsd.xsdInfo.controller.XsdJsjyglController.searchPkgl002List";
        ResponseEntity<String> res = new RestTemplate().exchange(url,
                HttpMethod.POST,
                new HttpEntity<>(jsonStr, headers),
                String.class
        );
        EducationSystemResponse<List<EducationSystemPkgl002id>> courseResponse = JSONUtil.toBean(
                res.getBody(),
                new TypeReference<EducationSystemResponse<List<EducationSystemPkgl002id>>>() {
                }, false
        );
        if (courseResponse == null) {
            log.error("getpkgl002id():获取参数pkgl002id失败 jsonStr为 {}", jsonStr);
            throw new ApiException(StatusCode.SERVER_BUSY);
        }

        return courseResponse.getData().get(0).getId();
    }

    /**
     * 优化课表结构
     */
    private List<EducationSystemWeeksCourses> paresCourseInfo(List<EducationSystemCourse> courses) {
        //将课表按上课时间顺序进行排序(周一 1-2节 -> 周日 13-15节)
        courses.sort(Comparator.comparing(EducationSystemCourse::getCourseTime));

        //提取全部课程内容+每周的课程内容
        List<EducationSystemWeeksCourses> weeksCoursesList = new ArrayList<>(20);
        for (int i = 0; i <= 19; i++) {
            String week = String.valueOf(i);
            List<EducationSystemCourse> courseList = courses.stream().filter(c -> {
                //提取全部课程内容
                if ("0".equals(week)) {
                    return true;
                }
                //提取每周的课程内容(1-19周)
                String[] split = c.getWeeksDetail().split(",");
                for (String s : split) {
                    if (s.equals(week)) {
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());

            //map,其中key对应周几(周一,周二...周日),value对应当天课程
            Map<String, List<EducationSystemCourse>> daysCourses = courseList.stream().collect(Collectors.groupingBy(c ->
                    c.getCourseTime().substring(0, 1)
            ));

            // 某天没课也要有一个空数组
            for (int j = 1; j <= 7; ++j) {
                daysCourses.computeIfAbsent(String.valueOf(j), k -> new ArrayList<>());
            }

            //对该周的课程内容添加到weekCoursesList
            weeksCoursesList.add(new EducationSystemWeeksCourses(week, daysCourses));
        }

        return weeksCoursesList;
    }

    /**
     * 保存课表数据
     */
    @Transactional
    public boolean saveCourse(EducationSystemLoginDTO educationSystemLoginDTO, List<EducationSystemTermsCoursesParse> courseList) {
        // 删除已有数据
        this.lambdaUpdate().eq(CourseInfo::getStudentId, educationSystemLoginDTO.getStudentNo()).remove();
        // 填充新的数据
        List<CourseInfo> courseInfoList = new ArrayList<>(8);
        // 每学期课程以及备注对应一行数据
        for (EducationSystemTermsCoursesParse termsCourses : courseList) {
            CourseInfo courseInfo = new CourseInfo(educationSystemLoginDTO);
            courseInfo.setTerm(termsCourses.getTerm());
            courseInfo.setDetail(JSONUtil.toJsonStr(termsCourses.getWeeksCourses()));
            courseInfo.setNote(JSONUtil.toJsonStr(termsCourses.getNotes()));

            // TODO: 临时新增，保存增加用户id
            CommonUser currentUser = UserContext.getCurrentUser();
            CommonUser.assertIsLogInUser(currentUser);
            courseInfo.setUid(String.valueOf(currentUser.getUid()));

            courseInfoList.add(courseInfo);
        }
        // 批量插入
        if (!this.saveBatch(courseInfoList)) {
            log.info("saveCourse(): 插入数据失败:{}", courseInfoList);
            return false;
        }
        return true;
    }

    /**
     * 获取当前周次
     */
    @Override
    public CurrentWeekVO getWeek() {
        // 从Nacos获取课程开始时间:"2022-08-29"
        String termStartTime = toolbagConfig.getTermStartTime();
        // 解析课程开始时间
        DateTime start = DateUtil.parse(termStartTime);
        // 获取当前时间
        DateTime now = DateUtil.date();
        // 计算周次
        long currentWeek = DateUtil.between(start, now, DateUnit.WEEK) + 1;
        // 当前日期在开学日期之前
        long interval = (now.getTime() - start.getTime());
        if (interval < 0) {
            currentWeek = 0;
        }
        return new CurrentWeekVO(termStartTime, currentWeek);
    }

    @Override
    public CourseVO getCourse(TermQueryDTO courseQueryDTO) {
        TermQueryDTO.checkIsValid(courseQueryDTO);

        String term = courseQueryDTO.getTerm();

        LambdaQueryWrapper<CourseInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        CourseInfo courseInfo = this.getOne(
                lambdaQueryWrapper
                        .eq(CourseInfo::getTerm, term)
                        .eq(StrUtil.isNotEmpty(courseQueryDTO.getStudentNo()), CourseInfo::getStudentId, courseQueryDTO.getStudentNo())
                        .eq(StrUtil.isNotEmpty(courseQueryDTO.getUid()), CourseInfo::getUid, courseQueryDTO.getUid())
                        .eq(StrUtil.isNotEmpty(courseQueryDTO.getWechatId()), CourseInfo::getWechatId, courseQueryDTO.getWechatId())
                        .eq(StrUtil.isNotEmpty(courseQueryDTO.getDeviceId()), CourseInfo::getDeviceId, courseQueryDTO.getDeviceId())
        );

        AssertUtil.isTrue(Objects.nonNull(courseInfo), "未查询到课表数据，请尝试重新登录课表");

        // 将课表JSON字符串转成List
        String courses = courseInfo.getDetail();
        List<EducationSystemWeeksCourses> termsCoursesList = JSONUtil.toBean(
                courses,
                new TypeReference<List<EducationSystemWeeksCourses>>() {
                },
                false
        );
        String[] notes = JSONUtil.toBean(
                courseInfo.getNote(),
                new TypeReference<String[]>() {
                },
                false
        );
        return new CourseVO(termsCoursesList, notes);
    }

    /**
     * 获取可选择的学期数组
     */
    @Override
    public Set<String> getTerm(TermQueryDTO idDTO) {
        return this.lambdaQuery()
                .select()
                .eq(StrUtil.isNotEmpty(idDTO.getStudentNo()), CourseInfo::getStudentId, idDTO.getStudentNo())
                .eq(StrUtil.isNotEmpty(idDTO.getUid()), CourseInfo::getUid, idDTO.getUid())
                .eq(StrUtil.isNotEmpty(idDTO.getWechatId()), CourseInfo::getWechatId, idDTO.getWechatId())
                .eq(StrUtil.isNotEmpty(idDTO.getDeviceId()), CourseInfo::getDeviceId, idDTO.getDeviceId())
                .list()
                .stream()
                .map(CourseInfo::getTerm)
                .collect(Collectors.toSet());
    }

    public boolean checkIsExist(String uid) {
        if (!NumberUtil.isNumber(uid)) {
            return false;
        }
        Long count = courseInfoMapper.selectCount(
                new LambdaQueryWrapper<CourseInfo>()
                        .eq(CourseInfo::getUid, uid)
        );
        return count != 0;
    }

}
