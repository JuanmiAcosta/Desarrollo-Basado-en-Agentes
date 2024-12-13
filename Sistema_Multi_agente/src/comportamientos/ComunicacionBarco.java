package comportamientos;

import agentes.AgenteBarco;
import estados.EstadosBarco;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.Behaviour;
import utiles.GestorComunicacion;
import utiles.GestorDF;
import utiles.Posicion;

public class ComunicacionBarco extends Behaviour {

    private final String CONV_BARCO_SKAL_ID = "barco-skal-conv";
    private final String CONV_BARCO_JARL_ID = "barco-jarl-conv";
    private String CONV_BARCO_VIDENTE_ID;

    private EstadosBarco paso;
    private AgenteBarco agente;
    private Boolean finish = false;
    private AID jarl;
    private AID skal;
    private AID vidente;

    private String coord;
    private Posicion posObjetivo;

    private ACLMessage msgSkal, msgJarl, msgVidente;

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

        if (agente.getPosObj() == null) {

            switch (this.paso) {
                case INICIO_MISION:

                    msgSkal = new ACLMessage(ACLMessage.REQUEST);
                    msgSkal.addReceiver(this.skal);
                    msgSkal.setReplyWith("presentation-request");
                    msgSkal.setContent("Bro, estoy listo para encontrar a la tripulación perdida. En plan.");
                    msgSkal.setConversationId(CONV_BARCO_SKAL_ID);
                    myAgent.send(msgSkal);

                    this.agente.getGraficos().agregarTraza(msgSkal.toString());
                    this.paso = EstadosBarco.ESPERANDO_TRADUCCION_INICIO;

                    break;

                case ESPERANDO_TRADUCCION_INICIO:

                    msgSkal = agente.blockingReceive();

                    if (msgSkal != null && msgSkal.getPerformative() == ACLMessage.INFORM) {

                        if (msgSkal.getSender().equals(skal) && GestorComunicacion.checkMensajeJarl(msgSkal.getContent())) {

                            // Traduccion del mensaje
                            String mensajeAMandar = msgSkal.getContent();

                            // Enviar PROPOSAL a Skal
                            msgJarl = new ACLMessage(ACLMessage.PROPOSE);
                            msgJarl.addReceiver(this.jarl);
                            msgJarl.setReplyWith("validation-request");
                            msgJarl.setContent(mensajeAMandar);
                            msgJarl.setConversationId(CONV_BARCO_JARL_ID);
                            myAgent.send(msgJarl);
                            agente.getGraficos().agregarTraza(msgJarl.toString());
                            this.paso = EstadosBarco.ESPERANDO_TOTEM_JARL;

                        } else {
                            System.out.println("No entiendo lo que me quieres decir");
                        }
                    } else {
                        System.out.println("Error esperando INFORM en: " + agente.getLocalName());
                    }

                case ESPERANDO_TOTEM_JARL:

                    msgJarl = agente.blockingReceive();

                    if (msgJarl != null && msgJarl.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {

                        if (msgJarl.getSender().equals(jarl) && GestorComunicacion.checkMensajeBarco(msgJarl.getContent())) {

                            // Jarl me da el token
                            String contentJarl = msgJarl.getContent();
                            CONV_BARCO_VIDENTE_ID = GestorComunicacion.obtenerTotem(contentJarl);

                            // Le mando mensaje al vidente
                            msgVidente = new ACLMessage(ACLMessage.REQUEST);
                            msgVidente.addReceiver(this.vidente);
                            msgVidente.setReplyWith("crew-coordinates-request");
                            msgVidente.setContent("Bro, tengo el amuleto de Jarl. ¿Puedes ayudarme a encontrar a la tripulación? En plan.");
                            msgVidente.setConversationId(CONV_BARCO_VIDENTE_ID);
                            myAgent.send(msgVidente);
                            agente.getGraficos().agregarTraza(msgVidente.toString());

                            // HAY QUE CAMBIAR DE PASOó
                            this.paso = EstadosBarco.ESPERANDO_COORD_NAUFRAGOS;

                        } else {

                            System.out.println("No esperaba ese mensaje en este momento");

                        }

                        // Enviar REQUEST a vidente
                    } else if (msgJarl != null && msgJarl.getPerformative() == ACLMessage.REJECT_PROPOSAL) {

                        if (msgJarl.getSender().equals(jarl) && GestorComunicacion.checkMensajeBarco(msgJarl.getContent())) {

                            // Se termina el programa
                            System.exit(0);

                        } else {

                            System.out.println("No esperaba ese mensaje en este momento soy barco");

                        }

                    } else {
                        System.out.println("Error esperando CONFIRM / DISCONFIRM en: " + agente.getLocalName());
                    }

                    break;

                case ESPERANDO_COORD_NAUFRAGOS:

                    msgVidente = agente.blockingReceive();

                    if (msgVidente != null && msgVidente.getPerformative() == ACLMessage.AGREE) {

                        coord = GestorComunicacion.obtenerTotem(msgVidente.getContent());
                        posObjetivo = obtenerPosicionDeMsg(coord);
                        this.agente.setPosObjetivo(posObjetivo);

                        this.paso = EstadosBarco.SOLICITAR_NUEVA_COORD_NAUF;

                    } else if (msgVidente != null && msgVidente.getPerformative() == ACLMessage.REFUSE) {

                        this.paso = EstadosBarco.SOLICITAR_COORD_JARL;

                    } else if (msgVidente != null && msgVidente.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {

                        System.out.println("Soy el barco, Jarl me ha timado con el totem :( ");
                        System.exit(0);

                    }
                    break;

                case SOLICITAR_NUEVA_COORD_NAUF:

                    msgVidente = msgVidente.createReply(ACLMessage.REQUEST);
                    msgVidente.setContent("Bro, tengo el amuleto de Jarl. Dame las siguientes En plan.");
                    agente.send(msgVidente);
                    agente.getGraficos().agregarTraza(msgVidente.toString());

                    this.paso = EstadosBarco.ESPERANDO_COORD_NAUFRAGOS;
                    break;

                case SOLICITAR_COORD_JARL:

                    msgSkal.createReply(ACLMessage.REQUEST);
                    msgSkal.setContent("Bro, dónde está Jarl? En plan.");
                    agente.send(msgSkal);
                    agente.getGraficos().agregarTraza(msgSkal.toString());

                    this.paso = EstadosBarco.ESPERANDO_TRADUCCION_3;
                    break;

                case ESPERANDO_TRADUCCION_3:

                    msgSkal = agente.blockingReceive();

                    if (msgSkal != null && msgSkal.getPerformative() == ACLMessage.INFORM) {

                        if (msgSkal.getSender().equals(skal) && GestorComunicacion.checkMensajeJarl(msgSkal.getContent())) {

                            // Traduccion del mensaje
                            String mensajeAMandar2 = msgSkal.getContent();

                            // Enviar PROPOSAL a Skal
                            msgJarl.createReply(ACLMessage.QUERY_REF);
                            msgJarl.setContent(mensajeAMandar2);
                            myAgent.send(msgJarl);
                            agente.getGraficos().agregarTraza(msgJarl.toString());

                            this.paso = EstadosBarco.ESPERANDO_COORDENADAS_JARL;
                            break;

                        } else {
                            System.out.println("No entiendo lo que me quieres decir");
                        }
                    } else {
                        System.out.println("Error esperando INFORM en: " + agente.getLocalName());
                    }

                case ESPERANDO_COORDENADAS_JARL:
                    
                    msgJarl = agente.blockingReceive();

                    if (msgJarl != null && msgJarl.getPerformative() == ACLMessage.INFORM) {

                        coord = GestorComunicacion.obtenerTotem(msgJarl.getContent());
                        posObjetivo = obtenerPosicionDeMsg(coord);
                        this.agente.setPosObjetivo(posObjetivo);

                        this.paso = EstadosBarco.SOLICITAR_FIN_MISION;

                    } else if (msgJarl != null && msgJarl.getPerformative() == ACLMessage.REFUSE) {

                        System.out.println("S no no puedo encontrar a Jarl ...");
                        System.exit(0);
              
                    }
                    
                    break;
                    
                case SOLICITAR_FIN_MISION:
                    
                    System.out.println("TRABAJITO DE ÓSCAR");
                    System.exit(0);

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

    private Posicion obtenerPosicionDeMsg(String posString) {
        String[] coords = posString.split(",");
        int filas = Integer.parseInt(coords[0]);
        int cols = Integer.parseInt(coords[1]);
        return new Posicion(filas, cols);
    }

}
