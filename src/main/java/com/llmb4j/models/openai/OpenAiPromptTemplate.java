package com.llmb4j.models.openai;

import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.LLMInputParse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月16 14:10
 **/
@Slf4j
public class OpenAiPromptTemplate implements LLMInputParse<OpenAiRoleMessage> {
    @Override
    public OpenAiRoleMessage format(String template, Record argsPayload) {
        Class<? extends Record> aClass = argsPayload.getClass();
        ChatRole chatRole = ChatRole.SYSTEM;

        String functionName="";
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
                if (paramName.equals("functionName")){
                    functionName = accessor.invoke(argsPayload).toString();
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("获取参数值失败", e);
            }

        }
        return new OpenAiRoleMessage(template,chatRole,functionName,null);
    }

    @Override
    public OpenAiRoleMessage format(String template, Map<String, Object> argsPayload) {
        ChatRole chatRole = ChatRole.SYSTEM;
        String functionName = "";
        for (Map.Entry<String, Object> entry : argsPayload.entrySet()) {
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();
            if (template.contains("{$" + paramName + "}")) {
                template = template.replace("{$" + paramName + "}", paramValue.toString());
            }
            if (paramValue instanceof ChatRole role) {
                chatRole = role;
            }
            if (paramName.equals("functionName")){
                functionName = paramValue.toString();
            }
        }
        return new OpenAiRoleMessage(template,chatRole,functionName,null);
    }
}
