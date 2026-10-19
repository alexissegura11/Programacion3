
import java.util.Stack;
import java.util.Scanner;

public class punto92 {
    public static void main(String[] args) {
        Stack<Integer> pila = new Stack<>();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENU DE LA PILA ---");
            System.out.println("1. Añadir un dato");
            System.out.println("2. Quitar un dato");
            System.out.println("3. Mostrar la pila");
            System.out.println("4. Consultar la cima");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese un numero entero: ");
                    int numero = sc.nextInt();
                    pila.push(numero);
                    System.out.println("Dato añadido correctamente.");
                    break;

                case 2:
                    if (!pila.isEmpty()) {
                        System.out.println("Dato eliminado: " + pila.pop());
                    } else {
                        System.out.println("La pila esta vacia.");
                    }
                    break;

                case 3:
                    if (!pila.isEmpty()) {
                        System.out.println("Contenido de la pila: " + pila);
                    } else {
                        System.out.println("La pila esta vacia.");
                    }
                    break;

                case 4:
                    if (!pila.isEmpty()) {
                        System.out.println("Elemento en la cima: " + pila.peek());
                    } else {
                        System.out.println("La pila esta vacia.");
                    }
                    break;

                case 5:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 5);

        sc.close();
    }
}
