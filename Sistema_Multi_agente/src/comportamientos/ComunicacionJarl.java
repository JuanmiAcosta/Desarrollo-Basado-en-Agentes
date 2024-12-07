package comportamientos;

import agentes.AgenteJarl;
import estados.EstadosJarl;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorComunicacion;
import utiles.GestorDF;

public class ComunicacionJarl extends Behaviour {

    private final String CONV_BARCO_JARL_ID = "barco-vikingo-vidente-conv";
    private EstadosJarl paso;
    private AgenteJarl agente;
    private Boolean finish = false;

    private AID barco,
                            skal;

    public ComunicacionJarl(AgenteJarl agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosJarl.ESPERANDO_ENVIO_BARCO;
        this.prepararParaComunicaciones();
    }

    private void prepararParaComunicaciones() {

        boolean todosLosAgentesRegistrados = false;
        AID[] agentes = null;
        AID[] agentesAld = null;

        while (!todosLosAgentesRegistrados) {
            // Buscar los agentes del DF
            agentes = GestorDF.buscarAgentes(this.agente, "explorador");
            agentesAld = GestorDF.buscarAgentes(this.agente, "aldeano");
            
            if (agentes.length == 1 && agentesAld.length == 3) { // Número esperado de servicios
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
        this.skal = GestorDF.buscarAgenteEnLista(agentesAld , "skal");
    }

    @Override
    public void action() {
        ACLMessage msgBarco = new ACLMessage(),
                                   msgSkal;
        String mensajeConfirm;
        Boolean esDigno = false;
        
        switch (this.paso) {
            case ESPERANDO_ENVIO_BARCO:
                msgBarco = agente.blockingReceive();
                
                if(msgBarco != null && msgBarco.getPerformative() == ACLMessage.PROPOSE) {
                    if(msgBarco.getSender().equals(barco) && GestorComunicacion.checkMensajeBarco(msgBarco.getContent())) {
                        System.out.println("[" + agente.getLocalName() + "] Recibido PROPOSE de Barco Vikingo");
                        
                         // Crear mensaje de confirmacion o denegación
                         esDigno = esBarcoDigno();
                         mensajeConfirm = GestorComunicacion.jarlConfirmaDigno(esBarcoDigno(), CONV_BARCO_JARL_ID);
                            
                         // Enviar CONFIRM o DISCONFIRM al barco
                         msgSkal = new ACLMessage(ACLMessage.REQUEST);
                         msgSkal.addReceiver(skal);
                         msgSkal.setContent(mensajeConfirm);
                         msgSkal.setReplyWith("validation-request");
                         msgSkal.setConversationId(CONV_BARCO_JARL_ID);
                         agente.send(msgSkal);
                         agente.getGraficos().agregarTraza(msgSkal.toString());
                         paso = EstadosJarl.ESPERANDO_RESP_SKAL;
                    }
                    else {
                        System.out.println("No entiendo lo que me quieres decir");
                    }
                }
                else {
                    System.out.println("No ha llegado nada");
                }

                break;
            
            case ESPERANDO_RESP_SKAL:
                msgSkal = agente.blockingReceive();
                
                if(msgSkal != null && msgSkal.getPerformative() == ACLMessage.INFORM) {
                    if(msgSkal.getSender().equals(skal) && GestorComunicacion.checkMensajeBarco(msgSkal.getContent())) {
                        // Envio de mensaje traducido al barco
                        if(esDigno) {
                            msgBarco = msgBarco.createReply(ACLMessage.CONFIRM);
                        }
                        else {
                            msgBarco = msgBarco.createReply(ACLMessage.DISCONFIRM);
                        }
                        
                        msgBarco.setContent(msgSkal.getContent());
                        agente.send(msgBarco);
                         agente.getGraficos().agregarTraza(msgBarco.toString());
                    }
                    else {
                        System.out.println("No entiendo lo que me quieres decir");
                    }
                }
                else {
                    System.out.println("No ha llegado nada");
                }
                
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

    private boolean esBarcoDigno() {
        return ((int) (Math.random() * 11)) < 8;
    }
}
