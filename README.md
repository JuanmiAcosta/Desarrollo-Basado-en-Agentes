# Práctica 2 - Movimiento de un Agente en un Mundo Bidimensional

## Descripción
Esta práctica consiste en desarrollar un agente inteligente capaz de desplazarse por un mundo bidimensional, evitando obstáculos y dirigiéndose hacia una posición objetivo. El agente será reactivo con estados internos y deberá resolver varios tipos de mapas, algunos con obstáculos de diferentes formas.

## Requisitos
- **NetBeans** como entorno de desarrollo.
- **JADE** para la implementación de los agentes.
- Conocimiento de programación orientada a objetos y conceptos básicos de sistemas multiagente.

## Estructura del Proyecto
El proyecto se compone de tres componentes principales:
1. **Mapa**: Representado por una matriz `NxM`, almacenada en un archivo de texto, donde cada celda contiene:
   - `-1`: Obstáculo (celda inaccesible).
   - `0`: Celda libre.
2. **Entorno**: Define el mundo bidimensional en el que se mueve el agente.
3. **Agente**: Tiene la capacidad de percibir su entorno inmediato y tomar decisiones sobre el mejor camino hacia el objetivo.

El agente podrá:
- Ver las celdas adyacentes.
- Evitar obstáculos.
- Encontrar la ruta más eficiente hacia su objetivo.
- Calcular el consumo de energía (incrementa con cada celda recorrida).

## Archivos del Proyecto
- **Código fuente**: Contiene las clases necesarias para la simulación (mapa, entorno, agente).
- **Mapas**: Archivos de texto que representan diferentes escenarios.
- **Presentación**: Explicación de las decisiones de diseño e implementación.

## Clonar Proyecto

   ```bash
   git clone https://github.com/tuusuario/proyecto-agentes.git
