package com.llmb4j.models.openai.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * '
 *
 * @author LiangTao
 * @date 2023年05月05 21:03
 **/
@Slf4j
public class RetryInterceptor implements Interceptor {
    private final int maxRetry;

    public RetryInterceptor(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        int retryNum = 0;
        // Try the request up to maxRetry times
        while (retryNum < maxRetry) {
            try {
                return chain.proceed(request);
            } catch (IOException e) {
                log.warn("请求失败:{}，开始重试，重试次数：{}", e.getMessage(), retryNum+1);
                retryNum++;
            }
        }
        log.error("请求失败，重试次数：{}", retryNum);
        return chain.proceed(request);
    }
}
