package excepciones;

/**
 * Excepción que lanzamos cuando intentamos liberar un cupo de una materia
 * pero su cola de espera está vacía.
 */
public class ColaDeEsperaVaciaException extends Exception {

    public ColaDeEsperaVaciaException(String mensaje) {
        super(mensaje);
    }
}
