package com.llmb4j.models.openai;

import com.llmb4j.models.base.BaseLLMConfig;
import lombok.Data;

import static com.llmb4j.util.SettingUtil.SETTING;

/**
 * @author LiangTao
 * @date 2023年05月29 11:32
 **/
@Data
public class OpenAiLLmConfig extends BaseLLMConfig {

    public static final String baseUrl;

    public static final String apiKey;

    static {
        baseUrl= SETTING.get("openai","baseUrl");
        apiKey= SETTING.get("openai","apiKey");
    }


}
