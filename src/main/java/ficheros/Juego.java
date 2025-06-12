package ficheros;

public class Juego {
    private int numeroPartida;
    private String usuario;
    private int cantidadDiscos;
    private int movimientosManuales;
    private double porcentajeExactitud;
    private int puntaje;

    public Juego(int numeroPartida,
                 String usuario,
                 int cantidadDiscos,
                 int movimientosManuales,
                 double porcentajeExactitud,
                 int puntaje) {
        this.numeroPartida       = numeroPartida;
        this.usuario             = usuario;
        this.cantidadDiscos      = cantidadDiscos;
        this.movimientosManuales = movimientosManuales;
        this.porcentajeExactitud = porcentajeExactitud;
        this.puntaje             = puntaje;
    }

    public int getNumeroPartida() {
        return numeroPartida;
    }

    public void setNumeroPartida(int numeroPartida) {
        this.numeroPartida = numeroPartida;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getCantidadDiscos() {
        return cantidadDiscos;
    }

    public void setCantidadDiscos(int cantidadDiscos) {
        this.cantidadDiscos = cantidadDiscos;
    }

    public int getMovimientosManuales() {
        return movimientosManuales;
    }

    public void setMovimientosManuales(int movimientosManuales) {
        this.movimientosManuales = movimientosManuales;
    }

    public double getPorcentajeExactitud() {
        return porcentajeExactitud;
    }

    public void setPorcentajeExactitud(double porcentajeExactitud) {
        this.porcentajeExactitud = porcentajeExactitud;
    }

    public int getPuntaje() {
        return puntaje;
    }

    @Override
    public String toString() {
        // Formato: NoPartida|Usuario|numDiscos|movManuales|%Exactitud|puntaje
        return numeroPartida + "|" +
               usuario + "|" +
               cantidadDiscos + "|" +
               movimientosManuales + "|" +
               porcentajeExactitud + "|" +
               puntaje;
    }
}