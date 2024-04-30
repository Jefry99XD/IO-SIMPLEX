package main;

import Controller.Simplex;
import Controller.SimplexDosFases;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //Simplex
        /*
        float[] z = {1, -2, 1};
        float[][] restricciones = {{1, 1, 1, 12}, {2, 1, -1, 6}, {-1, 3, 9}};
        Simplex simplex = new Simplex(z, restricciones);
        simplex.resolver();*/
        
        //-----------------------------------//
        //-----------------------------------//
        
        // Coeficientes de la función objetivo
        float[] zeta = { 1, -1, 1};

        // Restricciones
        float[][] restricciones = {
            { 1, 1, 2, 4 },   // 1*x1 + 1*x2 <= 3
            { 1, -2, 1, -2 },  // -1*x1 + 1*x2 >= 1

        };
        
        // Crear una instancia de DosFases
        SimplexDosFases simplex = new SimplexDosFases();
        simplex.faseUnoSimplex(zeta, restricciones);
        

    }

}
