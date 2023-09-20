package com.xiaoxiao.toolbag.service.serviceImpl;

import com.xiaoxiao.baseservice.rpc.model.resquest.SendMailRequest;
import com.xiaoxiao.baseservice.rpc.model.resquest.SendWeChatInformRequest;
import com.xiaoxiao.baseservice.rpc.service.RpcBaseService;
import com.xiaoxiao.toolbag.config.threadPool.ThreadPoolConfig;
import com.xiaoxiao.toolbag.service.RpcService;
import com.xiaoxiao.user.rpc.service.RpcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author yaoyao
 * @Description
 * @create 2023-06-12 0:33
 */
@Slf4j
@Service
public class RpcServiceImpl implements RpcService {
    @Resource
    private RpcBaseService rpcBaseService;

    @Resource
    private RpcUserService rpcUserService;

    @Resource(name = ThreadPoolConfig.BIZ_THREAD_POOL_BEAN_NAME)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final List<String> NOTICE_EMAILS = Arrays.asList(
            "1192991051@qq.com",
            "751009734@qq.com",
            "2634378554@qq.com"
    );

    @Override
    public void asyncSendOpenAiErrorMail(String message) {
        CompletableFuture.runAsync(
                () -> {
                    SendMailRequest sendMailRequest = new SendMailRequest();
                    NOTICE_EMAILS.forEach(email -> {
                        sendMailRequest.setTargetMail(email);
                        sendMailRequest.setTitle("openai错误");
                        sendMailRequest.setMsg(message);
                        try {
                            rpcBaseService.sendMail(sendMailRequest);
                        } catch (Throwable e) {
                            log.error("邮箱发送失败：" + e.getMessage());
                        }
                    });
                },
                threadPoolTaskExecutor
        );
    }

    @Override
    public void asyncSendErrorMail(String title, String message) {
        CompletableFuture.runAsync(
                () -> {
                    SendMailRequest sendMailRequest = new SendMailRequest();
                    NOTICE_EMAILS.forEach(email -> {
                        sendMailRequest.setTargetMail(email);
                        sendMailRequest.setTitle(title);
                        sendMailRequest.setMsg(message);
                        try {
                            rpcBaseService.sendMail(sendMailRequest);
                        } catch (Throwable e) {
                            log.error("邮箱发送失败：" + e.getMessage());
                        }
                    });
                },
                threadPoolTaskExecutor
        );
    }

    @Override
    public boolean sendWeChatInform(SendWeChatInformRequest sendWeChatInformRequest) {
        return rpcBaseService.sendWeChatInform(sendWeChatInformRequest);
    }

    @Override
    public List<Long> getActivateConfKeyUserIds(String key) {
        return rpcUserService.getActivateConfKeyUserIds(key);
    }
}
