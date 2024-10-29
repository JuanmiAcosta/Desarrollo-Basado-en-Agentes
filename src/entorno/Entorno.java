package entorno;

import agente.Posicion;
import java.util.ArrayList;

/**
 *
 * @author jorge
 */
public class Entorno {

    // Varaibles
    private static final int[] MOV_DIAGONALES = {4,5,6,7};
    
    private static final int ID_AGENTE = 9,
            ID_OBJETIVO = 3, RASTRO = 7;
    private Mapa mapa;
    private Posicion posAgente,
            posObjetivo;

    // Constructor
    public Entorno(Mapa mapa, Posicion posAgente, Posicion posObjetivo) {
        this.mapa = mapa;
        this.posAgente = posAgente;
        this.posObjetivo = posObjetivo;
        inicializarEntorno();
    }

    public Entorno() {
        this(null, new Posicion(0, 0), new Posicion(0, 0));
    }

    public Entorno(Entorno otroEntorno) {
        this(otroEntorno.mapa, otroEntorno.posAgente, otroEntorno.posObjetivo);
    }

    // Metodos
    private void inicializarEntorno() {
        mapa.colocarItem(posAgente, ID_AGENTE);
        mapa.colocarItem(posObjetivo, ID_OBJETIVO);
    }

    public Mapa getMapa() {
        return mapa;
    }

    public Posicion getPosAgente() {
        return posAgente;
    }

    public Posicion getPosObjetivo() {
        return posObjetivo;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public void setPosAgente(Posicion posAgente, Posicion posAnterior) {
        this.posAgente = posAgente;
        mapa.colocarItem(posAgente, ID_AGENTE);
        mapa.colocarItem(posAnterior, RASTRO);
    }

    public void setPosObjetivo(Posicion posObjetivo) {
        this.posObjetivo = posObjetivo;
    }

    public ArrayList<Boolean> getMovimientos() {
        ArrayList<Boolean> movimientos = new ArrayList<>();

        // Direcciones cardinales
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila() - 1, posAgente.getCol())));   // Arriba
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila() + 1, posAgente.getCol())));   // Abajo
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila(), posAgente.getCol() - 1)));   // Izquierda
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila(), posAgente.getCol() + 1)));   // Derecha

        // Direcciones diagonales
        movimientos.add(testDiagonal(new Posicion(posAgente.getFila() - 1, posAgente.getCol() - 1),MOV_DIAGONALES[0])); // Arriba-Izquierda
        movimientos.add(testDiagonal(new Posicion(posAgente.getFila() - 1, posAgente.getCol() + 1),MOV_DIAGONALES[1])); // Arriba-Derecha
        movimientos.add(testDiagonal(new Posicion(posAgente.getFila() + 1, posAgente.getCol() - 1),MOV_DIAGONALES[2])); // Abajo-Izquierda
        movimientos.add(testDiagonal(new Posicion(posAgente.getFila() + 1, posAgente.getCol() + 1),MOV_DIAGONALES[3])); // Abajo-Derecha

        return movimientos;
    }

    public boolean casillaDisponible(Posicion pos) {
        return mapa.posCorrecta(pos);
    }
    
    public boolean testDiagonal(Posicion pos, int mov){
        return mapa.movDiagonalValido(pos, mov);
    }

    @Override
    public String toString() {
        return "Entorno{" + "mapa=" + mapa + ", posAgente=" + posAgente + ", posObjetivo=" + posObjetivo + '}';
    }
}
