package com.xiaoxiao.toolbag.model.dto.encyclopedia;


import cn.hutool.core.bean.BeanUtil;
import com.xiaoxiao.toolbag.model.entity.Encyclopedia;
import lombok.Data;

@Data
public class EncyclopediaDTO {

	private Long id;

	/**
	 * 父节点id,默认为-1表示没有父级
	 */
	private Long parentId = -1L;

	/**
	 * docKey
	 */
	private String docKey;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 原始内容
	 */
	private String rowContent;

	/**
	 * MarkDown内容
	 */
	private String mdContent;

	/**
	 * 排序权重
	 */
	private Integer sortWeight;

	public static void checkIsValid(EncyclopediaDTO encyclopediaDTO) {

	}

	public static Encyclopedia prepareEncyclopedia(EncyclopediaDTO encyclopediaDTO) {
		Encyclopedia encyclopedia = new Encyclopedia();
		BeanUtil.copyProperties(encyclopediaDTO, encyclopedia);
		encyclopedia.setId(null);
		return encyclopedia;
	}

}
