package com.xiaoxiao.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;

/**
 * <p>
 * MybatisPlusConfig
 * </p>
 *
 * @author Junwei
 * @since 2022/10/28
 */
@Configuration
public class MybatisPlusConfig {

	/**
	 * 分页插件配置
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	/**
	 * 字段注入配置
	 */
	@Bean
	public MetaObjectHandler metaObjectHandler() {
		return new MetaObjectHandler() {
			@Override
			public void insertFill(MetaObject metaObject) {

				// 强制覆盖字段的值
				// metaObject.setValue("createTime", null);
				// metaObject.setValue("updateTime", null);
				// 新增数据默未删除
				// metaObject.setValue("isDelete", 0);

				this.strictInsertFill(
						metaObject,
						"createTime",
						() -> new Timestamp(System.currentTimeMillis()),
						Timestamp.class
				);

				this.strictInsertFill(
						metaObject,
						"updateTime",
						() -> new Timestamp(System.currentTimeMillis()),
						Timestamp.class
				);

				this.strictInsertFill(
						metaObject,
						"isDelete",
						() -> 0,
						Integer.class
				);
			}

			@Override
			public void updateFill(MetaObject metaObject) {

				// 强制覆盖字段的值
				// metaObject.setValue("updateTime", null);

				this.strictUpdateFill(
						metaObject,
						"updateTime",
						() -> new Timestamp(System.currentTimeMillis()),
						Timestamp.class
				);
			}
		};
	}

}
