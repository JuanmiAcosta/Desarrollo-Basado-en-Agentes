package agentes;

import utiles.Posicion;
import utiles.Sensores;
import comportamientos.*;
import jade.core.AID;
import utiles.Acciones;
import jade.core.Agent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import simulacion.Graficos;
import utiles.GestorDF;

/**
 *
 * @author jorge
 */
public class AgenteBarco extends Agent {

    // Variables
    private Posicion posAgente,
            posObj;
    private Sensores sensores;
    private ArrayList<Acciones> movDisponibles;
    private HashMap<Acciones, Double> movUtiles;
    private Acciones movDecidido;

    private Posicion posAnterior; //Para dibujar rastro

    private Boolean misionAcabada = false;

    //Gráficos en el agente
    private Graficos graficos;

    // Memoria del agente
    private HashMap<Posicion, Integer> memoria; // Memoria del agente

    // Constructores
    public AgenteBarco(Posicion posAgente, Posicion posObjetivo, Sensores sensores, Graficos g) {
        this.posAgente = posAgente;
        this.posObj = posObjetivo;
        this.sensores = sensores;
        this.movDisponibles = new ArrayList<>();
        this.movUtiles = new HashMap<>();
        this.memoria = new HashMap<>(); // Inicializar la memoria
        this.posAnterior = new Posicion(0, 0);
        this.graficos = g;
    }

    public AgenteBarco() {
        this(null, null, null, null);
    }

    // Metodos 
    @Override
    public void setup() { //Omite const. con parámetros y usa el sin parámetros

        System.out.println("Agente Barco iniciado: " + getLocalName());

        Object[] args = getArguments();
        if (args != null && args.length >= 4) {
            this.posAgente = (Posicion) args[0];
            this.posObj = (Posicion) args[1];
            this.sensores = (Sensores) args[2];
            this.movDisponibles = new ArrayList<>();
            this.movUtiles = new HashMap<>();
            this.memoria = new HashMap<>(); // Inicializar la memoria
            this.posAnterior = new Posicion(0, 0);
            this.graficos = (Graficos) args[3];
            this.actualizarMemoria();

        } else {
            System.out.println("Error: Parámetros de inicialización insuficientes para el agente.");
            doDelete(); // Eliminar agente si los parámetros son insuficientes
            return;
        }

        GestorDF.registrarAgente(this, "explorador", "barco-vikingo");

        addBehaviour(new AnalisisEntorno(this));        // Behaviour 1: Analisis del entorno
        addBehaviour(new Decision(this));       // Behaviour 2: Decision en base al entorno
        addBehaviour(new RealizacionMov(this));     // Behaviour 3: Realizacion del movimiento
        addBehaviour(new RepercusionEnt(this, sensores.getEntorno()));     // Behaviour 4: Repercusion en el entorno
        addBehaviour(new ComunicacionBarco(this));
    }

    public Sensores getSensores() {
        return sensores;
    }

    public void getMovDisponibles() {
        this.movDisponibles = sensores.analizarEntorno();
    }

    public HashMap<Acciones, Double> getMovUtiles() {
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

    public void setMovDecidido(Acciones movDecidido) {
        this.movDecidido = movDecidido;
    }

    public Acciones getMovDecidido() {
        return movDecidido;
    }

    private Posicion simularMovimiento(Acciones movimiento) {
        Posicion posSiguiente = new Posicion(posAgente); // Crear una copia de la posición actual

        switch (movimiento) {
            case ARR:
                posSiguiente.setFila(posAgente.getFila() - 1);  // Arriba
                break;
            case ABA:
                posSiguiente.setFila(posAgente.getFila() + 1);  // Abajo
                break;
            case IZQ:
                posSiguiente.setCol(posAgente.getCol() - 1);  // Izquierda
                break;
            case DER:
                posSiguiente.setCol(posAgente.getCol() + 1);  // Derecha
                break;
            case ARRIZQ:
                posSiguiente.setFila(posAgente.getFila() - 1);  // Arriba-Izquierda
                posSiguiente.setCol(posAgente.getCol() - 1);
                break;
            case ARRDER:
                posSiguiente.setFila(posAgente.getFila() - 1);  // Arriba-Derecha
                posSiguiente.setCol(posAgente.getCol() + 1);
                break;
            case ABAIZQ:
                posSiguiente.setFila(posAgente.getFila() + 1);  // Abajo-Izquierda
                posSiguiente.setCol(posAgente.getCol() - 1);
                break;
            case ABADER:
                posSiguiente.setFila(posAgente.getFila() + 1);  // Abajo-Derecha
                posSiguiente.setCol(posAgente.getCol() + 1);
                break;
            default:
                // Manejo de un caso inesperado (opcional)
                throw new IllegalArgumentException("Movimiento no válido: " + movimiento);
        }

        return posSiguiente; // Devolver la nueva posición
    }

    public void setMisionAcabada() {
        this.misionAcabada = true;
    }

    public Boolean getMisionAcabada() {
        return this.misionAcabada;
    }

    public void movUtiles() {
        movUtiles.clear(); // Suponiendo que movUtiles es un Map<Acciones, Double>

        HashMap<Acciones, Double> utilidades = new HashMap<>(); // Correcto

        // Iterar sobre las acciones disponibles
        for (Acciones accion : movDisponibles) {
            // Simular el movimiento y calcular su utilidad
            Posicion nuevaPos = this.simularMovimiento(accion);
            double utilidad = calcularUtilidad(nuevaPos, posObj); // Calcular la utilidad para la nueva posición
            utilidades.put(accion, utilidad); // Agregar al mapa
        }

        // Actualizar movUtiles con las utilidades calculadas
        movUtiles.putAll(utilidades);
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

    public void decidirMov() {
        Acciones mov = null; // Inicializamos mov como null
        double minUtilidad = Double.MAX_VALUE;

        Double utilidadActual = calcularUtilidad(this.posAgente, this.posObj);

        // Iterar sobre las entradas en movUtiles
        for (Map.Entry<Acciones, Double> entrada : movUtiles.entrySet()) {
            Acciones accion = entrada.getKey();  // Obtener la acción
            Double utilidadSiguiente = entrada.getValue(); // Obtener la utilidad actual

            // Simulamos el movimiento para obtener la posición resultante
            Posicion posSiguiente = simularMovimiento(accion);

            // Consultamos cuántas veces ha pasado por esta posición
            int vecesPasadas = memoria.getOrDefault(posSiguiente, 0);

            // Penalización adicional si la utilidad de la posición siguiente es peor
            double penalizacionUtilidad = utilidadActual < utilidadSiguiente ? (utilidadSiguiente - utilidadActual) * 100 : 0;

            // Calcular la utilidad total considerando las penalizaciones
            double utilidadTotal = utilidadSiguiente + 150 * vecesPasadas + penalizacionUtilidad;

            // Verificamos si esta es la menor utilidad total
            if (utilidadTotal < minUtilidad) {
                minUtilidad = utilidadTotal;
                mov = accion; // Guardamos la acción
            }
        }

        this.movDecidido = mov;
    }

    public void realizarMov() {
        posAnterior = new Posicion(posAgente); // Guardar la posición anterior

        switch (this.movDecidido) {
            case ARR:
                posAgente.setFila(posAgente.getFila() - 1);  // Arriba
                break;
            case ABA:
                posAgente.setFila(posAgente.getFila() + 1);  // Abajo
                break;
            case IZQ:
                posAgente.setCol(posAgente.getCol() - 1);  // Izquierda
                break;
            case DER:
                posAgente.setCol(posAgente.getCol() + 1);  // Derecha
                break;
            case ARRIZQ:
                posAgente.setFila(posAgente.getFila() - 1);  // Arriba-Izquierda
                posAgente.setCol(posAgente.getCol() - 1);
                break;
            case ARRDER:
                posAgente.setFila(posAgente.getFila() - 1);  // Arriba-Derecha
                posAgente.setCol(posAgente.getCol() + 1);
                break;
            case ABAIZQ:
                posAgente.setFila(posAgente.getFila() + 1);  // Abajo-Izquierda
                posAgente.setCol(posAgente.getCol() - 1);
                break;
            case ABADER:
                posAgente.setFila(posAgente.getFila() + 1);  // Abajo-Derecha
                posAgente.setCol(posAgente.getCol() + 1);
                break;
        }

        sensores.incrementarEnergia(); // Incrementar energía del agente
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
        StringBuilder movimientosDisponibles = new StringBuilder("DISPON. : { ");

        // Agregar acciones disponibles junto con su utilidad
        for (Acciones accion : Acciones.values()) {
            // Verificar la disponibilidad usando el índice
            if (movDisponibles.contains(accion)) {
                double utilidad = movUtiles.get(accion); // Obtener la utilidad
                movimientosDisponibles.append(String.format("%s: %.2e, ", accion.name(), utilidad));
            }
        }

        // Eliminar la última coma y espacio si hay acciones disponibles
        if (movimientosDisponibles.length() > 2) {
            movimientosDisponibles.setLength(movimientosDisponibles.length() - 2); // Quitar la última coma y espacio
        }
        movimientosDisponibles.append(" }");

        String direccion = (movDecidido != null) ? movDecidido.name() : "INVALIDA"; // Usar name() para obtener el nombre de la acción

        return "Agente {"
                + "\n   " + movimientosDisponibles
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

    public HashMap<Posicion, Integer> getMemoria() {
        return this.memoria;
    }
}
