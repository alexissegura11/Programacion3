package servicios;

import estructuras.ListaEnlazada;
import modelo.Persona;

/**
 * Registro general de personas.
 * Aquí guardamos estudiantes y docentes juntos en una sola lista
 * de referencias de tipo Persona, para demostrar el polimorfismo.
 */
public class RegistroPersonas {

    private ListaEnlazada<Persona> personas;

    // Aquí creamos el registro vacío.
    public RegistroPersonas() {
        this.personas = new ListaEnlazada<>();
    }

    // Aquí agregamos cualquier persona (estudiante o docente).
    public void agregar(Persona p) {
        personas.agregarFinal(p);
    }

    // Aquí se demuestra el polimorfismo: guardamos referencias de la clase
    // padre Persona, pero al llamar obtenerRol() cada objeto responde
    // con su propio comportamiento ("Estudiante" o "Docente").
    public String listarConRol() {
        if (personas.estaVacia()) {
            return "No hay personas registradas.";
        }
        StringBuilder sb = new StringBuilder();
        for (Persona p : personas) {
            sb.append(p.obtenerRol()).append(": ").append(p).append("\n");
        }
        return sb.toString();
    }
}
