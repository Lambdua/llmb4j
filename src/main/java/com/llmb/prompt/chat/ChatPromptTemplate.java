package com.llmb.prompt.chat;

import com.llmb.memory.LLMMemory;
import com.llmb.prompt.base.AbstractPromptTemplate;
import com.llmb.prompt.base.LLMInputParse;
import com.llmb.prompt.base.LLMOutputParse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年05月26 10:53
 **/
@Slf4j
public class ChatPromptTemplate extends AbstractPromptTemplate<ChatMessage> {

    @Override
    public LLMInputParse<ChatMessage> createDefaultInputParse() {
        return new LLmChatInputParse();
    }

    @Override
    public LLMOutputParse<ChatMessage,String> createDefaultOutputParse() {
        return ChatMessage::getMsg;
    }

    public static class LLmChatInputParse implements LLMInputParse<ChatMessage> {

        @Override
        public ChatMessage toMsg(String template, Record argsPayload) {
                Class<? extends Record> aClass = argsPayload.getClass();
                ChatRole chatRole = ChatRole.SYSTEM;

                for (RecordComponent recordComponent : aClass.getRecordComponents()) {
                    Method accessor = recordComponent.getAccessor();
                    accessor.setAccessible(true);
                    String paramName = recordComponent.getName();
                    try {
                        if (template.contains("{$" + paramName + "}")) {
                            template = template.replace("{$" + paramName + "}", accessor.invoke(argsPayload).toString());
                        }
                        if (recordComponent.getType().equals(ChatRole.class)) {
                            chatRole = (ChatRole) accessor.invoke(argsPayload);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("获取参数值失败", e);
                    }

                }
                return new ChatMessage(template, chatRole);
        }

        @Override
        public ChatMessage toMsg(String template, LLMMemory argsPayload) {
            ChatRole chatRole = ChatRole.SYSTEM;
            for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();
                if (template.contains("{$" + paramName + "}")) {
                    template = template.replace("{$" + paramName + "}", paramValue.toString());
                }
                if (paramValue instanceof ChatRole role) {
                    chatRole = role;
                }
            }
            return new ChatMessage(template, chatRole);
        }

    }
}
