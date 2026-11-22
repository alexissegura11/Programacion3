package modelo;

import estructuras.ListaEnlazada;

/**
 * Clase Estudiante. Hereda de Persona usando extends.
 * Aquí guardamos el semestre actual, el historial académico
 * y las materias inscritas y aprobadas.
 */
public class Estudiante extends Persona {

    private int semestreActual;
    private HistorialAcademico historial;
    private ListaEnlazada<String> materiasInscritas; // códigos de materias inscritas
    private ListaEnlazada<String> materiasAprobadas; // códigos de materias aprobadas

    // Aquí creamos un estudiante llamando al constructor de Persona con super.
    public Estudiante(int id, String nombre, String correo, int semestreActual) {
        super(id, nombre, correo);
        this.semestreActual = semestreActual;
        this.historial = new HistorialAcademico();
        this.materiasInscritas = new ListaEnlazada<>();
        this.materiasAprobadas = new ListaEnlazada<>();
    }

    public int getSemestreActual() {
        return semestreActual;
    }

    public void setSemestreActual(int semestreActual) {
        this.semestreActual = semestreActual;
    }

    public HistorialAcademico getHistorial() {
        return historial;
    }

    public ListaEnlazada<String> getMateriasInscritas() {
        return materiasInscritas;
    }

    public ListaEnlazada<String> getMateriasAprobadas() {
        return materiasAprobadas;
    }

    // Aquí verificamos si el estudiante ya aprobó una materia.
    // Esto lo usamos para saber si cumple los prerrequisitos.
    public boolean aprobo(String codigo) {
        return materiasAprobadas.contiene(codigo);
    }

    // Aquí marcamos una materia como aprobada.
    public void marcarAprobada(String codigo) {
        if (!materiasAprobadas.contiene(codigo)) {
            materiasAprobadas.agregarFinal(codigo);
        }
    }

    // Aquí quitamos una materia de la lista de aprobadas (para deshacer).
    public void quitarAprobada(String codigo) {
        materiasAprobadas.eliminar(codigo);
    }

    // Sobrescribimos el método abstracto de Persona con @Override.
    @Override
    public String obtenerRol() {
        return "Estudiante";
    }

    @Override
    public String toString() {
        return super.toString() + " | Semestre: " + semestreActual;
    }
}
