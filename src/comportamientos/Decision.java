/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamientos;

import agente.Agente;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author jorge
 */
public class Decision extends Behaviour {
    // Variables
    private Agente agente;
    
    // Constructor
    public Decision(Agente agente) {
        this.agente = agente;
    }

    // Metodos
    @Override
    public void action() {
        agente.setMovRealizar(agente.decidirMov());
    }

    @Override
    public boolean done() {
        return agente.objEncontrado();
    }
}
