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
    public void convertirOpuestosZ(float[] zeta){
        for (int i = 0; i < zeta.length; i++){
            zeta[i] = -zeta[i];
        }
    }
    
    //-----------------------------------------
    //-----------------------------------------
    
    public Simplex(float[] zeta, float[][] restricciones) {
        //Llamado a la funcion 
        convertirOpuestosZ(zeta);

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
    
    //-----------------------------------------
    //-----------------------------------------

    public void imprimirTabla(float[][] tabla) {
        for (float[] tabla1 : tabla) {
            for (int j = 0; j < tabla1.length; j++) {
                System.out.print(tabla1[j] + "         ");
            }
            System.out.println();
        }
    }
    
    //-----------------------------------------
    //-----------------------------------------
    
    public void imprimirTablaActual() {
        float[][] tabla = iteraciones.get(iteraciones.size() - 1);
        for (float[] fila : tabla) {
            for (float valor : fila) {
                // Convertir el valor a cadena y ajustar el ancho del campo para agregar espacios adicionales
                System.out.printf("              ");
            }
            System.out.println(); // Nueva línea para cada fila
        }
    }
    
    //-----------------------------------------
    //-----------------------------------------

    public void iteracion(){
        int indiceMasNegativo = masNegativo();
        float pivote = getPivote(indiceMasNegativo);
        operacionesPivote(indiceMasNegativo);
        
    }
    
    //-----------------------------------------
    //-----------------------------------------
    
    public void operacionesPivote(int indiceMasNegativo){
        float[][] tabla = iteraciones.get(iteraciones.size() - 1 );
        float pivote = getPivote(indiceMasNegativo);
        
        int filaPivote = -1;
        float menorValorRadio = Float.MAX_VALUE;
        
        for(int i = 1; i < tabla.length; i++){
            float valorActual = tabla[i][tabla[i].length - 1];
            if(tabla[i][indiceMasNegativo] > 0 && valorActual / tabla[i][indiceMasNegativo] < menorValorRadio){
                menorValorRadio = valorActual / tabla[i][indiceMasNegativo];
                filaPivote = i;
            }
        }
        if(filaPivote == -1){
            return;
        }
        
        for (int j = 0; j < tabla[filaPivote].length; j++) {
            tabla[filaPivote][j] /= pivote;
        }

        // Para cada otra fila en la tabla
        for (int i = 0; i < tabla.length; i++) {
            if (i != filaPivote) { // No necesitamos operar en la fila del pivote
                float factor = -tabla[i][indiceMasNegativo]; // Factor para hacer cero el elemento en la columna del pivote
                for (int j = 0; j < tabla[i].length; j++) {
                    tabla[i][j] += factor * tabla[filaPivote][j]; // Sumar la fila del pivote multiplicada por el factor
                }
            }
        }
        
        iteraciones.add(tabla);
        //imprimirTablaActual();
    }
    
    
    
    //-----------------------------------------
    //-----------------------------------------

    public int masNegativo(){
    //funcion que retorna el INDICE del z mas negativo, si quiere que devuelva el elemento cambielo a float
        float[][] actual = iteraciones.get(iteraciones.size() - 1);
        int indiceMasNegativo = 0;
        float[] zeta = actual[0];
        for(int i = 0; i < zeta.length-1; i++){
            if(zeta[i] < zeta[indiceMasNegativo]){
                //posicion del indicemasNegativo
                indiceMasNegativo = i;
            }
        }
        return indiceMasNegativo;
    }
    
    //-----------------------------------------
    //-----------------------------------------

    public float getPivote(int indiceMasNegativo) {
            float[][] actual = iteraciones.get(iteraciones.size() - 1); // Obtener la última iteración
            float menorValorZ = Float.MAX_VALUE;                        // Inicializar el menor valor de Z como el máximo posible
            float menorValorRadio = Float.MAX_VALUE;                    // Inicializar el menor valor de la columna de los radios
            int menorRadioIndice = 0;
            float pivote = 0; 
            // Encontrar el valor más negativo en Z
            for (int i = 0; i < actual.length; i++) {
                if (actual[i][indiceMasNegativo] < menorValorZ) {
                    menorValorZ = actual[i][indiceMasNegativo];
                }
            }
            // Buscar el menor valor en la columna de los radios
            for (int i = 1; i < actual.length; i++) {
                float valorActual = actual[i][actual[i].length - 1];
                if (valorActual < menorValorRadio) {
                    menorValorRadio = valorActual;
                    menorRadioIndice = i;
                }
            }
            //System.out.println("Valor más negativo en Z: " + menorValorZ);
            //System.out.println("Valor más pequeño en radio: " + menorValorRadio);
            // Encontrar el pivote
            pivote = actual[menorRadioIndice][indiceMasNegativo];
            //System.out.println("Pivote: " + pivote);
            // Devolver el valor del pivote
            return pivote;
        }

    //-----------------------------------------
    //-----------------------------------------
    public void CalcularRadios(int indiceMasNegativo) {
        float[][] ultimaIteracion = iteraciones.get(iteraciones.size() - 1);

        // Imprimir valor más negativo
        System.out.println("");
        //System.out.println("Valor más negativo: " + ultimaIteracion[0][indiceMasNegativo]);
        System.out.println("");
        
        for (int i = 1; i < ultimaIteracion.length; i++) {
            float valorMasNegativo = ultimaIteracion[i][indiceMasNegativo];
            float valorUltimaColumna = ultimaIteracion[i][ultimaIteracion[i].length - 1];
            float radio;

            if (valorMasNegativo != 0) {
                radio = valorUltimaColumna / valorMasNegativo;
            } else {
                radio = Float.POSITIVE_INFINITY;
            }

            //Si el radio da negativo, se pone inf
            if (radio < 0) {
                radio = Float.POSITIVE_INFINITY;
            }
            // Crear una nueva fila con la longitud adecuada
            float[] filaConRadio = new float[ultimaIteracion[i].length + 1];

            // Copiar los elementos de la fila existente
            System.arraycopy(ultimaIteracion[i], 0, filaConRadio, 0, ultimaIteracion[i].length);

            // Agregar el nuevo radio al final de la fila
            filaConRadio[filaConRadio.length - 1] = radio;

            // Reemplazar la fila existente con la nueva fila que incluye el radio
            ultimaIteracion[i] = filaConRadio;
        }
        System.out.println("Tabla con radios:");
        imprimirTabla(ultimaIteracion);
    }
    
}
