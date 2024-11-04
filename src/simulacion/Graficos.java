package simulacion;

import agente.Acciones;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author juanmi
 */
public class Graficos extends JFrame {

    private int[][] matriz;
    private JTextArea textAreaTraza;
    private JPanel panelMatriz;

    private final static int AGENTE = 9, OBJETIVO = 3, MURO = -1, SUELO = 0, CIRCULO = 7; // Agregado CIRCULO

    // Nuevo atributo para la dirección del agente
    private Acciones direccionAgente; 

    public Graficos(String textoInicial, int[][] matriz, Acciones direccionAgente) {
        this.matriz = matriz;
        this.direccionAgente = direccionAgente; // Inicializar dirección del agente
        inicializarComponentes(textoInicial);
    }
    
    public Graficos(){
        this.matriz=null;
        this.direccionAgente=null;
    }

    private void inicializarComponentes(String textoInicial) {
        // Configuración de la ventana principal
        setTitle("Simulación del Agente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Panel de la matriz a la izquierda
        panelMatriz = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarMatriz(g);
            }
        };
        panelMatriz.setPreferredSize(new Dimension(400, 600));
        panelMatriz.setBackground(Color.WHITE);

        // Área de texto para la traza de ejecución a la derecha
        textAreaTraza = new JTextArea();
        textAreaTraza.setEditable(false);
        textAreaTraza.setText(textoInicial);
        textAreaTraza.setLineWrap(true);
        textAreaTraza.setWrapStyleWord(true);
        JScrollPane scrollPaneTraza = new JScrollPane(textAreaTraza);
        scrollPaneTraza.setPreferredSize(new Dimension(400, 600));

        // Añadir los paneles al contenedor principal
        add(panelMatriz, BorderLayout.WEST);
        add(scrollPaneTraza, BorderLayout.EAST);

        setVisible(true);
    }

    private void dibujarMatriz(Graphics g) {
        if (matriz == null) {
            return;
        }

        int filas = matriz.length;
        int columnas = matriz[0].length;

        // Calcular el tamaño de las celdas como el mínimo entre el ancho y el alto disponible
        int anchoCelda = Math.min(400 / columnas, 600 / filas);
        int altoCelda = anchoCelda; // Hacer que la altura de la celda sea igual a su ancho

        // Calcular el offset para centrar la matriz en la ventana
        int offsetX = (400 - (anchoCelda * columnas)) / 2;
        int offsetY = (600 - (altoCelda * filas)) / 2;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Determinar el color de fondo según el valor
                if (matriz[i][j] == SUELO) {
                    g.setColor(Color.WHITE);
                    g.fillRect(offsetX + j * anchoCelda, offsetY + i * altoCelda, anchoCelda, altoCelda);
                } else if (matriz[i][j] == MURO) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(offsetX + j * anchoCelda, offsetY + i * altoCelda, anchoCelda, altoCelda);
                } else if (matriz[i][j] == OBJETIVO) {
                    // Dibujar el objetivo como un rombo rojo
                    g.setColor(Color.RED);
                    int[] xPoints = {
                        offsetX + j * anchoCelda + anchoCelda / 2,
                        offsetX + j * anchoCelda + anchoCelda,
                        offsetX + j * anchoCelda + anchoCelda / 2,
                        offsetX + j * anchoCelda
                    };
                    int[] yPoints = {
                        offsetY + i * altoCelda,
                        offsetY + i * altoCelda + altoCelda / 2,
                        offsetY + i * altoCelda + altoCelda,
                        offsetY + i * altoCelda + altoCelda / 2
                    };
                    g.fillPolygon(xPoints, yPoints, 4);
                } else if (matriz[i][j] == AGENTE) {
                    // Dibujar el agente como un triángulo negro que apunta en la dirección especificada
                    g.setColor(Color.BLACK);
                    int[] xPoints;
                    int[] yPoints;
                    switch (direccionAgente) {
                        case ARR: // Arriba
                            xPoints = new int[]{
                                offsetX + j * anchoCelda + anchoCelda / 2,
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda + altoCelda
                            };
                            break;
                        case ABA: // Abajo
                            xPoints = new int[]{
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda / 2
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda
                            };
                            break;
                        case IZQ: // Izquierda
                            xPoints = new int[]{
                                offsetX + j * anchoCelda + anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda,
                                offsetX + j * anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda / 2
                            };
                            break;
                        case DER: // Derecha
                            xPoints = new int[]{
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda + altoCelda / 2
                            };
                            break;
                        case ARRIZQ: // Arriba izquierda
                            xPoints = new int[]{
                                offsetX + j * anchoCelda + anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda,
                                offsetX + j * anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda / 2
                            };
                            break;
                        case ARRDER: // Arriba derecha
                            xPoints = new int[]{
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda + altoCelda / 2
                            };
                            break;
                        case ABAIZQ: // Abajo izquierda
                            xPoints = new int[]{
                                offsetX + j * anchoCelda + anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda,
                                offsetX + j * anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda / 2
                            };
                            break;
                        case ABADER: // Abajo derecha
                            xPoints = new int[]{
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda + anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda + altoCelda / 2
                            };
                            break;
                        default:
                            xPoints = new int[]{
                                offsetX + j * anchoCelda + anchoCelda / 2,
                                offsetX + j * anchoCelda,
                                offsetX + j * anchoCelda
                            };
                            yPoints = new int[]{
                                offsetY + i * altoCelda,
                                offsetY + i * altoCelda + altoCelda,
                                offsetY + i * altoCelda + altoCelda
                            };
                            break;
                    }
                    g.fillPolygon(xPoints, yPoints, 3);
                } else if (matriz[i][j] == CIRCULO) {
                    // Dibujar un círculo pequeño azul oscuro
                    g.setColor(Color.BLUE.darker()); // Color azul oscuro
                    g.fillOval(
                            offsetX + j * anchoCelda + anchoCelda / 4,
                            offsetY + i * altoCelda + altoCelda / 4,
                            anchoCelda / 2,
                            altoCelda / 2
                    ); // Dibujar círculo
                }

                // Dibujar el contorno de la celda
                g.setColor(Color.BLACK);
                g.drawRect(offsetX + j * anchoCelda, offsetY + i * altoCelda, anchoCelda, altoCelda);
            }
        }
    }

    public void agregarTraza(String nuevaAccion) {
        // Insertar la nueva acción al inicio de la traza
        textAreaTraza.insert(nuevaAccion + "\n" + "--------------------------------------------------------------------------------------------\n\n\n", 0);
    }

    public void actualizarMatriz(int[][] nuevaMatriz, Acciones nuevaDireccionAgente) {
        this.matriz = nuevaMatriz;
        this.direccionAgente = nuevaDireccionAgente; // Actualizar dirección del agente
        panelMatriz.repaint(); // Redibujar el panel con la matriz actualizada
    }
}
