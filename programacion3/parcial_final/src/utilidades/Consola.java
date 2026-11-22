package utilidades;

import java.util.Scanner;

/**
 * Clase de utilidades para la consola.
 * Aquí guardamos métodos que usamos en todo el programa para
 * mostrar títulos, separadores y leer datos con validación.
 */
public class Consola {

    public static final String LINEA = "==================================================";
    public static final String LINEA_CORTA = "--------------------------------------------------";

    // Aquí limpiamos la pantalla imprimiendo varias líneas en blanco.
    public static void limpiarPantalla() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    // Aquí mostramos un título grande con separadores.
    public static void titulo(String texto) {
        System.out.println();
        System.out.println(LINEA);
        System.out.println("  " + texto);
        System.out.println(LINEA);
    }

    // Aquí mostramos un subtítulo con separadores cortos.
    public static void subtitulo(String texto) {
        System.out.println();
        System.out.println(LINEA_CORTA);
        System.out.println("  " + texto);
        System.out.println(LINEA_CORTA);
    }

    // Aquí mostramos un mensaje de éxito.
    public static void exito(String texto) {
        System.out.println("[OK] " + texto);
    }

    // Aquí mostramos un mensaje de error.
    public static void error(String texto) {
        System.out.println("[ERROR] " + texto);
    }

    // Aquí leemos un número entero dentro de un rango.
    public static int leerEntero(Scanner sc, String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor < min || valor > max) {
                    System.out.println("Debe ser un número entre " + min + " y " + max + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    // Aquí leemos un número decimal dentro de un rango.
    public static double leerDouble(Scanner sc, String mensaje, double min, double max) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                double valor = Double.parseDouble(linea);
                if (valor < min || valor > max) {
                    System.out.println("Debe ser un número entre " + min + " y " + max + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número.");
            }
        }
    }

    // Aquí leemos un texto que no puede estar vacío.
    public static String leerTexto(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println("El texto no puede estar vacío.");
        }
    }

    // Aquí esperamos a que el usuario presione Enter para continuar.
    public static void pausa(Scanner sc) {
        System.out.print("\nPresiona Enter para continuar...");
        sc.nextLine();
    }
}
