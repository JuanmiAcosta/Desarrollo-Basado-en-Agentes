
package entorno;

import agente.Posicion;

/**
 *
 * @author jorge
 */
public class Entorno {
    // Varaibles
    private Mapa mapa;
    private Posicion posAgente,
                                    posObjetivo;
    private static final int ID_AGENTE = 9,
                                               ID_OBJETIVO = 3;
    
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

    public void setPosAgente(Posicion posAgente) {
        this.posAgente = posAgente;
    }

    public void setPosObjetivo(Posicion posObjetivo) {
        this.posObjetivo = posObjetivo;
    }
    
    
}
