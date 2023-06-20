package com.llmb4j.models.base;

import com.llmb4j.prompt.base.RoleMessage;
import lombok.*;

import java.util.List;

/**
 * llm对话的荷载参数基类
 * @author LiangTao
 * @date 2023年06月15 09:58
 **/
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(callSuper = true)
public class BaseLLMChatPayload extends BaseLLMPayload{
    /**
     * 对话历史
     */
    public List<? extends RoleMessage> chatHistory;
}
