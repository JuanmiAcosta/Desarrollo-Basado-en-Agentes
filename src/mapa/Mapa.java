package mapa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author juanmi
 */
public class Mapa {
    
    private int[][] mapa;
    private int filas;
    private int columnas;
    
    // Definir el ancho de cada celda
    private final static int anchoCelda = 3; //Sólo para impresión por consola

    public Mapa(String nombreArchivo) throws IOException {
        leerMapaDeRecurso(nombreArchivo);
    }

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

    public int[][] getMapa() {
        return mapa;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
}
