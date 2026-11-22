package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista enlazada simple genérica.
 * Aquí guardamos los elementos uno detrás de otro usando nodos,
 * en vez de usar un arreglo con posiciones fijas.
 */
public class ListaEnlazada<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private int tamanio;

    // Aquí creamos una lista vacía, sin nodos todavía.
    public ListaEnlazada() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    // Agregamos un elemento al final de la lista.
    public void agregarFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        // Si la lista está vacía, el nuevo nodo se vuelve la cabeza.
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            // Luego recorremos la lista hasta llegar al último nodo.
            Nodo<T> actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            // Finalmente conectamos el último nodo con el nuevo.
            actual.setSiguiente(nuevo);
        }
        tamanio++;
    }

    // Agregamos un elemento al inicio de la lista.
    public void agregarInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
        tamanio++;
    }

    // Eliminamos la primera aparición de un elemento.
    // Devolvemos true si lo encontramos y lo quitamos.
    public boolean eliminar(T dato) {
        if (cabeza == null) {
            return false;
        }
        // Si se cumple que el dato está en la cabeza, la saltamos.
        if (cabeza.getDato().equals(dato)) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return true;
        }
        // Luego recorremos buscando el dato en los nodos siguientes.
        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getDato().equals(dato)) {
                // Aquí desconectamos el nodo que vamos a eliminar.
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                tamanio--;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    // Eliminamos y devolvemos el primer elemento de la lista.
    public T eliminarPrimero() {
        if (cabeza == null) {
            return null;
        }
        T dato = cabeza.getDato();
        cabeza = cabeza.getSiguiente();
        tamanio--;
        return dato;
    }

    // Verificamos si la lista contiene un elemento.
    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    // Obtenemos el elemento que está en una posición determinada.
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    // Convertimos la lista en un texto legible, separando con comas.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = cabeza;
        boolean primero = true;
        while (actual != null) {
            if (!primero) {
                sb.append(", ");
            }
            sb.append(actual.getDato());
            primero = false;
            actual = actual.getSiguiente();
        }
        return sb.toString();
    }

    // Implementamos un iterador para poder usar el ciclo for-each.
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            public boolean hasNext() {
                return actual != null;
            }

            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }
}
