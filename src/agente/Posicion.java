
package agente;

import java.util.Objects;

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
    
    // Método de copia
    public Posicion(Posicion otra) {
        this.fila = otra.fila;
        this.col = otra.col;
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
    
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Posicion posicion = (Posicion) obj;
        return this.fila == posicion.fila && this.col == posicion.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, col);
    }
    
    /*
    equals(): Compara si dos objetos Posicion tienen los mismos valores en fila y col. 
    Si ambos son iguales, devuelve true; de lo contrario, devuelve false.
    
    hashCode(): Genera un código hash basado en fila y col usando Objects.hash. 
    Esto asegura que dos objetos Posicion con los mismos valores de fila y columna 
    generen el mismo código hash, permitiendo que el HashMap los trate como iguales.
    */
}
