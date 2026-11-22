package servicios;

import estructuras.Pila;
import modelo.Estudiante;
import modelo.HistorialAcademico;
import excepciones.EstudianteNoEncontradoException;

/**
 * Gestor de notas y reportes académicos.
 * Aquí registramos las notas y calculamos promedios y reportes.
 * También guardamos los últimos reportes consultados en una pila.
 */
public class GestorNotas {

    private GestorEstudiantes gestorEstudiantes;
    private SistemaDeshacerRehacer sistema;
    private Pila<String> ultimosReportes; // pila con los reportes consultados

    // Aquí creamos el gestor con la pila de reportes vacía.
    public GestorNotas(GestorEstudiantes gestorEstudiantes, SistemaDeshacerRehacer sistema) {
        this.gestorEstudiantes = gestorEstudiantes;
        this.sistema = sistema;
        this.ultimosReportes = new Pila<>();
    }

    // Aquí registramos una nota. Esta operación se puede deshacer.
    public void registrarNota(int id, final String codigo, int semestre, final double nota)
            throws EstudianteNoEncontradoException {
        final Estudiante est = gestorEstudiantes.buscar(id);

        if (semestre < 1 || semestre > HistorialAcademico.MAX_SEMESTRES) {
            throw new IllegalArgumentException("El semestre debe estar entre 1 y 10.");
        }
        if (nota < 0.0 || nota > 5.0) {
            throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0.");
        }

        final int sem = semestre - 1;
        final HistorialAcademico h = est.getHistorial();

        if (h.getMateriasPorSemestre(sem) >= HistorialAcademico.MAX_MATERIAS) {
            throw new IllegalStateException("El semestre ya tiene 20 materias registradas.");
        }

        // Aquí guardamos la nota y recordamos la posición donde quedó.
        final int pos = h.registrarNota(sem, nota);

        // Si la nota aprueba la materia, la marcamos como aprobada.
        final boolean aprobadaNueva =
                nota >= HistorialAcademico.NOTA_MINIMA_APROBACION && !est.aprobo(codigo);
        if (aprobadaNueva) {
            est.marcarAprobada(codigo);
        }

        // Guardamos la acción para deshacer y rehacer el registro de la nota.
        sistema.registrar(new Accion("Registrar nota " + nota + " (" + codigo + ") a " + est.getNombre()) {
            public void deshacer() {
                h.cambiarNota(sem, pos, 0.0);
                h.reducirContador(sem);
                if (aprobadaNueva) {
                    est.quitarAprobada(codigo);
                }
            }

            public void rehacer() {
                h.cambiarNota(sem, pos, nota);
                h.aumentarContador(sem);
                if (aprobadaNueva) {
                    est.marcarAprobada(codigo);
                }
            }
        });
    }

    // Aquí calculamos el promedio de un semestre específico.
    public String promedioSemestre(int id, int semestre) throws EstudianteNoEncontradoException {
        Estudiante est = gestorEstudiantes.buscar(id);
        double p = est.getHistorial().promedioSemestre(semestre - 1);
        String reporte = "Promedio del semestre " + semestre + " de " + est.getNombre() + ": " + p;
        ultimosReportes.push(reporte);
        return reporte;
    }

    // Aquí calculamos el promedio acumulado.
    public String promedioAcumulado(int id) throws EstudianteNoEncontradoException {
        Estudiante est = gestorEstudiantes.buscar(id);
        double p = est.getHistorial().promedioAcumulado();
        String reporte = "Promedio acumulado de " + est.getNombre() + ": " + p;
        ultimosReportes.push(reporte);
        return reporte;
    }

    // Aquí armamos el reporte de materias reprobadas.
    public String materiasReprobadas(int id) throws EstudianteNoEncontradoException {
        Estudiante est = gestorEstudiantes.buscar(id);
        String reporte = "Materias reprobadas de " + est.getNombre() + ":\n"
                + est.getHistorial().materiasReprobadas();
        ultimosReportes.push(reporte);
        return reporte;
    }

    // Aquí navegamos al reporte anterior usando la pila (opción "Atrás").
    public String reporteAnterior() {
        // Sacamos el reporte actual.
        ultimosReportes.pop();
        if (ultimosReportes.estaVacia()) {
            return null; // no hay reportes anteriores
        }
        return ultimosReportes.verTope();
    }
}
