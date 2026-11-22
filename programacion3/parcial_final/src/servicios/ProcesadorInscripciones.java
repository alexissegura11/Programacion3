package servicios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import estructuras.Cola;
import excepciones.ArchivoInvalidoException;
import excepciones.CupoLlenoException;

/**
 * Procesador de inscripciones por lotes.
 * Aquí leemos un archivo CSV con solicitudes de inscripción,
 * las metemos en una cola y las procesamos en orden.
 */
public class ProcesadorInscripciones {

    // Solicitud de inscripción leída del archivo.
    public static class Solicitud {
        private int idEstudiante;
        private String codigoMateria;

        public Solicitud(int idEstudiante, String codigoMateria) {
            this.idEstudiante = idEstudiante;
            this.codigoMateria = codigoMateria;
        }

        public int getIdEstudiante() {
            return idEstudiante;
        }

        public String getCodigoMateria() {
            return codigoMateria;
        }
    }

    private GestorMaterias gestorMaterias;

    // Aquí creamos el procesador con el gestor de materias.
    public ProcesadorInscripciones(GestorMaterias gestorMaterias) {
        this.gestorMaterias = gestorMaterias;
    }

    // Aquí procesamos todo el archivo CSV.
    public String procesar(String rutaArchivo) throws ArchivoInvalidoException {
        Cola<Solicitud> cola = new Cola<>();

        // 1. Leemos el archivo línea por línea.
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numLinea = 0;
            while ((linea = br.readLine()) != null) {
                numLinea++;
                linea = linea.trim();
                // Saltamos las líneas vacías.
                if (linea.isEmpty()) {
                    continue;
                }
                // 2. Validamos el formato: idEstudiante,codigoMateria
                String[] partes = linea.split(",");
                if (partes.length != 2) {
                    throw new ArchivoInvalidoException(
                            "La línea " + numLinea + " no tiene el formato 'idEstudiante,codigoMateria': " + linea);
                }
                int id;
                try {
                    id = Integer.parseInt(partes[0].trim());
                } catch (NumberFormatException e) {
                    throw new ArchivoInvalidoException(
                            "El ID en la línea " + numLinea + " no es un número: " + partes[0]);
                }
                String codigo = partes[1].trim();
                if (codigo.isEmpty()) {
                    throw new ArchivoInvalidoException(
                            "El código de materia en la línea " + numLinea + " está vacío.");
                }
                // Agregamos la solicitud a la cola.
                cola.encolar(new Solicitud(id, codigo));
            }
        } catch (IOException e) {
            throw new ArchivoInvalidoException("No se pudo leer el archivo: " + e.getMessage());
        }

        if (cola.estaVacia()) {
            throw new ArchivoInvalidoException("El archivo está vacío.");
        }

        // 3. Procesamos la cola en orden de llegada.
        int exitosas = 0;
        int enEspera = 0;
        int fallidas = 0;
        StringBuilder detalle = new StringBuilder();

        while (!cola.estaVacia()) {
            Solicitud s = cola.desencolar();
            try {
                gestorMaterias.inscribir(s.getIdEstudiante(), s.getCodigoMateria());
                exitosas++;
                detalle.append("OK: estudiante ").append(s.getIdEstudiante())
                       .append(" -> ").append(s.getCodigoMateria()).append("\n");
            } catch (CupoLlenoException e) {
                // Si está llena, lo pasamos a la cola de espera.
                try {
                    gestorMaterias.agregarAColaDeEspera(s.getIdEstudiante(), s.getCodigoMateria());
                    enEspera++;
                    detalle.append("EN ESPERA: ").append(e.getMessage()).append("\n");
                } catch (Exception ex) {
                    fallidas++;
                    detalle.append("FALLÓ (estudiante ").append(s.getIdEstudiante())
                           .append("): ").append(ex.getMessage()).append("\n");
                }
            } catch (Exception e) {
                fallidas++;
                detalle.append("FALLÓ (estudiante ").append(s.getIdEstudiante())
                       .append("): ").append(e.getMessage()).append("\n");
            }
        }

        // 4. Armamos y mostramos el resumen final.
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DEL PROCESAMIENTO ===\n");
        resumen.append("Total de solicitudes: ").append(exitosas + enEspera + fallidas).append("\n");
        resumen.append("Exitosas: ").append(exitosas).append("\n");
        resumen.append("En cola de espera: ").append(enEspera).append("\n");
        resumen.append("Fallidas: ").append(fallidas).append("\n");
        resumen.append("--------------------------------\n");
        resumen.append(detalle);
        return resumen.toString();
    }
}
