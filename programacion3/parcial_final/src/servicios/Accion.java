package servicios;

/**
 * Acción que se puede deshacer y rehacer.
 * Cada operación del sistema guarda aquí lo necesario
 * para restaurar el estado anterior de los datos.
 */
public abstract class Accion {

    private String descripcion;

    // Aquí creamos una acción con una descripción para mostrarla al usuario.
    public Accion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Cada acción sabe cómo deshacerse a sí misma.
    public abstract void deshacer();

    // Cada acción sabe cómo volver a hacerse (rehacer).
    public abstract void rehacer();
}
