package com.xiaoxiao.common.aspect;

import cn.hutool.json.JSONUtil;
import com.xiaoxiao.common.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    public WebLogAspect() {
    }

    /**
     * 定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
     */
    @Pointcut("execution(public * com.xiaoxiao.*.controller.*.*(..))")
    public void webLogPointcut() {
    }

    /**
     * 前置通知：
     * 1. 在执行目标方法之前执行，比如请求接口之前的登录验证;
     * 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
     */
    @Before("webLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {

    }

    /**
     * 返回通知：
     * 1. 在目标方法正常结束之后执行
     * 1. 在返回通知中补充请求日志信息，如返回时间，方法耗时，返回值，并且保存日志信息
     */
    @AfterReturning(returning = "ret", pointcut = "webLogPointcut()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.info("attributes：null, res: {}", ret);
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        log.info(
                "路径: {} 用户: {} 请求参数: {} 响应: {} 请求方式: {} 请求ip: {} ",
                request.getServletPath(),
                UserContext.getCurrentUser().getUid(),
                JSONUtil.toJsonStr(joinPoint.getArgs()),
                JSONUtil.toJsonStr(ret),
                request.getMethod(),
                request.getRemoteAddr()
        );
    }

    /**
     * 异常通知：
     * 1. 在目标方法非正常结束，发生异常或者抛出异常时执行
     * 1. 在异常通知中设置异常信息，并将其保存
     */
    @AfterThrowing(value = "webLogPointcut()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.error("无法获取请求", throwable);
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        log.error(
                "路径: {} 用户: {} 请求参数: {} 请求方式: {} 请求ip: {}",
                request.getServletPath(),
                UserContext.getCurrentUser().getUid(),
                JSONUtil.toJsonStr(joinPoint.getArgs()),
                request.getMethod(),
                request.getRemoteAddr(),
                throwable
        );
        throwable.printStackTrace();
    }
}
