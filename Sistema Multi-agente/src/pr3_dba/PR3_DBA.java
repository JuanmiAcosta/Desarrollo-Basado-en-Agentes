package pr3_dba;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import utiles.GestorComunicacion;

/**
 *
 * @author juanmi
 */
public class PR3_DBA {

    public static void main(String[] args) {
        
        /*
        try {
            // Iniciar el Main Container de JADE
            Runtime jadeRuntime = Runtime.instance();
            Profile mainProfile = new ProfileImpl();
            mainProfile.setParameter(Profile.GUI, "false"); 
            AgentContainer mainContainer = jadeRuntime.createMainContainer(mainProfile);

            System.out.println("Main container iniciado...");
            
            // Crear el agente barco vikingo (buscador)
            String barcoVikingoName = "barco-vikingo";
            String barcoVikingoClass = "agents.BarcoVikingo"; 
            AgentController barcoVikingoController = mainContainer.createNewAgent(barcoVikingoName, barcoVikingoClass, null);
            barcoVikingoController.start();
            System.out.println("Agente " + barcoVikingoController + " iniciado.");
            
            // Crear el agente Skal (traductor)
            String skalName = "skal";
            String skalClass = "agents.Skal"; 
            AgentController skalController = mainContainer.createNewAgent(skalName, skalClass, null);
            skalController.start();
            System.out.println("Agente " + skalController + " iniciado.");
            
            // Crear el agente Jarl
            String jarlName = "jarl";
            String jarlClass = "agents.Jarl"; 
            AgentController jarlController = mainContainer.createNewAgent(jarlName, jarlClass, null);
            jarlController.start();
            System.out.println("Agente " + jarlController + " iniciado.");
            
            // Crear el agente barco vikingo
            String videnteName = "vidente";
            String videnteClass = "agents.Vidente"; 
            AgentController videnteController = mainContainer.createNewAgent(videnteName, videnteClass, null);
            videnteController.start();
            System.out.println("Agente " + videnteController + " iniciado.");
            


        } catch (StaleProxyException e) {
            e.printStackTrace();
        }*/
        
        GestorComunicacion checkMensajes = new GestorComunicacion();

        // Mensaje de prueba para checkMensajeBarco
        String mensajeBarco = "Bro, este es un mensaje de prueba. En plan.";
        boolean esMensajeBarco = checkMensajes.checkMensajeBarco(mensajeBarco);
        System.out.println("¿Es mensaje de tipo Barco? " + esMensajeBarco);

        // Mensaje de prueba para checkMensajeJarl
        String mensajeJarl = "Joulupukki, este es un mensaje de prueba. Kiitos.";
        boolean esMensajeJarl = checkMensajes.checkMensajeJarl(mensajeJarl);
        System.out.println("¿Es mensaje de tipo Jarl? " + esMensajeJarl);

        // Traducción de mensaje de Barco a Jarl
        String mensajeTraducido = checkMensajes.traduceBarcoJarl(mensajeBarco);
        System.out.println("Mensaje traducido de Barco a Jarl: " + mensajeTraducido);

        // Prueba con mensaje que no cumple las condiciones
        String mensajeIncorrecto = "Hola, este mensaje no cumple con los criterios.";
        System.out.println("¿Es mensaje de tipo Barco? " + checkMensajes.checkMensajeBarco(mensajeIncorrecto));
        System.out.println("¿Es mensaje de tipo Jarl? " + checkMensajes.checkMensajeJarl(mensajeIncorrecto));
        System.out.println("Traducción de mensaje incorrecto: " + checkMensajes.traduceBarcoJarl(mensajeIncorrecto));

    }

}
