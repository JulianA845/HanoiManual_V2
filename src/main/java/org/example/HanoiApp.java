package org.example;

import controller.HanoiController;
import controller.HanoiControllerRanking;
import javafx.application.Application;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.ExtensionHanoiModel;
import model.HanoiModel;
import view.HanoiviewAuto;
import view.HanoiviewManual;
import java.util.Arrays;
import java.util.Optional;
import controller.HanoiControllerManual;

public class HanoiApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1) Pedir nombre de usuario
        TextInputDialog dlgUser = new TextInputDialog();
        dlgUser.setTitle("Torres de Hanoi");
        dlgUser.setHeaderText("¡Bienvenido!");
        dlgUser.setContentText("Introduce tu nombre de usuario:");
        Optional<String> optUser = dlgUser.showAndWait();
        if (optUser.isEmpty() || optUser.get().trim().isEmpty()) {
            primaryStage.close();
            return;
        }
        String usuario = optUser.get().trim();

        // 2) Elegir modo
        ChoiceDialog<String> dlgMode = new ChoiceDialog<>("Automático",
                Arrays.asList("Automático", "Manual", "Ranking"));
        dlgMode.setTitle("Modo de juego");
        dlgMode.setHeaderText("Selecciona un modo");
        dlgMode.setContentText("Modo:");
        Optional<String> optMode = dlgMode.showAndWait();
        if (optMode.isEmpty()) {
            primaryStage.close();
            return;
        }

        switch (optMode.get()) {
            case "Automático" -> {
                HanoiModel model = new HanoiModel();
                HanoiviewAuto view = new HanoiviewAuto();
                new HanoiController(model, view, primaryStage);
                primaryStage.setTitle("Torres de Hanoi – Automático");
                primaryStage.setScene(view.getScene());
            }
            case "Manual" -> {
                ExtensionHanoiModel model = new ExtensionHanoiModel(3);
                HanoiviewManual view = new HanoiviewManual(model);
                new HanoiControllerManual(model, view, primaryStage, usuario);
                primaryStage.setTitle("Torres de Hanoi – Manual - Usuario: " + usuario);
                primaryStage.setScene(view.getScene());
            }
            case "Ranking" -> {
                // Abre el ranking en una nueva ventana y sale para no mostrar
                // el primaryStage vacío
                HanoiControllerRanking.openRanking(primaryStage);
                return;
            }
        }

        // Solo para Automático o Manual
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}