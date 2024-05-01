/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// SimplexDosFases.java
package Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplexDosFases {
    //guarda las iteraciones
    private ArrayList<float[][]> iteraciones = new ArrayList<>();
    private float[] zeta;
    private float[][] restricciones;
    private ArrayList artificiales = new ArrayList();
    
    //las iteraciones
    public ArrayList<float[][]> getIteraciones() {
        return iteraciones;
    }
    public void setIteraciones(ArrayList<float[][]> iteraciones) {
        this.iteraciones = iteraciones;
    }
    
    

    public void faseUnoSimplex(float[] zeta, float[][] restricciones, String[] igualdades) {

        // Cambiar signo de zeta
        float[] zetaOpuesto = cambiarSignoZeta(zeta);

        // Paso 1: Crear la tabla inicial con variables de holgura y artificiales
        float[][] tablaInicial = crearTablaInicial(zetaOpuesto, restricciones, igualdades);

        // Convertir la columna de la variable artificial en una columna básica
        hacerColumnaBasica(tablaInicial);

        // Mostrar la tabla actualizada
        System.out.println("\n Tabla actualizada:");
        imprimirTabla(tablaInicial);
        
        
        // Calcular el radio para cada fila
        float[] radios = calcularRadio(tablaInicial);

        // Mostrar la tabla actualizada con los radios
        System.out.println("\n Tabla actualizada con radios:");
        imprimirTablaConRadio(tablaInicial, radios);
        
        
        // Continuar iterando mientras la fila del lado derecho no sea cero
        while (tablaInicial[0][tablaInicial[0].length - 1] < 0) {
        // Paso 1: Encontrar el índice del valor más negativo en la fila Z
        int indiceValorNegativo = obtenerIndiceMasNegativo(tablaInicial);
        //System.out.println("indice mas negativo----: "+indiceValorNegativo);

        // Paso 2: Calcular el pivote
        float pivote = getPivote(tablaInicial, indiceValorNegativo);
        //System.out.println("pivote: "+pivote);
        
        // Verificar si se encontró un pivote válido
        if (pivote == -1) {
            // Si no se encontró un pivote válido, la solución no tiene límite
            System.out.println("La solución no tiene límite.");
            break; // Salir del bucle while
        }

        // Paso 3: Actualizar la tabla
        actualizarTabla(tablaInicial, indiceValorNegativo);

        // Paso 4: Recalcular los radios
        radios = calcularRadio(tablaInicial);

        // Mostrar la tabla actualizada y los radios
        System.out.println("\n Tabla actualizada:");
        imprimirTabla(tablaInicial);
        System.out.println("\n Tabla actualizada con radios:");
        imprimirTablaConRadio(tablaInicial, radios);
    }
    // Una vez que la fase 1 haya terminado y el lado derecho sea cero, eliminar la primera fila y las columnas de las variables artificiales
 
    



    // Mostrar la tabla final después de eliminar la primera fila y las variables artificiales
    System.out.println("\n Tabla final:");
    imprimirTabla(tablaInicial);

 }
    //-------------------------------------------------
    //-------------------------------------------------

    //HACER LO DE ELIMINAR LA PRIMER FILA Y COLUMNA DE LAS ARTIFICIALES 
    
    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private float[] cambiarSignoZeta(float[] zeta) {
        float[] zetaOpuesto = new float[zeta.length];
        for (int i = 0; i < zeta.length; i++) {
            zetaOpuesto[i] = -zeta[i]; // Cambiar signo
        }
        return zetaOpuesto;
    }
    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private float[][] crearTablaInicial(float[] zeta, float[][] restricciones, String[] igualdades) {
    cambiarSignoZeta(zeta);

    // Tamaño de la tablaInicial
    int filaTabla = restricciones.length + 2; // Una fila para la fila de ceros, una fila para Zeta

    // Verificar cuántas holguras y artificiales hay que crear.
    int artificiales = 0;
    int holguras = 0;
    for (String igualdad : igualdades) {
        if ("<=".equals(igualdad))
            holguras++;
        else if ("=".equals(igualdad))
            artificiales++;
        else if (">=".equals(igualdad)) {
            artificiales++;
            holguras++;
        }
    }
    int columnaTabla = zeta.length + artificiales + holguras + 1;

    // Crear la matriz tablaInicial
    float[][] tablaInicial = new float[filaTabla][columnaTabla];

    // Agregar una fila al principio de la matriz con ceros
    float[] filaCeros = new float[columnaTabla];
    tablaInicial[0] = filaCeros;

    // Copiar zeta
    System.arraycopy(zeta, 0, tablaInicial[1], 0, zeta.length);

    // Copiar restricciones
    for (int i = 0; i < restricciones.length; i++) {
        // Copiar las restricciones menos el último valor, ya que es el término independiente
        System.arraycopy(restricciones[i], 0, tablaInicial[i + 2], 0, restricciones[i].length - 1);

        // Asignar el valor adecuado a las variables de holgura y artificiales
        if ("<=".equals(igualdades[i])) {
            tablaInicial[i + 2][zeta.length + holguras - 2 + i] = 1; // Variable de holgura
        } else if ("=".equals(igualdades[i])) {
            tablaInicial[i + 2][zeta.length + holguras - 2 + artificiales + i] = 1; // artificial
            tablaInicial[0][zeta.length + holguras - 2 + artificiales + i] = 1;
        } else if (">=".equals(igualdades[i])) {
            tablaInicial[i + 2][zeta.length + holguras - 2 + i] = -1; // holgura
            tablaInicial[i + 2][zeta.length + holguras - 2 + artificiales + i] = 1; // artificial
            tablaInicial[0][zeta.length + holguras - 2 + artificiales + i] = 1;
        }

        // Copiar el último valor de la restricción
        tablaInicial[i + 2][columnaTabla - 1] = restricciones[i][restricciones[i].length - 1];
    }

    // Imprimir la tabla inicial para verificar
    imprimirTabla(tablaInicial);

    // Guardar la tabla 0 en las iteraciones
    return tablaInicial;
    }
    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private void imprimirTabla(float[][] tabla) {
        // Imprimir la tabla
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.print(tabla[i][j] + "\t");
            }
            System.out.println();
        }
    }
    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private void hacerColumnaBasica(float[][] tabla) {
        // Obtener la fila Z
        float[] filaZ = tabla[0];
        int numVariablesArtificiales = 0;
        int[] columnasBasicas = new int[filaZ.length]; // Arreglo para almacenar las columnas básicas
        int numColumnasBasicas = 0;

        // Encontrar las columnas correspondientes a las variables artificiales en la fila Z
        for (int i = 0; i < filaZ.length - 1; i++) {
            if (filaZ[i] == 1.0f) {
                numVariablesArtificiales++;
                columnasBasicas[numColumnasBasicas++] = i; // Almacenar la columna básica
            }
        }

        // Hacer básicas las columnas encontradas en la fila Z
        for (int j = 0; j < numColumnasBasicas; j++) {
            int columnaBasica = columnasBasicas[j];
            // Verificar que la columna no sea la de la variable artificial de la fila Z
            if (columnaBasica != -1) {
                for (int fila = 0; fila < tabla.length; fila++) {
                    if (fila != 0) { // Ignorar la fila Z
                        if (tabla[fila][columnaBasica] == 1.0f) {
                            // Realizar operaciones de fila y columna en la fila Z
                            for (int k = 0; k < tabla[0].length; k++) {
                                tabla[0][k] -= tabla[fila][k];
                            }
                        }
                    }
                }
            }
        }
    }
    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private float[] calcularRadio(float[][] tabla) {
        // Encontrar el índice del valor más negativo en la primera fila
        int indiceValorNegativo = obtenerIndiceMasNegativo(tabla);

        // Si no hay valores negativos en la primera fila, la solución es óptima y no se puede calcular el radio
        if (indiceValorNegativo == -1) {
            System.out.println("No se puede calcular el radio. La solución es óptima.");
            return new float[0];
        }

        // Inicializar un arreglo para almacenar los radios
        float[] radios = new float[tabla.length - 1]; // Excluir la fila de la función objetivo

        // Calcular el radio para cada fila
        for (int fila = 1; fila < tabla.length; fila++) {
            // Obtener el valor correspondiente en la columna del índice del valor más negativo
            float valorColumnaNegativa = tabla[fila][indiceValorNegativo];

            // Si el valor en la columna del índice del valor más negativo es positivo, calcular el radio
            if (valorColumnaNegativa > 0) {
                radios[fila - 1] = tabla[fila][tabla[fila].length - 1] / valorColumnaNegativa;
            } else {
                // Si el valor en la columna del índice del valor más negativo es no positivo,
                // asignar infinito al radio para indicar que no hay pivote válido en esa fila
                radios[fila - 1] = Float.POSITIVE_INFINITY;
            }
        }

        return radios;
    }

    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private void imprimirTablaConRadio(float[][] tabla, float[] radios) {
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.print(tabla[i][j] + "\t");
            }
            // Agregar los radios en una columna adicional en todas las filas
            if (i != 0) { // Si no estamos en la primera fila (que contiene los coeficientes de la función objetivo)
                float radio = radios[i - 1];
                if (radio <= 0) { // Si el radio es negativo o cero, mostrar "-1"
                    System.out.print("-1\t");
                } else if (radio == Float.POSITIVE_INFINITY) { // Si el radio es infinito, mostrar "inf"
                    System.out.print("inf\t");
                } else {
                    System.out.print(radio + "\t"); // Mostrar el radio calculado
                }
            } else {
                System.out.print("\t"); // Dejar espacio en blanco en la primera fila
            }
            System.out.println();
        }
    }
    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private int obtenerIndiceMasNegativo(float[][] tabla) {
        // Encontrar el índice del valor más negativo en la fila Z
        int indiceMasNegativo = -1;
        float valorMasNegativo = Float.MAX_VALUE;

        for (int j = 0; j < tabla[0].length - 1; j++) { // Excluir la última columna (lado derecho)
            if (tabla[0][j] < valorMasNegativo) {
                indiceMasNegativo = j;
                valorMasNegativo = tabla[0][j];
            }
        }
        //System.out.println("valor mas negativo: "+valorMasNegativo);
        return indiceMasNegativo;
    }
    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private float getPivote(float[][] tabla, int indiceMasNegativo) {
        float menorValorZ = Float.MAX_VALUE; // Inicializar el menor valor de Z como el máximo posible
        float menorValorRadioPositivo = Float.MAX_VALUE; // Inicializar el menor valor positivo de la columna de los radios
        int indiceFilaPivote = -1;

        // Encontrar el valor más negativo en Z
        for (int i = 0; i < tabla.length; i++) {
            if (tabla[i][indiceMasNegativo] < menorValorZ) {
                menorValorZ = tabla[i][indiceMasNegativo];
            }
        }

        // Encontrar el menor valor positivo en la columna de los radios
        for (int i = 1; i < tabla.length; i++) {
            // Evitar divisiones por cero y radios negativos
            if (tabla[i][indiceMasNegativo] > 0) {
                float valorRadio = tabla[i][tabla[i].length - 1] / tabla[i][indiceMasNegativo];
                if (valorRadio < menorValorRadioPositivo) {
                    menorValorRadioPositivo = valorRadio;
                    indiceFilaPivote = i;
                }
            }
        }
        //System.out.println("indiceFilaPivote: " + indiceFilaPivote);
        //System.out.println("indiceMasNegativo: " + indiceMasNegativo);

        // Imprimir la fila del pivote
        /*System.out.println("Fila del pivoteeee:");
        for (int j = 0; j < tabla[indiceFilaPivote].length; j++) {
            System.out.print(tabla[indiceFilaPivote][j] + "\t");
        }
        System.out.println(); // Saltar a una nueva línea después de imprimir la fila del pivote
        */
        // Si no se encontró ningún radio positivo, retornar -1 para indicar que no hay pivote válido
        if (indiceFilaPivote == -1) {
            System.out.println("No se encontró un pivote válido.");
            return -1;
        }

        //System.out.println("Valor del pivote: " + tabla[indiceFilaPivote][indiceMasNegativo]);
        // Devolver el valor del pivote
        return tabla[indiceFilaPivote][indiceMasNegativo];
    }


    //-------------------------------------------------
    //-------------------------------------------------
    //------------------------------------------------- 
private void actualizarTabla(float[][] tabla, int indiceMasNegativo) {
    // Obtener el índice de la fila del pivote utilizando la función getPivote
    int indiceFilaPivote = obtenerIndiceFilaPivote(tabla, indiceMasNegativo);
    float pivote = getPivote(tabla, indiceMasNegativo);

    // Verificar si se encontró un pivote válido
    if (pivote == -1) {
        // Si no se encontró un pivote válido, no se puede continuar
        System.out.println("No se encontró un pivote válido.");
        return;
    }

    // Obtener el número de filas y columnas de la tabla
    int filas = tabla.length;
    int columnas = tabla[0].length;

    // Imprimir la fila del pivote antes de la división
    System.out.println("Fila del pivote antes de la división:");
    for (int j = 0; j < columnas; j++) {
        System.out.print(tabla[indiceFilaPivote][j] + "\t");
    }
    System.out.println(); // Saltar a una nueva línea después de imprimir la fila

    // Dividir cada elemento de la fila del pivote por el pivote
    for (int j = 0; j < columnas; j++) {
        tabla[indiceFilaPivote][j] /= pivote;
    }

    // Hacer cero todos los elementos de la columna del pivote excepto el pivote
    for (int i = 0; i < filas; i++) {
        if (i != indiceFilaPivote) { // No hacer nada en la fila del pivote
            float factor = tabla[i][indiceMasNegativo];
            // Iterar sobre todas las columnas
            for (int j = 0; j < columnas; j++) {
                // Hacer cero el elemento si no es el pivote
                if (j != indiceMasNegativo) {
                    tabla[i][j] -= factor * tabla[indiceFilaPivote][j];
                } else { // Si es el pivote, hacerlo igual a cero
                    tabla[i][j] = 0;
                }
            }
        }
    }
}

// Función para obtener el índice de la fila del pivote
private int obtenerIndiceFilaPivote(float[][] tabla, int indiceMasNegativo) {
    float menorValorZ = Float.MAX_VALUE; // Inicializar el menor valor de Z como el máximo posible
    float menorValorRadioPositivo = Float.MAX_VALUE; // Inicializar el menor valor positivo de la columna de los radios
    int indiceFilaPivote = -1;

    // Encontrar el valor más negativo en Z
    for (int i = 0; i < tabla.length; i++) {
        if (tabla[i][indiceMasNegativo] < menorValorZ) {
            menorValorZ = tabla[i][indiceMasNegativo];
        }
    }

    // Encontrar el menor valor positivo en la columna de los radios
    for (int i = 1; i < tabla.length; i++) {
        // Evitar divisiones por cero y radios negativos
        if (tabla[i][indiceMasNegativo] > 0) {
            float valorRadio = tabla[i][tabla[i].length - 1] / tabla[i][indiceMasNegativo];
            if (valorRadio < menorValorRadioPositivo) {
                menorValorRadioPositivo = valorRadio;
                indiceFilaPivote = i;
            }
        }
    }

    // Devolver el índice de la fila del pivote
    return indiceFilaPivote;
}

}




