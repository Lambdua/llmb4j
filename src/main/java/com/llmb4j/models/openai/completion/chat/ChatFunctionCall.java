package com.llmb4j.models.openai.completion.chat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatFunctionCall {

    String name;

    @JsonDeserialize(using = ChatFunctionCallArgumentsDeserializer.class)
    @JsonSerialize(using = ChatFunctionCallArgumentsSerializer.class)
    Map<String, Object> arguments;

    public static class ChatFunctionCallArgumentsSerializer extends JsonSerializer<Map<String, Object>> {
        @Override
        public void serialize(Map<String, Object> value, JsonGenerator gen, SerializerProvider serializers) throws java.io.IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(value);
            gen.writeString(s);
        }
    }

    public static class ChatFunctionCallArgumentsDeserializer extends JsonDeserializer<Map<String, Object>> {
        @Override
        public Map<String, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws java.io.IOException {
            String s = p.readValueAs(String.class);
            ObjectMapper objectMapper = (ObjectMapper) p.getCodec();
            return objectMapper.readValue(s, Map.class);
        }
    }


}
