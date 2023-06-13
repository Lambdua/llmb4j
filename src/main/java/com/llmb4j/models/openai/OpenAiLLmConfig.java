package com.llmb4j.models.openai;

import lombok.Data;

import static com.llmb4j.util.SettingUtil.SETTING;

/**
 * @author LiangTao
 * @date 2023年05月29 11:32
 **/
@Data
public class OpenAiLLmConfig {

    public static final String baseUrl;

    public static final String apiKey;

    static {
        baseUrl= SETTING.get("openai","baseUrl");
        apiKey= SETTING.get("openai","apiKey");
    }
    /**
     * 使用模型
     */
    String modelName;


    /**
     * 话题敏感度
     */
    private double temperature;


    private Integer maxTokens=256;


    /**
     * 话题新鲜度，值越大越有可能拓展到新话题 0-2
     */
    private double presencePenalty;

    /**
     * How many chat completion chatCompletionChoices to generate for each input message.
     */
   private Integer n;

   private boolean stream=false;

   private boolean verbose=false;

}
