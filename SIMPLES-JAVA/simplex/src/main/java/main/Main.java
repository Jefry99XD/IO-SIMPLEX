package main;

import Controller.Simplex;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        float[] z = {3, -2, 5};
        float[][] restricciones = {{1, 2, 1, 5}, {-3, 1, -1, 4}};


        Simplex simplex = new Simplex(z, restricciones, "Minimizar", "normal");
        simplex.resolver();
        ArrayList e = simplex.obtenerResultados();
        System.out.println(e);
    }

}
