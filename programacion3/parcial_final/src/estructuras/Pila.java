package estructuras;

/**
 * Pila genérica (LIFO: Last In, First Out).
 * El último elemento que entra es el primero que sale.
 * La usamos para el sistema de deshacer y rehacer, y para los reportes.
 */
public class Pila<T> {

    private ListaEnlazada<T> elementos;

    // Aquí creamos una pila vacía.
    public Pila() {
        this.elementos = new ListaEnlazada<>();
    }

    // Aquí guardamos un elemento encima de la pila.
    public void push(T dato) {
        elementos.agregarInicio(dato);
    }

    // Aquí sacamos el elemento de encima.
    // Si la pila está vacía, devolvemos null.
    public T pop() {
        return elementos.eliminarPrimero();
    }

    // Aquí miramos el elemento de encima sin sacarlo.
    public T verTope() {
        if (estaVacia()) {
            return null;
        }
        return elementos.obtener(0);
    }

    public boolean estaVacia() {
        return elementos.estaVacia();
    }

    public int tamanio() {
        return elementos.tamanio();
    }
}
