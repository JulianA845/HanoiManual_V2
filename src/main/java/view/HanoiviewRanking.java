package view;

import ficheros.Juego;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class HanoiviewRanking {
    private final TableView<Juego> tableView = new TableView<>();
    private final TableColumn<Juego, Integer> posicionColumn = new TableColumn<>("Posición");
    private final TableColumn<Juego, String> usuarioColumn = new TableColumn<>("Usuario");
    private final TableColumn<Juego, Integer> discosColumn = new TableColumn<>("Discos");
    private final TableColumn<Juego, Integer> movimientosColumn = new TableColumn<>("Movimientos");
    private final TableColumn<Juego, Double> exactitudColumn = new TableColumn<>("Exactitud (%)");
    private final TableColumn<Juego, Integer> puntajeColumn = new TableColumn<>("Puntaje");
    private final Button backButton = new Button("Volver");
    private final Scene scene;

    public HanoiviewRanking() {
        // 1) Limpio cualquier definición anterior (por ejemplo, de FXML)
        tableView.getColumns().clear();

        // 2) Política para no dejar hueco al final
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 3) Columnas
        posicionColumn.setCellValueFactory(cd ->
            new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(cd.getValue()) + 1)
        );
        posicionColumn.setSortable(false);

        usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        discosColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadDiscos"));
        movimientosColumn.setCellValueFactory(new PropertyValueFactory<>("movimientosManuales"));
        exactitudColumn.setCellValueFactory(new PropertyValueFactory<>("porcentajeExactitud"));
        puntajeColumn.setCellValueFactory(new PropertyValueFactory<>("puntaje"));

        // 4) Anchos relativos (opcional, ajusta porcentajes a tu gusto)
        posicionColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        usuarioColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));
        discosColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.14));
        movimientosColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.18));
        exactitudColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));
        puntajeColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.20));

        // 5) Añadir todas las columnas al TableView
        tableView.getColumns().addAll(
            posicionColumn,
            usuarioColumn,
            discosColumn,
            movimientosColumn,
            exactitudColumn,
            puntajeColumn
        );

        // 6) Placeholder cuando no hay datos
        tableView.setPlaceholder(new Label("Sin partidas para mostrar"));

        // Layout
        VBox root = new VBox(10, tableView, backButton);
        root.setPadding(new Insets(10));
        scene = new Scene(root, 700, 450);
    }

    /** Pasa los datos ya ordenados al TableView */
    public void setData(ObservableList<Juego> datos) {
        tableView.setItems(datos);
    }

    public Scene getScene() {
        return scene;
    }

    public Button getBackButton() {
        return backButton;
    }
}