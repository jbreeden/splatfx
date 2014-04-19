package com.github.crossfx;

import javafx.stage.Stage;

public class ApplicationAdapter extends javafx.application.Application {
  private static Callback1 onStartAction;
  
  public ApplicationAdapter(){}
  
  public static void setOnStart(Callback1 onStartAction){
    ApplicationAdapter.onStartAction = onStartAction;
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