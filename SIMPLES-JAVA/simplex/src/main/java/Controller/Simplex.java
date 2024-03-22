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
    //para devolver el saliente, pa la interfaz
    private String Saliente;
    //para devolver el Entrante, pa la interfaz
    private String Entrante;

    public String getSaliente() {
        return Saliente;
    }

    public void setSaliente(String Saliente) {
        this.Saliente = Saliente;
    }

    public String getEntrante() {
        return Entrante;
    }

    public void setEntrante(String Entrante) {
        this.Entrante = Entrante;
    }

    
    
    //las iteraciones
    public ArrayList<float[][]> getIteraciones() {
        return iteraciones;
    }
    public void setIteraciones(ArrayList<float[][]> iteraciones) {
        this.iteraciones = iteraciones;
    }
    
    
    //constructor, recibe funcion z, y las restricciones como matriz, se asume que de la interfaz las que no hay son 0
    
    /*Ejemplo de entrada
        float[] z = {2, 3, 4};
        float[][] restricciones = {{1, 2, 3, 100}, {4, 5, 6, 600}, {7, 8, 9, 1000}};
    el ultimo campo es el que esta despues del igual.
    quedaria la matriz 
2.0 3.0 4.0 0.0 0.0 0.0 0.0 
1.0 2.0 3.0 1.0 0.0 0.0 100.0 
4.0 5.0 6.0 0.0 1.0 0.0 600.0 
7.0 8.0 9.0 0.0 0.0 1.0 1000.0  
    */
public Simplex(float[] zeta, float[][] restricciones) {
    // +1 a la tabla para guardar el radio
    // ej x1 x2 x3 rhs radio
    float[][] tablaInicial = new float[zeta.length + 1][];
    for (int i = 0; i < tablaInicial.length; i++) {
        tablaInicial[i] = new float[restricciones[0].length + restricciones.length];
    }

    // Copiar zeta y restricciones
    System.arraycopy(zeta, 0, tablaInicial[0], 0, zeta.length);

    for (int i = 0; i < restricciones.length; i++) {
        //pone las restricciones menos el ultimo, porque el ultimo es el de <=
        System.arraycopy(restricciones[i], 0, tablaInicial[i + 1], 0, restricciones[i].length - 1);

        // para dar el valor de holgura
        tablaInicial[i + 1][zeta.length + i] = 1;

        // Copiar el último valor de la restricción
        tablaInicial[i + 1][tablaInicial[i + 1].length - 1] = restricciones[i][restricciones[i].length - 1];
    }

    // Imprimir la tabla inicial para verificar
    imprimirTabla(tablaInicial);
    //guarda la tabla 0 en las iteraciones
    iteraciones.add(tablaInicial);
}



public void imprimirTabla(float[][] tabla) {
        for (float[] tabla1 : tabla) {
            for (int j = 0; j < tabla1.length; j++) {
                System.out.print(tabla1[j] + " ");
            }
            System.out.println();
        }
}

    public void iteracion(){
        //aqui va todo el proceso de la iteracion
    }
    public int masNegativo(){
        //funcion que retorna el INDICE del z mas negativo, si quiere que devuelva el elemento cambielo a float
        float[][] actual = iteraciones.get(iteraciones.size() - 1);
        int indiceMasNegativo = 9999;
        float[] zeta = actual[0];
        for(int i =0; i< zeta.length-1;i++){
            if(zeta[i]<indiceMasNegativo){
                //posicion del indicemasNegativo
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
//Los radios los va poniendo en el ultimo elemento de la fila, puede modificar la ultima iteracion o crear una tabla para devolverla
//puede colocar el infinito como un -1 en la tabla
    }
    
}
