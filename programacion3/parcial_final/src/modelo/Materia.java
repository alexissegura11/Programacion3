package modelo;

import estructuras.Cola;
import estructuras.ListaEnlazada;
import excepciones.ColaDeEsperaVaciaException;

/**
 * Clase Materia.
 * Aquí guardamos el código, el nombre, los cupos, los créditos,
 * los prerrequisitos (lista enlazada), los inscritos y la cola de espera.
 */
public class Materia {

    private String codigo;
    private String nombre;
    private int cuposMaximos;
    private int creditos;
    private ListaEnlazada<String> prerrequisitos; // códigos de materias previas
    private ListaEnlazada<Integer> inscritos;     // IDs de estudiantes inscritos
    private Cola<Integer> colaDeEspera;           // IDs en espera de cupo

    // Aquí creamos una materia con sus datos básicos.
    public Materia(String codigo, String nombre, int cuposMaximos, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuposMaximos = cuposMaximos;
        this.creditos = creditos;
        this.prerrequisitos = new ListaEnlazada<>();
        this.inscritos = new ListaEnlazada<>();
        this.colaDeEspera = new Cola<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCuposMaximos() {
        return cuposMaximos;
    }

    public int getCreditos() {
        return creditos;
    }

    public ListaEnlazada<String> getPrerrequisitos() {
        return prerrequisitos;
    }

    // Aquí agregamos un prerrequisito a la lista enlazada.
    public void agregarPrerrequisito(String codigo) {
        if (!prerrequisitos.contiene(codigo)) {
            prerrequisitos.agregarFinal(codigo);
        }
    }

    // Aquí verificamos si la materia ya está llena.
    public boolean estaLlena() {
        return inscritos.tamanio() >= cuposMaximos;
    }

    public int cuposDisponibles() {
        return cuposMaximos - inscritos.tamanio();
    }

    public void agregarInscrito(int id) {
        if (!inscritos.contiene(id)) {
            inscritos.agregarFinal(id);
        }
    }

    public void quitarInscrito(int id) {
        inscritos.eliminar(id);
    }

    public boolean estaInscrito(int id) {
        return inscritos.contiene(id);
    }

    public int cantidadInscritos() {
        return inscritos.tamanio();
    }

    // Aquí encolamos a un estudiante en la cola de espera.
    public void encolarEspera(int id) {
        colaDeEspera.encolar(id);
    }

    public boolean estaEnCola(int id) {
        return colaDeEspera.contiene(id);
    }

    // Aquí sacamos al primer estudiante de la cola de espera.
    public Integer desencolarEspera() throws ColaDeEsperaVaciaException {
        if (colaDeEspera.estaVacia()) {
            throw new ColaDeEsperaVaciaException(
                    "La cola de espera de " + codigo + " está vacía.");
        }
        return colaDeEspera.desencolar();
    }

    public Cola<Integer> getColaDeEspera() {
        return colaDeEspera;
    }

    public boolean colaVacia() {
        return colaDeEspera.estaVacia();
    }

    public Integer verFrenteCola() {
        return colaDeEspera.verFrente();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " | Cupos: " + cuposDisponibles()
                + "/" + cuposMaximos + " | Créditos: " + creditos
                + " | Prerrequisitos: " + prerrequisitos
                + " | En cola: " + colaDeEspera.tamanio();
    }
}
