package excepciones;

/**
 * Excepción que lanzamos cuando intentamos deshacer o rehacer
 * y la pila correspondiente está vacía.
 */
public class PilaDeshacerVaciaException extends Exception {

    public PilaDeshacerVaciaException(String mensaje) {
        super(mensaje);
    }
}
