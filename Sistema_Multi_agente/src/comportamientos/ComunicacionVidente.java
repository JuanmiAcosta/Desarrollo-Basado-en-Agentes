package comportamientos;

import agentes.AgenteVidente;
import estados.EstadosVidente;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorComunicacion;
import utiles.GestorDF;
import utiles.Posicion;

public class ComunicacionVidente extends Behaviour {

    private final String CONV_BARCO_VIDENTE_ID = "barco-vikingo-vidente-conv";

    private EstadosVidente paso;
    private AgenteVidente agente;
    private Boolean finish = false;

    private AID barco;
    private ACLMessage msgBarco;

    public ComunicacionVidente(AgenteVidente agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosVidente.ESPERANDO_INICIO_BARCO;
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
            
            case ESPERANDO_INICIO_BARCO:
                
                msgBarco = agente.blockingReceive();
                System.out.println(msgBarco.toString());
                String posNaufrago = "Bro, no quedan náufragos. En plan.";
                Posicion pos = new Posicion();

                if (msgBarco != null && msgBarco.getPerformative() == ACLMessage.REQUEST) {
                    
                    if (msgBarco.getSender().equals(barco) && GestorComunicacion.checkMensajeBarco(msgBarco.getContent())) {
                        
                        if (!msgBarco.getConversationId().equals(CONV_BARCO_VIDENTE_ID)) {      // Si no es el codigo correcto
                            msgBarco = msgBarco.createReply(ACLMessage.NOT_UNDERSTOOD);
                            msgBarco.setContent("Bro, no puedo ayudarte. En plan.");
                        }
                        else if(agente.getPosNaufragos().size() == 0) {     // No quedan coordenadas
                            msgBarco = msgBarco.createReply(ACLMessage.REFUSE);
                            msgBarco.setContent(posNaufrago);
                        }
                        else {      // Quedan coordenadas y codigo de converasion correcto
                            msgBarco = msgBarco.createReply(ACLMessage.AGREE);
                            
                            // Obtiene la posicion del naufrago
                            pos = agente.getPosNaufragos().getFirst();
                            agente.getPosNaufragos().removeFirst();
                            posNaufrago = "[" + pos.getFila() + "," + pos.getCol() + "]";
                            
                            // Comunica la posicion del naufrago al barco
                            msgBarco.setContent("Bro, acepto. La ubicación está en las coordenadas " + posNaufrago + ". En plan.");
                        }
                        
                        agente.send(msgBarco);
                        agente.getGraficos().agregarTraza(msgBarco.toString());
                    }
                }

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
