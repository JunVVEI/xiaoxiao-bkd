package com.xiaoxiao.common.aspect;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.common.annotation.XiaoXiaoResponseBody;
import com.xiaoxiao.common.api.CommonPage;
import com.xiaoxiao.common.api.CommonResp;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

/**
 * <p>
 * ss
 * </p>
 *
 * @author Junwei
 * @since 2023/1/3
 */
@RestControllerAdvice
public class XiaoXiaoResponseAdvice implements ResponseBodyAdvice<Object> {
	private static final Class<? extends Annotation> ANNOTATION_TYPE = XiaoXiaoResponseBody.class;

	@Override
	public boolean supports(MethodParameter returnType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.hasMethodAnnotation(ANNOTATION_TYPE);
	}

	/**
	 * 当类或者方法使用了 @ResponseResultBody 就会调用这个方法
	 */
	@Override
	public Object beforeBodyWrite(Object body,
	                              @NotNull MethodParameter returnType,
	                              @NotNull MediaType selectedContentType,
	                              @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
	                              @NotNull ServerHttpRequest request,
	                              @NotNull ServerHttpResponse response) {
		// 避免重复封装响应报文
		if (body instanceof CommonResp) {
			return body;
		}

		if (body instanceof IPage) {
			return CommonResp.success(CommonPage.newCommonPage((IPage) body));
		}

		// 使用约定全局返回格式封装响应报文
		return CommonResp.success(body);
	}

}
