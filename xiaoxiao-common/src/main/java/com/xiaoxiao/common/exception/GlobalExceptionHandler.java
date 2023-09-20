package com.xiaoxiao.common.exception;

import com.xiaoxiao.common.api.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 全局异常处理器
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResp<Object> handle(ApiException e) {
        return CommonResp.failed(e.getStatusCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public CommonResp<Object> exceptionHandler(HttpServletRequest request, Exception exception) {
        log.error("请求路径: {}", request.getServletPath(), exception);
        return CommonResp.failed();
    }

}
