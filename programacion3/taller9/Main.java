package programacion3.taller9;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Primero creamos una lista donde vamos a guardar varios numeros
        ArrayList<Integer> objArr = new ArrayList<>();

        // Ahora empezamos a agregar los numeros que queremos guardar
        objArr.add(7);
        objArr.add(3);
        objArr.add(1);
        objArr.add(9);
        objArr.add(6);
        objArr.add(8);
        objArr.add(4);
        objArr.add(2);

        // Aqui mostramos todos los numeros que agregamos al principio
        System.out.println("Lista inicial: " + objArr);

        // Ahora insertamos el numero 25 en la posicion 4 de la lista
        objArr.add(4, 25);
        System.out.println("Despues de insertar 25: " + objArr);

        // Aqui consultamos que numero se encuentra en la posicion 6
        System.out.println("Elemento en posicion 6: " + objArr.get(6));

        // Luego revisamos que numero quedo guardado en la posicion 4
        System.out.println("Elemento en posicion 4: " + objArr.get(4));

        // Buscamos en que posicion aparece por primera vez el numero 9
        System.out.println("Posicion del numero 9: " + objArr.indexOf(9));

        // Tambien buscamos el numero 25 para conocer su posicion
        System.out.println("Posicion del numero 25: " + objArr.indexOf(25));

        // Aqui comprobamos si el numero 8 se encuentra dentro de la lista
        System.out.println("¿Contiene el numero 8?: " + objArr.contains(8));

        // Ahora revisamos si tenemos guardado el numero 100
        System.out.println("¿Contiene el numero 100?: " + objArr.contains(100));

        // Vamos a cambiar el numero de la posicion 0 por el numero 100
        objArr.set(0, 100);
        System.out.println("Despues de modificar la posicion 0: " + objArr);

        // Ahora eliminamos el elemento que se encuentra en la posicion 4
        objArr.remove(4);
        System.out.println("Despues de eliminar la posicion 4: " + objArr);

        // Aqui contamos cuantos elementos tiene actualmente nuestra lista
        System.out.println("Tamaño de la lista: " + objArr.size());

        // Comprobamos si la lista esta vacia o todavia tiene numeros
        System.out.println("¿La lista esta vacia?: " + objArr.isEmpty());

        // Ahora organizamos todos los numeros de menor a mayor
        Collections.sort(objArr);
        System.out.println("Lista ordenada: " + objArr);

        // Despues invertimos el orden en el que quedaron los numeros
        Collections.reverse(objArr);
        System.out.println("Lista en orden inverso: " + objArr);

        // Vamos a recorrer la lista mostrando cada numero junto a su posicion
        System.out.println("Elementos de la lista:");

        for (int i = 0; i < objArr.size(); i++) {
            System.out.println("Posicion " + i + ": " + objArr.get(i));
        }

        // Ahora recorremos la lista de una forma mas sencilla para mostrar cada numero
        System.out.println("Recorrido con for-each:");

        for (Integer elemento : objArr) {
            System.out.println(elemento);
        }

        // Por ultimo, borramos todos los elementos que quedaron en la lista
        objArr.clear();
        System.out.println("Lista despues de clear(): " + objArr);

        // Para terminar, comprobamos que la lista haya quedado completamente vacia
        System.out.println("¿La lista esta vacia?: " + objArr.isEmpty());
    }
}
