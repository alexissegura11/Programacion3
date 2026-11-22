package servicios;

import java.util.ArrayList;

/**
 * Gestor de rutas entre edificios.
 * Aquí guardamos una matriz de distancias entre edificios
 * y usamos el algoritmo de Dijkstra para encontrar la ruta más corta.
 */
public class GestorRutas {

    private String[] edificios;
    private int[][] distancias; // matriz de adyacencia; 0 = sin conexión directa

    // Aquí creamos el grafo con al menos cinco edificios.
    public GestorRutas() {
        edificios = new String[] {
            "Bloque A", "Bloque B", "Bloque C", "Biblioteca", "Auditorio", "Cafetería"
        };
        int n = edificios.length;
        distancias = new int[n][n];

        // Conectamos los edificios con distancias en metros (grafo no dirigido).
        conectar(0, 1, 120);
        conectar(0, 3, 200);
        conectar(1, 2, 150);
        conectar(1, 4, 300);
        conectar(2, 3, 100);
        conectar(2, 5, 180);
        conectar(3, 4, 90);
        conectar(4, 5, 60);
        conectar(0, 5, 350);
    }

    // Aquí conectamos dos edificios en ambas direcciones.
    private void conectar(int a, int b, int metros) {
        distancias[a][b] = metros;
        distancias[b][a] = metros;
    }

    public String[] getEdificios() {
        return edificios;
    }

    public int cantidadEdificios() {
        return edificios.length;
    }

    // Aquí está el algoritmo de Dijkstra.
    // Devolvemos el arreglo de distancias mínimas y llenamos los predecesores.
    private int[] dijkstra(int origen, int[] predecesor) {
        int n = edificios.length;
        int[] dist = new int[n];
        boolean[] visitado = new boolean[n];

        // Inicializamos todas las distancias como infinito.
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            predecesor[i] = -1;
        }
        dist[origen] = 0;

        // Luego recorremos todos los nodos.
        for (int k = 0; k < n; k++) {
            // Buscamos el nodo no visitado con la menor distancia.
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && (u == -1 || dist[i] < dist[u])) {
                    u = i;
                }
            }
            if (u == -1 || dist[u] == Integer.MAX_VALUE) {
                break;
            }
            visitado[u] = true;

            // Actualizamos las distancias de los vecinos de u.
            for (int v = 0; v < n; v++) {
                if (distancias[u][v] > 0 && !visitado[v]) {
                    int nueva = dist[u] + distancias[u][v];
                    if (nueva < dist[v]) {
                        dist[v] = nueva;
                        predecesor[v] = u;
                    }
                }
            }
        }
        return dist;
    }

    // Aquí armamos el texto con la ruta más corta entre dos edificios.
    public String rutaMasCorta(int origen, int destino) {
        int n = edificios.length;
        if (origen < 0 || origen >= n || destino < 0 || destino >= n) {
            return "Índice de edificio inválido.";
        }
        int[] predecesor = new int[n];
        int[] dist = dijkstra(origen, predecesor);

        if (dist[destino] == Integer.MAX_VALUE) {
            return "No hay ruta entre esos edificios.";
        }

        // Reconstruimos el camino de atrás hacia adelante.
        ArrayList<String> camino = new ArrayList<>();
        int actual = destino;
        while (actual != -1) {
            camino.add(edificios[actual]);
            if (actual == origen) {
                break;
            }
            actual = predecesor[actual];
        }

        // Le damos la vuelta al camino para mostrarlo en orden.
        StringBuilder ruta = new StringBuilder();
        for (int i = camino.size() - 1; i >= 0; i--) {
            if (i < camino.size() - 1) {
                ruta.append(" -> ");
            }
            ruta.append(camino.get(i));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Origen: ").append(edificios[origen]).append("\n");
        sb.append("Destino: ").append(edificios[destino]).append("\n");
        sb.append("Camino: ").append(ruta).append("\n");
        sb.append("Distancia total: ").append(dist[destino]).append(" metros");
        return sb.toString();
    }

    // Aquí armamos la lista de edificios con su número.
    public String listarEdificios() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < edificios.length; i++) {
            sb.append("  ").append(i).append(". ").append(edificios[i]).append("\n");
        }
        return sb.toString();
    }
}
