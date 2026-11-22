package modelo;

import estructuras.ListaEnlazada;

/**
 * Clase Docente. Hereda de Persona usando extends.
 * Aquí guardamos la especialidad y las materias que tiene asignadas.
 */
public class Docente extends Persona {

    private String especialidad;
    private ListaEnlazada<String> materiasAsignadas; // códigos de materias

    // Aquí creamos un docente llamando al constructor de Persona con super.
    public Docente(int id, String nombre, String correo, String especialidad) {
        super(id, nombre, correo);
        this.especialidad = especialidad;
        this.materiasAsignadas = new ListaEnlazada<>();
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public ListaEnlazada<String> getMateriasAsignadas() {
        return materiasAsignadas;
    }

    // Aquí le asignamos una materia al docente.
    public void asignarMateria(String codigo) {
        if (!materiasAsignadas.contiene(codigo)) {
            materiasAsignadas.agregarFinal(codigo);
        }
    }

    // Sobrescribimos el método abstracto de Persona con @Override.
    @Override
    public String obtenerRol() {
        return "Docente";
    }

    @Override
    public String toString() {
        return super.toString() + " | Especialidad: " + especialidad;
    }
}
