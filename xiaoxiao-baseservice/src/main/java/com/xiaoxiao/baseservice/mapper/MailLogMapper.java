package com.xiaoxiao.baseservice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoxiao.baseservice.model.dto.MailLogQuery;
import com.xiaoxiao.baseservice.model.entity.MailLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenhaowen
 */
@Mapper
public interface MailLogMapper extends BaseMapper<MailLog> {

}
