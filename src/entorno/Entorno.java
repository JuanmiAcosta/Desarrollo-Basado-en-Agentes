package entorno;

import agente.Posicion;
import java.util.ArrayList;

/**
 *
 * @author jorge
 */
public class Entorno {

    // Varaibles
    private static final int ID_AGENTE = 9,
            ID_OBJETIVO = 3, RASTRO =7;
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
        
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila() - 1, posAgente.getCol())));   // Arriba
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila() + 1, posAgente.getCol())));   // Abajo
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila(), posAgente.getCol() - 1)));   // Izquierda
        movimientos.add(casillaDisponible(new Posicion(posAgente.getFila(), posAgente.getCol() + 1)));   // Derecha

        return movimientos;
    }

    public boolean casillaDisponible(Posicion pos) {
        return mapa.posCorrecta(pos);
    }

    @Override
    public String toString() {
        return "Entorno{" + "mapa=" + mapa + ", posAgente=" + posAgente + ", posObjetivo=" + posObjetivo + '}';
    }
}
