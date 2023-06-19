# llmb4j 🔥

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[qq交流群](http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=9rc-2JbZiwWg7x7r-CByvCvYV9dBPbY6&authKey=POoOwST7MmIeuqukzX%2FvOZ3M4sWq7MgN79a4pBcGIEFDZjtw8Pm9mzSCynmH2REk&noverify=0&group_code=228337569)

使用java来构建可组合的LLM应用！

项目名称是：`llm bridge for java`的缩写

🚨⚠️ 目前此库正处于开发中，尚不能用于生产环境,欢迎大家积极提issues和pull request ⚠️🚨

## 1. 介绍

这是LangChain的Java语言实现,[LangChain](https://github.com/hwchase17/langchain)
是一个可组合的LLM应用框架，它可以帮助您快速构建LLM应用程序，而无需担心底层的复杂性。
但是官方目前只提供了Python和Js的实现，对于Java开发者来说，这是一个不小的挑战，因此我决定参考langchain的实现原理自己动手实现一个Java版本的LangChain。

## 2. 快速开始

本教程将带您快速了解如何使用Java构建端到端的LLM应用程序。

详细使用可以参考代码中的单元测试部分

### 2.1 版本，依赖和安装

- JDK:该库依赖于JDK18进行开发，后续会更新到JKD21。未来也不考虑兼容JDK8等低版本的JDK。
- Maven: 推荐3.8.6版本，至少需要3.5.4
- LLM: 目前只支持openai llm
- lombok: 1.18.26
- python: 目前很多库Java还没有很好的实现，因此我们会使用python来实现一些功能，后续逐步剔除,详细请看：[JEP的使用](./jep.md)

### 2.2 环境设置

请参考[环境设置](src/main/resources/config.setting_exapmle),并将其重命名为config.setting

### 2.3 llm调用
```java
    public void streamChatTest() {
        OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        //配置模板
        String template = "你好，我的名字叫：{$name},我应该叫你什么？";
        //使用record来进行模板参数注入，也可以通过map来进行参数注入
        record payload(String name, ChatRole role) {
        }
        //生成请求
        OpenAiRoleMessage request = chatPromptTemplate.format(template, new payload("小明", ChatRole.HUMAN));
        
        //创建一个默认聊天配置
        OpenAiLLmChatPayload chatPayload = OpenAiLLM.withDefaultChatModel();
        //添加聊天历史
        chatPayload.setChatHistory(Collections.singletonList(request));
        
        //设置stream为true，表示流式聊天。 流式聊天会在callback#onLlmNewToken中返回流响应
        chatPayload.setStream(true);
        //打印详细的聊天配置和请求、响应
        chatPayload.setVerbose(true);
        StringBuilder sb = new StringBuilder();
        
        //设置回调处理
        chatPayload.setCallbackHandler(new BaseCallbackHandler() {
            @Override
            public void onLlmNewToken(String token, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                //可以自己处理token
                sb.append(token);
            }
        });
        //开始聊天
        List<? extends RoleMessage> roleMessages = openAiLLM.generateChat(chatPayload);
    }

```

### 2.4 openai function调用

```java
  public void chatFunctionTest() {
        OpenAiPromptTemplate chatPromptTemplate = new OpenAiPromptTemplate();
        String location = "北京";
        String template = "你好，请问今天{$location}的天气怎么样?";

        record payload(String location, ChatRole role) {
        }
        OpenAiRoleMessage askRequest = chatPromptTemplate.format(template, new payload(location, ChatRole.HUMAN));
        OpenAiLLmChatPayload chatPayload = OpenAiLLM.withDefaultChatModel();
        chatPayload.setChatHistory(Collections.singletonList(askRequest));
        chatPayload.setStream(false);
        chatPayload.setVerbose(true);
        chatPayload.setN(1);
        chatPayload.setTemperature(1.0D);
        chatPayload.setModelName(OpenAiModels.gpt35turbo16k);

        //设置为none,来声明强制不适用function
        // chatPayload.setFunctionCall("none");

        List<ChatFunction> functions = new ArrayList<>();
        /**
         * "functions": [
         *     {
         *       "name": "get_current_weather",
         *       "description": "Get the current weather in a given location",
         *       "parameters": {
         *         "type": "object",
         *         "properties": {
         *           "location": {
         *             "type": "string",
         *             "description": "The city and state, e.g. San Francisco, CA"
         *           },
         *           "unit": {
         *             "type": "string",
         *             "enum": ["celsius", "fahrenheit"]
         *           }
         *         },
         *         "required": ["location"]
         *       }
         *     }
         *   ]
         */
        record Properties(String type, String description, @JsonProperty("enum") List<String> enumValue) {
        }
        record Parameters(String type, Map<String, Properties> properties, List<String> required) {
        }
        Properties locationP=new Properties("string", "城市和区，例如：上海市，杨浦区", null);
        Properties unitP=new Properties("string", "要使用的温度单位。从用户位置推断出这一点。", List.of("摄氏度", "华氏度"));
        Parameters parameters = new Parameters("object", Map.of("location", locationP, "unit", unitP), List.of("location"));

        ChatFunction chatFunction = new ChatFunction("get_current_weather", "获取给定位置的当前天气", parameters );
        functions.add(chatFunction);
        chatPayload.setFunctions(functions);
        chatPayload.setCallbackHandler(new BaseCallbackHandler() {
            @Override
            public void onLlmEnd(LLMResult response, UUID runId, UUID parentRunId, Map<String, Object> kwargs) {
                log.info("onLlmEnd:{}", response.toString());
            }
        });
        List<? extends RoleMessage> roleMessages = openAiLLM.generateChat(chatPayload);
        Assert.notEmpty(roleMessages);
        OpenAiRoleMessage functionCallMsg= (OpenAiRoleMessage) roleMessages.get(0);
        /**
         * {role=assistant, content=null, name=null, functionCall=ChatFunctionCall(name=get_current_weather, arguments={location=北京}), finishReason=function_call}
         */
        ChatFunctionCall functionCall = functionCallMsg.getFunctionCall();
        String callName = functionCall.getName();
        Map<String, Object> arguments = functionCall.getArguments();
        String callLocation = MapUtil.getStr(arguments, "location");
        Assert.isTrue(callLocation.contains(location));

        OpenAiRoleMessage functionResponseMsg = new OpenAiRoleMessage("今天天气晴朗，温度20度", ChatRole.FUNCTION,"get_current_weather",null);

        chatPayload.setChatHistory(List.of(askRequest,functionCallMsg,functionResponseMsg));
        List<? extends RoleMessage> finalResponse = openAiLLM.generateChat(chatPayload);
        Assert.notEmpty(finalResponse);
        OpenAiRoleMessage finalResponseMsg = (OpenAiRoleMessage) finalResponse.get(0);
        Assert.isTrue(finalResponseMsg.getRole().equals(ChatRole.AI));
        //响应： 今天北京的天气晴朗，温度为20摄氏度
        Assert.isTrue(finalResponseMsg.getContent().contains("20"));
    }
```



