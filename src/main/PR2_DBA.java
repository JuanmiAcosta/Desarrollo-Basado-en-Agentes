package main;

import agente.*;
import entorno.*;
import java.io.IOException;
import simulacion.Graficos;

/**
 *
 * @author juanmi
 */
public class PR2_DBA {
    
    public static void main(String[] args) throws IOException, InterruptedException {
        String paquete_mapas= "ejemplos_mapas/";
        Mapa mapa = new Mapa(paquete_mapas+"mapWithDiagonalWall.txt");
        mapa.imprimirMapa();
        
        System.out.println("\n");
        
        Sensores sensores = new Sensores(0);
        Agente agente = new Agente(new Posicion(0, 0), new Posicion(4,9), sensores);
        //Agente agente = new Agente(new Posicion(0, 0), new Posicion(mapa.getFilas()-1, mapa.getColumnas()-1), sensores);
        Entorno entorno = new Entorno(new Mapa(mapa), agente.getPosAgente(), agente.getPosObj());
        entorno.getMapa().imprimirMapa();
        sensores.setEntorno(entorno);
        Graficos graficos = new Graficos("Esperando acciones por parte del agente ...",entorno.getMapa().getMapa(), 0);
        
        System.out.println("\n");
        
        for(int i = 0; i < 200; i++) {
            
            // Para ver la ejecución poco a poco
            Thread.sleep(200);
            
            // Análisis del entorno 1BEHAVIOUR
            agente.getMovDisponibles();
            agente.movUtiles();
            
            // Decisión 2BEHAVIOUR
            int mov = agente.decidirMov();
            System.out.println(mov);
            
            // Realización del movimiento 3BEHAVIOUR
            agente.realizarMov(mov);
            agente.actualizarMemoria();
            System.out.println(agente.toString());
            System.out.println("\n");
            System.out.println(agente.getPosAgente());
            System.out.println(agente.getPosAnterior());
            
            // Repercusión en el entorno 4BEHAVIOUR
            entorno.setPosAgente(agente.getPosAgente(), agente.getPosAnterior());
            entorno.getMapa().imprimirMapa();
            System.out.println("\n");
            agente.imprimirMemoria();
            System.out.println("\n");
            
            //Gráficos
            graficos.agregarTraza(agente.toString());
            graficos.actualizarMatriz(entorno.getMapa().getMapa(), mov);
            
            if((agente.getPosAgente().getFila() == agente.getPosObj().getFila())
                    && (agente.getPosAgente().getCol() == agente.getPosObj().getCol())) {
                break;
            }
        }
    }
}
