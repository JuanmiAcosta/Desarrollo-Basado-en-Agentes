package comportamientos;

import agentes.AgenteBarco;
import estados.EstadosBarco;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorDF;

public class ComunicacionBarco extends Behaviour {

    private EstadosBarco paso;
    private AgenteBarco agente;
    private Boolean finish = false;
    private AID jarl;
    private AID skal;
    private AID vidente;

    public ComunicacionBarco(AgenteBarco agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosBarco.INICIO_MISION;

    }
    
    private void inicializarAgentes(){
        // Buscar los agentes del DF
        AID[] agentes = GestorDF.buscarAgentes(this.agente, "aldeano");
        
        this.jarl = GestorDF.buscarAgenteEnLista(agentes, "jarl");
        this.skal = GestorDF.buscarAgenteEnLista(agentes, "skal");
        this.vidente = GestorDF.buscarAgenteEnLista(agentes, "vidente");
    }

    @Override
    public void action() {

        switch (this.paso) {
            case INICIO_MISION:
                
                break;
            default:
                System.out.println("[Barco] Error: Estado desconocido.");
                myAgent.doDelete();
                break;
        }
    }

    @Override
    public boolean done() {
        return this.finish;
    }
}
