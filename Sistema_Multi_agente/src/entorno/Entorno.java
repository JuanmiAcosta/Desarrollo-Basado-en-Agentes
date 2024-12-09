package entorno;

import utiles.Acciones;
import utiles.Posicion;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author jorge
 */
public class Entorno {

    // Variables
    private static final int ID_AGENTE = 9,
            ID_OBJETIVO = 8,
            ID_JARL = 7;
    
    private Mapa mapa;
    private Posicion posAgente;
    private Posicion posJarl;
    private ArrayList<Posicion> posObjetivos;
  

    // Constructor
    public Entorno(Mapa mapa, Posicion posAgente, Posicion posJarl, ArrayList<Posicion> posObjetivos) {
        this.mapa = mapa;
        this.posAgente = posAgente;
        this.posJarl = posJarl;
        this.posObjetivos = posObjetivos;
        inicializarEntorno();
    }

    public Entorno() {
        this(null, new Posicion(0, 0), new Posicion(0, 0), null);
    }

    public Entorno(Entorno otroEntorno) {
        this(otroEntorno.mapa, otroEntorno.posAgente, otroEntorno.posJarl, otroEntorno.posObjetivos);
    }

    // Metodos
    private void inicializarEntorno() {
        mapa.colocarItem(posAgente, ID_AGENTE);
        mapa.colocarItem(posJarl, ID_JARL);

        if (!Objects.isNull(posObjetivos)) {
            for (Posicion objetivo : posObjetivos) {
                mapa.colocarItem(objetivo, ID_OBJETIVO);
            }
        }

    }

    public Mapa getMapa() {
        return mapa;
    }

    public Posicion getPosAgente() {
        return posAgente;
    }

    public ArrayList<Posicion> getPosObjetivos() {
        return posObjetivos;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public void setPosAgente(Posicion posAgente, Posicion posAnterior, Integer barriles) {
        this.posAgente = posAgente;
        mapa.colocarItem(posAgente, ID_AGENTE);
        mapa.colocarItem(posAnterior, barriles);
    }

    public ArrayList<Acciones> getMovimientos() {
        ArrayList<Acciones> movDisponibles = new ArrayList<>();

        // Verificar y agregar movimientos cardinales
        if (casillaDisponible(new Posicion(posAgente.getFila() - 1, posAgente.getCol()))) { // Arriba
            movDisponibles.add(Acciones.ARR);
        }
        if (casillaDisponible(new Posicion(posAgente.getFila() + 1, posAgente.getCol()))) { // Abajo
            movDisponibles.add(Acciones.ABA);
        }
        if (casillaDisponible(new Posicion(posAgente.getFila(), posAgente.getCol() - 1))) { // Izquierda
            movDisponibles.add(Acciones.IZQ);
        }
        if (casillaDisponible(new Posicion(posAgente.getFila(), posAgente.getCol() + 1))) { // Derecha
            movDisponibles.add(Acciones.DER);
        }

        // Verificar y agregar movimientos diagonales
        if (testDiagonal(new Posicion(posAgente.getFila() - 1, posAgente.getCol() - 1), Acciones.ARRIZQ)) { // Arriba-Izquierda
            movDisponibles.add(Acciones.ARRIZQ);
        }
        if (testDiagonal(new Posicion(posAgente.getFila() - 1, posAgente.getCol() + 1), Acciones.ARRDER)) { // Arriba-Derecha
            movDisponibles.add(Acciones.ARRDER);
        }
        if (testDiagonal(new Posicion(posAgente.getFila() + 1, posAgente.getCol() - 1), Acciones.ABAIZQ)) { // Abajo-Izquierda
            movDisponibles.add(Acciones.ABAIZQ);
        }
        if (testDiagonal(new Posicion(posAgente.getFila() + 1, posAgente.getCol() + 1), Acciones.ABADER)) { // Abajo-Derecha
            movDisponibles.add(Acciones.ABADER);
        }

        return movDisponibles; // Devolver la lista de movimientos disponibles
    }

    public boolean casillaDisponible(Posicion pos) {
        return mapa.posCorrecta(pos);
    }

    public boolean testDiagonal(Posicion pos, Acciones mov) {
        return mapa.movDiagonalValido(pos, mov);
    }

    @Override
    public String toString() {
        String str_a_devolver = "Entorno{" + "mapa=" + mapa + ", posAgente=" + posAgente + ", ";
        for (Posicion objetivo : posObjetivos) {
            str_a_devolver += " objetivo: " + objetivo.toString();
        }
        return str_a_devolver;

    }

}
