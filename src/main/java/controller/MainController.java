// MainController.java
package controller;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.ExtensionHanoiModel;
import model.HanoiModel;
import view.HanoiviewAuto;
import view.HanoiviewManual;

import java.util.Arrays;
import java.util.Optional;

public class MainController {
    private final Stage primaryStage;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        iniciarAplicacion();
    }

    private void iniciarAplicacion() {
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
            case "Automático" -> iniciarModoAutomatico();
            case "Manual" -> iniciarModoManual(usuario);
            case "Ranking" -> abrirRanking();
        }
    }

    private void iniciarModoAutomatico() {
        HanoiModel model = new HanoiModel();
        HanoiviewAuto view = new HanoiviewAuto();
        new HanoiController(model, view, primaryStage);
        primaryStage.setTitle("Torres de Hanoi – Automático");
        primaryStage.setScene(view.getScene());
        primaryStage.show();
    }

    private void iniciarModoManual(String usuario) {
        ExtensionHanoiModel model = new ExtensionHanoiModel(3);
        HanoiviewManual view = new HanoiviewManual(model);
        new HanoiControllerManual(model, view, primaryStage, usuario);
        primaryStage.setTitle("Torres de Hanoi – Manual - Usuario: " + usuario);
        primaryStage.setScene(view.getScene());
        primaryStage.show();
    }

    private void abrirRanking() {
        HanoiControllerRanking.openRanking();
    }
}