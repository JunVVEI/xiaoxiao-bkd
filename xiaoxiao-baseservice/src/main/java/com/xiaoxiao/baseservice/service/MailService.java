package com.xiaoxiao.baseservice.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoxiao.baseservice.model.dto.MailDTO;
import com.xiaoxiao.baseservice.model.dto.MailLogQuery;
import com.xiaoxiao.baseservice.model.entity.MailLog;
import com.xiaoxiao.common.api.CommonResp;

import java.util.List;

/**
 * <p>
 * MailService
 * </p>
 *
 * @author Junwei
 * @since 2022/12/3
 */
public interface MailService extends IService<MailLog> {

    /**
     * 发送邮件
     *
     * @param mailDTO 参数
     * @return 发送结果
     */
    boolean sendMail(MailDTO mailDTO);
    /**
     * 查询
     *
     * @param mailLogQuery 查询参数
     * @return 查询结果
     */
    Page<MailLog> query(MailLogQuery mailLogQuery);


    /**
     * 删除邮件日志
     *
     * @param id 邮件日志id
     */
    void deleteById(Long id);

    /**
     * 更新邮件日志
     *
     * @param mailLog 日志实体
     */
    void update(MailLog mailLog);

    /**
     * 新增邮件日志
     *
     * @param mailLog 邮件实体
     * @return 结果
     */
    boolean add(MailLog mailLog);

}
