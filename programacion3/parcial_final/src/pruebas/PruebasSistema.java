package pruebas;

import java.io.PrintWriter;
import servicios.GestorEstudiantes;
import servicios.GestorHorarios;
import servicios.GestorMaterias;
import servicios.GestorNotas;
import servicios.GestorRutas;
import servicios.ProcesadorInscripciones;
import servicios.SistemaDeshacerRehacer;
import excepciones.ArchivoInvalidoException;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;

/**
 * Pruebas automáticas del sistema.
 * Aquí comprobamos que cada funcionalidad funciona de verdad.
 * Se ejecuta con: java -cp build pruebas.PruebasSistema
 */
public class PruebasSistema {

    private static int total = 0;
    private static int pasaron = 0;
    private static int fallaron = 0;

    // Aquí verificamos una condición y contamos si pasó o falló.
    private static void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            pasaron++;
            System.out.println("  [PASÓ] " + nombre);
        } else {
            fallaron++;
            System.out.println("  [FALLÓ] " + nombre);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== PRUEBAS DEL SISTEMA DE GESTIÓN UNIVERSITARIA ===\n");

        // Creamos los servicios que vamos a probar.
        SistemaDeshacerRehacer sistema = new SistemaDeshacerRehacer();
        GestorEstudiantes ge = new GestorEstudiantes(sistema);
        GestorMaterias gm = new GestorMaterias(ge, sistema);
        GestorHorarios gh = new GestorHorarios(sistema);
        GestorNotas gn = new GestorNotas(ge, sistema);
        GestorRutas gr = new GestorRutas();
        ProcesadorInscripciones proc = new ProcesadorInscripciones(gm);

        // ---- [1] Estudiantes + HashMap + eliminar/deshacer ----
        System.out.println("\n[1] Gestión de estudiantes");
        ge.registrar(101, "Juan Pérez", "juan@correo.com", 3);
        ge.registrar(102, "María López", "maria@correo.com", 5);
        ge.registrar(103, "Ana Torres", "ana@correo.com", 2);
        verificar("Buscar por ID con HashMap", ge.buscar(101).getNombre().equals("Juan Pérez"));

        boolean exc = false;
        try {
            ge.buscar(999);
        } catch (EstudianteNoEncontradoException e) {
            exc = true;
        }
        verificar("EstudianteNoEncontradoException", exc);

        ge.eliminar(102);
        verificar("Eliminar estudiante", !ge.existe(102));
        sistema.deshacer();
        verificar("Deshacer eliminación", ge.existe(102));
        sistema.rehacer();
        verificar("Rehacer eliminación", !ge.existe(102));
        sistema.deshacer();
        verificar("Estudiante 102 restaurado", ge.existe(102));

        // ---- [2] Materias, prerrequisitos, cupo lleno y cola ----
        System.out.println("\n[2] Materias, prerrequisitos y cola de espera");
        gm.crear("MAT101", "Matemáticas I", 1, 3);   // cupo 1
        gm.crear("MAT201", "Matemáticas II", 2, 4);  // cupo 2
        gm.agregarPrerrequisito("MAT201", "MAT101");

        exc = false;
        try {
            gm.inscribir(101, "MAT201");
        } catch (PreRequisitoNoAprobadoException e) {
            exc = true;
        }
        verificar("PreRequisitoNoAprobadoException", exc);

        gn.registrarNota(101, "MAT101", 1, 4.5); // aprueba MAT101
        gm.inscribir(101, "MAT201");
        verificar("Inscripción cumpliendo prerrequisito", gm.buscarMateria("MAT201").estaInscrito(101));

        gm.inscribir(101, "MAT101"); // cupo 1 -> llena
        exc = false;
        try {
            gm.inscribir(102, "MAT101");
        } catch (CupoLlenoException e) {
            exc = true;
        }
        verificar("CupoLlenoException", exc);

        gm.agregarAColaDeEspera(102, "MAT101");
        verificar("Agregar a cola de espera", gm.buscarMateria("MAT101").estaEnCola(102));

        gm.cancelarInscripcion(101, "MAT101");
        gm.liberarCupo("MAT101");
        verificar("Liberar cupo asigna al primero de la cola", gm.buscarMateria("MAT101").estaInscrito(102));

        exc = false;
        try {
            gm.liberarCupo("MAT101");
        } catch (ColaDeEsperaVaciaException e) {
            exc = true;
        }
        verificar("ColaDeEsperaVaciaException", exc);

        // ---- [3] Horarios con matrices ----
        System.out.println("\n[3] Horarios con matrices boolean[7][24]");
        gh.crearAula("A101");
        gh.crearAula("A102");
        gh.reservar("A101", 1, 8, 2, "MAT101");
        verificar("Reservar horario", !gh.consultarDisponibilidad("A101", 1, 8));
        exc = false;
        try {
            gh.reservar("A101", 1, 9, 1, "MAT201");
        } catch (HorarioConflictivoException e) {
            exc = true;
        }
        verificar("HorarioConflictivoException", exc);
        verificar("Consultar disponibilidad (libre)", gh.consultarDisponibilidad("A101", 1, 10));
        gh.liberar("A101", 1, 8, 2);
        verificar("Liberar horario", gh.consultarDisponibilidad("A101", 1, 8));

        // ---- [4] Notas y reportes ----
        System.out.println("\n[4] Notas y reportes (double[10][20])");
        ge.registrar(105, "Pedro Notas", "pedro@correo.com", 1);
        gn.registrarNota(105, "MAT101", 1, 2.5); // reprobada
        gn.registrarNota(105, "MAT201", 1, 4.0);
        verificar("Promedio de semestre (3.25)", gn.promedioSemestre(105, 1).contains("3.25"));
        verificar("Promedio acumulado (3.25)", gn.promedioAcumulado(105).contains("3.25"));
        verificar("Reporte de materias reprobadas", gn.materiasReprobadas(105).contains("2.5"));

        // ---- [5] Rutas con Dijkstra ----
        System.out.println("\n[5] Rutas entre edificios (Dijkstra)");
        String ruta = gr.rutaMasCorta(0, 4);
        verificar("Ruta más corta generada", ruta.contains("Camino") && ruta.contains("metros"));
        verificar("Origen y destino correctos", ruta.contains("Bloque A") && ruta.contains("Auditorio"));
        verificar("Distancia total de 290 metros", ruta.contains("290"));

        // ---- [6] Procesamiento por lotes (CSV) ----
        System.out.println("\n[6] Procesamiento por lotes (CSV)");
        gm.crear("INF101", "Informática I", 1, 3); // cupo 1 para probar la espera
        ge.registrar(104, "Luis Gómez", "luis@correo.com", 1);

        String csv = System.getProperty("java.io.tmpdir") + "/inscripciones_prueba.csv";
        PrintWriter pw = new PrintWriter(csv);
        pw.println("104,INF101"); // exitosa (llena el cupo)
        pw.println("102,INF101"); // cupo lleno -> cola de espera
        pw.println("999,INF101"); // estudiante no existe -> fallida
        pw.println("103,INF101"); // cupo lleno -> cola de espera
        pw.close();

        String resumen = proc.procesar(csv);
        verificar("Batch: exitosas", resumen.contains("Exitosas: 1"));
        verificar("Batch: en cola de espera", resumen.contains("En cola de espera: 2"));
        verificar("Batch: fallidas", resumen.contains("Fallidas: 1"));

        exc = false;
        try {
            proc.procesar("/ruta/que/no/existe.csv");
        } catch (ArchivoInvalidoException e) {
            exc = true;
        }
        verificar("ArchivoInvalidoException", exc);

        // ---- [7] Pila deshacer/rehacer vacía ----
        System.out.println("\n[7] Pila deshacer/rehacer vacía");
        SistemaDeshacerRehacer s2 = new SistemaDeshacerRehacer();
        exc = false;
        try {
            s2.deshacer();
        } catch (PilaDeshacerVaciaException e) {
            exc = true;
        }
        verificar("PilaDeshacerVaciaException (deshacer)", exc);
        exc = false;
        try {
            s2.rehacer();
        } catch (PilaDeshacerVaciaException e) {
            exc = true;
        }
        verificar("PilaDeshacerVaciaException (rehacer)", exc);

        // ---- [8] Deshacer/rehacer de las 5 operaciones ----
        System.out.println("\n[8] Deshacer/rehacer de las 5 operaciones");
        SistemaDeshacerRehacer s3 = new SistemaDeshacerRehacer();
        GestorEstudiantes ge3 = new GestorEstudiantes(s3);
        GestorMaterias gm3 = new GestorMaterias(ge3, s3);
        GestorHorarios gh3 = new GestorHorarios(s3);
        GestorNotas gn3 = new GestorNotas(ge3, s3);

        ge3.registrar(201, "Est A", "a@correo.com", 1);
        ge3.registrar(202, "Est B", "b@correo.com", 1);
        gm3.crear("M1", "Materia 1", 5, 3);

        gm3.inscribir(201, "M1");
        verificar("Inscribir: queda inscrito", gm3.buscarMateria("M1").estaInscrito(201));
        s3.deshacer();
        verificar("Deshacer inscribir", !gm3.buscarMateria("M1").estaInscrito(201));
        s3.rehacer();
        verificar("Rehacer inscribir", gm3.buscarMateria("M1").estaInscrito(201));

        gm3.cancelarInscripcion(201, "M1");
        verificar("Cancelar inscripción", !gm3.buscarMateria("M1").estaInscrito(201));
        s3.deshacer();
        verificar("Deshacer cancelación", gm3.buscarMateria("M1").estaInscrito(201));
        s3.rehacer();
        verificar("Rehacer cancelación", !gm3.buscarMateria("M1").estaInscrito(201));

        gh3.crearAula("A1");
        gh3.crearAula("A2");
        gh3.reservar("A1", 2, 10, 2, "M1");
        gh3.cambiarHorario("A1", 2, 10, 2, "A2", 3, 14);
        verificar("Cambiar horario: nueva ocupada y vieja libre",
                !gh3.consultarDisponibilidad("A2", 3, 14) && gh3.consultarDisponibilidad("A1", 2, 10));
        s3.deshacer();
        verificar("Deshacer cambio de horario",
                gh3.consultarDisponibilidad("A2", 3, 14) && !gh3.consultarDisponibilidad("A1", 2, 10));
        s3.rehacer();
        verificar("Rehacer cambio de horario",
                !gh3.consultarDisponibilidad("A2", 3, 14) && gh3.consultarDisponibilidad("A1", 2, 10));

        gn3.registrarNota(201, "M1", 1, 4.0);
        verificar("Registrar nota: promedio 4.0", gn3.promedioSemestre(201, 1).contains("4.0"));
        s3.deshacer();
        verificar("Deshacer registrar nota (promedio 0.0)", gn3.promedioSemestre(201, 1).contains("0.0"));
        s3.rehacer();
        verificar("Rehacer registrar nota (promedio 4.0)", gn3.promedioSemestre(201, 1).contains("4.0"));

        ge3.eliminar(202);
        verificar("Eliminar estudiante", !ge3.existe(202));
        s3.deshacer();
        verificar("Deshacer eliminar estudiante", ge3.existe(202));
        s3.rehacer();
        verificar("Rehacer eliminar estudiante", !ge3.existe(202));

        // ---- Resultado final ----
        System.out.println("\n=== RESULTADO: " + pasaron + "/" + total
                + " pruebas pasaron, " + fallaron + " fallaron ===");
        if (fallaron > 0) {
            System.exit(1);
        }
    }
}
