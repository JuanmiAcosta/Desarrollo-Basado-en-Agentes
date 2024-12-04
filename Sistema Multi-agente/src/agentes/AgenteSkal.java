package agentes;

import jade.core.Agent;
import comportamientos.ComunicacionSkal;
import utiles.GestorDF;

public class AgenteSkal extends Agent {
            
    @Override
    protected void setup() {
        System.out.println("Agente Skal iniciado: " + getLocalName());
        
        GestorDF.registrarAgente(this, "aldeano", "skal");
        
        addBehaviour(new ComunicacionSkal(this));
    }
    
}