package com.llmb4j.models;

import com.llmb4j.models.openai.OpenAiLLM;
import com.llmb4j.prompt.ChatPromptTemplate;
import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.RoleMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author LiangTao
 * @date 2023年05月30 10:46
 **/
@Slf4j
public class OpenAiTest {
    @Test
    public void streamTest()  {
        OpenAiLLM openAiLLM = new OpenAiLLM();
        ChatPromptTemplate chatPromptTemplate = new ChatPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) {
        }
        RoleMessage request = chatPromptTemplate.format(template, new payload("小明", ChatRole.HUMAN));
        // Flowable<RoleMessage> chatMessageFlowable = openAiLLM.generateChat(Collections.singletonList(request));
        // List<RoleMessage> roleMessages = chatMessageFlowable.toList().blockingGet();
        // Assert.notEmpty(roleMessages);
    }

    @Test
    public void completionTest() {
        OpenAiLLM openAiLLM = new OpenAiLLM();
        ChatPromptTemplate chatPromptTemplate = new ChatPromptTemplate();
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        record payload(String name, ChatRole role) { }
        RoleMessage request = chatPromptTemplate.format(template, new payload("小明", ChatRole.HUMAN));
        // List<RoleMessage> chatCompletion = openAiLLM.generateCompletion(Collections.singletonList(request));
        // Assert.notEmpty(chatCompletion);
    }

}
