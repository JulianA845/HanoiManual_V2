package view;

import ficheros.Juego;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HanoiviewRanking {
    private final TableView<Juego> tableView = new TableView<>();

    // Botones para eliminar datos
    private final Button btnBorrarTodo = new Button("Borrar Todas las Partidas");
    private final Button btnBorrarUsuario = new Button("Borrar Usuario Seleccionado");
    private final Button btnBorrarPartida = new Button("Borrar Partida Seleccionada");
    private final Button btnActualizar = new Button("Actualizar");

    private final Scene scene;

    public HanoiviewRanking() {
        // 1) Limpio cualquier definición anterior (por ejemplo, de FXML)
        tableView.getColumns().clear();

        // 2) Política para no dejar hueco al final
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // 3) Columnas
        TableColumn<Juego, Integer> posicionColumn = new TableColumn<>("Posición");
        posicionColumn.setCellValueFactory(cd ->
                new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(cd.getValue()) + 1)
        );
        posicionColumn.setSortable(false);

        TableColumn<Juego, String> usuarioColumn = new TableColumn<>("Usuario");
        usuarioColumn.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        TableColumn<Juego, Integer> discosColumn = new TableColumn<>("Discos");
        discosColumn.setCellValueFactory(new PropertyValueFactory<>("cantidadDiscos"));
        TableColumn<Juego, Integer> movimientosColumn = new TableColumn<>("Movimientos");
        movimientosColumn.setCellValueFactory(new PropertyValueFactory<>("movimientosManuales"));
        TableColumn<Juego, Double> exactitudColumn = new TableColumn<>("Exactitud (%)");
        exactitudColumn.setCellValueFactory(new PropertyValueFactory<>("porcentajeExactitud"));
        TableColumn<Juego, Integer> puntajeColumn = new TableColumn<>("Puntaje");
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

        // 7) Configurar botones
        configurarBotones();

        // 8) Layout de botones
        HBox botonesRow1 = new HBox(10, btnBorrarPartida, btnBorrarUsuario);
        HBox botonesRow2 = new HBox(10, btnBorrarTodo, btnActualizar);

        botonesRow1.setAlignment(Pos.CENTER);
        botonesRow2.setAlignment(Pos.CENTER);

        VBox botonesContainer = new VBox(5, botonesRow1, botonesRow2);
        botonesContainer.setAlignment(Pos.CENTER);

        // Layout principal
        VBox root = new VBox(10, tableView, botonesContainer);
        root.setPadding(new Insets(10));
        scene = new Scene(root, 800, 550);
    }

    private void configurarBotones() {
        // Estilos de botones
        btnBorrarTodo.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold;");
        btnBorrarUsuario.setStyle("-fx-background-color: #f57c00; -fx-text-fill: white; -fx-font-weight: bold;");
        btnBorrarPartida.setStyle("-fx-background-color: #fbc02d; -fx-text-fill: black; -fx-font-weight: bold;");
        btnActualizar.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-weight: bold;");

        // Tamaños mínimos
        btnBorrarTodo.setMinWidth(150);
        btnBorrarUsuario.setMinWidth(150);
        btnBorrarPartida.setMinWidth(150);
        btnActualizar.setMinWidth(150);
    }

    /** Pasa los datos ya ordenados al TableView */
    public void setData(ObservableList<Juego> datos) {
        tableView.setItems(datos);
    }

    public Scene getScene() {
        return scene;
    }

    // Getters para los botones (para que el controlador pueda asignar eventos)
    public Button getBtnBorrarTodo() {
        return btnBorrarTodo;
    }

    public Button getBtnBorrarUsuario() {
        return btnBorrarUsuario;
    }

    public Button getBtnBorrarPartida() {
        return btnBorrarPartida;
    }

    public Button getBtnActualizar() {
        return btnActualizar;
    }

    // Getter para obtener la partida seleccionada
    public Juego getPartidaSeleccionada() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    // Getter para obtener el índice de la partida seleccionada
    public int getIndicePartidaSeleccionada() {
        return tableView.getSelectionModel().getSelectedIndex();
    }

    // Método para limpiar la selección
    public void limpiarSeleccion() {
        tableView.getSelectionModel().clearSelection();
    }
}