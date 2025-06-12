package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Stack;

public class HanoiviewAuto {
    private final Scene scene;
    private BorderPane root;
    private ComboBox<Integer> discCountCombo;
    private Button solveButton, resetButton, saveButton, cancelButton;
    private TextArea movesText;
    private final Pane[] towerPanes;

    public HanoiviewAuto() {
        towerPanes = new Pane[3];
        buildUI();
        scene = new Scene(root, 950, 650);
    }

    private void buildUI() {
        root = new BorderPane();

        // Top: controles
        discCountCombo = createCombo();
        solveButton   = createStyled("Resolver");
        resetButton   = createStyled("Reiniciar");
        saveButton    = createStyled("Guardar");
        cancelButton  = createStyled("Cancelar");
        cancelButton.setDisable(true);

        HBox controls = new HBox(10,
            discCountCombo, solveButton, cancelButton, resetButton, saveButton
        );
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(15));
        root.setTop(controls);

        // Center: área con las 3 torres
        AnchorPane centerPane = new AnchorPane();
        double paneW = 200, paneH = 400;
        for (int i = 0; i < 3; i++) {
            Pane tower = createTowerPane(paneW, paneH);
            towerPanes[i] = tower;
            AnchorPane.setLeftAnchor(tower, 50 + i * (paneW + 50));
            AnchorPane.setTopAnchor(tower, 50.0);
            centerPane.getChildren().add(tower);
        }
        root.setCenter(centerPane);

        // Right: panel de movimientos, más ancho y alto
        movesText = new TextArea();
        movesText.setEditable(false);
        movesText.setPrefSize(320, 500);
        VBox right = new VBox(movesText);
        right.setPadding(new Insets(15));
        root.setRight(right);
    }

    private Pane createTowerPane(double w, double h) {
        Pane p = new Pane();
        p.setPrefSize(w, h);

        // varilla
        Rectangle rod = new Rectangle(10, h*0.5, Color.SADDLEBROWN);
        rod.setLayoutX((w-10)/2);
        rod.setLayoutY(h - h*0.5 - 5);

        // base
        Rectangle base = new Rectangle(w*0.8, 10, Color.SADDLEBROWN);
        base.setLayoutX(w*0.1);
        base.setLayoutY(h - 5);

        p.getChildren().addAll(rod, base);
        return p;
    }

    private ComboBox<Integer> createCombo() {
        ComboBox<Integer> cb = new ComboBox<>();
        cb.getItems().addAll(3, 4, 5, 6);
        cb.setValue(3);
        return cb;
    }

    private Button createStyled(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font:14px Arial; -fx-padding:8 15;");
        return btn;
    }

    /**
     * Actualiza la torre en la posición idx con la lista de discos
     * y el número total de discos para escalar anchos.
     */
    public void updateTower(int idx, Stack<Integer> discs, int totalDiscs) {
        Pane tower = towerPanes[idx];
        // Eliminamos únicamente los discos previos (alto == 20)
        tower.getChildren().removeIf(n -> n instanceof Rectangle && ((Rectangle)n).getHeight() == 20);

        double maxWidth = tower.getPrefWidth() * 0.8;
        double baseY    = tower.getPrefHeight() - 5;

        int level = 0;
        for (int size : discs) {
            double w = maxWidth * (size / (double) totalDiscs);
            Rectangle disc = new Rectangle(w, 20, Color.hsb(size * 40, 0.7, 0.7));
            disc.setArcWidth(8);
            disc.setArcHeight(8);
            disc.setStroke(Color.BLACK);

            double x = (tower.getPrefWidth() - w) / 2;
            double y = baseY - 20 * (level + 1);
            disc.setLayoutX(x);
            disc.setLayoutY(y);

            tower.getChildren().add(disc);
            level++;
        }
    }

    // Getters para el controlador
    public Scene getScene()                     { return scene; }
    public ComboBox<Integer> getDiscCountCombo() { return discCountCombo; }
    public Button getSolveButton()         { return solveButton;    }
    public Button getResetButton()              { return resetButton; }
    public Button getSaveButton()               { return saveButton; }
    public Button getCancelButton()             { return cancelButton; }
    public TextArea getMovesText()              { return movesText; }
    public Pane[] getTowers()                   { return towerPanes; }
}