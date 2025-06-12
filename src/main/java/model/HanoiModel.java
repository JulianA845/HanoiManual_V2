package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class HanoiModel {
    /** Un movimiento “de-to” que usa el controlador */
    public record Move(int from, int to) {}

    @SuppressWarnings("unchecked")
    private final Stack<Integer>[] towers = new Stack[3];
    private int discCount;

    private final List<String> movesHistory = new ArrayList<>();
    // lista de Move para animar paso a paso
    private final List<Move> autoMoves = new ArrayList<>();

    /** Constructor por defecto, arranca con 3 discos. */
    public HanoiModel() {
        this(3);
    }

    public HanoiModel(int discCount) {
        this.discCount = discCount;
        for (int i = 0; i < 3; i++) {
            towers[i] = new Stack<>();
        }
        initialize(discCount);
    }

    /** (Re)inicializa las torres para N discos en la torre 0. */
    public void initialize(int discs) {
        this.discCount = discs;
        movesHistory.clear();
        autoMoves.clear();
        for (Stack<Integer> t : towers) {
            t.clear();
        }
        for (int size = discs; size >= 1; size--) {
            towers[0].push(size);
        }
    }

    /** Genera y devuelve la lista de movimientos sin ejecutarlos: */
    public List<Move> getAutoMoves() {
        autoMoves.clear();
        generateMoves(discCount, 0, 2, 1);
        return List.copyOf(autoMoves);
    }

    /** Graba un movimiento en la cola interna */
    private void generateMoves(int n, int from, int to, int aux) {
        if (n == 0) return;
        generateMoves(n - 1, from, aux, to);
        autoMoves.add(new Move(from, to));
        generateMoves(n - 1, aux, to, from);
    }



    /** Los textos de los movimientos ya realizados (para el TextArea) */
    public List<String> getMovesHistory() {
        return List.copyOf(movesHistory);
    }

    /** Devuelve el nº total de discos (para escalar colores/anchos) */
    public int getTotalDiscs() {
        return discCount;
    }

    /** Permite al controlador acceder al Stack real de cada torre */
    public List<Stack<Integer>> getTowers() {
        // Arrays.asList no permite genéricos en tiempo de compilación, así que:
        List<Stack<Integer>> list = new ArrayList<>();
        Collections.addAll(list, towers);
        return list;
    }

    /**
     * Registra un movimiento en formato:
     * Movimiento 0: Disco 1 de Torre 1 a Torre 3
     */
    public void recordMove(int disc, int from, int to) {
        int idx = movesHistory.size();
        String line = String.format(
            "Movimiento %d: Disco %d de Torre %d a Torre %d",
            idx,          // número de movimiento (0-based)
            disc,         // tamaño del disco
            from + 1,     // torre origen (1-based)
            to + 1        // torre destino (1-based)
        );
        movesHistory.add(line);
    }

    // Agregar este método a HanoiModel
    public Stack<Integer> getTower(int index) {
        if (index >= 0 && index < 3) {
            return towers[index];
        }
        throw new IllegalArgumentException("Índice de torre inválido: " + index);
    }
}