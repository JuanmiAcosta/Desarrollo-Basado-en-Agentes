package simulacion;

import utiles.Acciones;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class Graficos extends JFrame {

    private int[][] matriz;
    private JTextArea textAreaTraza;
    private JPanel panelMatriz;

    private int panelWidth = 1000;
    private int panelHeight = 1000;

    private final static int AGENTE = 9, OBJETIVO = 8, JARL = 7, MURO = -1, SUELO = 0, SUELO1 = 1, SUELO2 = 2, SUELO3 = 3;

    private Acciones direccionAgente;

    // Mapa para almacenar las imágenes del agente según la dirección
    private Map<Acciones, Image> imagenesAgente;
    private Map<Integer, Image> imagenes;

    public Graficos(String textoInicial, int[][] matriz, Acciones direccionAgente) {
        this.matriz = matriz;
        this.direccionAgente = direccionAgente;
        cargarImagenes(); // Cargar imágenes del agente
        inicializarComponentes(textoInicial);
    }

    public Graficos() {
        this.matriz = null;
        this.direccionAgente = null;
        cargarImagenes();
    }

    private void cargarImagenes() {
        imagenesAgente = new HashMap<>();
        imagenes = new HashMap<>();
        try {
            imagenes.put(SUELO, ImageIO.read(getClass().getResource("/assets/SUELO.png")));
            imagenes.put(SUELO1, ImageIO.read(getClass().getResource("/assets/SUELO1.png")));
            imagenes.put(SUELO2, ImageIO.read(getClass().getResource("/assets/SUELO2.png")));
            imagenes.put(SUELO3, ImageIO.read(getClass().getResource("/assets/SUELO3.png")));
            imagenes.put(MURO, ImageIO.read(getClass().getResource("/assets/MURO.png")));
            imagenes.put(OBJETIVO, ImageIO.read(getClass().getResource("/assets/OBJETIVO.png")));
            imagenes.put(JARL, ImageIO.read(getClass().getResource("/assets/JARL.png")));
            imagenesAgente.put(Acciones.ARR, ImageIO.read(getClass().getResource("/assets/ARR.png")));
            imagenesAgente.put(Acciones.ABA, ImageIO.read(getClass().getResource("/assets/ABA.png")));
            imagenesAgente.put(Acciones.IZQ, ImageIO.read(getClass().getResource("/assets/IZQ.png")));
            imagenesAgente.put(Acciones.DER, ImageIO.read(getClass().getResource("/assets/DER.png")));
            imagenesAgente.put(Acciones.ARRIZQ, ImageIO.read(getClass().getResource("/assets/ARRIZQ.png")));
            imagenesAgente.put(Acciones.ARRDER, ImageIO.read(getClass().getResource("/assets/ARRDER.png")));
            imagenesAgente.put(Acciones.ABAIZQ, ImageIO.read(getClass().getResource("/assets/ABAIZQ.png")));
            imagenesAgente.put(Acciones.ABADER, ImageIO.read(getClass().getResource("/assets/ABADER.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar las imágenes del agente.");
        }
    }

    private void inicializarComponentes(String textoInicial) {
        setTitle("Simulación del Agente");
        setSize(1800, 1000);  // Tamaño inicial de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);  // Hacer la ventana redimensionable
        setLayout(new BorderLayout());  // Usamos BorderLayout

        // Panel del mapa (PanelMatriz)
        panelMatriz = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarMatriz(g);
            }
        };
        panelMatriz.setPreferredSize(new Dimension(1200, 800));  // Tamaño del panel del mapa
        panelMatriz.setBackground(Color.BLACK);

        // Area de texto para la traza (TextAreaTraza)
        textAreaTraza = new JTextArea();
        textAreaTraza.setEditable(false);
        textAreaTraza.setText(textoInicial);
        textAreaTraza.setLineWrap(true);
        textAreaTraza.setWrapStyleWord(true);
        JScrollPane scrollPaneTraza = new JScrollPane(textAreaTraza);
        scrollPaneTraza.setPreferredSize(new Dimension(400, 800));  // Tamaño ajustado del área de trazas

        // Crear un JSplitPane para dividir la ventana en dos secciones
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelMatriz, scrollPaneTraza);
        splitPane.setDividerLocation(1200);  // Establecer la posición inicial del divisor
        splitPane.setResizeWeight(1.0);  // Permitir que el panel de trazas se redimensione, mientras que el mapa mantiene su tamaño

        // Colocar el JSplitPane en la ventana principal
        add(splitPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void dibujarMatriz(Graphics g) {
        if (matriz == null) {
            return;
        }

        // Obtener el tamaño actual del panel
        int panelWidth = panelMatriz.getWidth();
        int panelHeight = panelMatriz.getHeight();

        // Ajustar el tamaño de las celdas en función del tamaño del panel
        int filas = matriz.length;
        int columnas = matriz[0].length;

        // Asegurar que las celdas sean cuadradas
        int anchoCelda = Math.min(panelWidth / columnas, panelHeight / filas);
        int altoCelda = anchoCelda;  // El alto debe ser igual al ancho para que las celdas sean cuadradas

        // Asegurarse de que el panel se ajuste correctamente (sin cortar la parte inferior)
        int offsetX = (panelWidth - (anchoCelda * columnas)) / 2;
        int offsetY = (panelHeight - (altoCelda * filas)) / 2;

        // Dibujar la matriz
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Image imagen = imagenes.getOrDefault(matriz[i][j], imagenes.get(SUELO));
                if (imagen != null) {
                    g.drawImage(imagen, offsetX + j * anchoCelda, offsetY + i * altoCelda, anchoCelda, altoCelda, this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(offsetX + j * anchoCelda, offsetY + i * altoCelda, anchoCelda, altoCelda);
                }

                if (matriz[i][j] == AGENTE) {
                    imagen = imagenesAgente.get(direccionAgente);
                    if (imagen != null) {
                        g.drawImage(imagen, offsetX + j * anchoCelda, offsetY + i * altoCelda, anchoCelda, altoCelda, this);
                    }
                }
                g.setColor(Color.BLACK);
                g.drawRect(offsetX + j * anchoCelda, offsetY + i * altoCelda, anchoCelda, altoCelda);
            }
        }
    }

    public void agregarTraza(String nuevaAccion) {
        textAreaTraza.insert(nuevaAccion + "\n" + "--------------------------------------------------------------------------------------------\n\n\n", 0);
    }

    public void actualizarMatriz(int[][] nuevaMatriz, Acciones nuevaDireccionAgente) {
        this.matriz = nuevaMatriz;
        this.direccionAgente = nuevaDireccionAgente;
        panelMatriz.repaint();
    }

    public void mostrarVentanaVictoria() {
        JFrame ventanaVictoria = new JFrame("¡Victoria!");
        ventanaVictoria.setSize(400, 400);
        ventanaVictoria.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaVictoria.setLocationRelativeTo(this);

        class ImagenLabel extends JLabel {

            Acciones[] direcciones = {Acciones.ARR, Acciones.ARRDER, Acciones.DER, Acciones.ABADER, Acciones.ABA, Acciones.ABAIZQ, Acciones.IZQ, Acciones.ARRIZQ};
            int indice = 0;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image sueloImagen = imagenes.get(SUELO);
                if (sueloImagen != null) {
                    g.drawImage(sueloImagen, 0, 0, getWidth(), getHeight(), this);
                }
                Image agenteImagen = imagenesAgente.get(direcciones[indice]);
                if (agenteImagen != null) {
                    g.drawImage(agenteImagen, 0, 0, getWidth(), getHeight(), this);
                }
            }

            public void siguienteImagen() {
                indice = (indice + 1) % direcciones.length;
                repaint();
            }
        }

        ImagenLabel labelImagen = new ImagenLabel();
        ventanaVictoria.add(labelImagen);
        ventanaVictoria.setVisible(true);

        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                labelImagen.siguienteImagen();
            }
        }, 0, 500);
    }

}
