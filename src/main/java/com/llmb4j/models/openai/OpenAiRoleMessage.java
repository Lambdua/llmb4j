package com.llmb4j.models.openai;

import com.llmb4j.models.openai.completion.chat.ChatFunctionCall;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LiangTao
 * @date 2023年06月15 13:34
 **/
@Data
@NoArgsConstructor
public class OpenAiRoleMessage extends RoleMessage {

    private String name;

    private ChatFunctionCall functionCall;

    public OpenAiRoleMessage(String content, ChatRole role, String name, ChatFunctionCall functionCall) {
        super(content, role);
        this.name = name;
        this.functionCall = functionCall;
    }

}
