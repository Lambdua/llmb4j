package com.llmb4j.models.openai;

import com.llmb4j.models.openai.completion.chat.ChatFunctionCall;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public static String getBufferString(List<OpenAiRoleMessage> roleMessages) {
        StringBuilder sb = new StringBuilder();
        for (OpenAiRoleMessage m : roleMessages) {
            switch (m.getRole()) {
                case HUMAN, AI, SYSTEM, CHAT ->
                        sb.append(m.getRole().getValue()).append(": ").append(m.getContent()).append(System.lineSeparator());
                case FUNCTION ->
                    sb.append(m.getRole().getValue()).append(": ").append(m.getFunctionCall().getName()).append(":").append(m.getFunctionCall().getArguments()).append(System.lineSeparator());
                default -> throw new IllegalArgumentException("Got unsupported message type: " + m);
            }
        }
        return sb.toString();

    }

}
