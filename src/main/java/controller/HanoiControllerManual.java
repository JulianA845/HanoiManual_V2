package controller;

import javafx.stage.Stage;
import model.ExtensionHanoiModel;
import servicios.ServicesManual;
import view.HanoiviewManual;

public class HanoiControllerManual {
    private final ServicesManual aux;
    private final HanoiviewManual view;

    public HanoiControllerManual(ExtensionHanoiModel model, HanoiviewManual view, Stage stage, String usuario) {
        this.view = view;
        this.aux = new ServicesManual(model, view, stage, usuario);
        bindHandlers();
        aux.startNewGame(view.getDiscCountCombo().getValue());
    }

    // Centralizes binding de botones
    private void bindHandlers() {
        view.getDiscCountCombo().setOnAction(_ -> aux.startNewGame(view.getDiscCountCombo().getValue()));
        view.getBotonColumna(0). setOnAction(_ -> aux.handleColumnClick(0));
        view.getBotonColumna(1). setOnAction(_ -> aux.handleColumnClick(1));
        view.getBotonColumna(2). setOnAction(_ -> aux.handleColumnClick(2));
        view.getCancelButton().  setOnAction(_ -> aux.closeGame());
        view.getResetButton().   setOnAction(_ -> aux.startNewGame(view.getDiscCountCombo().getValue()));
        view.getSaveButton().    setOnAction(_ -> aux.saveGame());
    }
}