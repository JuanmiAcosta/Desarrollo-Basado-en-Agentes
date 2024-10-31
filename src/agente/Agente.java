package agente;

import comportamientos.*;
import jade.core.Agent;
import java.util.ArrayList;
import java.util.HashMap;
import simulacion.Graficos;

/**
 *
 * @author jorge
 */
public class Agente extends Agent {

    // Variables
    private Posicion posAgente,
            posObj;
    private Sensores sensores;
    private ArrayList<Boolean> movDisponibles;
    private ArrayList<Double> movUtiles;
    private int movDecidido;
    private Posicion posAnterior; //Para dibujar rastro
    private int movRealizar;

    //Gráficos en el agente
    private Graficos graficos;

    // Memoria del agente
    private HashMap<Posicion, Integer> memoria; // Memoria del agente

    // Constructores
    public Agente(Posicion posAgente, Posicion posObjetivo, Sensores sensores, Graficos g) {
        this.posAgente = posAgente;
        this.posObj = posObjetivo;
        this.sensores = sensores;
        this.movDisponibles = new ArrayList<>();
        this.movUtiles = new ArrayList<>();
        this.memoria = new HashMap<>(); // Inicializar la memoria
        this.posAnterior = new Posicion(0, 0);
        this.movRealizar = -1;
        this.graficos = g;
    }

    public Agente() {
        this(null, null, null, null);
    }

    // Metodos 
    @Override
    public void setup() { //Omite const. con parámetros y usa el sin parámetros

        Object[] args = getArguments();
        if (args != null && args.length >= 4) {
            this.posAgente = (Posicion) args[0];
            this.posObj = (Posicion) args[1];
            this.sensores = (Sensores) args[2];
            this.movDisponibles = new ArrayList<>();
            this.movUtiles = new ArrayList<>();
            this.memoria = new HashMap<>(); // Inicializar la memoria
            this.posAnterior = new Posicion(0, 0);
            this.movRealizar = -1;
            this.graficos = (Graficos) args[3];
            this.actualizarMemoria();

        } else {
            System.out.println("Error: Parámetros de inicialización insuficientes para el agente.");
            doDelete(); // Eliminar agente si los parámetros son insuficientes
            return;
        }

        addBehaviour(new AnalisisEntorno(this));        // Behaviour 1: Analisis del entorno
        addBehaviour(new Decision(this));       // Behaviour 2: Decision en base al entorno
        addBehaviour(new RealizacionMov(this));     // Behaviour 3: Realizacion del movimiento
        addBehaviour(new RepercusionEnt(this, sensores.getEntorno()));     // Behaviour 4: Repercusion en el entorno
    }

    public Sensores getSensores() {
        return sensores;
    }

    public void getMovDisponibles() {
        this.movDisponibles = sensores.analizarEntorno();
    }

    public ArrayList<Double> getMovUtiles() {
        return this.movUtiles;
    }

    public Posicion getPosAgente() {
        return posAgente;
    }

    public Posicion getPosAnterior() {
        return posAnterior;
    }

    public void setPosAgente(Posicion posAgente) {
        this.posAgente = posAgente;
    }

    public Posicion getPosObj() {
        return posObj;
    }

    public void setMovRealizar(int movRealizar) {
        this.movRealizar = movRealizar;
    }

    public int getMovRealizar() {
        return movRealizar;
    }

    private Posicion simularMovimiento(int movimiento) {
        Posicion posSiguiente = new Posicion(posAgente);

        switch (movimiento) {
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
            case 4:
                posSiguiente.setFila(posAgente.getFila() - 1);  // Arriba-Izquierda
                posSiguiente.setCol(posAgente.getCol() - 1);
                break;
            case 5:
                posSiguiente.setFila(posAgente.getFila() - 1);  // Arriba-Derecha
                posSiguiente.setCol(posAgente.getCol() + 1);
                break;
            case 6:
                posSiguiente.setFila(posAgente.getFila() + 1);  // Abajo-Izquierda
                posSiguiente.setCol(posAgente.getCol() - 1);
                break;
            case 7:
                posSiguiente.setFila(posAgente.getFila() + 1);  // Abajo-Derecha
                posSiguiente.setCol(posAgente.getCol() + 1);
                break;
        }

        return posSiguiente;
    }

    public void movUtiles() {
        movUtiles.clear();

        for (int i = 0; i < movDisponibles.size(); i++) {
            if (!movDisponibles.get(i)) {
                movUtiles.add(Double.MAX_VALUE);
            } else {
                movUtiles.add(calcularUtilidad(this.simularMovimiento(i), posObj));
            }
        }
    }

    private int distanciaManhattan(Posicion posAgente, Posicion posObj) {
        return Math.abs(posObj.getFila() - posAgente.getFila()) + Math.abs(posObj.getCol() - posAgente.getCol());
    }

    // Calcula la distancia euclidiana entre dos posiciones
    private double distanciaEuclidea(Posicion posAgente, Posicion posObj) {
        int diffFila = posObj.getFila() - posAgente.getFila();
        int diffCol = posObj.getCol() - posAgente.getCol();
        return Math.sqrt(diffFila * diffFila + diffCol * diffCol);
    }

    // Calcula la utilidad basada en la media de la distancia Manhattan y la distancia Euclidiana
    private double calcularUtilidad(Posicion posAgente, Posicion posObj) {
        int distanciaManhattan = distanciaManhattan(posAgente, posObj);
        double distanciaEuclidea = distanciaEuclidea(posAgente, posObj);

        // Devuelve la media entre ambas distancias
        return (distanciaManhattan + distanciaEuclidea) / 2.0;
    }

    public int decidirMov() {
        int mov = -1;
        Double minUtilidad = Double.MAX_VALUE;
        int minVecesPasadas = Integer.MAX_VALUE;

        for (int i = 0; i < movUtiles.size(); i++) {
            Double utilidad = movUtiles.get(i);

            // Simulamos el movimiento para obtener la posición resultante
            Posicion posSiguiente = simularMovimiento(i);

            // Consultamos cuántas veces ha pasado por esta posición
            int vecesPasadas = memoria.getOrDefault(posSiguiente, 0);

            if (((utilidad + vecesPasadas * vecesPasadas) < minUtilidad)) {// && (vecesPasadas <=2)) { 
                minUtilidad = (utilidad + vecesPasadas * vecesPasadas); // penalizamos cuadráticamente
                minVecesPasadas = vecesPasadas;
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
            case 4:
                posAgente.setFila(posAgente.getFila() - 1);  // Arriba-Izquierda
                posAgente.setCol(posAgente.getCol() - 1);
                break;
            case 5:
                posAgente.setFila(posAgente.getFila() - 1);  // Arriba-Derecha
                posAgente.setCol(posAgente.getCol() + 1);
                break;
            case 6:
                posAgente.setFila(posAgente.getFila() + 1);  // Abajo-Izquierda
                posAgente.setCol(posAgente.getCol() - 1);
                break;
            case 7:
                posAgente.setFila(posAgente.getFila() + 1);  // Abajo-Derecha
                posAgente.setCol(posAgente.getCol() + 1);
                break;
        }

        movDecidido = mov;
        sensores.incrementarEnergia();
    }

    // Método para actualizar la memoria del agente
    public void actualizarMemoria() {
        Posicion pos = new Posicion(posAgente);
        memoria.put(pos, memoria.getOrDefault(pos, 0) + 1);
    }

    public void imprimirMemoria() {
        System.out.println("Memoria del Agente:");
        for (HashMap.Entry<Posicion, Integer> entry : memoria.entrySet()) {
            Posicion posicion = entry.getKey();
            Integer vecesPasadas = entry.getValue();
            System.out.println(String.format("Posición: %s, Veces Pasadas: %d", posicion, vecesPasadas));
        }
    }

    @Override
    public String toString() {
        // Convertir los ArrayList a su formato legible usando direcciones descriptivas
        String movimientos = String.format(
                "DISPON. : { ARRIBA: %b, ABAJO: %b, IZQUIERDA: %b, DERECHA: %b, ARRIBA-IZQUIERDA: %b, ARRIBA-DERECHA: %b, ABAJO-IZQUIERDA: %b, ABAJO-DERECHA: %b }",
                movDisponibles.get(0), movDisponibles.get(1), movDisponibles.get(2), movDisponibles.get(3),
                movDisponibles.get(4), movDisponibles.get(5), movDisponibles.get(6), movDisponibles.get(7)
        );

        // Asumiendo que tienes un ArrayList<Double> para la utilidad de movimientos
        String utilidadMovimientos = String.format(
                "UTIL. : { ARRIBA: %.2e, ABAJO: %.2e, IZQUIERDA: %.2e, DERECHA: %.2e, ARRIBA-IZQUIERDA: %.2e, ARRIBA-DERECHA: %.2e, ABAJO-IZQUIERDA: %.2e, ABAJO-DERECHA: %.2e }",
                movUtiles.get(0), movUtiles.get(1), movUtiles.get(2), movUtiles.get(3),
                movUtiles.get(4), movUtiles.get(5), movUtiles.get(6), movUtiles.get(7)
        );

        // Convertir el valor de movDecidido en una dirección de texto descriptiva
        String direccion;
        switch (movDecidido) {
            case 0:
                direccion = "ARR.";
                break;
            case 1:
                direccion = "AB.";
                break;
            case 2:
                direccion = "IZQ.";
                break;
            case 3:
                direccion = "DER.";
                break;
            case 4:
                direccion = "ARR-IZQ";
                break;
            case 5:
                direccion = "ARR-DER";
                break;
            case 6:
                direccion = "AB-IZQ";
                break;
            case 7:
                direccion = "AB-DER";
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

    public boolean objEncontrado() {
        return posAgente.equals(posObj);
    }

    public Graficos getGraficos() {
        return this.graficos;
    }
}
