package com.xiaoxiao.common.util;

import cn.hutool.core.util.StrUtil;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.exception.ApiException;
import org.springframework.lang.Nullable;

/**
 * <p>
 *
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
public class AssertUtil {
	private AssertUtil() {
		throw new UnsupportedOperationException("禁止实例化");
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new ApiException(StatusCode.FAILED, message);
		}
	}

	public static void isTrue(boolean expression, StatusCode statusCode) {
		if (!expression) {
			throw new ApiException(statusCode);
		}
	}

	public static void isTrue(boolean expression, StatusCode statusCode, String message) {
		if (!expression) {
			throw new ApiException(statusCode, message);
		}
	}

	public static void isTrue(boolean expression, RuntimeException runtimeException) {
		if (!expression) {
			throw runtimeException;
		}
	}

	@Deprecated
	public static void notNull(@Nullable Object object, String message) {
		if (object == null) {
			throw new ApiException(StatusCode.FAILED, message);
		}
	}

	@Deprecated
	public static void hasText(@Nullable String text, String message) {
		if (StrUtil.isBlank(text)) {
			throw new ApiException(StatusCode.FAILED, message);
		}
	}

}
