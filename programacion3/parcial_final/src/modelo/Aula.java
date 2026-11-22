package modelo;

/**
 * Clase Aula.
 * Aquí guardamos una matriz booleana de 7 días por 24 horas
 * para saber si cada franja está ocupada o libre.
 */
public class Aula {

    public static final int DIAS = 7;
    public static final int HORAS = 24;

    private String nombre;
    private boolean[][] ocupacion; // [dia][hora] -> true si está ocupada

    // Aquí creamos un aula con toda su matriz libre.
    public Aula(String nombre) {
        this.nombre = nombre;
        this.ocupacion = new boolean[DIAS][HORAS];
    }

    public String getNombre() {
        return nombre;
    }

    // Aquí intentamos reservar una franja de horas.
    // Devolvemos true si se pudo reservar, false si hay conflicto.
    public boolean reservar(int dia, int hora, int duracion) {
        // Si la franja se sale del rango 0..23, no se puede reservar.
        if (hora < 0 || hora + duracion > HORAS) {
            return false;
        }
        // Luego recorremos las horas de la franja.
        for (int i = 0; i < duracion; i++) {
            // Si alguna hora ya está ocupada, hay conflicto.
            if (ocupacion[dia][hora + i]) {
                return false;
            }
        }
        // Si no hubo conflicto, marcamos toda la franja como ocupada.
        for (int i = 0; i < duracion; i++) {
            ocupacion[dia][hora + i] = true;
        }
        return true;
    }

    // Aquí liberamos una franja de horas.
    public void liberar(int dia, int hora, int duracion) {
        for (int i = 0; i < duracion; i++) {
            ocupacion[dia][hora + i] = false;
        }
    }

    // Aquí consultamos si una hora específica está libre.
    public boolean consultarDisponibilidad(int dia, int hora) {
        return !ocupacion[dia][hora];
    }

    public boolean[][] getOcupacion() {
        return ocupacion;
    }
}
