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

    /**
     * Borra todas las partidas del archivo.
     * @throws IOException si hay problemas al escribir el archivo
     */
    public void borrarTodasLasPartidas() throws IOException {
        try (FileWriter fw = new FileWriter(file, false)) {
            // Escribir archivo vacío (truncar contenido)
            fw.write("");
        }
    }

    /**
     * Borra todas las partidas de un usuario específico.
     * @param usuario nombre del usuario cuyas partidas se van a eliminar
     * @throws IOException si hay problemas de lectura/escritura
     */
    public void borrarPartidasDeUsuario(String usuario) throws IOException {
        List<String> todasLasPartidas = leerPartidas();
        List<String> partidasAMantener = new ArrayList<>();

        for (String linea : todasLasPartidas) {
            String[] datos = linea.split("\\|");
            if (datos.length >= 2) {
                String usuarioPartida = datos[1];
                // Mantener solo las partidas que NO son del usuario a eliminar
                if (!usuarioPartida.equalsIgnoreCase(usuario)) {
                    partidasAMantener.add(linea);
                }
            }
        }

        // Reescribir el archivo con las partidas que se mantienen
        reescribirArchivo(partidasAMantener);
    }

    /**
     * Borra una partida específica basada en su posición en la lista.
     * @param indice índice de la partida a eliminar (basado en 0)
     * @throws IOException si hay problemas de lectura/escritura
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public void borrarPartidaPorIndice(int indice) throws IOException {
        List<String> todasLasPartidas = leerPartidas();

        if (indice < 0 || indice >= todasLasPartidas.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }

        todasLasPartidas.remove(indice);
        reescribirArchivo(todasLasPartidas);
    }

    /**
     * Obtiene la lista de usuarios únicos que tienen partidas guardadas.
     * @return lista de nombres de usuarios únicos
     * @throws IOException si hay problemas de lectura
     */
    public List<String> obtenerUsuarios() throws IOException {
        List<String> usuarios = new ArrayList<>();
        Set<String> usuariosUnicos = new HashSet<>();

        List<String> partidas = leerPartidas();
        for (String linea : partidas) {
            String[] datos = linea.split("\\|");
            if (datos.length >= 2) {
                String usuario = datos[1];
                if (usuariosUnicos.add(usuario)) { // add() retorna true si es nuevo
                    usuarios.add(usuario);
                }
            }
        }

        return usuarios;
    }

    /**
     * Reescribe completamente el archivo con la lista de partidas proporcionada.
     * @param partidas lista de líneas a escribir en el archivo
     * @throws IOException si hay problemas de escritura
     */
    private void reescribirArchivo(List<String> partidas) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            for (String linea : partidas) {
                bw.write(linea);
                bw.newLine();
            }
        }
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