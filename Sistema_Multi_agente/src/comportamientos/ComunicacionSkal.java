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
    private AID jarl;
    
    private ACLMessage msgBarco, msgJarl;

    public ComunicacionSkal(AgenteSkal agent) {
        super(agent);
        this.agente = agent;
        this.paso = EstadosSkal.ESPERANDO_MENSAJE_INICIO;
        this.prepararParaComunicaciones();
    }

    private void prepararParaComunicaciones() {

        boolean todosLosAgentesRegistrados = false;
        AID[] agentes = null;
        AID[] agentesAld = null;

        while (!todosLosAgentesRegistrados) {

            // Buscar los agentes del DF. 
            // En este caso va a buscar el agente explorador que solo hay 1 
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

        // A partir de los agentes buscados se comrpueba si entre ellos 
        // está el barco vikingo
        this.barco = GestorDF.buscarAgenteEnLista(agentes, "barco-vikingo");
        this.jarl = GestorDF.buscarAgenteEnLista(agentesAld , "jarl");
    }

    @Override
    public void action() {
        String mensajeTraducido;
        
        switch (this.paso) {
            case ESPERANDO_MENSAJE_INICIO:
                msgBarco = agente.blockingReceive();
                
                if(msgBarco != null && msgBarco.getPerformative() == ACLMessage.REQUEST) {

                    if(msgBarco.getSender().equals(barco)){ 
                    
                        // Traduccion del mensaje
                        mensajeTraducido = GestorComunicacion.traduceBarcoJarl(msgBarco.getContent());

                        // Enviar INFORM al barco vikingo
                        msgBarco = msgBarco.createReply(ACLMessage.INFORM);
                        msgBarco.setContent(mensajeTraducido);
                        agente.send(msgBarco);
                        agente.getGraficos().agregarTraza(msgBarco.toString());
                        this.paso = EstadosSkal.ESPERANDO_JARL_INICIO;
                    }
                    else {
                        System.out.println("No entiendo lo que me quieres decir soy Skal");
                    }
                }
                else {
                    System.out.println("Error esperando REQUEST en: " + agente.getLocalName());
                }

                break;
                
            case ESPERANDO_JARL_INICIO:
                msgJarl = agente.blockingReceive();
                
                if(msgJarl != null && msgJarl.getPerformative() == ACLMessage.REQUEST) {

                    if(msgJarl.getSender().equals(jarl)){                     
                        // Traduccion del mensaje
                        mensajeTraducido = GestorComunicacion.traduceJarlBarco(msgJarl.getContent());

                        // Enviar INFORM al barco vikingo
                        msgJarl = msgJarl.createReply(ACLMessage.INFORM);
                        msgJarl.setContent(mensajeTraducido);
                        agente.send(msgJarl);
                        agente.getGraficos().agregarTraza(msgJarl.toString());
                        this.paso = EstadosSkal.ESPERANDO_SOLICITUD_TRADUCCION_BARCO;
                    }
                    else {
                        System.out.println("No entiendo lo que me quieres decir, soy Skal");
                    }
                }
                else {
                    System.out.println("Error esperando REQUEST en: " + agente.getLocalName());
                }

                break;
                
            case ESPERANDO_SOLICITUD_TRADUCCION_BARCO:
                
                msgBarco = agente.blockingReceive();
                
                if(msgBarco != null && msgBarco.getPerformative() == ACLMessage.REQUEST) {
                    if(msgBarco.getSender().equals(barco)) {
                        mensajeTraducido = GestorComunicacion.traduceBarcoJarl(msgBarco.getContent());

                        msgBarco = msgBarco.createReply(ACLMessage.INFORM);
                        msgBarco.setContent(mensajeTraducido);
                        agente.send(msgBarco);
                        agente.getGraficos().agregarTraza(msgBarco.toString());
                        
                        this.paso = EstadosSkal.ESPERANDO_SOLICITUD_TRADUCCION_JARL;
                    }
                }
                
                break;
            
            case ESPERANDO_SOLICITUD_TRADUCCION_JARL:
                
                msgJarl = agente.blockingReceive();
                
                if(msgJarl != null && msgJarl.getPerformative() == ACLMessage.REQUEST) {
                    if(msgJarl.getSender().equals(jarl)) {
                        mensajeTraducido = GestorComunicacion.traduceJarlBarco(msgJarl.getContent());

                        msgJarl = msgJarl.createReply(ACLMessage.INFORM);
                        msgJarl.setContent(mensajeTraducido);
                        agente.send(msgJarl);
                        agente.getGraficos().agregarTraza(msgJarl.toString());
                        
                        this.paso = EstadosSkal.ESPERANDO_TRADUCCION_BARCO_2;
                    }
                }
                
                break;
                
            case ESPERANDO_TRADUCCION_BARCO_2:
                
                // ESPERA MENSAJE FINAL DEL BARCO
                
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
