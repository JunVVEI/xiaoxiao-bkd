package com.xiaoxiao.common.api;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author JunWEI
 * @since 2022/10/21
 */
@Getter
@ToString
public class CommonResp<T> {

	private final StatusCode code;
	private final String message;
	private final T data;

	private CommonResp(StatusCode code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * 成功返回结果
	 *
	 * @param data 获取的数据
	 */
	public static <T> CommonResp<T> success(T data) {
		return new CommonResp<T>(StatusCode.SUCCESS, StatusCode.SUCCESS.getMessage(), data);
	}

	/**
	 * 成功返回结果
	 *
	 * @param msg 提示信息
	 */
	public static <T> CommonResp<T> success(String msg) {
		return new CommonResp<T>(StatusCode.SUCCESS, msg, null);
	}

	/**
	 * 成功返回结果
	 */
	public static <T> CommonResp<T> success() {
		return new CommonResp<T>(StatusCode.SUCCESS, StatusCode.SUCCESS.getMessage(), null);
	}

	/**
	 * 成功返回结果
	 *
	 * @param data    获取的数据
	 * @param message 提示信息
	 */
	public static <T> CommonResp<T> success(T data, String message) {
		return new CommonResp<T>(StatusCode.SUCCESS, message, data);
	}

	/**
	 * 失败返回结果
	 *
	 * @param statusCode 错误码
	 */
	public static <T> CommonResp<T> failed(StatusCode statusCode) {
		return new CommonResp<T>(statusCode, statusCode.getMessage(), null);
	}

	/**
	 * 失败返回结果
	 *
	 * @param statusCode 错误码
	 * @param message    错误信息
	 */
	public static <T> CommonResp<T> failed(StatusCode statusCode, String message) {
		return new CommonResp<T>(statusCode, message, null);
	}

	/**
	 * 失败返回结果
	 *
	 * @param message 提示信息
	 */
	public static <T> CommonResp<T> failed(String message) {
		return new CommonResp<T>(StatusCode.FAILED, message, null);
	}

	/**
	 * 失败返回结果
	 */
	public static <T> CommonResp<T> failed() {
		return failed(StatusCode.FAILED);
	}

	/**
	 * 参数验证失败返回结果
	 */
	public static <T> CommonResp<T> validateFailed() {
		return failed(StatusCode.VALIDATE_FAILED);
	}

	/**
	 * 参数验证失败返回结果
	 *
	 * @param message 提示信息
	 */
	public static <T> CommonResp<T> validateFailed(String message) {
		return new CommonResp<T>(StatusCode.VALIDATE_FAILED, message, null);
	}

	/**
	 * 未登录返回结果
	 */
	public static <T> CommonResp<T> unauthorized(T data) {
		return new CommonResp<T>(StatusCode.UNAUTHORIZED, StatusCode.UNAUTHORIZED.getMessage(), data);
	}

	/**
	 * 未登录返回结果
	 */
	public static <T> CommonResp<T> unauthorized() {
		return new CommonResp<T>(StatusCode.UNAUTHORIZED, StatusCode.UNAUTHORIZED.getMessage(), null);
	}

	/**
	 * 未授权返回结果
	 */
	public static <T> CommonResp<T> forbidden(T data) {
		return new CommonResp<T>(StatusCode.FORBIDDEN, StatusCode.FORBIDDEN.getMessage(), data);
	}

	/**
	 * 未授权返回结果
	 */
	public static <T> CommonResp<T> forbidden() {
		return new CommonResp<T>(StatusCode.FORBIDDEN, StatusCode.FORBIDDEN.getMessage(), null);
	}

	/**
	 * 判断响应是否成功
	 */
	public static <T> boolean isSuccess(CommonResp<T> resp) {
		return !Objects.isNull(resp) && Objects.equals(resp.getCode(), StatusCode.SUCCESS);
	}

}
