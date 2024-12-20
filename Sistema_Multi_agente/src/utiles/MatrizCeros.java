package utiles;

public class MatrizCeros {
    public static void main(String[] args) {
        int[][] matriz = new int[100][100];

        // Inicializamos todos los valores de la matriz a 0
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                matriz[i][j] = 0;
            }
        }

        // Imprimimos la matriz (opcional)
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }
}
