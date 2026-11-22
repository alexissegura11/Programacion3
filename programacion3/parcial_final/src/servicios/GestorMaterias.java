package servicios;

import java.util.HashMap;
import modelo.Estudiante;
import modelo.Materia;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.PreRequisitoNoAprobadoException;

/**
 * Gestor de materias.
 * Aquí guardamos las materias en un HashMap y manejamos
 * las inscripciones, los prerrequisitos y la cola de espera.
 */
public class GestorMaterias {

    private HashMap<String, Materia> materias;
    private GestorEstudiantes gestorEstudiantes;
    private SistemaDeshacerRehacer sistema;

    // Aquí creamos el gestor con el HashMap vacío.
    public GestorMaterias(GestorEstudiantes gestorEstudiantes, SistemaDeshacerRehacer sistema) {
        this.materias = new HashMap<>();
        this.gestorEstudiantes = gestorEstudiantes;
        this.sistema = sistema;
    }

    // Aquí creamos una materia nueva.
    public Materia crear(String codigo, String nombre, int cuposMaximos, int creditos) {
        Materia m = new Materia(codigo, nombre, cuposMaximos, creditos);
        materias.put(codigo, m);
        return m;
    }

    public Materia buscarMateria(String codigo) {
        return materias.get(codigo);
    }

    public boolean existe(String codigo) {
        return materias.containsKey(codigo);
    }

    // Aquí agregamos un prerrequisito a una materia (lista enlazada).
    public boolean agregarPrerrequisito(String codigo, String codigoPre) {
        Materia m = materias.get(codigo);
        if (m == null) {
            return false;
        }
        m.agregarPrerrequisito(codigoPre);
        return true;
    }

    // Aquí inscribimos un estudiante. Puede lanzar varias excepciones:
    // - EstudianteNoEncontradoException si el ID no existe.
    // - PreRequisitoNoAprobadoException si no cumple los prerrequisitos.
    // - CupoLlenoException si la materia ya está llena.
    public void inscribir(int idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException, PreRequisitoNoAprobadoException, CupoLlenoException {
        final Estudiante est = gestorEstudiantes.buscar(idEstudiante);
        final Materia m = obtenerMateria(codigoMateria);

        if (m.estaInscrito(est.getId())) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esa materia.");
        }

        // Luego recorremos los prerrequisitos de la materia.
        for (String pre : m.getPrerrequisitos()) {
            // Si no aprobó alguno, lanzamos la excepción.
            if (!est.aprobo(pre)) {
                throw new PreRequisitoNoAprobadoException(
                        "El estudiante " + est.getNombre() + " no ha aprobado el prerrequisito " + pre + ".");
            }
        }

        // Si la materia está llena, lanzamos la excepción de cupo lleno.
        if (m.estaLlena()) {
            throw new CupoLlenoException("La materia " + codigoMateria + " está llena.");
        }

        inscribirInterno(est, m);
        registrarAccionInscribir(est, m);
    }

    // Aquí cancelamos una inscripción. También se puede deshacer.
    public void cancelarInscripcion(int idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException {
        final Estudiante est = gestorEstudiantes.buscar(idEstudiante);
        final Materia m = obtenerMateria(codigoMateria);

        if (!m.estaInscrito(est.getId())) {
            throw new IllegalArgumentException("El estudiante no está inscrito en esa materia.");
        }

        cancelarInterno(est, m);
        sistema.registrar(new Accion("Cancelar inscripción de " + est.getNombre() + " en " + m.getCodigo()) {
            public void deshacer() {
                inscribirInterno(est, m);
            }

            public void rehacer() {
                cancelarInterno(est, m);
            }
        });
    }

    // Aquí agregamos un estudiante a la cola de espera de una materia.
    public void agregarAColaDeEspera(int idEstudiante, String codigoMateria)
            throws EstudianteNoEncontradoException {
        Materia m = obtenerMateria(codigoMateria);
        Estudiante est = gestorEstudiantes.buscar(idEstudiante);

        if (m.estaInscrito(idEstudiante)) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en esa materia.");
        }
        if (m.estaEnCola(idEstudiante)) {
            throw new IllegalArgumentException("El estudiante ya está en la cola de espera.");
        }
        m.encolarEspera(idEstudiante);
    }

    // Aquí liberamos un cupo: sacamos al primero de la cola y lo inscribimos.
    public void liberarCupo(String codigoMateria) throws ColaDeEsperaVaciaException {
        Materia m = obtenerMateria(codigoMateria);
        Integer id = m.desencolarEspera(); // si está vacía, lanza la excepción
        try {
            Estudiante est = gestorEstudiantes.buscar(id);
            inscribirInterno(est, m);
        } catch (EstudianteNoEncontradoException e) {
            // Si el estudiante ya no existe, seguimos con el siguiente de la cola.
        }
    }

    // Aquí armamos la lista de todas las materias.
    public String listar() {
        if (materias.isEmpty()) {
            return "No hay materias creadas.";
        }
        StringBuilder sb = new StringBuilder();
        for (Materia m : materias.values()) {
            sb.append(m).append("\n");
        }
        return sb.toString();
    }

    // Aquí armamos el detalle de la cola de espera de una materia.
    public String verColaDeEspera(String codigo) {
        Materia m = materias.get(codigo);
        if (m == null) {
            return "La materia " + codigo + " no existe.";
        }
        return "Cola de espera de " + codigo + " (" + m.getColaDeEspera().tamanio()
                + "): frente = " + m.getColaDeEspera().verFrente();
    }

    // Método privado: obtenemos la materia o lanzamos un error si no existe.
    private Materia obtenerMateria(String codigo) {
        Materia m = materias.get(codigo);
        if (m == null) {
            throw new IllegalArgumentException("La materia " + codigo + " no existe.");
        }
        return m;
    }

    // Método interno que hace la inscripción real (sin guardar acción).
    private void inscribirInterno(Estudiante est, Materia m) {
        m.agregarInscrito(est.getId());
        if (!est.getMateriasInscritas().contiene(m.getCodigo())) {
            est.getMateriasInscritas().agregarFinal(m.getCodigo());
        }
    }

    // Método interno que hace la cancelación real (sin guardar acción).
    private void cancelarInterno(Estudiante est, Materia m) {
        m.quitarInscrito(est.getId());
        est.getMateriasInscritas().eliminar(m.getCodigo());
    }

    // Aquí guardamos la acción de inscripción para deshacer y rehacer.
    private void registrarAccionInscribir(final Estudiante est, final Materia m) {
        sistema.registrar(new Accion("Inscribir " + est.getNombre() + " en " + m.getCodigo()) {
            public void deshacer() {
                cancelarInterno(est, m);
            }

            public void rehacer() {
                inscribirInterno(est, m);
            }
        });
    }

    public HashMap<String, Materia> getMaterias() {
        return materias;
    }
}
