package agentes;

import jade.core.Agent;
import comportamientos.ComunicacionVidente;
import simulacion.Graficos;
import utiles.GestorDF;

public class AgenteVidente extends Agent {
    
    private Graficos g;
            
    @Override
    protected void setup() {
        System.out.println("Agente Vidente iniciado: " + getLocalName());
        
        Object[] args = getArguments();
        if (args != null && args.length == 1) {
            this.g = (Graficos) args[0];

        } else {
            System.out.println("Error: Parámetros de inicialización insuficientes para el agente.");
            doDelete(); // Eliminar agente si los parámetros son insuficientes
            return;
        }
        
        GestorDF.registrarAgente(this, "aldeano", "vidente");
        
        addBehaviour(new ComunicacionVidente(this));
    }
    
}