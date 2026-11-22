package estructuras;

/**
 * Cola genérica (FIFO: First In, First Out).
 * El primer elemento que entra es el primero que sale.
 * La usamos para la cola de espera de las materias y para el procesamiento por lotes.
 */
public class Cola<T> {

    private ListaEnlazada<T> elementos;

    // Aquí creamos una cola vacía.
    public Cola() {
        this.elementos = new ListaEnlazada<>();
    }

    // Aquí agregamos un elemento al final de la cola.
    public void encolar(T dato) {
        elementos.agregarFinal(dato);
    }

    // Aquí sacamos el elemento del frente de la cola.
    // Si está vacía, devolvemos null.
    public T desencolar() {
        return elementos.eliminarPrimero();
    }

    // Aquí miramos el frente sin sacarlo.
    public T verFrente() {
        if (estaVacia()) {
            return null;
        }
        return elementos.obtener(0);
    }

    // Verificamos si un elemento ya está dentro de la cola.
    public boolean contiene(T dato) {
        return elementos.contiene(dato);
    }

    public boolean estaVacia() {
        return elementos.estaVacia();
    }

    public int tamanio() {
        return elementos.tamanio();
    }
}
