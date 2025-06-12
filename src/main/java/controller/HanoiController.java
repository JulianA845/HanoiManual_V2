package controller;

import javafx.stage.Stage;
import model.HanoiModel;
import servicios.Services;
import view.HanoiviewAuto;

public class HanoiController {
    private final Services aux;
    private final HanoiviewAuto view;

    public HanoiController(HanoiModel model, HanoiviewAuto view, Stage stage) {
        this.view = view;
        this.aux = new Services(model, view, stage);
        bindHandlers();
        aux.startNewGame(view.getDiscCountCombo().getValue());
    }

    // Centralizes binding de botones
    private void bindHandlers() {
        view.getDiscCountCombo().setOnAction(_ -> aux.startNewGame(view.getDiscCountCombo().getValue()));
        view.getSolveButton().   setOnAction(_ -> aux.beginSolve());
        view.getCancelButton().  setOnAction(_ -> aux.stopSolve());
        view.getResetButton().   setOnAction(_ -> aux.startNewGame(view.getDiscCountCombo().getValue()));
        view.getSaveButton().    setOnAction(_ -> aux.saveMoves());
    }
}