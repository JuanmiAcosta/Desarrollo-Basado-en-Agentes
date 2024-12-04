package comportamientos;

import agentes.AgenteJarl;
import estados.EstadosJarl;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;

public class ComunicacionJarl extends Behaviour {

    private EstadosJarl paso;
    private AgenteJarl agente;
    private Boolean finish = false;

    public ComunicacionJarl(AgenteJarl agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosJarl.ESPERANDO_INICIO_CONV;
    }

    @Override
    public void action() {

        switch (this.paso) {
            case ESPERANDO_INICIO_CONV:
                
                break;
            default:
                System.out.println("[Jarl] Error: Estado desconocido.");
                myAgent.doDelete();
                break;
        }
    }

    @Override
    public boolean done() {
        return this.finish;
    }
}
