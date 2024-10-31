/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamientos;

import agente.Agente;
import entorno.Entorno;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author jorge
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
        entorno.setPosAgente(agente.getPosAgente(), agente.getPosAnterior());
        entorno.getMapa().imprimirMapa();
        agente.imprimirMemoria();
    }

    @Override
    public boolean done() {
        return agente.objEncontrado();
    }
}
