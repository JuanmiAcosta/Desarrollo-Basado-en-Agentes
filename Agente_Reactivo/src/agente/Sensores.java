/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agente;

import entorno.Entorno;
import agente.Acciones;
import java.util.ArrayList;

/**
 *
 * @author jorge
 */
public class Sensores {
    // Variables
    private int energia;
    Entorno entorno;
    
    // Constructor
    public Sensores(int energia) {
        this.energia = energia;
        this.entorno = null;
    }
    
    public Sensores() {
        this(0);
    }
    
    public Sensores(Sensores otro) {
        this(otro.energia);
    }
    
    // Metodos
    public ArrayList<Acciones> analizarEntorno() {
        return entorno.getMovimientos(); 
    } 

    public void setEnergia(int energia) {
        this.energia = energia;
    }
    
    public void incrementarEnergia() {
        this.energia++;
    }

    public void setEntorno(Entorno entorno) {
        this.entorno = entorno;
    }
    
    public int getEnergia(){
        return this.energia;
    }

    public Entorno getEntorno() {
        return entorno;
    }

    @Override
    public String toString() {
        return "Sensores{" + "energia=" + energia + ", entorno=" + entorno + '}';
    }
}
