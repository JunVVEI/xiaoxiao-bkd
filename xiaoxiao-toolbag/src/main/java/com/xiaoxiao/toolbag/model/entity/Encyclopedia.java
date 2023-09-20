package com.xiaoxiao.toolbag.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
 * <p>
 * 校园百科
 * </p>
 *
 * @author chenjunwei
 * @since 2023-01-26 12:20:39
 */
@Data
@TableName("doc")
public class Encyclopedia implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 父节点id
	 */
	@TableField("parent_id")
	private Long parentId;

	/**
	 * 唯一key
	 */
	@TableField("doc_key")
	private String docKey;

	/**
	 * 标题
	 */
	@TableField("title")
	private String title;

	/**
	 * 原始内容
	 */
	@TableField("row_content")
	private String rowContent;

	/**
	 * MarkDown内容
	 */
	@TableField("md_content")
	private String mdContent;

	/**
	 * 浏览量
	 */
	@TableField("view_count")
	private Integer viewCount;

	/**
	 * 排序权重
	 */
	@TableField("sort_weight")
	private Integer sortWeight;

	/**
	 * 前台显示状态0仅在后台可见、1用户可见
	 */
	@TableField("show_state")
	private Integer showState;

	/**
	 * 删除标志（0代表未删除，1代表已删除）
	 */
	@TableField(value = "is_delete", fill = FieldFill.INSERT)
	@TableLogic
	private Integer isDelete;

	/**
	 * 创建者
	 */
	@TableField("creator")
	private String creator;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Timestamp createTime;

	/**
	 * 更新者
	 */
	@TableField("updater")
	private String updater;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	private Timestamp updateTime;
}
