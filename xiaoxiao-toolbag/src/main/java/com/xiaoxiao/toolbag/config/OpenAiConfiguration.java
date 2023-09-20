package com.xiaoxiao.toolbag.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

@Configuration
@Data
@RefreshScope
public class OpenAiConfiguration {

    /**
     * open ai 密钥配置
     */
    @Value("${open.ai.key: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}")
    private String openAiKey;

    /**
     * 请求超时配置
     */
    @Value("${open.ai.request.timeout:20}")
    private long timeout;


    /**
     * openai 代理代理转发url
     */
    @Value("${open.ai.base.url:https://www.xiaoxiaoai.info}")
    private String baseUrl;

    @Value("${open.ai.isopen:1}")
    private Integer isOpen;

    @Value("${open.ai.checkIsSchoolUser:1}")
    private Integer checkIsSchoolUser;

    @Bean
    public OpenAiService openAiService() {
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(openAiKey, Duration.ofSeconds(timeout));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        OpenAiApi openAiApi = retrofit.create(OpenAiApi.class);
        ExecutorService executorService = client.dispatcher().executorService();

        return new OpenAiService(openAiApi, executorService);

    }

}
