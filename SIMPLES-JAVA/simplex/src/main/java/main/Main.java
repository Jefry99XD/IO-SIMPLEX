package main;

import Controller.Simplex;

public class Main {

    public static void main(String[] args) {
        float[] z = {5,4};
        float[][] restricciones = {{2, -1, 4},{5, 3, 15}};

        Simplex simplex = new Simplex(z, restricciones);
        simplex.resolver();
    }

}
