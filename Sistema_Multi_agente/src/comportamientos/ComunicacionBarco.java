package comportamientos;

import agentes.AgenteBarco;
import estados.EstadosBarco;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorDF;

public class ComunicacionBarco extends Behaviour {

    private final String CONV_BARCO_SKAL_ID = "barco-skal-conv";
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
        this.prepararParaComunicaciones();
    }

    private void prepararParaComunicaciones() {

        boolean todosLosAgentesRegistrados = false;
        AID[] agentes = null;

        while (!todosLosAgentesRegistrados) {

            // Buscar los agentes del DF
            agentes = GestorDF.buscarAgentes(this.agente, "aldeano");

            if (agentes.length == 3) { // Número esperado de servicios
                todosLosAgentesRegistrados = true;
            } else {
                try {
                    Thread.sleep(100); // Esperar 1 segundo antes de volver a buscar
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        this.jarl = GestorDF.buscarAgenteEnLista(agentes, "jarl");
        this.skal = GestorDF.buscarAgenteEnLista(agentes, "skal");
        this.vidente = GestorDF.buscarAgenteEnLista(agentes, "vidente");
    }

    @Override
    public void action() {

        ACLMessage msg;

        if (agente.getPosObj() == null) {

            switch (this.paso) {
                case INICIO_MISION:

                    msg = new ACLMessage(ACLMessage.REQUEST);
                    msg.addReceiver(this.skal);
                    msg.setReplyWith("presentation-request");
                    msg.setContent("Bro, estoy listo para encontrar a la tripulación perdida. En plan.");
                    msg.setConversationId(CONV_BARCO_SKAL_ID);
                    myAgent.send(msg);

                    //System.out.println("[Master] Enviado REQUEST a todos los esclavos.");
                    this.agente.getGraficos().agregarTraza(msg.toString());
                    this.paso = EstadosBarco.ESPERANDO_TRADUCCION_INICIO;

                    break;
                default:
                    System.out.println("[Barco] Error: Estado desconocido.");
                    myAgent.doDelete();
                    break;
            }
        }

    }

    @Override
    public boolean done() {
        return this.finish;
    }
}
