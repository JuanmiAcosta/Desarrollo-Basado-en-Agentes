package entorno;

import agente.Posicion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author juanmi
 */
public class Mapa {

    // Variables
    private String nombreArchivo;
    private int[][] mapa;
    private int filas;
    private int columnas;

    // Definir el ancho de cada celda
    private final static int anchoCelda = 3; //Sólo para impresión por consola

    // Constructor
    public Mapa(String nombreArchivo) throws IOException {
        this.nombreArchivo = nombreArchivo;
        leerMapaDeRecurso(nombreArchivo);
    }

    public Mapa(Mapa otroMapa) throws IOException {
        this(otroMapa.getNombreArchivo());
    }

    // Metodos
    private void leerMapaDeRecurso(String rutaArchivo) throws IOException {
        // Obtener el archivo como recurso desde el classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(rutaArchivo);

        if (inputStream == null) {
            throw new IOException("Archivo no encontrado: " + rutaArchivo);
        }

        try (BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream))) {
            this.filas = Integer.parseInt(lector.readLine().trim());
            this.columnas = Integer.parseInt(lector.readLine().trim());
            mapa = new int[filas][columnas];

            for (int i = 0; i < filas; i++) {
                String[] fila = lector.readLine().trim().split("\\s+"); // Dividir por espacios en blanco

                for (int j = 0; j < columnas; j++) {
                    mapa[i][j] = Integer.parseInt(fila[j]);  // Convertir y almacenar en la matriz
                }
            }
        } catch (IOException e) {
            throw new IOException("Error al leer el archivo: " + rutaArchivo, e);
        }
    }

    public void imprimirMapa() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Imprimir cada número con un ancho fijo
                System.out.printf("%" + anchoCelda + "d", mapa[i][j]);
            }
            System.out.println(); // Nueva línea al final de cada fila
        }
    }

    public void colocarItem(Posicion pos, int idItem) {
        if (posCorrecta(pos)) {
            mapa[pos.getFila()][pos.getCol()] = idItem;
        }
    }

    public int[][] getMapa() {
        return mapa;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getCasilla(int fila, int col) {
        return mapa[fila][col];
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    // Comprueba que la posición esté dentro de los límites del mapa 
    // y que la casilla es transitable
    public boolean posCorrecta(Posicion pos) {
        return (pos.getFila() >= 0 && pos.getFila() < filas)
                && (pos.getCol() >= 0 && pos.getCol() < columnas)
                && (mapa[pos.getFila()][pos.getCol()] == 0 || mapa[pos.getFila()][pos.getCol()] == 3
                || mapa[pos.getFila()][pos.getCol()] == 7 || mapa[pos.getFila()][pos.getCol()] == 9);
    }

    public boolean muro(Posicion pos) {
        return !posCorrecta(pos); // Realmente significa muro o fuera del límite
    }

    public boolean movDiagonalValido(Posicion pos, int mov) { // (ej noroeste) Comprobamos que arriba y a la izquierda no haya muro y sea una pos válida
        switch (mov) {
            case 4: // NOROESTE
                return (!muro(new Posicion(pos.getFila() + 1, pos.getCol())) && !muro(new Posicion(pos.getFila(), pos.getCol() + 1))

                        || !muro(new Posicion(pos.getFila() + 1, pos.getCol())) || !muro(new Posicion(pos.getFila(), pos.getCol() + 1)))

                        && !muro(pos); // La posición actual también debe ser válida

            case 5: // NORESTE
                return (!muro(new Posicion(pos.getFila() + 1, pos.getCol())) && !muro(new Posicion(pos.getFila(), pos.getCol() - 1))

                        || !muro(new Posicion(pos.getFila() + 1, pos.getCol())) || !muro(new Posicion(pos.getFila(), pos.getCol() - 1)))

                        && !muro(pos); // La posición actual también debe ser válida

            case 6: // SUROESTE
                return (!muro(new Posicion(pos.getFila() - 1, pos.getCol())) && !muro(new Posicion(pos.getFila(), pos.getCol() + 1))

                        || !muro(new Posicion(pos.getFila() - 1, pos.getCol())) || !muro(new Posicion(pos.getFila(), pos.getCol() + 1)))

                        && !muro(pos); // La posición actual también debe ser válida

            case 7: // SURESTE
                return (!muro(new Posicion(pos.getFila() - 1, pos.getCol())) && !muro(new Posicion(pos.getFila(), pos.getCol() - 1))

                        || !muro(new Posicion(pos.getFila() - 1, pos.getCol())) || !muro(new Posicion(pos.getFila(), pos.getCol() - 1)))

                        && !muro(pos); // La posición actual también debe ser válida

            default:
                return false; // En caso de un movimiento no diagonal
        }
    }

}
