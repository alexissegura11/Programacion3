import java.util.Random;

public class Taller {
    public static void main(String[] args) {


        System.out.println("--- 1. ARREGLO ORIGINAL ---");

        int[] numeros = {7, 8, 9, 10, 11, 12, 13};
        int posicion = 9;
        if (posicion >= 0 && posicion < numeros.length) {
            System.out.println("El valor en la posición " + posicion + " es: " + numeros[posicion]);
        } else {
            Random rand = new Random();
            int aleatorio = rand.nextInt(30) + 1;
            System.out.println("La posición " + posicion + " no existe.");
            System.out.println("Generando un número aleatorio de ejemplo: " + aleatorio);
        }
        // Imprimimos el arreglo original usando un bucle for (más limpio)
        System.out.println("\nImprimiendo el arreglo original:");
        for (int i = 0; i < numeros.length; i++) {
            System.out.println("Posición " + i + ": " + numeros[i]);
        }

        
        
        
        
        // ==========================================
        // 2. ARREGLO GRANDE CON NÚMEROS ALEATORIOS
        // ==========================================
        System.out.println("\n--- 2. ARREGLO ALEATORIO CON OPERADOR NEW ---");
        int cantidadElementos = 10; // Simulamos que nos piden 10 números
        // Creación usando el operador new
        int[] arregloAleatorio = new int[cantidadElementos];
        Random rand = new Random();
        // Llenamos el arreglo con números aleatorios
        for (int i = 0; i < arregloAleatorio.length; i++) {
            arregloAleatorio[i] = rand.nextInt(100) + 1; // Números del 1 al 100
        }
        // Imprimimos el arreglo aleatorio
        for (int i = 0; i < arregloAleatorio.length; i++) {
            System.out.println("Posición " + i + ": " + arregloAleatorio[i]);
        }

        
        
        
        
        
        
        // ==========================================
        // 3. MATRICES (Ejercicios 17 al 24)
        // ==========================================
        // Instrucción de la imagen: int[][] m = new int[3][3];
        System.out.println("\n--- 3. MATRIZ CON OPERADOR NEW ---");

        int filas = 3;
        int columnas = 3;

        // Creación de la matriz usando el operador new (tal como pide la imagen)
        int[][] matriz = new int[filas][columnas];

        // Llenamos la matriz con números aleatorios
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = rand.nextInt(50) + 1; // Números aleatorios del 1 al 50
            }
        }

        // Imprimimos la matriz en formato de cuadrícula
        System.out.println("Imprimiendo la matriz de " + filas + "x" + columnas + ":");
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // El \t es un tabulador para que los números queden alineados
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println(); // Salto de línea al terminar cada fila
        }
    }
}