package servicios;

import ficheros.ManejadorDeArchivos;
import ficheros.Juego;
import view.HanoiviewRanking;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import java.io.IOException;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;

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
            configurarEventosBotones();
        } catch (IOException e) {
            mostrarError("Error al cargar los datos: " + e.getMessage());
        }

        // Configurar la ventana
        stage.setTitle("Ranking de Jugadores");
        stage.setScene(view.getScene());
        stage.show();
    }

    private void configurarEventosBotones() {
        // Botón para borrar todas las partidas
        view.getBtnBorrarTodo().setOnAction(e -> borrarTodasLasPartidas());

        // Botón para borrar partidas de un usuario específico
        view.getBtnBorrarUsuario().setOnAction(e -> borrarPartidasDeUsuario());

        // Botón para borrar una partida específica
        view.getBtnBorrarPartida().setOnAction(e -> borrarPartidaSeleccionada());

        // Botón para actualizar la tabla
        view.getBtnActualizar().setOnAction(e -> cargarDatos());
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

    private void borrarTodasLasPartidas() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Borrar todas las partidas?");
        confirmacion.setContentText("Esta acción eliminará permanentemente todas las partidas guardadas. ¿Desea continuar?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                manejador.borrarTodasLasPartidas();
                cargarDatos(); // Actualizar la tabla
                mostrarInformacion("Todas las partidas han sido eliminadas exitosamente.");
            } catch (IOException e) {
                mostrarError("Error al eliminar las partidas: " + e.getMessage());
            }
        }
    }

    private void borrarPartidasDeUsuario() {
        try {
            List<String> usuarios = manejador.obtenerUsuarios();

            if (usuarios.isEmpty()) {
                mostrarInformacion("No hay usuarios para eliminar.");
                return;
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(usuarios.get(0), usuarios);
            dialog.setTitle("Seleccionar Usuario");
            dialog.setHeaderText("Eliminar partidas de usuario");
            dialog.setContentText("Seleccione el usuario cuyas partidas desea eliminar:");

            Optional<String> resultado = dialog.showAndWait();
            if (resultado.isPresent()) {
                String usuarioSeleccionado = resultado.get();

                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Confirmar eliminación");
                confirmacion.setHeaderText("¿Borrar partidas del usuario: " + usuarioSeleccionado + "?");
                confirmacion.setContentText("Esta acción eliminará permanentemente todas las partidas de este usuario. ¿Desea continuar?");

                Optional<ButtonType> confirmResult = confirmacion.showAndWait();
                if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                    manejador.borrarPartidasDeUsuario(usuarioSeleccionado);
                    cargarDatos(); // Actualizar la tabla
                    mostrarInformacion("Las partidas del usuario '" + usuarioSeleccionado + "' han sido eliminadas exitosamente.");
                }
            }
        } catch (IOException e) {
            mostrarError("Error al eliminar las partidas del usuario: " + e.getMessage());
        }
    }

    private void borrarPartidaSeleccionada() {
        Juego partidaSeleccionada = view.getPartidaSeleccionada();
        int indiceSeleccionado = view.getIndicePartidaSeleccionada();

        if (partidaSeleccionada == null || indiceSeleccionado == -1) {
            mostrarInformacion("Por favor, seleccione una partida para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Borrar la partida seleccionada?");
        confirmacion.setContentText("Usuario: " + partidaSeleccionada.getUsuario() +
                "\nDiscos: " + partidaSeleccionada.getCantidadDiscos() +
                "\nPuntaje: " + partidaSeleccionada.getPuntaje() +
                "\n\n¿Desea eliminar esta partida?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                manejador.borrarPartidaPorIndice(indiceSeleccionado);
                cargarDatos(); // Actualizar la tabla
                view.limpiarSeleccion(); // Limpiar la selección
                mostrarInformacion("La partida ha sido eliminada exitosamente.");
            } catch (IOException e) {
                mostrarError("Error al eliminar la partida: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                mostrarError("Error: La partida seleccionada ya no existe.");
                cargarDatos(); // Recargar datos para sincronizar
            }
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}