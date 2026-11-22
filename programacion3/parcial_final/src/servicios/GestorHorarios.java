package servicios;

import java.util.HashMap;
import modelo.Aula;
import excepciones.HorarioConflictivoException;

/**
 * Gestor de horarios.
 * Aquí guardamos las aulas y manejamos las reservas usando
 * la matriz booleana de 7 días por 24 horas de cada aula.
 */
public class GestorHorarios {

    public static final String[] NOMBRES_DIAS = {
        "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"
    };

    private HashMap<String, Aula> aulas;
    private SistemaDeshacerRehacer sistema;

    // Aquí creamos el gestor con el HashMap vacío.
    public GestorHorarios(SistemaDeshacerRehacer sistema) {
        this.aulas = new HashMap<>();
        this.sistema = sistema;
    }

    // Aquí creamos un aula nueva.
    public void crearAula(String nombre) {
        aulas.put(nombre, new Aula(nombre));
    }

    public Aula buscarAula(String nombre) {
        return aulas.get(nombre);
    }

    public boolean existeAula(String nombre) {
        return aulas.containsKey(nombre);
    }

    // Aquí reservamos una franja de horas en un aula.
    public void reservar(String aula, int dia, int hora, int duracion, String materia)
            throws HorarioConflictivoException {
        final Aula a = obtenerAula(aula);
        validarDiaHora(dia, hora, duracion);
        if (!a.reservar(dia, hora, duracion)) {
            throw new HorarioConflictivoException(
                    "Conflicto: el aula " + aula + " ya está ocupada en "
                    + NOMBRES_DIAS[dia] + " a las " + hora + ".");
        }
        sistema.registrar(new Accion("Reservar " + materia + " en " + aula
                + " (" + NOMBRES_DIAS[dia] + " " + hora + "h)") {
            public void deshacer() {
                a.liberar(dia, hora, duracion);
            }

            public void rehacer() {
                a.reservar(dia, hora, duracion);
            }
        });
    }

    // Aquí liberamos una franja de horas de un aula.
    public void liberar(String aula, int dia, int hora, int duracion) {
        final Aula a = obtenerAula(aula);
        a.liberar(dia, hora, duracion);
        sistema.registrar(new Accion("Liberar " + aula + " (" + NOMBRES_DIAS[dia] + " " + hora + "h)") {
            public void deshacer() {
                a.reservar(dia, hora, duracion);
            }

            public void rehacer() {
                a.liberar(dia, hora, duracion);
            }
        });
    }

    // Aquí cambiamos el horario de una reserva de un aula a otra.
    public void cambiarHorario(String aulaVieja, int diaV, int horaV, int dur,
                               String aulaNueva, int diaN, int horaN)
            throws HorarioConflictivoException {
        final Aula av = obtenerAula(aulaVieja);
        final Aula an = obtenerAula(aulaNueva);

        // Liberamos la franja vieja y reservamos la nueva.
        av.liberar(diaV, horaV, dur);
        if (!an.reservar(diaN, horaN, dur)) {
            // Si falla, devolvemos la franja vieja y lanzamos el error.
            av.reservar(diaV, horaV, dur);
            throw new HorarioConflictivoException(
                    "Conflicto al cambiar: el aula " + aulaNueva + " está ocupada en "
                    + NOMBRES_DIAS[diaN] + " a las " + horaN + ".");
        }

        sistema.registrar(new Accion("Cambiar horario de " + aulaVieja + " a " + aulaNueva) {
            public void deshacer() {
                an.liberar(diaN, horaN, dur);
                av.reservar(diaV, horaV, dur);
            }

            public void rehacer() {
                av.liberar(diaV, horaV, dur);
                an.reservar(diaN, horaN, dur);
            }
        });
    }

    // Aquí consultamos si una hora específica está libre.
    public boolean consultarDisponibilidad(String aula, int dia, int hora) {
        Aula a = aulas.get(aula);
        if (a == null) {
            return false;
        }
        return a.consultarDisponibilidad(dia, hora);
    }

    // Aquí armamos la lista de aulas disponibles.
    public String listar() {
        if (aulas.isEmpty()) {
            return "No hay aulas creadas.";
        }
        StringBuilder sb = new StringBuilder();
        for (Aula a : aulas.values()) {
            sb.append("- ").append(a.getNombre()).append("\n");
        }
        return sb.toString();
    }

    // Aquí mostramos la matriz de ocupación de un aula (X = ocupado).
    public String mostrarHorario(String nombre) {
        Aula a = aulas.get(nombre);
        if (a == null) {
            return "El aula " + nombre + " no existe.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Horario del aula ").append(nombre).append(" (X = ocupado):\n");
        sb.append("        ");
        for (int h = 0; h < Aula.HORAS; h++) {
            sb.append(String.format("%2d ", h));
        }
        sb.append("\n");
        for (int d = 0; d < Aula.DIAS; d++) {
            sb.append(String.format("%-8s ", NOMBRES_DIAS[d]));
            for (int h = 0; h < Aula.HORAS; h++) {
                sb.append(a.getOcupacion()[d][h] ? " X " : " . ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Método privado: obtenemos el aula o lanzamos un error si no existe.
    private Aula obtenerAula(String nombre) {
        Aula a = aulas.get(nombre);
        if (a == null) {
            throw new IllegalArgumentException("El aula " + nombre + " no existe.");
        }
        return a;
    }

    // Aquí validamos el día, la hora y la duración.
    private void validarDiaHora(int dia, int hora, int duracion) throws HorarioConflictivoException {
        if (dia < 0 || dia > 6) {
            throw new HorarioConflictivoException("El día debe estar entre 0 y 6.");
        }
        if (hora < 0 || hora > 23) {
            throw new HorarioConflictivoException("La hora debe estar entre 0 y 23.");
        }
        if (duracion < 1 || hora + duracion > 24) {
            throw new HorarioConflictivoException("La duración excede el horario permitido (0 a 23).");
        }
    }
}
