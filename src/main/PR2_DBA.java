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
        Mapa mapa = new Mapa(paquete_mapas+"mapWithoutObstacle.txt");
        mapa.imprimirMapa();
        
        System.out.println("\n");
        
        Sensores sensores = new Sensores(0);
        Agente agente = new Agente(new Posicion(0, 0), new Posicion(mapa.getFilas() - 1, mapa.getColumnas() - 1), sensores);
        Entorno entorno = new Entorno(new Mapa(mapa), agente.getPosAgente(), agente.getPosObj());
        entorno.getMapa().imprimirMapa();
        sensores.setEntorno(entorno);
        Graficos graficos = new Graficos("Esperando acciones por parte del agente...",entorno.getMapa().getMapa(), 0);
        
        System.out.println("\n");
        
        boolean encontrado = false;
        
        for(int i = 0; i < 20; i++) {
            
            // Para ver la ejec
            Thread.sleep(2000);
            
            agente.getMovDisponibles();
            agente.movUtiles();
            int mov = agente.decidirMov();
            System.out.println(mov);
            
            agente.realizarMov(mov);
            System.out.println(agente.toString());
            System.out.println("\n");
            
            System.out.println(entorno.getPosAgente());
            System.out.println(agente.getPosAgente());
            System.out.println(agente.getPosAnterior());
            entorno.setPosAgente(agente.getPosAgente(), agente.getPosAnterior());
            entorno.getMapa().imprimirMapa();
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
