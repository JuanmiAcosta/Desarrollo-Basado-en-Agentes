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
        ACLMessage msgBarco;
        String mensajeConfirm;
        
        switch (this.paso) {
            case ESPERANDO_INICIO_CONV:
                msgBarco = agente.blockingReceive();
                
                if(msgBarco != null && msgBarco.getPerformative() == ACLMessage.PROPOSE) {
                    if(msgBarco.getSender().equals(barco) && GestorComunicacion.checkMensajeBarco(msgBarco.getContent())) {
                        System.out.println("[" + agente.getLocalName() + "] Recibido PROPOSE de Barco Vikingo");
                        
                        if(esBarcoDigno()) {
                            // Crear mensaje de confirmacion
                            mensajeConfirm = 
                            
                            // Enviar CONFIRM al barco
                            msgBarco = msgBarco.createReply(ACLMessage.CONFIRM);
                            msgBarco.setContent()
                            
                        }
                        else {
                            // Enviar DISCONFIRM
                            
                        }
                    }
                    else {
                        System.out.println("No entiendo lo que me quieres decir");
                    }
                }
                else {
                    System.out.println();
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
