package excepciones;

/**
 * Excepción que lanzamos cuando un estudiante intenta inscribirse
 * a una materia sin haber aprobado alguno de sus prerrequisitos.
 */
public class PreRequisitoNoAprobadoException extends Exception {

    public PreRequisitoNoAprobadoException(String mensaje) {
        super(mensaje);
    }
}
