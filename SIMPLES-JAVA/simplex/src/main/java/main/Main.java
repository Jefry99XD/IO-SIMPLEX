package main;

import Controller.Simplex;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        float[] z = {15, 10};
        String[] igualdades = {"<=", "<=", "="};
        float[][] restricciones = {{1, 0, 2}, {0, 1, 3}, {1, 1, 4}};


        Simplex simplex = new Simplex(z, restricciones, "Maximizar", "Gran M", igualdades);
        simplex.resolver();
        ArrayList e = simplex.obtenerResultados();
        System.out.println(e);
    }

}
