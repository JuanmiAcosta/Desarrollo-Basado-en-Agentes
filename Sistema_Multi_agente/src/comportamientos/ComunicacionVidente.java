package comportamientos;

import agentes.AgenteVidente;
import estados.EstadosVidente;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorDF;

public class ComunicacionVidente extends Behaviour {

    private EstadosVidente paso;
    private AgenteVidente agente;
    private Boolean finish = false;

    private AID barco;

    public ComunicacionVidente(AgenteVidente agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosVidente.ESPERANDO_PETICION_COORD;
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
            case ESPERANDO_PETICION_COORD:

                break;
            default:
                System.out.println("[Vidente] Error: Estado desconocido.");
                myAgent.doDelete();
                break;
        }
    }

    @Override
    public boolean done() {
        return this.finish;
    }
}
