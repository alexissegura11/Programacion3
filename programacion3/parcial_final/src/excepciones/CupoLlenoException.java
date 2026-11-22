package excepciones;

/**
 * Excepción que lanzamos cuando una materia ya no tiene cupos disponibles.
 */
public class CupoLlenoException extends Exception {

    public CupoLlenoException(String mensaje) {
        super(mensaje);
    }
}
