package modelo;

/**
 * Historial académico del estudiante.
 * Aquí guardamos las notas en un arreglo estático de
 * 10 semestres por 20 materias (double[10][20]).
 */
public class HistorialAcademico {

    public static final int MAX_SEMESTRES = 10;
    public static final int MAX_MATERIAS = 20;
    public static final double NOTA_MINIMA_APROBACION = 3.0;

    private double[][] notas;          // [semestre][materia]
    private int[] materiasPorSemestre; // cuántas materias tiene cada semestre

    // Aquí creamos el historial vacío, todo en 0.
    public HistorialAcademico() {
        notas = new double[MAX_SEMESTRES][MAX_MATERIAS];
        materiasPorSemestre = new int[MAX_SEMESTRES];
    }

    public double getNota(int semestre, int materia) {
        return notas[semestre][materia];
    }

    // Aquí guardamos una nota nueva. Devolvemos la posición donde quedó.
    public int registrarNota(int semestre, double nota) {
        int pos = materiasPorSemestre[semestre];
        if (pos >= MAX_MATERIAS) {
            throw new IllegalStateException("El semestre ya tiene 20 materias.");
        }
        notas[semestre][pos] = nota;
        materiasPorSemestre[semestre]++;
        return pos;
    }

    // Aquí cambiamos una nota que ya estaba guardada (lo usamos para deshacer/rehacer).
    public void cambiarNota(int semestre, int materia, double nota) {
        notas[semestre][materia] = nota;
    }

    public void aumentarContador(int semestre) {
        materiasPorSemestre[semestre]++;
    }

    public void reducirContador(int semestre) {
        if (materiasPorSemestre[semestre] > 0) {
            materiasPorSemestre[semestre]--;
        }
    }

    // Aquí calculamos el promedio de un semestre específico.
    public double promedioSemestre(int semestre) {
        int n = materiasPorSemestre[semestre];
        if (n == 0) {
            return 0.0;
        }
        double suma = 0.0;
        for (int i = 0; i < n; i++) {
            suma += notas[semestre][i];
        }
        return suma / n;
    }

    // Aquí calculamos el promedio acumulado de todos los semestres.
    public double promedioAcumulado() {
        int total = 0;
        double suma = 0.0;
        for (int s = 0; s < MAX_SEMESTRES; s++) {
            for (int i = 0; i < materiasPorSemestre[s]; i++) {
                suma += notas[s][i];
                total++;
            }
        }
        if (total == 0) {
            return 0.0;
        }
        return suma / total;
    }

    // Aquí armamos el reporte de materias reprobadas (nota menor a 3.0).
    public String materiasReprobadas() {
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < MAX_SEMESTRES; s++) {
            for (int i = 0; i < materiasPorSemestre[s]; i++) {
                if (notas[s][i] < NOTA_MINIMA_APROBACION) {
                    sb.append("Semestre ").append(s + 1)
                      .append(", materia ").append(i + 1)
                      .append(": ").append(notas[s][i]).append("\n");
                }
            }
        }
        if (sb.length() == 0) {
            return "No hay materias reprobadas.";
        }
        return sb.toString();
    }

    public int getMateriasPorSemestre(int semestre) {
        return materiasPorSemestre[semestre];
    }
}
