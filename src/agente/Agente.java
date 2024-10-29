package agente;

import java.util.ArrayList;
import java.util.HashMap;

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
    private ArrayList<Double> movUtiles;

    private int movDecidido;
    private Posicion posAnterior; //Para dibujar rastro

    // Memoria del agente
    private HashMap<Posicion, Integer> memoria; // Memoria del agente

    // Constructores
    public Agente(Posicion posAgente, Posicion posObjetivo, Sensores sensores) {
        this.posAgente = posAgente;
        this.posObj = posObjetivo;
        this.sensores = sensores;
        this.movDisponibles = new ArrayList<>();
        this.movUtiles = new ArrayList<>();
        this.memoria = new HashMap<>(); // Inicializar la memoria
        this.posAnterior = new Posicion(0,0);
        this.actualizarMemoria();
    }

    public Agente() {
        this(null, null, null);
        this.memoria = null;
    }

    public Agente(Agente otroAgente) {
        this(otroAgente.posAgente, otroAgente.posObj, otroAgente.sensores);
        this.memoria = new HashMap<>(otroAgente.memoria); // Copia la memoria
    }

    // Metodos 
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

        for (int i = 0; i < movUtiles.size(); i++) {
            Double utilidad = movUtiles.get(i);

            // Simulamos el movimiento para obtener la posición resultante
            Posicion posSiguiente = simularMovimiento(i);

            // Consultamos cuántas veces ha pasado por esta posición
            int vecesPasadas = memoria.getOrDefault(posSiguiente, 0);
            
            //if (!memoria.containsKey(simularMovimiento(i))){
                if (posAnterior.getFila() == simularMovimiento(i).getFila() && (Math.abs(posAnterior.getCol() - simularMovimiento(i).getCol()) == 2) ||
                    (posAnterior.getCol() == simularMovimiento(i).getCol() && (Math.abs(posAnterior.getFila() - simularMovimiento(i).getFila()) == 2))){
                    utilidad --;
                }
            //}
            
            if ((utilidad+vecesPasadas*2) < minUtilidad){ // ESTE IF SÓLO GOTADO PERO INEFICIENTE
                minUtilidad = (utilidad+vecesPasadas*2);
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

        movDecidido = mov;
        sensores.incrementarEnergia();
    }

    // Método para actualizar la memoria del agente
    public void actualizarMemoria() {
        Posicion pos = new Posicion(posAgente);
        memoria.put(pos, memoria.getOrDefault(posAgente, 0) + 1);
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
        // Convertir los ArrayList a su formato legible
        String movimientos = String.format(
                "DISPON. : { ARRIBA: %b, ABAJO: %b, IZQUIERDA: %b, DERECHA: %b }",
                movDisponibles.get(0), movDisponibles.get(1), movDisponibles.get(2), movDisponibles.get(3)
        );

        // Asumiendo que tienes un ArrayList<Integer> para la utilidad de movimientos
        String utilidadMovimientos = String.format(
                "UTIL. : { ARRIBA: %.2e, ABAJO: %.2e, IZQUIERDA: %.2e, DERECHA: %.2e }",
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
