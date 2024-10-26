
package agente;

/**
 *
 * @author jorge
 */
public class Posicion {
    // Variables
    private int fila,
                          col;
    
    // Constructores
    public Posicion(int fila, int col) {
        this.fila = fila;
        this.col = col;
    }
    
    public Posicion() {
        this(0 ,0);
    }
    
    public Posicion(Posicion otraPos) {
        this(otraPos.fila, otraPos.col);
    }
    
    // Métodos
    public int getFila() {
        return fila;
    }

    public int getCol() {
        return col;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setCol(int col) {
        this.col = col;
    }  

    @Override
    public String toString() {
        return "Posicion{" + "fila=" + fila + ", col=" + col + '}';
    }
}
