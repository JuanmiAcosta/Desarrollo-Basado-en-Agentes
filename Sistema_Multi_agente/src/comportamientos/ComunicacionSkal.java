package comportamientos;

import agentes.AgenteSkal;
import estados.EstadosSkal;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorComunicacion;
import utiles.GestorDF;

public class ComunicacionSkal extends Behaviour {

    private final String CONV_BARCO_SKAL_ID = "barco-skal-conv";
    private EstadosSkal paso;
    private AgenteSkal agente;
    private Boolean finish = false;

    private AID barco;

    public ComunicacionSkal(AgenteSkal agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosSkal.ESPERANDO_MENSAJE_INICIO;
        this.prepararParaComunicaciones();
    }

    private void prepararParaComunicaciones() {

        boolean todosLosAgentesRegistrados = false;
        AID[] agentes = null;

        while (!todosLosAgentesRegistrados) {

            // Buscar los agentes del DF. 
            // En este caso va a buscar el agente explorador que solo hay 1 
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

        // A partir de los agentes buscados se comrpueba si entre ellos 
        // está el barco vikingo
        this.barco = GestorDF.buscarAgenteEnLista(agentes, "barco-vikingo");
    }

    @Override
    public void action() {
        ACLMessage msg;
        String mensajeTraducido;
        
        switch (this.paso) {
            case ESPERANDO_MENSAJE_INICIO:
                msg = agente.blockingReceive();
                
                if(msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                    if(msg.getSender() == barco && GestorComunicacion.checkMensajeBarco(msg.getContent())) {
                        System.out.println("[" + agente.getLocalName() + "] Recibido REQUEST de Barco Vikingo");
                    
                        // Traduccion del mensaje
                        mensajeTraducido = GestorComunicacion.traduceBarcoJarl(msg.getContent());

                        // Enviar INFORM al barco vikingo
                        msg = msg.createReply();
                        msg.setPerformative(ACLMessage.INFORM);
                        msg.setContent(mensajeTraducido);
                        // msg.setInReplyTo("presentation-request");
                        agente.send(msg);
                        agente.getGraficos().agregarTraza(msg.toString());
                    }
                    else {
                        System.out.println("No entiendo lo que me quieres decir");
                    }
                }
                else {
                    System.out.println("Error esperando REQUEST en: " + agente.getLocalName());
                }

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
