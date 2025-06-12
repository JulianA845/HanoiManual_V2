package controller;

import javafx.stage.Stage;
import servicios.ServicesRanking;
import view.HanoiviewRanking;

public class HanoiControllerRanking {

    public HanoiControllerRanking(Stage stage) {
        HanoiviewRanking view = new HanoiviewRanking();
        ServicesRanking aux = new ServicesRanking(view, stage);
        aux.inicializarRanking();
    }
    // Centralizes binding de botones
    public static void openRanking() {
        Stage rankingStage = new Stage();
        new HanoiControllerRanking(rankingStage);
    }
}