package com.xiaoxiao.baseservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoxiao.baseservice.config.BaseServiceThreadPoolConfig;
import com.xiaoxiao.baseservice.mapper.MailLogMapper;
import com.xiaoxiao.baseservice.model.dto.MailDTO;
import com.xiaoxiao.baseservice.model.dto.MailLogQuery;
import com.xiaoxiao.baseservice.model.entity.MailLog;
import com.xiaoxiao.baseservice.model.enums.ConstEnum;
import com.xiaoxiao.baseservice.service.MailService;
import com.xiaoxiao.common.api.CommonResp;
import com.xiaoxiao.common.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * MailServiceImpl
 * </p>
 *
 * @author Junwei
 * @since 2022/12/3
 */
@Service
@Slf4j
public class MailServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements MailService {

	@Resource
	private MailLogMapper mailLogMapper;

	@Resource
	private JavaMailSenderImpl mailSender;

	@Resource(name = BaseServiceThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
	protected ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/**
	 * 邮件发件人
	 */
	@Value("${mail.fromMail.addr}")
	private String from;

	@Override
	public boolean sendMail(MailDTO mailDTO) {
		log.info("发送邮件 {}", mailDTO);
		CompletableFuture.runAsync(
				() -> {
					SimpleMailMessage message = new SimpleMailMessage();
					message.setFrom(from);
					message.setTo(mailDTO.getTargetMail());
					message.setSubject(mailDTO.getTitle());
					message.setText(mailDTO.getMsg());
					try {
						mailSender.send(message);
						saveMailLog(mailDTO, ConstEnum.SUCCESS.getVal());
					} catch (Exception e) {
						log.info("发送邮件异常 {}", mailDTO, e);
						saveMailLog(mailDTO, ConstEnum.FAILED.getVal());
					}
				},
				threadPoolTaskExecutor
		);
		return true;
	}

	public void saveMailLog(MailDTO mailDTO, int result) {
		MailLog mailLog = new MailLog();
		mailLog.setTargetAddress(mailDTO.getTargetMail());
		mailLog.setSendContent(mailDTO.getMsg());
		mailLog.setSendReason(mailDTO.getTitle());
		mailLog.setSendResult(result);
		try {
			add(mailLog);
		} catch (Exception e) {
			log.warn("邮件日志保存失败");
		}
	}

	@Override
	public void deleteById(Long id) {
		mailLogMapper.deleteById(id);
	}

	@Override
	public void update(MailLog mailLog) {
		mailLogMapper.updateById(mailLog);
	}

	@Override
	public boolean add(MailLog mailLog) {
		mailLog.setSendTime(new Timestamp(System.currentTimeMillis()));
		AssertUtil.isTrue(save(mailLog), "新增邮件日志失败");
		return true;
	}

	@Override
	public Page<MailLog> query(MailLogQuery mailLogQuery) {
		return page(
				new Page<>(mailLogQuery.getCurrentPage(), mailLogQuery.getPageSize()),
				new QueryWrapper<MailLog>().lambda()
						.eq(mailLogQuery.getId() != null, MailLog::getId, mailLogQuery.getId())
						.eq(mailLogQuery.getTargetAddress() != null, MailLog::getTargetAddress, mailLogQuery.getTargetAddress())
						.like(mailLogQuery.getSendContent() != null, MailLog::getSendContent, mailLogQuery.getSendContent())
						.eq(mailLogQuery.getSendTime() != null, MailLog::getSendTime, mailLogQuery.getSendTime())
						.like(mailLogQuery.getSendReason() != null, MailLog::getSendReason, mailLogQuery.getSendReason())
						.eq(mailLogQuery.getSendResult() != null, MailLog::getSendResult, mailLogQuery.getSendResult())
						.ge(mailLogQuery.getStartTime() != null, MailLog::getSendTime, mailLogQuery.getStartTime())
						.le(mailLogQuery.getEndTime() != null, MailLog::getSendTime, mailLogQuery.getEndTime())

		);

	}
}
