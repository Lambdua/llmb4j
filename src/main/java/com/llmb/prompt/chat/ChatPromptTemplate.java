package com.llmb.prompt.chat;

import com.llmb.memory.LLMMemory;
import com.llmb.prompt.base.AbstractStrPromptTemplate;
import com.llmb.prompt.base.LLMStrInputParse;
import com.llmb.prompt.base.LLmStrOutputParse;
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
public class ChatPromptTemplate extends AbstractStrPromptTemplate<ChatMessage> {

    @Override
    public LLMStrInputParse<ChatMessage> createDefaultInputParse() {
        return new LLmChatInputParse();
    }

    @Override
    public LLmStrOutputParse<ChatMessage> createDefaultOutputParse() {
        return ChatMessage::getMsg;
    }

    public static class LLmChatInputParse implements LLMStrInputParse<ChatMessage> {

        @Override
        public ChatMessage toMsg(String target, Record argsPayload) {
                Class<? extends Record> aClass = argsPayload.getClass();
                ChatRole chatRole = ChatRole.SYSTEM;

                for (RecordComponent recordComponent : aClass.getRecordComponents()) {
                    Method accessor = recordComponent.getAccessor();
                    accessor.setAccessible(true);
                    String paramName = recordComponent.getName();
                    try {
                        if (target.contains("{$" + paramName + "}")) {
                            target = target.replace("{$" + paramName + "}", accessor.invoke(argsPayload).toString());
                        }
                        if (recordComponent.getType().equals(ChatRole.class)) {
                            chatRole = (ChatRole) accessor.invoke(argsPayload);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("获取参数值失败", e);
                    }

                }
                return new ChatMessage(target, chatRole);
        }

        @Override
        public ChatMessage toMsg(String target, LLMMemory argsPayload) {
            ChatRole chatRole = ChatRole.SYSTEM;
            for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
                String paramName = entry.getKey();
                Object paramValue = entry.getValue();
                if (target.contains("{$" + paramName + "}")) {
                    target = target.replace("{$" + paramName + "}", paramValue.toString());
                }
                if (paramValue instanceof ChatRole role) {
                    chatRole = role;
                }
            }
            return new ChatMessage(target, chatRole);
        }

    }
}
