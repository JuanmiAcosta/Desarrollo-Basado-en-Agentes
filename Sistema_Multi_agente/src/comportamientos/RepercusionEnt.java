/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comportamientos;

import agentes.AgenteBarco;
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
    private AgenteBarco agente;
    private Entorno entorno;

    // Constructor
    public RepercusionEnt(AgenteBarco agente, Entorno entorno) {
        this.agente = agente;
        this.entorno = entorno;
    }

    // Metodos
    @Override
    public void action() {

        if (agente.getPosObj() != null) {

            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
                Logger.getLogger(RepercusionEnt.class.getName()).log(Level.SEVERE, null, ex);
            }
            entorno.setPosAgente(agente.getPosAgente(), agente.getPosAnterior(), agente.getMemoria().get(agente.getPosAnterior()));
            //entorno.getMapa().imprimirMapa();
            //agente.imprimirMemoria();
            agente.getGraficos().actualizarMatriz(agente.getSensores().getEntorno().getMapa().getMapa(), agente.getMovDecidido());
            
            //Cuando encontramos un objetivo pasamos su posición a null para seguir con comunicaciones
            if (this.agente.objEncontrado()){
                this.agente.setPosObjetivo(null);
                this.agente.borrarMemoria();
            }
            
        }
    }

    @Override
    public boolean done() {
        if (agente.getMisionAcabada()) {
            agente.getGraficos().mostrarVentanaVictoria();
            return true;
        }
        return false;
    }
}
