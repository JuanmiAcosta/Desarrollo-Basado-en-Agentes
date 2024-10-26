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
    // Pregunta al entorno si los bumpers estan libres o no, entorno es el que tiene acceso a todo el mapa
    // agente le pregunta al entorno si puede ir arriba, abajo, izq o derecha
    // no se le puede pasar las posiciones al entorno debido a que el entorno sabe donde esta el agente 
    // agente solo ve lo que tiene alrededor y puede guardar todo lo que queramos

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

        sensores.incrementarEnergia();
    }

    @Override
    public String toString() {
        return "Agente{" + "posAgente=" + posAgente + ", posObj=" + posObj + ", sensores=" + sensores + ", movDisponibles=" + movDisponibles + ", movUtiles=" + movUtiles + '}';
    }
}
