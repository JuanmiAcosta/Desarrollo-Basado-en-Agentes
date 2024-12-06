package utiles;

/**
 *
 * @author juanmi
 */
public class GestorComunicacion {     

    public static Boolean checkMensajeBarco(String msg) {
        return (msg.startsWith("Bro") && msg.endsWith("En plan."));
    }

    public static Boolean checkMensajeJarl(String msg) {
        return (msg.startsWith("Joulupukki") && msg.endsWith("Kiitos."));
    }
    
    public static String jarlConfirmaDigno(Boolean confirma) {
        String msg;
        
        if(confirma) {
            
        }
        else {
            
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
        
    }
} 
