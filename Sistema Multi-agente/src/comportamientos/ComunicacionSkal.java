package comportamientos;

import agentes.AgenteSkal;
import estados.EstadosSkal;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorDF;

public class ComunicacionSkal extends Behaviour {

    private EstadosSkal paso;
    private AgenteSkal agente;
    private Boolean finish = false;

    private AID barco;

    public ComunicacionSkal(AgenteSkal agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosSkal.ESPERANDO_INICIO_MISION;
        this.prepararParaComunicaciones();
    }

    private void prepararParaComunicaciones() {

        boolean todosLosAgentesRegistrados = false;
        AID[] agentes = null;

        while (!todosLosAgentesRegistrados) {

            // Buscar los agentes del DF
            agentes = GestorDF.buscarAgentes(this.agente, "explorador");
            if (agentes.length == 1) { // Número esperado de servicios
                todosLosAgentesRegistrados = true;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        this.barco = GestorDF.buscarAgenteEnLista(agentes, "barco-vikingo");
    }

    @Override
    public void action() {

        switch (this.paso) {
            case ESPERANDO_INICIO_MISION:
                System.out.println("El Skal está listo");
                myAgent.doDelete();

                break;
            default:
                System.out.println("[Skal] Error: Estado desconocido.");
                myAgent.doDelete();
                break;
        }
    }

    @Override
    public boolean done() {
        return this.finish;
    }
}
