package servicios;

import java.util.HashMap;
import modelo.Docente;

/**
 * Gestor de docentes.
 * Aquí guardamos los docentes en un HashMap para buscarlos por ID.
 */
public class GestorDocentes {

    private HashMap<Integer, Docente> docentes;

    // Aquí creamos el gestor con el HashMap vacío.
    public GestorDocentes() {
        this.docentes = new HashMap<>();
    }

    // Aquí registramos un docente nuevo.
    public Docente registrar(int id, String nombre, String correo, String especialidad) {
        Docente d = new Docente(id, nombre, correo, especialidad);
        docentes.put(id, d);
        return d;
    }

    // Aquí buscamos un docente por ID. Si no existe, devolvemos null.
    public Docente buscar(int id) {
        return docentes.get(id);
    }

    // Aquí le asignamos una materia a un docente.
    public boolean asignarMateria(int id, String codigo) {
        Docente d = docentes.get(id);
        if (d == null) {
            return false;
        }
        d.asignarMateria(codigo);
        return true;
    }

    // Aquí armamos la lista de todos los docentes.
    public String listar() {
        if (docentes.isEmpty()) {
            return "No hay docentes registrados.";
        }
        StringBuilder sb = new StringBuilder();
        for (Docente d : docentes.values()) {
            sb.append(d).append("\n");
            sb.append("   Materias asignadas: ").append(d.getMateriasAsignadas()).append("\n");
        }
        return sb.toString();
    }

    public HashMap<Integer, Docente> getDocentes() {
        return docentes;
    }
}
