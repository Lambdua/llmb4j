package com.llmb4j.util;

import cn.hutool.core.lang.Assert;
import com.llmb4j.common.Generation;
import com.llmb4j.common.LLMResult;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;

import java.util.List;
import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年06月15 13:23
 **/
public class ChatMsgUtil {
    private ChatMsgUtil() {
        throw new IllegalStateException("工具类，不允许实例化");
    }

    public static Map<String, String> msgToMap(RoleMessage msg) {
        return Map.of("type", msg.type(), "content", msg.getContent());
    }

    public static List<RoleMessage> llmResultToRoleMessage(LLMResult llmResult) {
        Assert.notEmpty(llmResult.getGenerations(), "llmResult.getGenerations() must not be empty");
        Assert.notNull(llmResult.getLlmOutput(), "llmResult.getLlmOutput() must not be null");
        return llmResult.getGenerations().stream().map(itemGenerations -> {
            String content = itemGenerations.stream().map(Generation::getText).reduce("", String::concat);
            return new RoleMessage(content, ChatRole.CHAT);
        }).toList();
    }

    public static List<Map<String, String>> msgsToMaps(List<RoleMessage> msgs) {
        return msgs.stream().map(ChatMsgUtil::msgToMap).toList();
    }

    public static RoleMessage mapToMsg(Map<String, String> map) {
        return new RoleMessage(map.get("content"), ChatRole.valueOf(map.get("type")));
    }

    public static List<RoleMessage> mapsToMsgs(List<Map<String, String>> maps) {
        return maps.stream().map(ChatMsgUtil::mapToMsg).toList();
    }

    public static String getBufferString(List<? extends RoleMessage> messages) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            RoleMessage m = messages.get(i);
            sb.append(m.getRole().getValue()).append(": ").append(m.getContent());
            if (i != messages.size() - 1) {
                sb.append(System.lineSeparator()).append("  ");
            }
        }
        return sb.toString();
    }

}
