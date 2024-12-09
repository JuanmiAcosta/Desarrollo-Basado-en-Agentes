package pr3_dba;

import entorno.Entorno;
import entorno.Mapa;
import utiles.Sensores;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import simulacion.Graficos;
import utiles.Acciones;
import utiles.Posicion;

/**
 *
 * @author juanmi
 */
public class PR3_DBA { //  mapWithoutObstacle.txt 9 9

    public static void main(String[] args) throws IOException {

        try {
            // Iniciar el Main Container de JADE
            Runtime jadeRuntime = Runtime.instance();
            Profile mainProfile = new ProfileImpl();
            mainProfile.setParameter(Profile.GUI, "false");
            AgentContainer mainContainer = jadeRuntime.createMainContainer(mainProfile);

            System.out.println("Main container iniciado...");

            // Ruta del archivo (asegúrate de colocarlo en el paquete `entrada` o ajustar la ruta según tu entorno)
            String rutaArchivo = "src/entrada/entrada.txt";

            String mapaSeleccionado = "";
            int filaAgente = 0;
            int colAgente = 0;
            int filJarl = 0;
            int colJarl = 0;
            int numNaufragos = 0;
            ArrayList<Posicion> posNaufragos = new ArrayList<Posicion>();

            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                // Leer la primera línea del archivo
                String linea = br.readLine();
                if (linea != null) {
                    // Dividir la línea en partes separadas por espacios
                    String[] inputs = linea.split(" ");

                    // Verificar que la línea contiene exactamente 3 parámetros
                    if (inputs.length >= 6) {
                        // Asignar los valores a las variables
                        mapaSeleccionado = inputs[0];
                        filaAgente = Integer.parseInt(inputs[1]);
                        colAgente = Integer.parseInt(inputs[2]);
                        filJarl = Integer.parseInt(inputs[3]);
                        colJarl = Integer.parseInt(inputs[4]);
                        numNaufragos = Integer.parseInt(inputs[5]);

                        for (int i = 1; i < numNaufragos + 1; i++) {
                            posNaufragos.add(new Posicion(Integer.parseInt(inputs[5 + (1 * i)]), Integer.parseInt(inputs[5 + (2 * i)])));
                        }

                    } else {
                        System.out.println("Error: La línea en el archivo debe contener exactamente 6 parámetros.");
                        return;
                    }
                } else {
                    System.out.println("Error: El archivo está vacío.");
                    return;
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
                return;
            }

            Mapa mapa = new Mapa("ejemplos_mapas/" + mapaSeleccionado);
            Sensores sensores = new Sensores(0);
            Entorno entorno = new Entorno(new Mapa(mapa), new Posicion(filaAgente, colAgente), new Posicion(filJarl, colJarl), posNaufragos);
            sensores.setEntorno(entorno);
            Graficos graficos = new Graficos("Esperando conversaciones de los agentes ...", entorno.getMapa().getMapa(), Acciones.ARR);

            Object[] argsAgent = new Object[]{new Posicion(filaAgente, colAgente), null, sensores, graficos};

            // Crear el agente barco vikingo (buscador)
            String barcoVikingoName = "barco-vikingo";
            String barcoVikingoClass = "agentes.AgenteBarco";
            AgentController barcoVikingoController = mainContainer.createNewAgent(barcoVikingoName, barcoVikingoClass, argsAgent);
            barcoVikingoController.start();
            //System.out.println("Agente " + barcoVikingoController + " iniciado.");

            // Crear el agente Skal (traductor)
            Object[] argsSkal = new Object[]{graficos};

            String skalName = "skal";
            String skalClass = "agentes.AgenteSkal";
            AgentController skalController = mainContainer.createNewAgent(skalName, skalClass, argsSkal);
            skalController.start();
            //System.out.println("Agente " + skalController + " iniciado.");

            // Crear el agente Jarl
            Object[] argsJarl = new Object[]{new Posicion(filJarl, colJarl), graficos};

            String jarlName = "jarl";
            String jarlClass = "agentes.AgenteJarl";
            AgentController jarlController = mainContainer.createNewAgent(jarlName, jarlClass, argsJarl);
            jarlController.start();
            //System.out.println("Agente " + jarlController + " iniciado.");

            // Crear el agente vidente
            Object[] argsVidente = new Object[]{graficos, posNaufragos};

            String videnteName = "vidente";
            String videnteClass = "agentes.AgenteVidente";
            AgentController videnteController = mainContainer.createNewAgent(videnteName, videnteClass, argsVidente);
            videnteController.start();
            //System.out.println("Agente " + videnteController + " iniciado.");

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

        /*
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
         */
    }

}
