package main;

import controller.Controller;
import javafx.application.Application;

import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;


public class Main extends Application {
    public  static  String PATH;
    @Override
    public void start(Stage primaryStage){

        Controller  controller =  new Controller();





    }

    public static void main (String[] args){
         PATH = args[0];
        launch(args);


    }
}
