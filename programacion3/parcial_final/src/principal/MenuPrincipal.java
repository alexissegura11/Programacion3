package principal;

import java.util.Scanner;
import modelo.Docente;
import modelo.Estudiante;
import servicios.GestorDocentes;
import servicios.GestorEstudiantes;
import servicios.GestorHorarios;
import servicios.GestorMaterias;
import servicios.GestorNotas;
import servicios.GestorRutas;
import servicios.ProcesadorInscripciones;
import servicios.RegistroPersonas;
import servicios.SistemaDeshacerRehacer;
import utilidades.Consola;
import excepciones.ArchivoInvalidoException;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;

/**
 * Menú principal interactivo por consola.
 * Aquí conectamos todos los servicios y mostramos cada submenú.
 */
public class MenuPrincipal {

    private Scanner sc;
    private SistemaDeshacerRehacer sistema;
    private GestorEstudiantes gestorEstudiantes;
    private GestorDocentes gestorDocentes;
    private RegistroPersonas registroPersonas;
    private GestorMaterias gestorMaterias;
    private GestorHorarios gestorHorarios;
    private GestorNotas gestorNotas;
    private GestorRutas gestorRutas;
    private ProcesadorInscripciones procesador;

    // Aquí creamos el menú y conectamos todos los servicios.
    public MenuPrincipal() {
        sc = new Scanner(System.in);
        sistema = new SistemaDeshacerRehacer();
        gestorEstudiantes = new GestorEstudiantes(sistema);
        gestorDocentes = new GestorDocentes();
        registroPersonas = new RegistroPersonas();
        gestorMaterias = new GestorMaterias(gestorEstudiantes, sistema);
        gestorHorarios = new GestorHorarios(sistema);
        gestorNotas = new GestorNotas(gestorEstudiantes, sistema);
        gestorRutas = new GestorRutas();
        procesador = new ProcesadorInscripciones(gestorMaterias);
    }

    // Aquí arrancamos el menú principal.
    public void iniciar() {
        while (true) {
            Consola.limpiarPantalla();
            Consola.titulo("SISTEMA DE GESTIÓN UNIVERSITARIA");
            System.out.println("  1. Gestión de estudiantes");
            System.out.println("  2. Gestión de docentes");
            System.out.println("  3. Gestión de materias");
            System.out.println("  4. Gestión de horarios (aulas)");
            System.out.println("  5. Reportes académicos (notas)");
            System.out.println("  6. Rutas entre edificios (Dijkstra)");
            System.out.println("  7. Inscripciones por lotes (CSV)");
            System.out.println("  8. Deshacer / Rehacer");
            System.out.println("  9. Personas registradas (polimorfismo)");
            System.out.println("  0. Salir");
            int op = Consola.leerEntero(sc, "\nElige una opción: ", 0, 9);
            switch (op) {
                case 1: menuEstudiantes(); break;
                case 2: menuDocentes(); break;
                case 3: menuMaterias(); break;
                case 4: menuHorarios(); break;
                case 5: menuReportes(); break;
                case 6: menuRutas(); break;
                case 7: menuLotes(); break;
                case 8: menuDeshacerRehacer(); break;
                case 9: mostrarPolimorfismo(); break;
                case 0:
                    System.out.println("\n¡Hasta luego!");
                    return;
                default:
                    break;
            }
        }
    }

    // ===================== ESTUDIANTES =====================
    private void menuEstudiantes() {
        while (true) {
            Consola.titulo("GESTIÓN DE ESTUDIANTES");
            System.out.println("  1. Registrar estudiante");
            System.out.println("  2. Buscar estudiante por ID");
            System.out.println("  3. Mostrar historial académico");
            System.out.println("  4. Eliminar estudiante");
            System.out.println("  5. Listar todos los estudiantes");
            System.out.println("  0. Volver");
            int op = Consola.leerEntero(sc, "\nElige una opción: ", 0, 5);

            if (op == 0) {
                return;
            }
            if (op == 1) {
                int id = Consola.leerEntero(sc, "ID: ", 1, Integer.MAX_VALUE);
                if (gestorEstudiantes.existe(id)) {
                    Consola.error("Ya existe un estudiante con ese ID.");
                    Consola.pausa(sc);
                    continue;
                }
                String nombre = Consola.leerTexto(sc, "Nombre: ");
                String correo = Consola.leerTexto(sc, "Correo: ");
                int semestre = Consola.leerEntero(sc, "Semestre actual (1-10): ", 1, 10);
                Estudiante e = gestorEstudiantes.registrar(id, nombre, correo, semestre);
                registroPersonas.agregar(e);
                Consola.exito("Estudiante registrado: " + e);
            } else if (op == 2) {
                int id = Consola.leerEntero(sc, "ID a buscar: ", 1, Integer.MAX_VALUE);
                try {
                    Estudiante e = gestorEstudiantes.buscar(id);
                    System.out.println("Encontrado: " + e);
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 3) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                try {
                    System.out.println(gestorEstudiantes.mostrarHistorial(id));
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 4) {
                int id = Consola.leerEntero(sc, "ID a eliminar: ", 1, Integer.MAX_VALUE);
                try {
                    gestorEstudiantes.eliminar(id);
                    Consola.exito("Estudiante eliminado (puedes deshacerlo desde el menú 8).");
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 5) {
                System.out.println(gestorEstudiantes.listar());
            }
            Consola.pausa(sc);
        }
    }

    // ===================== DOCENTES =====================
    private void menuDocentes() {
        while (true) {
            Consola.titulo("GESTIÓN DE DOCENTES");
            System.out.println("  1. Registrar docente");
            System.out.println("  2. Asignar materia a docente");
            System.out.println("  3. Listar docentes");
            System.out.println("  0. Volver");
            int op = Consola.leerEntero(sc, "\nElige una opción: ", 0, 3);

            if (op == 0) {
                return;
            }
            if (op == 1) {
                int id = Consola.leerEntero(sc, "ID: ", 1, Integer.MAX_VALUE);
                if (gestorDocentes.buscar(id) != null) {
                    Consola.error("Ya existe un docente con ese ID.");
                    Consola.pausa(sc);
                    continue;
                }
                String nombre = Consola.leerTexto(sc, "Nombre: ");
                String correo = Consola.leerTexto(sc, "Correo: ");
                String especialidad = Consola.leerTexto(sc, "Especialidad: ");
                Docente d = gestorDocentes.registrar(id, nombre, correo, especialidad);
                registroPersonas.agregar(d);
                Consola.exito("Docente registrado: " + d);
            } else if (op == 2) {
                int id = Consola.leerEntero(sc, "ID del docente: ", 1, Integer.MAX_VALUE);
                String codigo = Consola.leerTexto(sc, "Código de la materia: ");
                if (gestorDocentes.asignarMateria(id, codigo)) {
                    Consola.exito("Materia " + codigo + " asignada al docente " + id + ".");
                } else {
                    Consola.error("No existe un docente con ID " + id + ".");
                }
            } else if (op == 3) {
                System.out.println(gestorDocentes.listar());
            }
            Consola.pausa(sc);
        }
    }

    // ===================== MATERIAS =====================
    private void menuMaterias() {
        while (true) {
            Consola.titulo("GESTIÓN DE MATERIAS");
            System.out.println("  1. Crear materia");
            System.out.println("  2. Agregar prerrequisito");
            System.out.println("  3. Inscribir estudiante");
            System.out.println("  4. Cancelar inscripción");
            System.out.println("  5. Agregar a cola de espera");
            System.out.println("  6. Liberar cupo (asignar al primero de la cola)");
            System.out.println("  7. Listar materias");
            System.out.println("  0. Volver");
            int op = Consola.leerEntero(sc, "\nElige una opción: ", 0, 7);

            if (op == 0) {
                return;
            }
            if (op == 1) {
                String codigo = Consola.leerTexto(sc, "Código: ");
                if (gestorMaterias.existe(codigo)) {
                    Consola.error("Ya existe una materia con ese código.");
                    Consola.pausa(sc);
                    continue;
                }
                String nombre = Consola.leerTexto(sc, "Nombre: ");
                int cupos = Consola.leerEntero(sc, "Cupos máximos: ", 1, 1000);
                int creditos = Consola.leerEntero(sc, "Créditos: ", 1, 20);
                gestorMaterias.crear(codigo, nombre, cupos, creditos);
                Consola.exito("Materia creada: " + codigo + " - " + nombre);
            } else if (op == 2) {
                String codigo = Consola.leerTexto(sc, "Código de la materia: ");
                String pre = Consola.leerTexto(sc, "Código del prerrequisito: ");
                if (gestorMaterias.agregarPrerrequisito(codigo, pre)) {
                    Consola.exito("Prerrequisito " + pre + " agregado a " + codigo + ".");
                } else {
                    Consola.error("La materia " + codigo + " no existe.");
                }
            } else if (op == 3) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                String codigo = Consola.leerTexto(sc, "Código de la materia: ");
                try {
                    gestorMaterias.inscribir(id, codigo);
                    Consola.exito("Estudiante inscrito correctamente.");
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                } catch (PreRequisitoNoAprobadoException ex) {
                    Consola.error(ex.getMessage());
                } catch (CupoLlenoException ex) {
                    Consola.error(ex.getMessage());
                    String resp = Consola.leerTexto(sc, "¿Deseas agregarlo a la cola de espera? (s/n): ");
                    if (resp.equalsIgnoreCase("s")) {
                        try {
                            gestorMaterias.agregarAColaDeEspera(id, codigo);
                            Consola.exito("Estudiante agregado a la cola de espera.");
                        } catch (Exception ex2) {
                            Consola.error(ex2.getMessage());
                        }
                    }
                } catch (IllegalArgumentException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 4) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                String codigo = Consola.leerTexto(sc, "Código de la materia: ");
                try {
                    gestorMaterias.cancelarInscripcion(id, codigo);
                    Consola.exito("Inscripción cancelada.");
                    // Si hay alguien en la cola de espera, le asignamos el cupo.
                    if (gestorMaterias.buscarMateria(codigo) != null
                            && !gestorMaterias.buscarMateria(codigo).colaVacia()) {
                        gestorMaterias.liberarCupo(codigo);
                        Consola.exito("El cupo se asignó al primer estudiante de la cola.");
                    }
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                } catch (ColaDeEsperaVaciaException ex) {
                    Consola.error(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 5) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                String codigo = Consola.leerTexto(sc, "Código de la materia: ");
                try {
                    gestorMaterias.agregarAColaDeEspera(id, codigo);
                    Consola.exito("Estudiante agregado a la cola de espera.");
                } catch (Exception ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 6) {
                String codigo = Consola.leerTexto(sc, "Código de la materia: ");
                try {
                    gestorMaterias.liberarCupo(codigo);
                    Consola.exito("Cupo liberado y asignado al primero de la cola.");
                } catch (ColaDeEsperaVaciaException ex) {
                    Consola.error(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 7) {
                System.out.println(gestorMaterias.listar());
            }
            Consola.pausa(sc);
        }
    }

    // ===================== HORARIOS =====================
    private void menuHorarios() {
        while (true) {
            Consola.titulo("GESTIÓN DE HORARIOS (AULAS)");
            System.out.println("  Días: 0=Domingo 1=Lunes 2=Martes 3=Miércoles 4=Jueves 5=Viernes 6=Sábado");
            System.out.println("  1. Crear aula");
            System.out.println("  2. Reservar horario");
            System.out.println("  3. Liberar horario");
            System.out.println("  4. Cambiar horario");
            System.out.println("  5. Consultar disponibilidad");
            System.out.println("  6. Ver horario de un aula");
            System.out.println("  0. Volver");
            int op = Consola.leerEntero(sc, "\nElige una opción: ", 0, 6);

            if (op == 0) {
                return;
            }
            if (op == 1) {
                String nombre = Consola.leerTexto(sc, "Nombre del aula: ");
                gestorHorarios.crearAula(nombre);
                Consola.exito("Aula creada: " + nombre);
            } else if (op == 2) {
                String aula = Consola.leerTexto(sc, "Aula: ");
                int dia = Consola.leerEntero(sc, "Día (0-6): ", 0, 6);
                int hora = Consola.leerEntero(sc, "Hora de inicio (0-23): ", 0, 23);
                int dur = Consola.leerEntero(sc, "Duración (horas): ", 1, 24);
                String materia = Consola.leerTexto(sc, "Materia: ");
                try {
                    gestorHorarios.reservar(aula, dia, hora, dur, materia);
                    Consola.exito("Horario reservado.");
                } catch (HorarioConflictivoException ex) {
                    Consola.error(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 3) {
                String aula = Consola.leerTexto(sc, "Aula: ");
                int dia = Consola.leerEntero(sc, "Día (0-6): ", 0, 6);
                int hora = Consola.leerEntero(sc, "Hora de inicio (0-23): ", 0, 23);
                int dur = Consola.leerEntero(sc, "Duración (horas): ", 1, 24);
                try {
                    gestorHorarios.liberar(aula, dia, hora, dur);
                    Consola.exito("Horario liberado.");
                } catch (IllegalArgumentException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 4) {
                String av = Consola.leerTexto(sc, "Aula actual: ");
                int dv = Consola.leerEntero(sc, "Día actual (0-6): ", 0, 6);
                int hv = Consola.leerEntero(sc, "Hora actual (0-23): ", 0, 23);
                int dur = Consola.leerEntero(sc, "Duración (horas): ", 1, 24);
                String an = Consola.leerTexto(sc, "Aula nueva: ");
                int dn = Consola.leerEntero(sc, "Día nuevo (0-6): ", 0, 6);
                int hn = Consola.leerEntero(sc, "Hora nueva (0-23): ", 0, 23);
                try {
                    gestorHorarios.cambiarHorario(av, dv, hv, dur, an, dn, hn);
                    Consola.exito("Horario cambiado.");
                } catch (HorarioConflictivoException ex) {
                    Consola.error(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 5) {
                String aula = Consola.leerTexto(sc, "Aula: ");
                int dia = Consola.leerEntero(sc, "Día (0-6): ", 0, 6);
                int hora = Consola.leerEntero(sc, "Hora (0-23): ", 0, 23);
                boolean libre = gestorHorarios.consultarDisponibilidad(aula, dia, hora);
                System.out.println("Disponibilidad en " + aula + " (" + GestorHorarios.NOMBRES_DIAS[dia]
                        + " " + hora + "h): " + (libre ? "LIBRE" : "OCUPADO"));
            } else if (op == 6) {
                String aula = Consola.leerTexto(sc, "Aula: ");
                System.out.println(gestorHorarios.mostrarHorario(aula));
            }
            Consola.pausa(sc);
        }
    }

    // ===================== REPORTES =====================
    private void menuReportes() {
        while (true) {
            Consola.titulo("REPORTES ACADÉMICOS (NOTAS)");
            System.out.println("  1. Registrar nota");
            System.out.println("  2. Promedio de un semestre");
            System.out.println("  3. Promedio acumulado");
            System.out.println("  4. Materias reprobadas");
            System.out.println("  5. Atrás (reporte anterior)");
            System.out.println("  0. Volver");
            int op = Consola.leerEntero(sc, "\nElige una opción: ", 0, 5);

            if (op == 0) {
                return;
            }
            if (op == 1) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                String codigo = Consola.leerTexto(sc, "Código de la materia: ");
                int semestre = Consola.leerEntero(sc, "Semestre (1-10): ", 1, 10);
                double nota = Consola.leerDouble(sc, "Nota (0.0-5.0): ", 0.0, 5.0);
                try {
                    gestorNotas.registrarNota(id, codigo, semestre, nota);
                    Consola.exito("Nota registrada.");
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    Consola.error(ex.getMessage());
                } catch (IllegalStateException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 2) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                int semestre = Consola.leerEntero(sc, "Semestre (1-10): ", 1, 10);
                try {
                    System.out.println(gestorNotas.promedioSemestre(id, semestre));
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 3) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                try {
                    System.out.println(gestorNotas.promedioAcumulado(id));
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 4) {
                int id = Consola.leerEntero(sc, "ID del estudiante: ", 1, Integer.MAX_VALUE);
                try {
                    System.out.println(gestorNotas.materiasReprobadas(id));
                } catch (EstudianteNoEncontradoException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 5) {
                String anterior = gestorNotas.reporteAnterior();
                if (anterior == null) {
                    Consola.error("No hay reportes anteriores.");
                } else {
                    System.out.println(anterior);
                }
            }
            Consola.pausa(sc);
        }
    }

    // ===================== RUTAS (DIJKSTRA) =====================
    private void menuRutas() {
        Consola.titulo("RUTAS ENTRE EDIFICIOS (DIJKSTRA)");
        System.out.println(gestorRutas.listarEdificios());
        int origen = Consola.leerEntero(sc, "Edificio de origen: ", 0, gestorRutas.cantidadEdificios() - 1);
        int destino = Consola.leerEntero(sc, "Edificio de destino: ", 0, gestorRutas.cantidadEdificios() - 1);
        System.out.println();
        System.out.println(gestorRutas.rutaMasCorta(origen, destino));
        Consola.pausa(sc);
    }

    // ===================== LOTES (CSV) =====================
    private void menuLotes() {
        Consola.titulo("INSCRIPCIONES POR LOTES (CSV)");
        System.out.println("El archivo debe tener una solicitud por línea: idEstudiante,codigoMateria");
        String ruta = Consola.leerTexto(sc, "Ruta del archivo CSV: ");
        try {
            System.out.println(procesador.procesar(ruta));
        } catch (ArchivoInvalidoException ex) {
            Consola.error(ex.getMessage());
        }
        Consola.pausa(sc);
    }

    // ===================== DESHACER / REHACER =====================
    private void menuDeshacerRehacer() {
        while (true) {
            Consola.titulo("DESHACER / REHACER");
            System.out.println("  Operaciones para deshacer: " + sistema.tamanioDeshacer());
            System.out.println("  Operaciones para rehacer: " + sistema.tamanioRehacer());
            if (sistema.verTopeDeshacer() != null) {
                System.out.println("  Última acción (deshacer): " + sistema.verTopeDeshacer());
            }
            if (sistema.verTopeRehacer() != null) {
                System.out.println("  Última acción (rehacer): " + sistema.verTopeRehacer());
            }
            System.out.println();
            System.out.println("  1. Deshacer");
            System.out.println("  2. Rehacer");
            System.out.println("  0. Volver");
            int op = Consola.leerEntero(sc, "\nElige una opción: ", 0, 2);

            if (op == 0) {
                return;
            }
            if (op == 1) {
                try {
                    sistema.deshacer();
                    Consola.exito("Operación deshecha.");
                } catch (PilaDeshacerVaciaException ex) {
                    Consola.error(ex.getMessage());
                }
            } else if (op == 2) {
                try {
                    sistema.rehacer();
                    Consola.exito("Operación rehecha.");
                } catch (PilaDeshacerVaciaException ex) {
                    Consola.error(ex.getMessage());
                }
            }
            Consola.pausa(sc);
        }
    }

    // Aquí demostramos el polimorfismo con referencias de la clase Persona.
    private void mostrarPolimorfismo() {
        Consola.titulo("PERSONAS REGISTRADAS (POLIMORFISMO)");
        System.out.println(registroPersonas.listarConRol());
        Consola.pausa(sc);
    }
}
