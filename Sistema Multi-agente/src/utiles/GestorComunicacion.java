package utiles;

/**
 *
 * @author juanmi
 */
public class GestorComunicacion {     

    public Boolean checkMensajeBarco(String msg) {
        return (msg.startsWith("Bro") && msg.endsWith("En plan."));
    }

    public Boolean checkMensajeJarl(String msg) {
        return (msg.startsWith("Joulupukki") && msg.endsWith("Kiitos."));
    }

    public String traduceBarcoJarl(String msg) {
        String finalMsg = msg;
        if (finalMsg.startsWith("Bro")) {
            finalMsg = finalMsg.replaceFirst("Bro", "Joulupukki");
        }
        if (finalMsg.endsWith("En plan.")) {
            finalMsg = finalMsg.replaceFirst("En plan\\.$", "Kiitos."); // Asegura el reemplazo correcto del final.
        }
        return finalMsg;
    }

}
