package view;

import ficheros.Juego;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class HanoiviewRanking {

    private final Scene scene;
    private final TableView<Juego> table;
    private final Button backButton;

    public HanoiviewRanking() {
        table = new TableView<>();
        backButton = new Button("Volver");
        buildUI();
        scene = new Scene(createRoot(), 600, 400);
    }

    private void buildUI() {
        TableColumn<Juego, Integer> colNum   = new TableColumn<>("NoPartida");
        colNum.setCellValueFactory(new PropertyValueFactory<>("numeroPartida"));

        TableColumn<Juego, String> colUser   = new TableColumn<>("Usuario");
        colUser.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        TableColumn<Juego, Integer> colDiscs = new TableColumn<>("Discos");
        colDiscs.setCellValueFactory(new PropertyValueFactory<>("cantidadDiscos"));

        TableColumn<Juego, Integer> colMoves = new TableColumn<>("Mov.Manuales");
        colMoves.setCellValueFactory(new PropertyValueFactory<>("movimientosManuales"));

        TableColumn<Juego, Double>  colPct   = new TableColumn<>("% Exactitud");
        colPct.setCellValueFactory(new PropertyValueFactory<>("porcentajeExactitud"));

        TableColumn<Juego, Integer> colScore = new TableColumn<>("Puntaje");
        colScore.setCellValueFactory(new PropertyValueFactory<>("puntaje"));

        table.getColumns().addAll(colNum, colUser, colDiscs, colMoves, colPct, colScore);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private BorderPane createRoot() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setCenter(table);
        root.setBottom(backButton);
        BorderPane.setMargin(backButton, new Insets(10));
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    public void setData(ObservableList<Juego> data) {
        table.setItems(data);
    }

    public Button getBackButton() {
        return backButton;
    }
}