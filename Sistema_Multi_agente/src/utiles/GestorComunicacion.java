package utiles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author juanmi
 */
public class GestorComunicacion {

    public static Boolean checkMensajeBarco(String msg) {
        return (msg.startsWith("Bro") && msg.endsWith("En plan."));
    }

    public static Boolean checkMensajeJarl(String msg) {
        return (msg.startsWith("Joulupukki") && msg.endsWith("Kiitos.")) || (msg.startsWith("Hyvää joulua") && msg.endsWith("Nähdään pian."));
    }

    // Generación del string si es digno o no
    public static String jarlConfirmaDigno(Boolean confirma, String idConv) {
        String msg;

        if (confirma) {
            msg = "Hyvää joulua, viento a favor. ID [" + idConv + "]. Nähdään pian.";
        } else {
            msg = "Hyvää joulua, no estás perapado. Vuelve a remar. Nähdään pian.";
        }

        return msg;
    }

    public static String traduceBarcoJarl(String msg) {
        String finalMsg = msg;

        if (finalMsg.startsWith("Bro")) {
            finalMsg = finalMsg.replaceFirst("Bro", "Joulupukki");
        }

        if (finalMsg.endsWith("En plan.")) {
            finalMsg = finalMsg.replaceFirst("En plan\\.$", "Kiitos."); // Asegura el reemplazo correcto del final.
        }

        return finalMsg;
    }

    public static String traduceJarlBarco(String msg) {
        String finalMsg = msg;

        if (finalMsg.startsWith("Hyvää joulua")) {
            finalMsg = finalMsg.replaceFirst("Hyvää joulua", "Bro");
        }

        if (finalMsg.endsWith("Nähdään pian.")) {
            finalMsg = finalMsg.replaceFirst("Nähdään pian\\.?\\s*$", "En plan.");
        }

        return finalMsg;
    }
    
    public static String obtenerTotem(String msg){
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(msg);
        matcher.find();
        return matcher.group(1);
    }
}
