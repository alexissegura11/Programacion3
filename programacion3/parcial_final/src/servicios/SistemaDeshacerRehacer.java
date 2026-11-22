package servicios;

import estructuras.Pila;
import excepciones.PilaDeshacerVaciaException;

/**
 * Sistema de deshacer y rehacer.
 * Aquí guardamos dos pilas: una con las acciones que se pueden deshacer
 * y otra con las que se pueden rehacer.
 */
public class SistemaDeshacerRehacer {

    private Pila<Accion> pilaDeshacer;
    private Pila<Accion> pilaRehacer;

    // Aquí creamos el sistema con las dos pilas vacías.
    public SistemaDeshacerRehacer() {
        pilaDeshacer = new Pila<>();
        pilaRehacer = new Pila<>();
    }

    // Aquí guardamos una acción nueva en la pila de deshacer.
    // Al hacer algo nuevo, limpiamos la pila de rehacer.
    public void registrar(Accion accion) {
        pilaDeshacer.push(accion);
        pilaRehacer = new Pila<>();
    }

    // Aquí deshacemos la última acción guardada.
    public void deshacer() throws PilaDeshacerVaciaException {
        if (pilaDeshacer.estaVacia()) {
            throw new PilaDeshacerVaciaException("No hay operaciones para deshacer.");
        }
        Accion accion = pilaDeshacer.pop();
        accion.deshacer();
        pilaRehacer.push(accion);
    }

    // Aquí rehacemos la última acción deshecha.
    public void rehacer() throws PilaDeshacerVaciaException {
        if (pilaRehacer.estaVacia()) {
            throw new PilaDeshacerVaciaException("No hay operaciones para rehacer.");
        }
        Accion accion = pilaRehacer.pop();
        accion.rehacer();
        pilaDeshacer.push(accion);
    }

    public String verTopeDeshacer() {
        Accion a = pilaDeshacer.verTope();
        return a == null ? null : a.getDescripcion();
    }

    public String verTopeRehacer() {
        Accion a = pilaRehacer.verTope();
        return a == null ? null : a.getDescripcion();
    }

    public int tamanioDeshacer() {
        return pilaDeshacer.tamanio();
    }

    public int tamanioRehacer() {
        return pilaRehacer.tamanio();
    }
}
