package com.llmb4j.util;

import com.llmb4j.models.base.PoolProperties;
import com.llmb4j.models.openai.OpenAiLLmConfig;
import com.llmb4j.models.openai.api.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * @author LiangTao
 * @date 2023年06月02 16:07
 **/
public interface LLmConstants {

    String llmLogTypeKey = "llmLogType";

    Logger llmLogger = LoggerFactory.getLogger("aiChat");

    OpenAiService defaultOpenAiService =  new OpenAiService(OpenAiLLmConfig.apiKey, Duration.ofSeconds(30), OpenAiLLmConfig.baseUrl, new PoolProperties());



}
