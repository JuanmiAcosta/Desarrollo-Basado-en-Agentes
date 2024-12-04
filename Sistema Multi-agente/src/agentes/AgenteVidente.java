package agentes;

import jade.core.Agent;
import comportamientos.ComunicacionVidente;
import utiles.GestorDF;

public class AgenteVidente extends Agent {
            
    @Override
    protected void setup() {
        System.out.println("Agente Vidente iniciado: " + getLocalName());
        
        GestorDF.registrarAgente(this, "aldeano", "vidente");
        
        addBehaviour(new ComunicacionVidente(this));
    }
    
}