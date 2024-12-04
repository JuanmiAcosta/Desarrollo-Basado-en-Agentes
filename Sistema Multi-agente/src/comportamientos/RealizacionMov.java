/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamientos;

import agentes.AgenteBarco;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author jorge
 */
public class RealizacionMov extends Behaviour {

    // Variables
    private AgenteBarco agente;

    // Constructor
    public RealizacionMov(AgenteBarco agente) {
        this.agente = agente;
    }

    // Metodos
    @Override
    public void action() {

        if (agente.getPosObj() != null) {
            agente.realizarMov();
            agente.actualizarMemoria();
        }
    }

    @Override
    public boolean done() {
        return agente.getMisionAcabada();
    }
}
