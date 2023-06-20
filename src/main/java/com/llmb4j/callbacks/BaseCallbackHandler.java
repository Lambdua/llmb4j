package com.llmb4j.callbacks;

/**
 * @author LiangTao
 * @date 2023年06月07 09:48
 **/
public abstract class BaseCallbackHandler implements LLMManagerMixin, ChainManagerMixin, RunManagerMixin, ToolManagerMixin, CallbackManagerMixin {

   private boolean throwError = true;


   public boolean isThrowError() {
      return throwError;
   }

   public void setThrowError(boolean throwError) {
      this.throwError = throwError;
   }
}
