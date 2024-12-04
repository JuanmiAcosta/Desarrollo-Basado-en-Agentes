package agentes;

import jade.core.Agent;
import comportamientos.ComunicacionJarl;
import java.util.ArrayList;
import utiles.Posicion;

public class AgenteJarl extends Agent {
    
    private Posicion posJarl;
        
    @Override
    protected void setup() {
        System.out.println("Agente Jarl iniciado: " + getLocalName());
        
        addBehaviour(new ComunicacionJarl(this));
    }
    
}