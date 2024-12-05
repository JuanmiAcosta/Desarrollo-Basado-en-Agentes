package main;

import agente.Acciones;
import agente.Posicion;
import agente.Sensores;
import entorno.Entorno;
import entorno.Mapa;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.io.IOException;

import java.util.Scanner;
import simulacion.Graficos;

/*

mapWithoutObstacle.txt 9 9 0 0 -> 9 energía -> Otra versión 9
mapWithVerticalWall.txt 9 9 7 3 -> 54 energía -> Otra versión 40
mapWithHorizontalWall.txt 9 5 3 5 -> 10 energía -> Otra versión 8
mapWithDiagonalWall.txt 9 9 9 0 -> 9 energía -> Otra versión 9
mapWithComplexObstacle1.txt 9 6 5 6 -> 11 energía -> Otra versión 9
mapWithComplexObstacle2.txt 9 6 3 6 -> 27 energía -> Otra versión 14
mapWithComplexObstacle3.txt 6 6 9 4 -> 46 energía -> Otra versión 31
mapComplex.txt 29 14 15 14 -> 277 energía -> Otra versión 124

*/

public class PR2_DBA {

    public static void main(String[] args) throws IOException {
        try {
            // Iniciar el Main Container de JADE
            Runtime jadeRuntime = Runtime.instance();
            Profile mainProfile = new ProfileImpl();
            mainProfile.setParameter(Profile.GUI, "false");  // Mostrar GUI del Main Container
            AgentContainer mainContainer = jadeRuntime.createMainContainer(mainProfile);

            System.out.println("Main container iniciado...");

            Scanner scanner = new Scanner(System.in);
            String[] inputs;

            while (true) {
                // Solicitar todos los datos en una línea
                System.out.print("Introduce el nombre del mapa, la fila del agente, la columna del agente, la fila del objetivo y la columna del objetivo separados por espacios: ");
                String inputLine = scanner.nextLine();
                inputs = inputLine.split(" ");

                // Verificar si se han ingresado exactamente 5 parámetros
                if (inputs.length == 5) {
                    break;
                } else {
                    System.out.println("Error: Debes ingresar exactamente 5 parámetros. Inténtalo de nuevo.\n");
                }
            }

            // Asignar los valores a las variables
            String mapaSeleccionado = inputs[0];
            int filaAgente = Integer.parseInt(inputs[1]);
            int colAgente = Integer.parseInt(inputs[2]);
            int filaObjetivo = Integer.parseInt(inputs[3]);
            int colObjetivo = Integer.parseInt(inputs[4]);

            scanner.close();

            // Mostrar los datos ingresados
            System.out.println("\nDatos ingresados:");
            System.out.println("Mapa seleccionado: " + mapaSeleccionado);
            System.out.println("Coordenadas del agente: (" + filaAgente + ", " + colAgente + ")");
            System.out.println("Coordenadas del objetivo: (" + filaObjetivo + ", " + colObjetivo + ")");

            Mapa mapa = new Mapa("ejemplos_mapas/" + mapaSeleccionado);

            Sensores sensores = new Sensores(0);
            //Agente agente = new Agente(new Posicion(filaAgente, colAgente), new Posicion(filaObjetivo, colObjetivo), sensores);
            Entorno entorno = new Entorno(new Mapa(mapa), new Posicion(filaAgente, colAgente), new Posicion(filaObjetivo, colObjetivo));
            sensores.setEntorno(entorno);
            Graficos graficos = new Graficos("Esperando acciones por parte del agente ...", entorno.getMapa().getMapa(), Acciones.ARR);

            // Ruta completa de la clase del agente (asume que están en el paquete 'ejercicios')
            String claseAgente = "agente.Agente";

            // Crear un contenedor secundario para el agente
            Profile agentProfile = new ProfileImpl();
            ContainerController agentContainer = jadeRuntime.createAgentContainer(agentProfile);

            // Crear el agente basado en el número del ejercicio
            Object[] argsAgent = new Object[] { new Posicion(filaAgente, colAgente), new Posicion(filaObjetivo, colObjetivo), sensores , graficos};
            AgentController agent = agentContainer.createNewAgent("agente", claseAgente, argsAgent);

            // Iniciar el agente
            agent.start();
            System.out.println("Agente iniciado.");

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

}
