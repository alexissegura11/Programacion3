package servicios;

import java.util.HashMap;
import modelo.Estudiante;
import modelo.HistorialAcademico;
import excepciones.EstudianteNoEncontradoException;

/**
 * Gestor de estudiantes.
 * Aquí guardamos los estudiantes en un HashMap para buscarlos
 * rápido por su ID.
 */
public class GestorEstudiantes {

    private HashMap<Integer, Estudiante> estudiantes;
    private SistemaDeshacerRehacer sistema;

    // Aquí creamos el gestor con el HashMap vacío.
    public GestorEstudiantes(SistemaDeshacerRehacer sistema) {
        this.estudiantes = new HashMap<>();
        this.sistema = sistema;
    }

    // Aquí registramos un estudiante nuevo.
    public Estudiante registrar(int id, String nombre, String correo, int semestre) {
        Estudiante e = new Estudiante(id, nombre, correo, semestre);
        estudiantes.put(id, e);
        return e;
    }

    public boolean existe(int id) {
        return estudiantes.containsKey(id);
    }

    // Aquí buscamos por ID usando el HashMap.
    // Si no está, lanzamos la excepción EstudianteNoEncontradoException.
    public Estudiante buscar(int id) throws EstudianteNoEncontradoException {
        Estudiante e = estudiantes.get(id);
        if (e == null) {
            throw new EstudianteNoEncontradoException("No existe un estudiante con ID " + id + ".");
        }
        return e;
    }

    // Aquí eliminamos un estudiante. Esta operación se puede deshacer.
    public void eliminar(int id) throws EstudianteNoEncontradoException {
        final Estudiante e = buscar(id); // si no existe, lanza la excepción
        estudiantes.remove(id);
        // Guardamos la acción para poder deshacer y rehacer la eliminación.
        sistema.registrar(new Accion("Eliminar estudiante " + e.getNombre() + " (ID " + id + ")") {
            public void deshacer() {
                estudiantes.put(e.getId(), e);
            }

            public void rehacer() {
                estudiantes.remove(e.getId());
            }
        });
    }

    // Aquí armamos la lista de todos los estudiantes registrados.
    public String listar() {
        if (estudiantes.isEmpty()) {
            return "No hay estudiantes registrados.";
        }
        StringBuilder sb = new StringBuilder();
        for (Estudiante e : estudiantes.values()) {
            sb.append(e).append("\n");
            sb.append("   Inscritas: ").append(e.getMateriasInscritas()).append("\n");
            sb.append("   Aprobadas: ").append(e.getMateriasAprobadas()).append("\n");
        }
        return sb.toString();
    }

    // Aquí armamos el historial académico completo de un estudiante.
    public String mostrarHistorial(int id) throws EstudianteNoEncontradoException {
        Estudiante e = buscar(id);
        HistorialAcademico h = e.getHistorial();
        StringBuilder sb = new StringBuilder();
        sb.append("Historial de ").append(e.getNombre())
          .append(" (semestre actual: ").append(e.getSemestreActual()).append(")\n");
        for (int s = 0; s < HistorialAcademico.MAX_SEMESTRES; s++) {
            if (h.getMateriasPorSemestre(s) > 0) {
                sb.append("  Semestre ").append(s + 1).append(": ");
                for (int i = 0; i < h.getMateriasPorSemestre(s); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(h.getNota(s, i));
                }
                sb.append("  (promedio: ").append(h.promedioSemestre(s)).append(")\n");
            }
        }
        sb.append("Materias aprobadas: ").append(e.getMateriasAprobadas());
        return sb.toString();
    }

    public HashMap<Integer, Estudiante> getEstudiantes() {
        return estudiantes;
    }
}
