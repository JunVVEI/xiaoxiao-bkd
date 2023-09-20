package com.xiaoxiao.baseservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wf.captcha.SpecCaptcha;
import com.xiaoxiao.baseservice.constant.BaseServiceConstant;
import com.xiaoxiao.baseservice.model.dto.CaptchaDTO;
import com.xiaoxiao.baseservice.model.vo.CaptchaVO;
import com.xiaoxiao.baseservice.service.CaptchaService;
import com.xiaoxiao.common.api.StatusCode;
import com.xiaoxiao.common.cache.RedisService;
import com.xiaoxiao.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @author mayunfei
 */
@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

	@Resource
	private RedisService redisService;

	@Override
	public CaptchaVO captchaImg() {
		try {
			SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);

			String verCode = specCaptcha.text().toLowerCase();
			// 设置获取key
			String key = UUID.randomUUID().toString();
			String verCode64 = specCaptcha.toBase64();

			// 存入redis
			saveCaptcha(key, verCode);

			CaptchaVO captchaVO = new CaptchaVO();
			captchaVO.setKey(key);
			captchaVO.setVerCode64(verCode64);

			return captchaVO;
		} catch (Exception e) {
			log.error("获取验证码图片失败: ", e);
			throw new ApiException("获取验证码图片失败");
		}
	}

	@Override
	public CaptchaVO captchaCode() {
		// 设置获取key
		String key = UUID.randomUUID().toString().replaceAll("-", "");
		return generateCaptchaVO(key);
	}

	/**
	 * 返回验证码文本
	 *
	 * @param key 指定key
	 * @return
	 */
	@Override
	public CaptchaVO captchaCode(String key) {
		return generateCaptchaVO(key);
	}

	@Override
	public boolean verify(CaptchaDTO captchaDTO) {
		try {
			CaptchaDTO.checkIsValid(captchaDTO);

			// 根据key获取redis中储存的验证码
			String storedCode = redisService.get(
					BaseServiceConstant.REDIS_CAPTCHA_PREFIX + captchaDTO.getKey()
			);
			// 判断验证码
			return StrUtil.isNotBlank(storedCode) && StrUtil.equals(storedCode, captchaDTO.getUserCode().trim(), true);
		} catch (Exception e) {
			log.error("验证码验证异常: {} ", captchaDTO, e);
			throw new ApiException(StatusCode.BIZ_ERROR);
		}

	}

	private void saveCaptcha(String key, String verCode) {
		try {
			redisService.set(
					BaseServiceConstant.REDIS_CAPTCHA_PREFIX + key,
					verCode,
					BaseServiceConstant.CAPTCHA_CODE_EXPIRED,
					TimeUnit.SECONDS
			);
		} catch (Exception e) {
			log.error("redis缓存验证码异常 key: {} code: {}", key, verCode, e);
			throw new ApiException(StatusCode.BIZ_ERROR);
		}

	}

	private CaptchaVO generateCaptchaVO(String key){
		try {
			SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
			String verCode = specCaptcha.text().toLowerCase();
			// 存入redis
			saveCaptcha(key, verCode);
			CaptchaVO captchaVO = new CaptchaVO();
			captchaVO.setKey(key);
			captchaVO.setVerCode(verCode);
			return captchaVO;
		}catch (Exception e) {
			log.error("获取验证码失败: ", e);
			throw new ApiException("获取验证码失败");
		}
	}

}
