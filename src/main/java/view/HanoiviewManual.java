package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.ExtensionHanoiModel;


public class HanoiviewManual {

    private final Scene scene;
    private BorderPane root;
    private ComboBox<Integer> discCountCombo;
    private Button resetButton, saveButton, cancelButton;
    private Button botonCol1, botonCol2, botonCol3;
    
    private ExtensionHanoiModel model;
    
    private Pane[] panes;
    private HBox center;
    
    private double towerHeight, paneWidth, discHeight, maxDiscWidth, step;
    private int selectedCol = -1;

    public HanoiviewManual(ExtensionHanoiModel model) {
        this.model = model;              // asignarlo antes de cualquier llamado a setupGame/updateDiscs
        buildUI();
        scene = new Scene(root, 600, 400);

        // valores por defecto
        towerHeight = 200;
        discHeight  = 20;
        paneWidth   = 180;

        // inicializamos array de panes y HBox central
        panes = new Pane[3];
        center = new HBox(20);
        center.setPadding(new Insets(20));
        center.setAlignment(Pos.BOTTOM_CENTER);
        root.setCenter(center);

        // preparamos botones de movimiento
        botonCol1.setOnAction(e -> selectColumn(0));
        botonCol2.setOnAction(e -> selectColumn(1));
        botonCol3.setOnAction(e -> selectColumn(2));

        // reset crea el estado inicial con el número elegido
        resetButton.setOnAction(e -> {
            int n = discCountCombo.getValue();
            setupGame(n);
        });

        // primer arranque (ahora model ya está inicializado)
        setupGame(discCountCombo.getValue());
    }

    private void setupGame(int discCount) {
        // recalcular paso y ancho máximo
        maxDiscWidth = paneWidth - 20;
        step = (maxDiscWidth - 20) / Math.max(1, discCount - 1);

        // limpiar HBox y reconstruir torres vacías
        center.getChildren().clear();
        for (int i = 0; i < 3; i++) {
            Pane p = new Pane();
            p.setPrefSize(paneWidth, towerHeight + discHeight);
            p.setStyle("-fx-border-color: black; -fx-background-color: #EEE;");

// varilla
            Rectangle peg = new Rectangle(10, towerHeight);
            peg.setId("peg");
            peg.setFill(Color.DARKGRAY);

            peg.setTranslateX((paneWidth - 10) / 2);
            p.getChildren().add(peg);

// base
            Rectangle base = new Rectangle(paneWidth, discHeight);
            base.setId("base");
            base.setFill(Color.SADDLEBROWN);

            base.setTranslateY(towerHeight);
            p.getChildren().add(base);


            panes[i] = p;
            center.getChildren().add(p);
        }

        // inicializar estructuras internas: torre 0 con todos los discos
        ArrayList[] towers = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            towers[i] = new ArrayList<>();
        }
        for (int size = 1; size <= discCount; size++) {
            towers[0].add(size);
        }
        selectedCol = -1;

        // dibujar discos según towers[]
        updateDiscs();
        }

    public Scene getScene() {
        return scene;
    }

    private void buildUI() {
        root = new BorderPane();

        // Top: selector de número de discos y botones generales
        discCountCombo = new ComboBox<>();
        discCountCombo.getItems().addAll(3, 4, 5, 6);
        discCountCombo.setValue(3);

        resetButton  = new Button("Reset");
        saveButton   = new Button("Save");
        cancelButton = new Button("Cancel");

        HBox topBar = new HBox(10, discCountCombo, resetButton, saveButton, cancelButton);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER);
        root.setTop(topBar);

        // Bottom: botones para seleccionar columnas
        botonCol1 = new Button("Columna 1");
        botonCol2 = new Button("Columna 2");
        botonCol3 = new Button("Columna 3");
        HBox bottomBar = new HBox(10, botonCol1, botonCol2, botonCol3);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setAlignment(Pos.CENTER);
        root.setBottom(bottomBar);
    }

    private void selectColumn(int idx) {
        if (selectedCol == -1) {
            selectedCol = idx;
        } else {
            model.move(selectedCol, idx);
            selectedCol = -1;
            updateDiscs();
        }
    }
    // ✅ Métodos getter correctos
    public ComboBox<Integer> getDiscCountCombo() {
        return discCountCombo;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getBotonColumna(int idx) {
        return switch (idx) {
            case 0 -> botonCol1;
            case 1 -> botonCol2;
            case 2 -> botonCol3;
            default -> throw new IndexOutOfBoundsException("Columna inválida");
        };
    }

    private void updateDiscs() {
    // repintar cada torre
    List<List<Integer>> towers = model.getTowers();       // 1) Obténlas del modelo
    for (int i = 0; i < panes.length; i++) {
        Pane p = panes[i];
        p.getChildren().removeIf(n -> n instanceof Rectangle
                                   && "disc".equals(n.getId()));

        List<Integer> tower = towers.get(i);              // 2) towers ya está inicializada
        for (int j = 0; j < tower.size(); j++) {
            int size = tower.get(j);
            double w = maxDiscWidth - (size - 1) * step;
            Rectangle disc = new Rectangle(w, discHeight);
            disc.setId("disc");
            disc.setFill(Color.hsb(j * 360.0 / tower.size(), 0.7, 0.8));
            disc.setTranslateX((paneWidth - w) / 2);
            disc.setTranslateY(towerHeight - (j + 1) * discHeight);
            p.getChildren().add(disc);
        }
    }
}
    // ✅ GETTERS CORREGIDOS:
    public Pane[] getTowerPanes() {
        return panes;
    }

    public Pane getTowerPane(int idx) {
        if (idx < 0 || idx > 2) {
            throw new IndexOutOfBoundsException("Índice de torre inválido");
        }
        return panes[idx];
    }
}