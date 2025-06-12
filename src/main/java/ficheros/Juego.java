package ficheros;

public class Juego {
    private transient int ranking;
    private String usuario;
    private int cantidadDiscos;
    private int movimientosManuales;
    private double porcentajeExactitud;
    private int puntaje;

    public Juego(String usuario,
                 int cantidadDiscos,
                 int movimientosManuales,
                 double porcentajeExactitud,
                 int puntaje) {
        this.usuario             = usuario;
        this.cantidadDiscos      = cantidadDiscos;
        this.movimientosManuales = movimientosManuales;
        this.porcentajeExactitud = porcentajeExactitud;
        this.puntaje             = puntaje;
    }
    // getter / setter ranking
    public int getRanking() { return ranking; }

    public void setRanking(int ranking) { this.ranking = ranking; }

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
        return "|" +
               usuario + "|" +
               cantidadDiscos + "|" +
               movimientosManuales + "|" +
               porcentajeExactitud + "|" +
               puntaje;
    }
}