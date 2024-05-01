/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// SimplexDosFases.java
package Controller;

public class SimplexDosFases {

    public void faseUnoSimplex(float[] zeta, float[][] restricciones, String[] igualdades) {
        // Cambiar signo de zeta
        float[] zetaOpuesto = cambiarSignoZeta(zeta);

        // Paso 1: Crear la tabla inicial con variables de holgura y artificiales
        float[][] tablaInicial = crearTablaInicial(zetaOpuesto, restricciones, igualdades);

        // Mostrar la tabla inicial
        System.out.println("Tabla inicial:");
        imprimirTabla(tablaInicial);

        // Convertir la columna de la variable artificial en una columna básica
        hacerColumnaBasica(tablaInicial);

        // Mostrar la tabla actualizada
        System.out.println("\n Tabla actualizada:");
        imprimirTabla(tablaInicial);

        // Verificar si el término independiente de la primera fila en la tabla actualizada es cero
        if (tablaInicial[0][tablaInicial[0].length - 1] != 0) {
            // Calcular el radio
            float[] radios = calcularRadio(tablaInicial);

            // Mostrar la tabla con el cálculo del radio
            System.out.println("\n Tabla con radio:");
            imprimirTablaConRadio(tablaInicial, radios);

            // Obtener el índice del valor más negativo en la fila Z
            int indiceMasNegativo = obtenerIndiceMasNegativo(tablaInicial);
            
            // Obtener el pivote
            float pivote = getPivote(tablaInicial, indiceMasNegativo);
            System.out.println("Pivote: " + pivote);
        } else {
            System.out.println("El término independiente de la primera fila es cero. Se detiene el proceso.");
        }
    }

    private float[] cambiarSignoZeta(float[] zeta) {
        float[] zetaOpuesto = new float[zeta.length];
        for (int i = 0; i < zeta.length; i++) {
            zetaOpuesto[i] = -zeta[i]; // Cambiar signo
        }
        return zetaOpuesto;
    }

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





    private void imprimirTabla(float[][] tabla) {
        // Imprimir la tabla
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.print(tabla[i][j] + "\t");
            }
            System.out.println();
        }
    }

    private void hacerColumnaBasica(float[][] tabla) {
        // Encontrar la fila que tiene un valor de 1 en la columna de la variable artificial
        int filaBasica = -1;
        for (int i = 1; i < tabla.length; i++) { // Empezar desde la segunda fila
            if (tabla[i][tabla[0].length - 2] == 1.0f) {
                filaBasica = i;
                break;
            }
        }

        if (filaBasica != -1) {
            // Sumar a la primera fila la fila seleccionada multiplicada por -1
            for (int j = 0; j < tabla[0].length; j++) {
                tabla[0][j] -= tabla[filaBasica][j];
            }
        } else {
            System.out.println("No se encontró una fila básica.");
        }
    }

    private float[] calcularRadio(float[][] tabla) {
        // Encontrar el índice del valor más negativo en la primera fila
        int indiceValorNegativo = -1;
        float valorMasNegativo = Float.MAX_VALUE;

        for (int j = 0; j < tabla[0].length - 1; j++) { // Excluir la última columna (lado derecho)
            if (tabla[0][j] < valorMasNegativo) {
                indiceValorNegativo = j;
                valorMasNegativo = tabla[0][j];
            }
        }

        if (valorMasNegativo >= 0) {
            System.out.println("No se puede calcular el radio. La solución es ilimitada.");
            return new float[0]; // Devolver un arreglo vacío si no hay valores negativos en la primera fila
        }

        // Calcular el radio dividiendo cada elemento de la última columna entre el elemento correspondiente
        // en la columna que contiene el valor más negativo de cada fila
        float[] radios = new float[tabla.length - 1]; // Excluir la primera fila (función objetivo)
        for (int i = 1; i < tabla.length; i++) { // Empezar desde la segunda fila
            if (tabla[i][indiceValorNegativo] != 0) { // Evitar división por cero
                radios[i - 1] = tabla[i][tabla[0].length - 1] / tabla[i][indiceValorNegativo];
            } else {
                radios[i - 1] = Float.POSITIVE_INFINITY; // Si hay una división por cero, asignar infinito al radio
            }
        }

        return radios;
    }

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

        return indiceMasNegativo;
    }

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
        
        //System.out.println("menorValorZ:" + menorValorZ);
        //System.out.println("indiceFilaPivote:" + indiceFilaPivote);

        // Encontrar el menor valor positivo en la columna de los radios
        for (int i = 1; i < tabla.length; i++) {
            // Evitar divisiones por cero
            if (tabla[i][indiceMasNegativo] != 0) {
                float valorRadio = tabla[i][tabla[i].length - 1] / tabla[i][indiceMasNegativo];
                if (valorRadio >= 0 && valorRadio < menorValorRadioPositivo) {
                    menorValorRadioPositivo = valorRadio;
                    indiceFilaPivote = i;
                }
            }
        }

        // Si no se encontró ningún radio positivo, retornar -1 para indicar que no hay pivote válido
        if (indiceFilaPivote == -1) {
            System.out.println("No se encontró un pivote válido.");
            return -1;
        }

        // Devolver el valor del pivote
        return tabla[indiceFilaPivote][indiceMasNegativo];
    }
}

