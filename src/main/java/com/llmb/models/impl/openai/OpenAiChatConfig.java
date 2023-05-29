package com.llmb.models.impl.openai;

import com.llmb.models.base.ChatConfig;
import lombok.Data;

import static com.llmb.util.SettingUtil.SETTING;

/**
 * @author LiangTao
 * @date 2023年05月29 11:32
 **/
@Data
public class OpenAiChatConfig implements ChatConfig {

    public static final String baseUrl;

    public static final String apiKey;

    static {
        baseUrl= SETTING.get("openai","baseUrl");
        apiKey= SETTING.get("openai","apiKey");
    }
    /**
     * 使用模型
     */
    String useModel;

    /**
     * 系统提示
     */
    private String systemPrompt;

    /**
     * 话题敏感度
     */
    private double temperature;

    /**
     * 话题新鲜度，值越大越有可能拓展到新话题 0-2
     */
    private double presencePenalty;

}
