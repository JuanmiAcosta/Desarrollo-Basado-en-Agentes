package agentes;

import jade.core.Agent;
import comportamientos.ComunicacionJarl;
import jade.core.AID;
import utiles.GestorDF;
import utiles.Posicion;

public class AgenteJarl extends Agent {
    
    private Posicion posJarl;
        
    @Override
    protected void setup() {
        System.out.println("Agente Jarl iniciado: " + getLocalName());
        
        GestorDF.registrarAgente(this, "aldeano", "jarl");
        
        addBehaviour(new ComunicacionJarl(this));
    }
    
}