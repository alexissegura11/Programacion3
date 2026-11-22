package excepciones;

/**
 * Excepción que lanzamos cuando se intenta reservar un aula
 * en un día y hora en el que ya está ocupada.
 */
public class HorarioConflictivoException extends Exception {

    public HorarioConflictivoException(String mensaje) {
        super(mensaje);
    }
}
