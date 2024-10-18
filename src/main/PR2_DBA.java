package main;

import agente.Posicion;
import entorno.Entorno;
import java.io.IOException;
import entorno.Mapa;

/**
 *
 * @author juanmi
 */
public class PR2_DBA {
    public static void main(String[] args) throws IOException {
        String paquete_mapas= "ejemplos_mapas/";
        Mapa mapa = new Mapa(paquete_mapas+"mapWithComplexObstacle1.txt");
        mapa.imprimirMapa();
        
        System.out.println("\n");
        
        Entorno entorno = new Entorno(new Mapa(mapa), new Posicion(0, 0), new Posicion(mapa.getFilas() - 1, mapa.getColumnas() - 1));
        entorno.getMapa().imprimirMapa();
    }
}
