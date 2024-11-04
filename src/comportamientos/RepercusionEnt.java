/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamientos;

import agente.Agente;
import entorno.Entorno;
import jade.core.behaviours.Behaviour;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juanmi
 */
public class RepercusionEnt extends Behaviour {
    // Variables
    private Agente agente;
    private Entorno entorno;
    
    // Constructor
    public RepercusionEnt(Agente agente, Entorno entorno) {
        this.agente = agente;
        this.entorno = entorno;
    }

     // Metodos
    @Override
    public void action() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(RepercusionEnt.class.getName()).log(Level.SEVERE, null, ex);
        }
        entorno.setPosAgente(agente.getPosAgente(), agente.getPosAnterior());
        entorno.getMapa().imprimirMapa();
        agente.imprimirMemoria();
        agente.getGraficos().agregarTraza(agente.toString());
        agente.getGraficos().actualizarMatriz(agente.getSensores().getEntorno().getMapa().getMapa(), agente.getMovDecidido());
    }

    @Override
    public boolean done() {
        return agente.objEncontrado();
    }
}
