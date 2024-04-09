package main;

import Controller.Simplex;

public class Main {

    public static void main(String[] args) {
        float[] z = {1, -2, 1};
        float[][] restricciones = {{1, 1, 1, 12}, {2, 1, -1, 6}, {-1, 3, 9}};


        Simplex simplex = new Simplex(z, restricciones);
        simplex.resolver();
    }

}
