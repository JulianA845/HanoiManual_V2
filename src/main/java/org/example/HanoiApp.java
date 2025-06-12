package org.example;

import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class HanoiApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        new MainController(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}