package agentes;

import jade.core.Agent;
import comportamientos.ComunicacionVidente;
import java.util.ArrayList;
import simulacion.Graficos;
import utiles.GestorDF;
import utiles.Posicion;

public class AgenteVidente extends Agent {
    
    private Graficos g;
    private ArrayList<Posicion> posNaufragos;
            
    @Override
    protected void setup() {
        System.out.println("Agente Vidente iniciado: " + getLocalName());
        
        Object[] args = getArguments();
        if (args != null && args.length == 2) {
            this.g = (Graficos) args[0];
            this.posNaufragos = (ArrayList<Posicion>) args[1];
        } else {
            System.out.println("Error: Parámetros de inicialización insuficientes para el agente.");
            doDelete(); // Eliminar agente si los parámetros son insuficientes
            return;
        }
        
        GestorDF.registrarAgente(this, "aldeano", "vidente");
        
        addBehaviour(new ComunicacionVidente(this));
    }

    public Graficos getGraficos() {
        return g;
    }

    public ArrayList<Posicion> getPosNaufragos() {
        return posNaufragos;
    }
}