package main;

import Controller.Simplex;
<<<<<<< HEAD
import Controller.SimplexDosFases;
import java.util.Arrays;
=======
import java.util.ArrayList;
>>>>>>> 6e9c7de169ff4e6f2fc2fb8e574b4f63d1274017

public class Main {

    public static void main(String[] args) {
<<<<<<< HEAD
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
        

=======
        float[] z = {15, 10};
        String[] igualdades = {"<=", "=", ">="};
        float[][] restricciones = {{1, 0, 2}, {0, 1, 3}, {1, 1, 4}};


        Simplex simplex = new Simplex(z, restricciones, "Minimizar", "Gran M", igualdades);
        simplex.resolver();
        ArrayList e = simplex.obtenerResultados();
        System.out.println(e);
>>>>>>> 6e9c7de169ff4e6f2fc2fb8e574b4f63d1274017
    }

}
