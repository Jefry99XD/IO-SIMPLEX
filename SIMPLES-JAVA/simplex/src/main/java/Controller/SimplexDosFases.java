/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// SimplexDosFases.java
package Controller;

import java.util.ArrayList;
import java.util.Arrays;

public class SimplexDosFases {

    //guarda las iteraciones
    private ArrayList<float[][]> iteraciones = new ArrayList<>();
    private ArrayList artificiales = new ArrayList();

    //las iteraciones
    public ArrayList<float[][]> getIteraciones() {
        return iteraciones;
    }

    public void setIteraciones(ArrayList<float[][]> iteraciones) {
        this.iteraciones = iteraciones;
    }
    private boolean sinLimite = false;

    private float[] zeta;
    private float[][] restricciones;

    public void faseUnoSimplex(float[] zeta, float[][] restricciones, String[] igualdades, String tipoProblema) {
        this.zeta = zeta;
        this.restricciones = restricciones;
        // Determinar si se debe invertir la función objetivo al inicio
        if (tipoProblema.equals("Maximizar")) {
            // Invertir el signo de zeta para maximización
            zeta = cambiarSignoZeta(zeta);
        }

        // Paso 1: Crear la tabla inicial con variables de holgura y artificiales
        float[][] tablaInicial = crearTablaInicial(zeta, restricciones, igualdades);

        // Convertir la columna de la variable artificial en una columna básica
        hacerColumnaBasica(tablaInicial);

        // Calcular el radio para cada fila
        float[] radios = calcularRadio(tablaInicial);

        // Continuar iterando mientras la fila del lado derecho no sea cero
        while (tablaInicial[0][tablaInicial[0].length - 1] < 0) {
            iteraciones.add(Arrays.copyOf(tablaInicial, tablaInicial.length));
            // Paso 1: Encontrar el índice del valor más negativo en la fila Z
            int indiceValorNegativo = obtenerIndiceMasNegativo(tablaInicial);

            // Obtener el índice de la fila pivote dependiendo de la fase
            // Paso 2: Calcular el pivote
            float pivote = getPivote(tablaInicial, indiceValorNegativo);

            // Verificar si se encontró un pivote válido
            if (pivote == -1) {
                sinLimite = true;
                // Si no se encontró un pivote válido, la solución no tiene límite
                //System.out.println("La solución no tiene límite.");
                break; // Salir del bucle while
            }

            // Paso 3: Actualizar la tabla
            actualizarTabla(tablaInicial, indiceValorNegativo, 2);

            // Paso 4: Recalcular los radios
            radios = calcularRadio(tablaInicial);
        }

        // Una vez que la fase 1 haya terminado y el lado derecho sea cero, eliminar la primera fila y las columnas de las variables artificiales
        tablaInicial = eliminarFilaYColumnas(tablaInicial);
        iteraciones.add(Arrays.copyOf(tablaInicial, tablaInicial.length));

        // Multiplicar la fila Z por -1 si se está minimizando
        if (tipoProblema.equals("Minimizar")) {
            for (int i = 0; i < tablaInicial[0].length; i++) {
                tablaInicial[0][i] *= -1;
            }
        }

        // Mostrar la tabla final después de eliminar la primera fila y las variables artificiales
        // Verificar si es necesario continuar con la fase 2
        boolean faseDosNecesaria = false;
        for (float valor : tablaInicial[0]) {
            if (valor < 0) {
                faseDosNecesaria = true;
                break;
            }
        }

        if (faseDosNecesaria) {
            faseDosSimplex();
        }
    }

    //-------------------------------------------------
    //-------------------------------------------------
    // En tu método faseDosSimplex(float[][] tablaInicial)
    public void faseDosSimplex() {
        while (tieneNegativos(iteraciones.get(iteraciones.size() - 1))) {
            iteracion();
        }
    }

    //-------------------------------------------------
    //-------------------------------------------------
    // Función para verificar si una fila tiene números negativos
    private boolean tieneNegativos(float[] fila) {
        for (float valor : fila) {
            if (valor < 0) {
                return true;
            }
        }
        return false;
    }

    //-------------------------------------------------
    //-------------------------------------------------
    // Función para obtener el índice de la fila del pivote
    private int obtenerIndiceFilaPivote(float[][] tabla, int indiceMasNegativo, int indiceInicio) {
        float menorValorRadioPositivo = Float.MAX_VALUE;
        int indiceFilaPivote = -1;

        for (int i = indiceInicio; i < tabla.length; i++) { // Usar el índice de inicio proporcionado
            if (tabla[i][indiceMasNegativo] > 0) {
                float valorRadio = tabla[i][tabla[i].length - 1] / tabla[i][indiceMasNegativo];
                //System.out.println("Valor de tabla[" + i + "][" + indiceMasNegativo + "]: " + tabla[i][indiceMasNegativo]);
                if (valorRadio < menorValorRadioPositivo) {
                    menorValorRadioPositivo = valorRadio;
                    indiceFilaPivote = i;
                }
            }
        }
        // System.out.println("indice fila pivoteeee: " + indiceFilaPivote);
        return indiceFilaPivote;
    }

    //-------------------------------------------------
    //-------------------------------------------------
    private float[][] eliminarFilaYColumnas(float[][] tabla) {
        // Obtener el número de filas y columnas de la tabla
        int filas = tabla.length;
        int columnas = tabla[0].length;

        // Crear una nueva matriz con una fila menos y sin las columnas de las variables artificiales
        float[][] nuevaTabla = new float[filas - 1][columnas - artificiales.size()];

        // Copiar los elementos de la tabla original a la nueva tabla, excluyendo la fila Z y las columnas de las variables artificiales
        int filaNueva = 0;
        for (int fila = 1; fila < filas; fila++) { // Empezar desde la segunda fila para omitir la fila Z
            int columnaNueva = 0;
            for (int columna = 0; columna < columnas; columna++) {
                if (!artificiales.contains(columna)) { // Verificar si la columna es una variable artificial
                    nuevaTabla[filaNueva][columnaNueva] = tabla[fila][columna];
                    columnaNueva++;
                }
            }
            filaNueva++;
        }

        return nuevaTabla;
    }

    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private boolean tieneNegativos(float[][] tabla) {
        for (int j = 0; j < tabla[0].length - 1; j++) {
            if (tabla[0][j] < 0) {
                return true;
            }
        }
        return false;
    }

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
            if ("<=".equals(igualdad)) {
                holguras++;
            } else if ("=".equals(igualdad)) {
                artificiales++;
            } else if (">=".equals(igualdad)) {
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
                // Añadir el índice de la columna artificial a la lista artificiales
                this.artificiales.add(zeta.length + holguras - 2 + artificiales + i);
            } else if (">=".equals(igualdades[i])) {
                tablaInicial[i + 2][zeta.length + holguras - 3 + i] = -1; // holgura
                tablaInicial[i + 2][zeta.length + holguras - 3 + artificiales + i] = 1; // artificial
                tablaInicial[0][zeta.length + holguras - 2 + artificiales + i] = 1;
                // Añadir el índice de la columna artificial a la lista artificiales
                this.artificiales.add(zeta.length + holguras - 2 + artificiales + i);
            }

            // Copiar el último valor de la restricción
            tablaInicial[i + 2][columnaTabla - 1] = restricciones[i][restricciones[i].length - 1];
        }

        // Imprimir la tabla inicial para verificar
        //imprimirTabla(tablaInicial);
        float[][] copiaTablaInicial = new float[tablaInicial.length][];
        for (int i = 0; i < tablaInicial.length; i++) {
            copiaTablaInicial[i] = tablaInicial[i].clone();
        }

        // Guardar la copia de la tabla inicial en las iteraciones
        iteraciones.add(copiaTablaInicial);
        // Guardar la tabla 0 en las iteraciones
        return tablaInicial;
    }

    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    public void iteracion() {
        float[][] ultima = iteraciones.get(iteraciones.size() - 1);
        int indiceMasNegativo = obtenerIndiceMasNegativo(ultima);
        actualizarTabla(ultima, indiceMasNegativo, 1);

        // Crear una nueva instancia de la matriz y copiar los valores de la iteración actual
        float[][] nuevaIteracion = new float[iteraciones.get(iteraciones.size() - 1).length][];
        for (int i = 0; i < iteraciones.get(iteraciones.size() - 1).length; i++) {
            nuevaIteracion[i] = iteraciones.get(iteraciones.size() - 1)[i].clone();
        }

        // Agregar la nueva iteración a la lista
        iteraciones.add(nuevaIteracion);
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
            return new float[0];
        }

        // Inicializar un arreglo para almacenar los radios
        float[] radios = new float[tabla.length - 2]; // Excluir la fila de la función objetivo y la primera fila de restricciones

        // Calcular el radio para cada fila a partir de la tercera fila
        for (int fila = 2; fila < tabla.length; fila++) {
            // Obtener el valor correspondiente en la columna del índice del valor más negativo
            float valorColumnaNegativa = tabla[fila][indiceValorNegativo];

            // Si el valor en la columna del índice del valor más negativo es positivo, calcular el radio
            if (valorColumnaNegativa > 0) {
                radios[fila - 2] = tabla[fila][tabla[fila].length - 1] / valorColumnaNegativa;
            } else {
                // Si el valor en la columna del índice del valor más negativo es no positivo,
                // asignar infinito al radio para indicar que no hay pivote válido en esa fila
                radios[fila - 2] = Float.POSITIVE_INFINITY;
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
            if (i >= 2) { // Comenzar desde la tercera fila
                float radio = radios[i - 2]; // Ajustar el índice para los radios
                if (radio <= 0) { // Si el radio es negativo o cero, mostrar "-1"
                    System.out.print("-1\t");
                } else if (radio == Float.POSITIVE_INFINITY) { // Si el radio es infinito, mostrar "inf"
                    System.out.print("inf\t");
                } else {
                    System.out.print(radio + "\t"); // Mostrar el radio calculado
                }
            } else {
                System.out.print("\t"); // Dejar espacio en blanco en la primera y segunda fila
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
        //System.out.println("");
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

        // Si no se encontró ningún radio positivo, retornar -1 para indicar que no hay pivote válido
        if (indiceFilaPivote == -1) {
            return -1;
        }

        //System.out.println("Valor del pivote: " + tabla[indiceFilaPivote][indiceMasNegativo]);
        // Devolver el valor del pivote
        return tabla[indiceFilaPivote][indiceMasNegativo];
    }

    //-------------------------------------------------
    //-------------------------------------------------
    //-------------------------------------------------
    private void actualizarTabla(float[][] tabla, int indiceMasNegativo, int fase) {
        // Obtener el índice de la fila del pivote utilizando la función getPivote
        //int indiceFilaPivote = obtenerIndiceFilaPivote(tabla, indiceMasNegativo, 2); // Cambia el índice de inicio a 2
        int indiceInicio = fase == 1 ? 1 : 2;

        // Obtener el índice de la fila del pivote utilizando la función getPivote
        int indiceFilaPivote = obtenerIndiceFilaPivote(tabla, indiceMasNegativo, indiceInicio);
        // Verificar si se encontró un índice de fila pivote válido
        if (indiceFilaPivote >= 0) {
            float pivote = getPivote(tabla, indiceMasNegativo);

            // Verificar si se encontró un pivote válido
            if (pivote == -1) {
                // Si no se encontró un pivote válido, no se puede continuar
                //System.out.println("No se encontró un pivote válido.");
                return;
            }

            // Obtener el número de filas y columnas de la tabla
            int filas = tabla.length;
            int columnas = tabla[0].length;
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
        } else {
            // Si el índice de fila pivote no es válido, imprimir un mensaje de error
            System.out.println("El índice de fila pivote no es válido.");
        }
    }

    public ArrayList<String> obtenerResultados() {
        float[][] ultima = iteraciones.get(iteraciones.size() - 1);
        ArrayList<Integer> indicesBasicas = new ArrayList<>();
        ArrayList<String> resultados = new ArrayList<>();
        if (sinLimite) {
            resultados.add("No hay solucion factible");
            return resultados;
        }
        resultados.add("Z = " + String.valueOf(ultima[0][ultima[0].length - 1]));

        for (int i = 0; i < ultima[0].length - 1; i++) { // Cambié ultima[0].length por ultima[0].length - 1
            if (esColumnaBasica(ultima, i)) {
                indicesBasicas.add(i);
            }
        }
        for (int i = 1; i < ultima.length; i++) {
            float[] fila = ultima[i];
                float valor = fila[fila.length - 1];
                String a = String.valueOf(valor);
                resultados.add(a); // Corregí el cálculo del índice
        }

        return resultados;
    }

    //funcion que verifica si es basica la columna
    public boolean esColumnaBasica(float[][] matriz, int indiceColumna) {
        int filas = matriz.length;
        int contador1 = 0;
        for (int i = 0; i < filas; i++) {
            if (matriz[i][indiceColumna] == 1) {
                contador1++;
            } else if (matriz[i][indiceColumna] != 0) {
                return false;
            }
        }
        return contador1 == 1;
    }

}
