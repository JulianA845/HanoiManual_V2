package servicios;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ExtensionHanoiModel;
import view.HanoiviewManual;
import ficheros.ManejadorDeArchivos;
import ficheros.Juego;
import javafx.scene.control.Alert;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Stack;

public class ServicesManual {
    private ExtensionHanoiModel model;
    private final HanoiviewManual view;
    private final Stage stage;
    private final String usuario;
    private int discoSeleccionado = -1; // -1 = ningún disco seleccionado
    private int columnaSeleccionada = -1; // columna del disco seleccionado
    private ManejadorDeArchivos manejador;
    private int numeroPartida = 1;

    public ServicesManual(ExtensionHanoiModel model, HanoiviewManual view, Stage stage, String usuario) {
        this.model = model;
        this.view = view;
        this.stage = stage;
        this.usuario = usuario;

        try {
            this.manejador = new ManejadorDeArchivos("partidas.bin");
        } catch (IOException e) {
            showError("Error al inicializar el archivo: " + e.getMessage());
        }
    }

    public void startNewGame(int discCount) {
        this.model = new ExtensionHanoiModel(discCount);
        discoSeleccionado = -1;
        columnaSeleccionada = -1;
        renderTowers();

        // Actualizar título de la ventana
        stage.setTitle("Torres de Hanoi – Manual (" + discCount + " discos)");
    }

    public void handleColumnClick(int columna) {
        var torre = model.getTower(columna);

        if (discoSeleccionado == -1) {
            // Primer click: seleccionar disco
            if (!torre.isEmpty()) {
                discoSeleccionado = torre.peek();
                columnaSeleccionada = columna;
                renderTowers(); // Re-renderizar para mostrar selección
            }
        } else {
            // Segundo click: intentar mover disco
            if (model.moverDisco(columnaSeleccionada, columna)) {
                // Movimiento exitoso
                renderTowers();

                // Verificar si el juego se completó
                if (model.juegoCompletado()) {
                    showGameCompleted();
                }
            } else {
                // Movimiento inválido
                showError("Movimiento inválido: No puedes colocar un disco grande sobre uno pequeño.");
            }

            // Resetear selección
            discoSeleccionado = -1;
            columnaSeleccionada = -1;
        }
    }

    private void renderTowers() {
        // 1) limpiamos solo los discos antiguos
        for (Pane pane : view.getTowerPanes()) {
            pane.getChildren().removeIf(n -> n instanceof Rectangle && "disc".equals(n.getId()));
        }

        final double DISC_HEIGHT = 20;
        final double BASE_WIDTH  = 40;
        int totalDiscs = model.getTotalDiscs();             // número total de discos en la partida
        double hueStep  = 360.0 / totalDiscs;               // paso de tono para la paleta

        // 2) por cada torre...
        for (int torreIndex = 0; torreIndex < 3; torreIndex++) {
            Stack<Integer> discosEnTorre = model.getTower(torreIndex);
            Pane torrePane = view.getTowerPane(torreIndex);

            // 3) dibujamos cada disco
            for (int nivel = 0; nivel < discosEnTorre.size(); nivel++) {
                int tamañoDisco = discosEnTorre.get(nivel);
                double width = BASE_WIDTH + (tamañoDisco - 1) * 20;
                Rectangle disco = new Rectangle(width, DISC_HEIGHT);
                disco.setId("disc");

                // 4) color de relleno según su tamaño
                double hue = (tamañoDisco - 1) * hueStep;
                disco.setFill(Color.hsb(hue, 0.7, 0.8));

                // 5) contorno: si es el tope de la torre seleccionada, lo resaltamos
                if (torreIndex == columnaSeleccionada
                        && nivel == discosEnTorre.size() - 1) {
                    disco.setStroke(Color.GOLD);
                    disco.setStrokeWidth(4);
                } else {
                    disco.setStroke(Color.BLACK);
                    disco.setStrokeWidth(1);
                }

                // 6) posición X centrada
                disco.setTranslateX((torrePane.getPrefWidth() - width) / 2);
                // 7) posición Y apilada desde abajo
                double y = torrePane.getPrefHeight()
                        - (nivel + 1) * (DISC_HEIGHT + 2)
                        - 10;
                disco.setTranslateY(y);

                torrePane.getChildren().add(disco);
            }
        }
    }


    private void showGameCompleted() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText("¡Juego Completado!");

        double porcentaje = model.calcularPorcentajeExactitud();
        int puntaje = model.calcularPuntaje();

        String mensaje = String.format(
                "Has completado el juego!\n\n" +
                        "Movimientos realizados: %d\n" +
                        "Movimientos óptimos: %d\n" +
                        "Porcentaje de exactitud: %.2f%%\n" +
                        "Puntaje obtenido: %d",
                model.getMovimientosManuales(),
                model.getMovimientosOptimos(),
                porcentaje,
                puntaje
        );

        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void saveGame() {
        if (!model.juegoCompletado()) {
            showError("Solo puedes guardar partidas completadas.");
            return;
        }

        try {
            Juego juego = new Juego(
                    usuario,
                    model.getTotalDiscs(),
                    model.getMovimientosManuales(),
                    model.calcularPorcentajeExactitud(),
                    model.calcularPuntaje()
            );

            manejador.guardarPartida(juego);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Guardado");
            alert.setContentText("Partida guardada exitosamente.");
            alert.showAndWait();

        } catch (IOException e) {
            showError("Error al guardar la partida: " + e.getMessage());
        }
    }

    public void closeGame() {
        stage.close();
    }

    private void showError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}