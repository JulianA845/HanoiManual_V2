package ficheros;

import java.io.*;
import java.util.*;

/**
 * Crea/valida el archivo .bin donde se almacenan las partidas.
 */
public class ManejadorDeArchivos {

    private final File file;

    public ManejadorDeArchivos(String rutaArchivo) throws IOException {
        file = new File(rutaArchivo);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public void guardarPartida(Juego juego) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(juego.toString());
            bw.newLine();
        }
    }

    /**
     * Lee todas las partidas almacenadas.
     * @return lista de líneas (cada una con formato No|Usuario|...|Puntaje)
     * @throws IOException si hay problemas de lectura
     */
    public List<String> leerPartidas() throws IOException {
        List<String> partidas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                partidas.add(linea);
            }
        }
        return partidas;
    }

    // Opcional: si quieres mantener compatibilidad con la firma antigua:
    @Deprecated
    public void guardarPartida(String usuario,
                               int numDiscos,
                               int movimientosManuales,
                               double porcentajeExactitud,
                               int puntaje) throws IOException {
        guardarPartida(new Juego(
           usuario, numDiscos,
            movimientosManuales, porcentajeExactitud, puntaje
        ));
    }
}