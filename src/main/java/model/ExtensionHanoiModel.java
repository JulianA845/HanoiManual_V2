package model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExtensionHanoiModel {

    private final HanoiModel baseModel;
    private final Stack<Integer>[] towers;
    private int discCount;
    private int movimientosManuales; // Para tracking de movimientos del usuario

    @SuppressWarnings("unchecked")
    public ExtensionHanoiModel(int discCount) {
        this.discCount = discCount;
        this.movimientosManuales = 0;
        this.baseModel = new HanoiModel(discCount);
        
        // Acceder a las torres del modelo base usando reflexión
        try {
            Field towersField = HanoiModel.class.getDeclaredField("towers");
            towersField.setAccessible(true);
            this.towers = (Stack<Integer>[]) towersField.get(baseModel);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo acceder a las torres del modelo base", e);
        }
    }

    /**
     * Devuelve la pila de discos de la torre indicada.
     * El tope de la pila es el disco superior.
     */
    public Stack<Integer> getTower(int index) {
        if (index >= 0 && index < 3) {
            return towers[index];
        }
        throw new IllegalArgumentException("Índice de torre inválido: " + index);
    }

    /**
     * Intenta mover el disco superior de la torre 'from' a la torre 'to'.
     * @return true si el movimiento es válido y se ha realizado, false en caso contrario.
     */
    public boolean moverDisco(int from, int to) {
        // Validar índices
        if (from < 0 || from >= 3 || to < 0 || to >= 3) {
            return false;
        }
        
        // No se puede mover a la misma torre
        if (from == to) {
            return false;
        }
        
        Stack<Integer> torreOrigen = towers[from];
        Stack<Integer> torreDestino = towers[to];
        
        // La torre origen debe tener al menos un disco
        if (torreOrigen.isEmpty()) {
            return false;
        }
        
        // Si la torre destino no está vacía, el disco a mover debe ser menor
        if (!torreDestino.isEmpty() && torreOrigen.peek() > torreDestino.peek()) {
            return false;
        }
        
        // Realizar el movimiento
        int disco = torreOrigen.pop();
        torreDestino.push(disco);
        movimientosManuales++;
        
        // Registrar el movimiento en el modelo base para historial
        baseModel.recordMove(disco, from, to);
        
        return true;
    }

    /**
     * Devuelve el número total de discos del juego.
     * Método necesario para compatibilidad con el controlador.
     */
    public int getTotalDiscs() {
        return discCount;
    }

    /**
     * Reinicia el juego con el número actual de discos.
     */
    public void reiniciar() {
        movimientosManuales = 0;
        baseModel.initialize(discCount);
    }

    /**
     * Reinicia el juego con un nuevo número de discos.
     */
    public void reiniciar(int nuevosDiscos) {
        this.discCount = nuevosDiscos;
        this.movimientosManuales = 0;
        baseModel.initialize(nuevosDiscos);
    }

    /**
     * Verifica si el juego ha sido completado.
     * @return true si todos los discos están en la torre 2 (destino)
     */
    public boolean juegoCompletado() {
        return towers[2].size() == discCount && 
               towers[0].isEmpty() && 
               towers[1].isEmpty();
    }

    /**
     * Devuelve el número de movimientos manuales realizados.
     */
    public int getMovimientosManuales() {
        return movimientosManuales;
    }

    /**
     * Calcula el número mínimo de movimientos para resolver el puzzle.
     * Formula: 2^n - 1
     */
    public int getMovimientosOptimos() {
        return (int) Math.pow(2, discCount) - 1;
    }

    /**
     * Calcula el porcentaje de exactitud basado en movimientos óptimos vs manuales.
     * @return porcentaje entre 0.0 y 100.0
     */
    public double calcularPorcentajeExactitud() {
        if (movimientosManuales == 0) {
            return 0.0;
        }
        
        int movimientosOptimos = getMovimientosOptimos();
        // Porcentaje = (movimientos óptimos / movimientos manuales) * 100
        // Si el usuario hizo menos movimientos que los óptimos, se limita a 100%
        double porcentaje = ((double) movimientosOptimos / movimientosManuales) * 100.0;
        return Math.min(porcentaje, 100.0);
    }

    /**
     * Calcula el puntaje basado en el porcentaje de exactitud.
     * Puntaje máximo = número de discos * 100
     */
    public int calcularPuntaje() {
        double porcentaje = calcularPorcentajeExactitud();
        int puntajeMaximo = discCount * 100; // 3 discos = 300, 4 discos = 400, etc.
        return (int) Math.round((porcentaje / 100.0) * puntajeMaximo);
    }

    /**
     * Acceso al modelo base para funcionalidades adicionales.
     */
    public HanoiModel getBaseModel() {
        return baseModel;
    }

    /**
     * Obtiene el historial de movimientos del modelo base.
     */
    public java.util.List<String> getHistorialMovimientos() {
        return baseModel.getMovesHistory();
    }

    public void move(int from, int to) {
        Stack<Integer> src = towers[from];
        Stack<Integer> dst = towers[to];
        if (src.isEmpty()) {
            throw new IllegalStateException("Torre " + from + " vacía");
        }
        int disk = src.peek();
        if (!dst.isEmpty() && dst.peek() < disk) {
            throw new IllegalArgumentException(
                    "No puedes poner disco " + disk + " sobre disco más pequeño " + dst.peek());
        }
        dst.push(src.pop());
    }

    /** Devuelve un snapshot (lista de listas) de las torres para dibujarlas. */
    public List<List<Integer>> getTowers() {
        List<List<Integer>> snapshot = new ArrayList<>();
        for (Stack<Integer> stack : towers) {
            snapshot.add(new ArrayList<>(stack));
        }
        return snapshot;
    }
}

