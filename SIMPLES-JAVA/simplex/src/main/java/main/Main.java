package main;

import Controller.Simplex;
import Controller.SimplexDosFases;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        float[] z = {2, -1};
        String[] igualdades = {"<=", ">="};
        float[][] restricciones = {{1,1,3}, {-1,1,1}};

        SimplexDosFases simples = new SimplexDosFases();
        simples.faseUnoSimplex(z, restricciones, igualdades);
        //Simplex simplex = new Simplex(z, restricciones, "Minimizar", "Gran M", igualdades);
        //simplex.resolver();
        //ArrayList e = simplex.obtenerResultados();
        //System.out.println(e);
    }

}
