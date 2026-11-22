package estructuras;

/**
 * Nodo genérico que usamos para armar las estructuras encadenadas.
 * Aquí guardamos el dato y la referencia al nodo que sigue.
 */
public class Nodo<T> {

    private T dato;
    private Nodo<T> siguiente;

    // Aquí creamos un nodo con su dato. Todavía no apunta a ningún otro nodo.
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
