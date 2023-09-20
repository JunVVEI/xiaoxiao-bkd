package com.xiaoxiao.common.user;

import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 * 用户上下文，用于获取线程隔离的用户信息
 * </p>
 *
 * @author Junwei
 * @since 2022/11/21
 */
public class UserContext {

	private UserContext() {
		throw new UnsupportedOperationException("禁止实例化");
	}

	private static final ThreadLocal<CommonUser> USER = new ThreadLocal<>();

	protected static void set(CommonUser commonUser) {
		USER.set(commonUser);
	}


	public static CommonUser getCurrentUser() {
		CommonUser commonUser = USER.get();
		if (Objects.isNull(commonUser)) {
			// 如果为空返回一个空用户
			return new CommonUser();
		}
		return USER.get();
	}

	protected static void remove() {
		USER.remove();
	}

}
