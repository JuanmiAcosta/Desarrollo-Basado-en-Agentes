package agente;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author jorge
 */
public class Agente {
    // Variables

    private Posicion posAgente,
            posObj;
    private Sensores sensores;
    private ArrayList<Boolean> movDisponibles;
    private ArrayList<Integer> movUtiles;
    
    private int movDecidido;
    private Posicion posAnterior;

    // Constructores
    public Agente(Posicion posAgente, Posicion posObjetivo, Sensores sensores) {
        this.posAgente = posAgente;
        this.posObj = posObjetivo;
        this.sensores = sensores;
        this.movDisponibles = new ArrayList<>();
        this.movUtiles = new ArrayList<>();
    }

    public Agente() {
        this(null, null, null);
    }

    public Agente(Agente otroAgente) {
        this(otroAgente.posAgente, otroAgente.posObj, otroAgente.sensores);
    }

    // Metodos 
    public Sensores getSensores() {
        return sensores;
    }

    public void getMovDisponibles() {
        this.movDisponibles = sensores.analizarEntorno();
    }

    public ArrayList<Integer> getMovUtiles() {
        return this.movUtiles;
    }

    public Posicion getPosAgente() {
        return posAgente;
    }
    
    public Posicion getPosAnterior(){
        return posAnterior;
    }

    public void setPosAgente(Posicion posAgente) {
        this.posAgente = posAgente;
    }

    public Posicion getPosObj() {
        return posObj;
    }

    public void movUtiles() {
        movUtiles.clear();

        for (int i = 0; i < movDisponibles.size(); i++) {
            if (!movDisponibles.get(i)) {
                movUtiles.add(Integer.MAX_VALUE);
            } else {
                Posicion posSiguiente = new Posicion(posAgente);
                
                switch (i) {
                    case 0:
                        posSiguiente.setFila(posAgente.getFila() - 1);  // Arriba
                        break;
                    case 1:
                        posSiguiente.setFila(posAgente.getFila() + 1);  // Abajo
                        break;
                    case 2:
                        posSiguiente.setCol(posAgente.getCol() - 1);  // Izquierda
                        break;
                    case 3:
                        posSiguiente.setCol(posAgente.getCol() + 1);  // Derecha
                        break;
                }
                
                movUtiles.add(distanciaManhattan(posSiguiente, posObj));
            }
        }
    }

    private int distanciaManhattan(Posicion posAgente, Posicion posObj) {
        return Math.abs(posObj.getFila() - posAgente.getFila()) + Math.abs(posObj.getCol() - posAgente.getCol());
    }

    public int decidirMov() {
        int mov = -1,
                movPeque = Integer.MAX_VALUE;

        for (int i = 0; i < movUtiles.size(); i++) {
            if (movUtiles.get(i) < movPeque) {
                movPeque = movUtiles.get(i);
                mov = i;
            }
        }

        return mov;
    }

    public void realizarMov(int mov) {
        
        posAnterior = new Posicion(posAgente);
        
        switch (mov) {
            case 0:
                posAgente.setFila(posAgente.getFila() - 1);  // Arriba
                break;
            case 1:
                posAgente.setFila(posAgente.getFila() + 1);  // Abajo
                break;
            case 2:
                posAgente.setCol(posAgente.getCol() - 1);  // Izquierda
                break;
            case 3:
                posAgente.setCol(posAgente.getCol() + 1);  // Derecha
                break;
        }
        
        movDecidido=mov;
        sensores.incrementarEnergia();
    }

          @Override
    public String toString() {
        // Convertir los ArrayList a su formato legible
        String movimientos = String.format(
                "DISPON. : { ARRIBA: %b, ABAJO: %b, IZQUIERDA: %b, DERECHA: %b }",
                movDisponibles.get(0), movDisponibles.get(1), movDisponibles.get(2), movDisponibles.get(3)
        );

        // Asumiendo que tienes un ArrayList<Integer> para la utilidad de movimientos
        String utilidadMovimientos = String.format(
                "UTIL. : { ARRIBA: %d, ABAJO: %d, IZQUIERDA: %d, DERECHA: %d }",
                movUtiles.get(0), movUtiles.get(1), movUtiles.get(2), movUtiles.get(3)
        );
        
        String direccion;
        switch (movDecidido) { // Suponiendo que tienes un entero que se llama 'direccionEntero'
        case 0:
            direccion = "ARRIBA";
            break;
        case 1:
            direccion = "ABAJO";
            break;
        case 2:
            direccion = "IZQUIERDA";
            break;
        case 3:
            direccion = "DERECHA";
            break;
        default:
            direccion = "INVALIDA"; // En caso de un valor inesperado
            break;
    }

        return "Agente {"
                + "\n   " + movimientos
                + "\n   " + utilidadMovimientos
                + "\n   MOVIMIENTO DECIDIDO: " + direccion
                + "\n   ENERGÍA GASTADA: " + sensores.getEnergia()
                + "\n}";
    }
}
