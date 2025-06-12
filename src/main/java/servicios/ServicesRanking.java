package servicios;

import ficheros.ManejadorDeArchivos;
import ficheros.Juego;
import view.HanoiviewRanking;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.List;
import java.util.Comparator;

public class ServicesRanking {

    private final HanoiviewRanking view;
    private final Stage stage;
    private ManejadorDeArchivos manejador;

    public ServicesRanking(HanoiviewRanking view, Stage stage) {
        this.view = view;
        this.stage = stage;
    }

    public void inicializarRanking() {
        try {
            manejador = new ManejadorDeArchivos("partidas.bin");
            cargarDatos();
        } catch (IOException e) {
            mostrarError("Error al cargar los datos: " + e.getMessage());
        }

        // Configurar la ventana
        stage.setTitle("Ranking de Jugadores");
        stage.setScene(view.getScene());
        stage.show();
    }

    private void cargarDatos() {
        try {
            List<String> lineasPartidas = manejador.leerPartidas();
            ObservableList<Juego> juegos = FXCollections.observableArrayList();

            for (String linea : lineasPartidas) {
                String[] datos = linea.split("\\|");
                if (datos.length == 6) {
                    Juego juego = new Juego(
                            datos[1],                   // usuario
                            Integer.parseInt(datos[2]), // cantidadDiscos
                            Integer.parseInt(datos[3]), // movimientosManuales
                            Double.parseDouble(datos[4]), // porcentajeExactitud
                            Integer.parseInt(datos[5])  // puntaje
                    );
                    juegos.add(juego);
                }
            }

            // Ordenar por puntaje (mayor a menor)
            juegos.sort(Comparator.comparingInt(Juego::getPuntaje).reversed());

            view.setData(juegos);

        } catch (IOException e) {
            mostrarError("Error al leer las partidas: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}