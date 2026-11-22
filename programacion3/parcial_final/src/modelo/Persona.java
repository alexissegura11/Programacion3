package modelo;

/**
 * Clase abstracta Persona.
 * Aquí guardamos los datos que comparten los estudiantes y los docentes.
 * No se puede crear una Persona directamente: solo sus clases hijas.
 */
public abstract class Persona {

    private int id;
    private String nombre;
    private String correo;

    // Aquí creamos una persona con sus datos comunes.
    public Persona(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // Método abstracto: cada clase hija está obligada a decir su rol.
    public abstract String obtenerRol();

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ")";
    }
}
