package com.github.jsfx;

import javafx.stage.Stage;

public class JsApplication extends javafx.application.Application {
  private static JsCallback1 onStartAction;
  
  public JsApplication(){}
  
  public static void setOnStart(JsCallback1 onStartAction){
    JsApplication.onStartAction = onStartAction;
  }
  
  @Override
  public void start(Stage stage){
    try {
      onStartAction.call(stage);
    }
    catch(Exception ex){
      throw ex;
    }
  }
}