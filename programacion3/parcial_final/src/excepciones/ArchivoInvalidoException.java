package excepciones;

/**
 * Excepción que lanzamos cuando un archivo CSV no existe,
 * está vacío o tiene un formato inválido.
 */
public class ArchivoInvalidoException extends Exception {

    public ArchivoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
