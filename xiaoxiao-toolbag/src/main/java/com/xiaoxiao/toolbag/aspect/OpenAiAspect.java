package com.xiaoxiao.toolbag.aspect;

import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.cache.RedisService;
import com.xiaoxiao.common.exception.ApiException;
import com.xiaoxiao.common.user.CommonUser;
import com.xiaoxiao.common.user.UserContext;
import com.xiaoxiao.toolbag.annotation.OpenAiLimit;
import com.xiaoxiao.toolbag.config.OpenAiConfiguration;
import com.xiaoxiao.toolbag.service.CourseInfoService;
import com.xiaoxiao.toolbag.service.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Objects;

/**
 * @author yaoyao
 * @Description
 * @create 2023-05-21 23:22
 */
@Aspect
@Component
@Slf4j
public class OpenAiAspect {
    @Resource
    private RedisService redisService;

    @Resource
    private CourseInfoService courseInfoService;

    @Resource
    private OpenAiConfiguration openAiConfiguration;

    @Resource
    private RpcService rpcService;

    private static final String PREFIX = "OPENAI_LIMIT";

    @Pointcut(value = "@annotation(com.xiaoxiao.toolbag.annotation.OpenAiLimit)")
    public void openAiLimitPointCut() {
    }

    @Around(value = "openAiLimitPointCut()")
    public Object openAiLimitAround(ProceedingJoinPoint proceedingJoinPoint) {

        if (Objects.equals(openAiConfiguration.getCheckIsSchoolUser(), 1) && !checkIsSchoolUser()) {
            throw new ApiException(StatusCode.FAILED, "请登录课表后使用该功能");
        }

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        long milliSecondsLeftDay = 86400000 - DateUtils.getFragmentInDays(Calendar.getInstance(), Calendar.DATE);
        OpenAiLimit limit = methodSignature.getMethod().getAnnotation(OpenAiLimit.class);

        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);
        Long uid = currentUser.getUid();

        String countString = redisService.get(PREFIX + uid.toString());
        int count = 1;
        if (Objects.nonNull(countString)) {
            count = Integer.parseInt(countString) + 1;
        }

        if (count > limit.count()) {
            throw new ApiException(StatusCode.FAILED, "当日请求次数已达上限");
        }

        redisService.set(PREFIX + uid, Integer.toString(count), milliSecondsLeftDay, limit.timeUnit());
        Object[] args = proceedingJoinPoint.getArgs();

        try {
            return proceedingJoinPoint.proceed(args);
        } catch (ApiException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.toString());
            rpcService.asyncSendOpenAiErrorMail(e.toString());
            throw new ApiException(StatusCode.FAILED);
        } finally {
            log.debug(uid.toString() + count);
        }
    }

    /**
     * 检查是否已经登陆课表，以此判断是否是校内用户
     */
    private boolean checkIsSchoolUser() {
        CommonUser currentUser = UserContext.getCurrentUser();
        CommonUser.assertIsLogInUser(currentUser);

        return courseInfoService.checkIsExist(String.valueOf(currentUser.getUid()));
    }

}
