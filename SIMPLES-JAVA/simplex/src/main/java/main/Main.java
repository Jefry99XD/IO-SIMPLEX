/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import Controller.Simplex;

/**
 *
 * @author jeffr
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        float[] z = {2, 3, 4};
        float[][] restricciones = {{1, 2, 3, 100}, {4, 5, 6, 600}, {7, 8, 9, 1000}};

        Simplex simplex = new Simplex(z, restricciones);
        
        simplex.CalcularRadios(simplex.masNegativo());
        float pivote = simplex.getPivote(simplex.masNegativo());
       // int pivote = simplex.getPivote(simplex.masNegativo());
        //System.out.println("El pivote es: " + pivote);
        
    }
    
}
