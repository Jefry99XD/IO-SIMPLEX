/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.util.ArrayList;
//este 
/**
 *
 * @author jeffr
 */
public class Simplex {
    //guarda las iteraciones
    private ArrayList<float[][]> iteraciones = new ArrayList<>();
    private final float[] zeta;
    private final float[][] restricciones;
    private final ArrayList artificiales = new ArrayList();

    //gran m, normal o 2 fases  
    private final String metodo;
    
    //minimizar o maximizar
    private final String tipo;
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
    
    //Convierte la Z en su signo opuesto
    public void convertirOpuestosZ(float[] zeta) {
        for (int i = 0; i < zeta.length; i++) {
            if (zeta[i] != 0) { // Verifica si el elemento no es igual a 0
                zeta[i] = -zeta[i];
            }
        }
    }

    
    //-----------------------------------------
    //-----------------------------------------
    //Inicializa la tabla inicial
    
public Simplex(float[] zeta, float[][] restricciones, String tipo, String metodo, String[] igualdades) {
    this.zeta = zeta;
    this.restricciones=restricciones;
    this.metodo = metodo;
    this.tipo = tipo;

    // Convierte los coeficientes de z en su signo opuesto si es maximización
    if ("Maximizar".equals(this.tipo))
        convertirOpuestosZ(zeta);

    // Tamaño de la tablaInicial
    int filaTabla = restricciones.length + 1;

    // Verificar cuántas holguras y artificiales hay que crear.
    int ars = 0;
    int holguras = 0;
    for (String igualdad : igualdades) {
        if (null != igualdad)
            switch (igualdad) {
            case "<=" -> holguras++;
            case "=" -> ars++;
            case ">=" -> {
                ars++;
                holguras++;
            }
            default -> {
            }
        }
    }
    int columnaTabla = zeta.length + ars + holguras + 1;

    // Crear la matriz tablaInicial
    float[][] tablaInicial = new float[filaTabla][columnaTabla];
    int numVariablesZeta = zeta.length;
    int numRestricciones = restricciones.length;

    // Copiar zeta y restricciones
    System.arraycopy(zeta, 0, tablaInicial[0], 0, zeta.length);
    for (int i = 0; i < restricciones.length; i++) {
        // Copiar las restricciones menos el último valor, ya que es el término independiente
        System.arraycopy(restricciones[i], 0, tablaInicial[i + 1], 0, restricciones[i].length - 1);

            if (null != igualdades[i]) // Asignar el valor adecuado a las variables de holgura y artificiales
        
            switch (igualdades[i]) {
                case "<=":
                    // Variable de holgura
                    tablaInicial[i + 1][numVariablesZeta + i] = 1;
                    break;
                case "=":
                    // Variable artificial
                    tablaInicial[i + 1][numVariablesZeta + numRestricciones + i-3] = 1;
                    
                    break;
                case ">=":
                    // Holgura
                    tablaInicial[i + 1][numVariablesZeta + i] = -1;
                    // Artificial
                    tablaInicial[i + 1][numVariablesZeta + numRestricciones + i-1] = 1;
                    break;
            }


        // Copiar el último valor de la restricción
            tablaInicial[i + 1][columnaTabla - 1] = restricciones[i][restricciones[i].length - 1];
    }
    // Imprimir la tabla inicial para verificar
    float[][] copiaTablaInicial = new float[tablaInicial.length][];
    for (int i = 0; i < tablaInicial.length; i++) {
        copiaTablaInicial[i] = tablaInicial[i].clone();
    }

    // Guardar la copia de la tabla inicial en las iteraciones
    iteraciones.add(copiaTablaInicial);
}
  
    //-----------------------------------------
    //-----------------------------------------
    //   Imprime la tabla
    public void imprimirTabla(float[][] tabla) {
        //Recorre cada fila y columna de la tabla y luego los imprime
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                System.out.printf("%12.2f ", tabla[i][j]);
            }

            // Excluir la fila Z, es decir, para que no imprima 
            // el radio de Z, por que este no se calculo
            if (i > 0) { 
                float valorMasNegativo = Float.MAX_VALUE;
                float[] filaZ = tabla[0];
                int indiceMasNegativo = 0;

                // Encontrar el coeficiente más negativo en la fila Z
                // Itera sobre cada elemento de la fila Z, excepto el último
                // El coeficiente más negativo se utiliza para determinar cuál variable entrará 
                // (variable entrante) en la siguiente iteración.
                for (int j = 0; j < filaZ.length - 1; j++) {
                    if (filaZ[j] < valorMasNegativo) {
                        valorMasNegativo = filaZ[j];
                        indiceMasNegativo = j;
                    }
                }

                // Calcula el radio
                float radio = tabla[i][tabla[i].length - 1] / tabla[i][indiceMasNegativo];

                // Si el radio es negativo, muestra "Infinity"
                if (radio < 0) {
                    System.out.printf("      %d ", -1 );
                } else {
                    System.out.printf("      %.2f ", radio);
                }
            }

            // Agregar un salto de línea después de imprimir cada fila
            System.out.println(); 
        }
    }



    //-----------------------------------------
    //-----------------------------------------
    //Itera sobre las iteraciones  hasta que no haya valores negativos en la fila Z. 
    //En cada iteración, muestra la tabla resultante y actualiza la tabla actual con la función iteracion(). 
    public void resolver() {
        if ("Gran M".equals(metodo)) {
            simplexGranM();
        } else {
            simplexNormal();
        }
        if("Minimizar".equals(tipo)){
            float[][] ultimaMatriz = iteraciones.get(iteraciones.size() - 1);
            ultimaMatriz[0][ultimaMatriz[0].length-1] *=-1; 
        }
    }
    
    
    //funcion que usa el simplex normal
    public void simplexNormal(){
        int iteracion = 0;
        
                while (hayValoresNegativosEnZ(iteraciones.get(iteraciones.size() - 1))) {
            // Muestra la tabla después de cada iteración
            iteracion();
            iteracion++; // Incrementa el número de iteración
        }
    }
    
    //funcion para hacer basica una columna, usada para hacer basica la artificial
    //recibe el indice de la columna
    public float[] obtenerFilaParaHacerBasica(int indiceColumna) {
        float[][] ultima = iteraciones.get(iteraciones.size() - 1);

        for (float[] fila : ultima) {
            if (fila[indiceColumna] == 1) {
                return fila;
            }
        }
        return null; // Devuelve null si no se encuentra un 1 en la columna
    }

        public void hacerBasicaArtificiales(float bigM) {
            // Función que modifica la primera fila para que las artificiales sean básicas antes de empezar a iterar
            float[][] iteracion = iteraciones.get(0);
            for (Object indice : artificiales) {
                int indiceArtificial = (int) indice;
                float[] filaArtificial = obtenerFilaParaHacerBasica(indiceArtificial);

                if (filaArtificial != null) {
                    // Realizar la operación -bigM * filaArtificial + primeraFilaUltima
                    for (int i = 0; i < filaArtificial.length; i++) {
                        iteracion[0][i] -= bigM * filaArtificial[i];
                    }
                }
            }
        }

  
    
    //metodo gran M
    public void simplexGranM(){
        //preparacion de Z
        float[][] iteracion1 = iteraciones.get(0);
        float bigM = 1000;
        int inicioartificial=zeta.length+restricciones.length-1;
        for(int i=inicioartificial;i<iteracion1[0].length-1;i++){
                artificiales.add(i);
                iteracion1[0][i] = bigM;
        }
        hacerBasicaArtificiales(bigM);
        int iteracion = 0;
            while (hayValoresNegativosEnZ(iteraciones.get(iteraciones.size() - 1))) {
                iteracion();
                iteracion++; 
        }
        
    }
        
    //-----------------------------------------
    //-----------------------------------------
    // Metodo que verifica si hay valores negativos en la fila Z
    public boolean hayValoresNegativosEnZ(float[][] tabla) {
        float[] filaZ = tabla[0];
        for (int i = 0; i < filaZ.length - 1; i++) { 
            if (filaZ[i] < 0) {
                return true;
            }
        }
        return false;
    }
    //-----------------------------------------
    //-----------------------------------------

public void iteracion() {
    int indiceMenorZ = menorZ();
    float pivote = getPivote(indiceMenorZ);
    int indiceSalida = getSalida(indiceMenorZ);
    actualizarTabla(indiceSalida, indiceMenorZ, pivote);
    float[][] nuevaIteracion = new float[iteraciones.get(iteraciones.size() - 1).length][];
    for (int i = 0; i < iteraciones.get(iteraciones.size() - 1).length; i++) {
        nuevaIteracion[i] = iteraciones.get(iteraciones.size() - 1)[i].clone();
    }
    iteraciones.add(nuevaIteracion);
}

    
    //-----------------------------------------
    //-----------------------------------------
    // Encuentra el índice de la variable con el coeficiente más negativo en la fila Z.
    // Retorna el índice de la variable con el coeficiente más negativo.
    public int menorZ() {
        // // Obtiene la última iteración de la tabla
        float[][] actual = iteraciones.get(iteraciones.size() - 1);
        int indiceMenorZ = 0;
        float[] z = actual[0];
        for (int i = 1; i < z.length - 1; i++) { 
            if (z[i] < z[indiceMenorZ]) {
                indiceMenorZ = i; 
            }
        }
        return indiceMenorZ;
    }
    
    //-----------------------------------------
    //-----------------------------------------
    // Encuentra el índice de la variable de salida en función del radio de la tabla actual.
    // Retorna el índice de la fila con el radio más pequeño
    public int getSalida(int indiceEntrada) {
    float[][] actual = iteraciones.get(iteraciones.size() - 1);
    int indiceSalida = 1;
    
    float menorRadio = Float.MAX_VALUE;
    for (int i = 1; i < actual.length; i++) {
        float radio = actual[i][actual[i].length - 1] / actual[i][indiceEntrada];
        if (radio < menorRadio && radio > 0) {
            menorRadio = radio;
            indiceSalida = i;
        }
    }
    return indiceSalida;
}

    
    //-----------------------------------------
    //-----------------------------------------
    // Actualiza la tabla después de cada iteración
    // Recibe el índice de la fila de salida, el índice de la columna de entrada y el valor del pivote.
    // Actualiza las filas de la tabla, dividiendo la fila de salida por el pivote y realizando operaciones de fila para hacer cero lo demas.
    public void actualizarTabla(int indiceSalida, int indiceEntrada, float pivote) {
        float[][] tabla = iteraciones.get(iteraciones.size() - 1);
        float[] filaSalida = tabla[indiceSalida];
        float divisor = getPivote(indiceEntrada);
        for (int i = 0; i < filaSalida.length; i++) {
            filaSalida[i] /= divisor;
            //System.out.println("Nuevo valor en filaSalida[" + i + "]: " + filaSalida[i]);
        }

        // Actualiza las otras filas
        for (int i = 0; i < tabla.length; i++) {
            if (i != indiceSalida) {
                float factor = tabla[i][indiceEntrada];
                //System.out.println("Factor en fila " + i + ": " + factor);
                //System.out.println("Fila " + i + ": " + Arrays.toString(tabla[i]));
                for (int j = 0; j < tabla[i].length; j++) {
                    tabla[i][j] -= factor * filaSalida[j];
                }
            }
        }

        // Actualiza la fila Z
        float[] filaZ = tabla[0];
        for (int i = 0; i < filaZ.length; i++) {
            if (i != indiceEntrada) {
                filaZ[i] -= filaZ[indiceEntrada] * filaSalida[i];
            }
        }

        // Actualiza las variables de salida y entrada para la interfaz
        Saliente = "x" + (indiceSalida - 1); // Restamos 1 porque la primera fila corresponde a Z
        Entrante = "x" + indiceEntrada;
        iteraciones.set(iteraciones.size() - 1, tabla);
    }
    
    //-----------------------------------------
    //-----------------------------------------

    public float getPivote(int indiceMasNegativo) {
        float[][] actual = iteraciones.get(iteraciones.size() - 1); // Obtener la última iteración
        float menorValorZ = Float.MAX_VALUE; // Inicializar el menor valor de Z como el máximo posible
        float menorValorRadioPositivo = Float.MAX_VALUE; // Inicializar el menor valor positivo de la columna de los radios
        int indiceFilaPivote = -1;

        // Encontrar el valor más negativo en Z
        for (float[] actual1 : actual) {
            if (actual1[indiceMasNegativo] < menorValorZ) {
                menorValorZ = actual1[indiceMasNegativo];
            }
        }

        // Encontrar el menor valor positivo en la columna de los radios
        for (int i = 1; i < actual.length; i++) {
            // Evitar divisiones por cero
            if (actual[i][indiceMasNegativo] != 0) {
                float valorRadio = actual[i][actual[i].length - 1] / actual[i][indiceMasNegativo];
                if (valorRadio >= 0 && valorRadio < menorValorRadioPositivo) {
                    menorValorRadioPositivo = valorRadio;
                    indiceFilaPivote = i;
                }
            }
        }

        // Si no se encontró ningún radio positivo, retornar -1 para indicar que no hay pivote válido
        if (indiceFilaPivote == -1) {
            sinLimite = true;
            //System.out.println("No se encontró un pivote válido.");
            return -1;
        }

        return actual[indiceFilaPivote][indiceMasNegativo];
    }
    private boolean sinLimite = false;
    
    //funcion para devolver los resultados de la ultima iteracion de z y variables.
     public ArrayList<String> obtenerResultados() {
        float[][] ultima = iteraciones.get(iteraciones.size() - 1);
        ArrayList<Integer> indicesBasicas = new ArrayList<>();
        ArrayList<String> resultados = new ArrayList<>();
        if(sinLimite){
            resultados.add("No hay solucion factible");
            return resultados;
        }
        resultados.add("Z = " + String.valueOf(ultima[0][ultima[0].length - 1]));
        
        for (int i = 0; i < ultima[0].length - 1; i++) { 
            if (esColumnaBasica(ultima, i)) {
                indicesBasicas.add(i);
            }
        }
        for (int i = 1; i < ultima.length; i++) {
            float[] fila = ultima[i];
                float valor = fila[fila.length - 1];
                String a = String.valueOf(valor);
                resultados.add(i + " = " + a); 
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
            }
            else if (matriz[i][indiceColumna] != 0) {
                return false;
            }
        }
        return contador1 == 1;
}
    
}
