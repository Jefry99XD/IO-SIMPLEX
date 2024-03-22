/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.util.ArrayList;

/**
 *
 * @author jeffr
 */
public class Simplex {
    //guarda las iteraciones
    private ArrayList<float[][]> iteraciones = new ArrayList<>();

    public ArrayList<float[][]> getIteraciones() {
        return iteraciones;
    }
    public void setIteraciones(ArrayList<float[][]> iteraciones) {
        this.iteraciones = iteraciones;
    }
public Simplex(float[] zeta, float[][] restricciones) {
    // +1 a la tabla para guardar el radio
    // ej x1 x2 x3 rhs radio
    float[][] tablaInicial = new float[zeta.length + 1][];
    tablaInicial[0] = zeta;

    for (int i = 0; i < restricciones.length; i++) {
        tablaInicial[i + 1] = new float[restricciones[i].length]; // Inicializar la fila antes de copiar
        System.arraycopy(restricciones[i], 0, tablaInicial[i + 1], 0, restricciones[i].length);
    }
    iteraciones.add(tablaInicial);
}

    public void iteracion(){

    }
    public int masNegativo(){
        //funcion que retorna el indice del z mas negativo
        float[][] actual = iteraciones.get(iteraciones.size());
        int indiceMasNegativo = 9999;

        float[] zeta = actual[0];
        for(int i =0; i< zeta.length-1;i++){
            if(zeta[i]<indiceMasNegativo){
                indiceMasNegativo = i;
            }
        }
        return indiceMasNegativo;
    }
    public int getPivote(int indiceMasNegativo){
        //funcion que obtiene el pivote de la tabla, recibe el indice mas negativo
        float[][] actual = iteraciones.get(iteraciones.size());
        int pivote = 0;
        for (int i = 0; i < actual.length; i++) {
            
        }
        return pivote;
    }
    public void CalcularRadios(int indiceMasNegativo){
                
    }

    @Override
    public String toString() {
        return "Simplex{" + "iteraciones=" + iteraciones + '}';
    }
    
}
