# jep
使用的是[jep](https://github.com/ninia/jep)来实现java 调用python的功能，详情请移步查看。


## 涉及到的功能
目前项目主要使用了python 的tiktoken库来计算token的长度。其余目前都是使用java来实现的。

## 如何排除jep的依赖
如果不想调用python依赖，请重写：`com.llmb4j.models.base.BaseLanguageModel.getTokenIds` 方法即可

