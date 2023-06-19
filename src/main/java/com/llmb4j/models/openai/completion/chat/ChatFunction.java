package com.llmb4j.models.openai.completion.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class ChatFunction {

    @NonNull
    String name;
    String description;
    Object parameters;

}
