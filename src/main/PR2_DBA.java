package main;

import agente.Agente;
import agente.Posicion;
import agente.Sensores;
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
        Mapa mapa = new Mapa(paquete_mapas+"mapWithoutObstacle.txt");
        mapa.imprimirMapa();
        
        System.out.println("\n");
        
        Sensores sensores = new Sensores(0);
        Agente agente = new Agente(new Posicion(0, 0), new Posicion(mapa.getFilas() - 1, mapa.getColumnas() - 1), sensores);
        Entorno entorno = new Entorno(new Mapa(mapa), agente.getPosAgente(), agente.getPosObj());
        entorno.getMapa().imprimirMapa();
        sensores.setEntorno(entorno);
        
        System.out.println("\n");
        
        boolean encontrado = false;
        
        for(int i = 0; i < 20; i++) {
            agente.getMovDisponibles();
            agente.movUtiles();
            int mov = agente.decidirMov();
            System.out.println(mov);
            
            agente.realizarMov(mov);
            System.out.println(agente.toString());
            System.out.println("\n");
            
            entorno.setPosAgente(agente.getPosAgente());
            System.out.println(entorno.getPosAgente());
            entorno.getMapa().imprimirMapa();
            System.out.println("\n");
            
            if((agente.getPosAgente().getFila() == agente.getPosObj().getFila())
                    && (agente.getPosAgente().getCol() == agente.getPosObj().getCol())) {
                break;
            }
        }
    }
}
