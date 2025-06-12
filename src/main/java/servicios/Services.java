package servicios;

import javafx.animation.TranslateTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.HanoiModel;
import view.HanoiviewAuto;
import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class Services {
    private static final double DISC_HEIGHT = 20;
    private static final double LIFT_DURATION  = 0.4;
    private static final double MOVE_DURATION  = 0.8;
    private static final double LOWER_DURATION = 0.4;
    private static final double BUFFER        = 20;

    private final HanoiModel model;
    private final HanoiviewAuto view;
    private final Stage      stage;
    private Queue<HanoiModel.Move> queue;
    private boolean solving;

    public Services(HanoiModel model, HanoiviewAuto view, Stage stage) {
        this.model = model;
        this.view  = view;
        this.stage = stage;
    }

    // 1) Unification "reset" y "initialize" en un solo methods
    public void startNewGame(int discs) {
        if (solving) return;
        model.initialize(discs);
        view.getMovesText().clear();
        refreshView();
    }

    public void beginSolve() {
        if (solving) return;
        solving = true;
        setControls(false);
        queue = new LinkedList<>(model.getAutoMoves());
        processNext();
    }

    public void stopSolve() {
        queue.clear();
        solving = false;
        setControls(true);
    }

    private void processNext() {
        if (!solving || queue.isEmpty()) {
            finishing();
            return;
        }
        HanoiModel.Move m = queue.poll();
        animate(m.from(), m.to(), () -> {
            int disc = model.getTowers().get(m.from()).pop();
            model.getTowers().get(m.to()).push(disc);
            model.recordMove(disc, m.from(), m.to());
            refreshView();
            processNext();
        });
    }

    private void finishing() {
        solving = false;
        setControls(true);
    }

    private void setControls(boolean enabled) {
        view.getDiscCountCombo().setDisable(!enabled);
        view.getSolveButton().setDisable(!enabled);
        view.getResetButton().setDisable(!enabled);
        view.getSaveButton().setDisable(!enabled);
        view.getCancelButton().setDisable(enabled);
    }

    private void animate(int from, int to, Runnable onDone) {
        Pane source = view.getTowers()[from];
        if (source.getChildren().size() <= 2) {
            onDone.run();
            return;
        }

        Rectangle disc = (Rectangle) source.getChildren()
                .removeLast();
        Pane sceneRoot = (Pane) view.getScene().getRoot();
        Bounds sb     = source.localToScene(source.getBoundsInLocal());
        double startX = sb.getMinX() + disc.getLayoutX();
        double startY = sb.getMinY() + disc.getLayoutY();
        disc.setLayoutX(startX);
        disc.setLayoutY(startY);
        sceneRoot.getChildren().add(disc);

        Rectangle rod = (Rectangle) source.getChildren().getFirst();
        double rodTop = rod.localToScene(rod.getBoundsInLocal()).getMinY();
        double liftBy = (startY - rodTop) + BUFFER;

        Pane target = view.getTowers()[to];
        Bounds tb   = target.localToScene(target.getBoundsInLocal());
        double discW = disc.getWidth();
        double centerX = tb.getMinX() + (target.getPrefWidth() - discW) / 2;
        int level = (int) target.getChildren().stream()
                .filter(n -> n instanceof Rectangle && ((Rectangle)n).getHeight() == DISC_HEIGHT)
                .count();
        double destY = tb.getMinY() + target.getPrefHeight() - 5 - DISC_HEIGHT * (level + 1);

        TranslateTransition lift  = makeTrans(disc, -liftBy,               0, LIFT_DURATION);
        TranslateTransition moveX = makeTrans(disc,  0,         centerX - startX, MOVE_DURATION);
        TranslateTransition lower = makeTrans(disc, destY - (startY - liftBy), 0, LOWER_DURATION);

        SequentialTransition seq = new SequentialTransition(lift, moveX, lower);
        seq.setOnFinished(_ -> {
            sceneRoot.getChildren().remove(disc);
            target.getChildren().add(disc);
            onDone.run();
            if (!solving) finishing();
        });
        seq.play();
    }

    // crea un TranslateTransition reutilizable
    private TranslateTransition makeTrans(Rectangle node,
                                          double byY, double byX,
                                          double seconds) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(seconds), node);
        t.setByX(byX);
        t.setByY(byY);
        return t;
    }

    private void refreshView() {
        for (int i = 0; i < 3; i++) {
            view.updateTower(i, model.getTowers().get(i), model.getTotalDiscs());
        }
        view.getMovesText().setText(String.join("\n", model.getMovesHistory()));
    }

    public void saveMoves() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fc.showSaveDialog(stage);
        if (file == null) return;

        try (var w = new BufferedWriter(new FileWriter(file))) {
            for (String m : model.getMovesHistory()) {
                w.write(m); w.newLine();
            }
            new Alert(Alert.AlertType.INFORMATION,
                    "Movimientos guardados en:\n" + file.getAbsolutePath()
            ).showAndWait();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Error al guardar:\n" + ex.getMessage())
                    .showAndWait();
        }
    }
}