package excepciones;

/**
 * Excepción que lanzamos cuando buscamos un estudiante por su ID
 * y ese ID no existe en el sistema.
 */
public class EstudianteNoEncontradoException extends Exception {

    public EstudianteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
