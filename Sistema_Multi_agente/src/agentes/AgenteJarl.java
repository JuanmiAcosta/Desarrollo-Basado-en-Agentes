package agentes;

import jade.core.Agent;
import comportamientos.ComunicacionJarl;
import jade.core.AID;
import simulacion.Graficos;
import utiles.GestorDF;
import utiles.Posicion;

public class AgenteJarl extends Agent {
    
    private Posicion posJarl;
    private Graficos g;
        
    @Override
    protected void setup() {
        System.out.println("Agente Jarl iniciado: " + getLocalName());
        
        Object[] args = getArguments();
        if (args != null && args.length == 2) {
            this.posJarl = (Posicion) args[0];
            this.g = (Graficos) args[1];

        } else {
            System.out.println("Error: Parámetros de inicialización insuficientes para el agente.");
            doDelete(); // Eliminar agente si los parámetros son insuficientes
            return;
        }
        
        GestorDF.registrarAgente(this, "aldeano", "jarl");
        
        addBehaviour(new ComunicacionJarl(this));
    }
    
}