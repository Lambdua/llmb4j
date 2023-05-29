package com.llmb.prompt;

import cn.hutool.core.lang.Assert;
import com.llmb.prompt.chat.ChatPromptTemplate;
import com.llmb.prompt.chat.ChatRole;
import com.llmb.prompt.chat.ChatMessage;
import org.junit.Test;

import java.util.Map;

/**
 * @author LiangTao
 * @date 2023年05月29 09:32
 **/
public class ChatPromptTemplateTest {

    private final String template = """
            这是一段测试文本，参数以{$param}的形式插入,然后通过template来进行参数注入。
            {$name}-{$age}-{$sex}-{$测试}
            """;
    private final ChatPromptTemplate chatPromptTemplate = new ChatPromptTemplate();

    @Test
    public void recordTemplateTest() {
        record paramPayload(String param, String name, Integer age, Boolean 测试, ChatRole chatRole) {
        }
        paramPayload paramPayload = new paramPayload("record声明方式", "姓名", 18, true, ChatRole.AI);
        ChatMessage msg = chatPromptTemplate.toMsg(template, paramPayload);
        Assert.equals(ChatRole.AI, msg.role());
        Object parse = chatPromptTemplate.parse(msg);
        String result = """
            这是一段测试文本，参数以record声明方式的形式插入,然后通过template来进行参数注入。
            姓名-18-{$sex}-true
            """;

        Assert.equals(result, parse);
    }

    @Test
    public void mapTemplateTest() {
        ChatMessage msg = chatPromptTemplate.toMsg(template, Map.of("param", "map声明方式", "name", "姓名", "age", 18, "测试", true, "chatRole", ChatRole.AI));
        Assert.equals(ChatRole.AI, msg.role());
        Object parse = chatPromptTemplate.parse(msg);
        String result = """
            这是一段测试文本，参数以map声明方式的形式插入,然后通过template来进行参数注入。
            姓名-18-{$sex}-true
            """;
        Assert.equals(result, parse);
    }

}
