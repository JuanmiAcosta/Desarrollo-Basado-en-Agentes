package comportamientos;

import agentes.AgenteJarl;
import estados.EstadosJarl;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorDF;

public class ComunicacionJarl extends Behaviour {

    private EstadosJarl paso;
    private AgenteJarl agente;
    private Boolean finish = false;

    private AID barco;

    public ComunicacionJarl(AgenteJarl agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosJarl.ESPERANDO_INICIO_CONV;
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
            case ESPERANDO_INICIO_CONV:
                System.out.println("El Jarl está listo");
                myAgent.doDelete();

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
