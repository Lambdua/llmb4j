package com.llmb4j.prompt;

import com.llmb4j.prompt.base.ChatRole;
import com.llmb4j.prompt.base.PromptValue;
import com.llmb4j.prompt.base.RoleMessage;

import java.util.Collections;
import java.util.List;

/**
 * @author LiangTao
 * @date 2023年06月16 11:16
 **/
public class StringPromptValue implements PromptValue {
    private String text;

    @Override
    public List<RoleMessage> toMessages() {
        return Collections.singletonList(new RoleMessage(text, ChatRole.HUMAN));
    }

    @Override
    public String toString() {
        return text;

    }
}
