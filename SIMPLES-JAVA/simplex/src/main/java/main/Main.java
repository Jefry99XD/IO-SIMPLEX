package main;

import Controller.Simplex;
import Controller.SimplexDosFases;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        float[] z = {2, 4, 4, -3};
        String[] igualdades = {"=", "="};
        float[][] restricciones = {{1, 1, 1, 0, 4}, {1, 4, 0, 4, 8}};

        SimplexDosFases simples = new SimplexDosFases();
        simples.faseUnoSimplex(z, restricciones, igualdades);
        //Simplex simplex = new Simplex(z, restricciones, "Minimizar", "Gran M", igualdades);
        //simplex.resolver();
        //ArrayList e = simplex.obtenerResultados();
        //System.out.println(e);
    }

}


