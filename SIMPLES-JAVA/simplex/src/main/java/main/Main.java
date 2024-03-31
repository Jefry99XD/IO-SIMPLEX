package main;

import Controller.Simplex;

public class Main {

    public static void main(String[] args) {
        float[] z = {2, 3, 4};
        float[][] restricciones = {{1, 2, 3, 100}, {4, 5, 6, 600}, {7, 8, 9, 1000}};

        Simplex simplex = new Simplex(z, restricciones);
        simplex.resolver();
    }

}
