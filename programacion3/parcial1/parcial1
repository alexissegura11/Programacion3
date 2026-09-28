import java.util.Scanner;

public class CinemaApp {

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final String[] HORARIOS = {
        "14:00 - 16:30",
        "16:30 - 19:00",
        "19:00 - 21:00"
    };

    private static final int MAX_PELICULAS = 100;

    // Representa una película registrada.
    private static class Pelicula {
        private final String nombre;
        private final String idioma;
        private final boolean es3D;
        private final int duracion;

        public Pelicula(String nombre, String idioma, boolean es3D, int duracion) {
            this.nombre = nombre;
            this.idioma = idioma;
            this.es3D = es3D;
            this.duracion = duracion;
        }

        public String getNombre() {
            return nombre;
        }

        public boolean es3D() {
            return es3D;
        }

        @Override
        public String toString() {
            String tipo = es3D ? "3D" : "35mm";
            return nombre + " | " + idioma + " | " + tipo
                    + " | " + duracion + " minutos";
        }
    }

    // Representa una función. Cada función tiene su propia matriz de sillas vendidas.
    private static class Funcion {
        private final Pelicula pelicula;
        private final boolean[][] sillasVendidas;

        public Funcion(Pelicula pelicula) {
            this.pelicula = pelicula;
            this.sillasVendidas = new boolean[8][12];
        }

        public Pelicula getPelicula() {
            return pelicula;
        }

        public boolean estaVendida(int fila, int columna) {
            return sillasVendidas[fila][columna];
        }

        public void venderSilla(int fila, int columna) {
            sillasVendidas[fila][columna] = true;
        }

        public int contarSillasDisponibles(int numeroSala) {
            int disponibles = 0;

            for (int fila = 0; fila < 8; fila++) {
                int sillasEnFila = cantidadSillasEnFila(numeroSala, fila);

                for (int columna = 0; columna < sillasEnFila; columna++) {
                    if (!sillasVendidas[fila][columna]) {
                        disponibles++;
                    }
                }
            }

            return disponibles;
        }
    }

    // Representa una sala y sus tres funciones del día.
    private static class Sala {
        private final int numero;
        private final Funcion[] funciones;

        public Sala(int numero) {
            this.numero = numero;
            this.funciones = new Funcion[3];
        }

        public int getNumero() {
            return numero;
        }

        public Funcion getFuncion(int horario) {
            return funciones[horario];
        }

        public void asignarFuncion(int horario, Pelicula pelicula) {
            funciones[horario] = new Funcion(pelicula);
        }

        public boolean acepta(Pelicula pelicula) {
            // Las salas 1 y 2 aceptan películas 35mm.
            // La sala 3 acepta solamente películas 3D.
            if (numero == 3) {
                return pelicula.es3D();
            }
            return !pelicula.es3D();
        }
    }

    private static final Sala[] SALAS = {
        new Sala(1),
        new Sala(2),
        new Sala(3)
    };

    private static final Pelicula[] PELICULAS = new Pelicula[MAX_PELICULAS];
    private static int cantidadPeliculas = 0;

    public static void main(String[] args) {
        boolean continuar = true;

        System.out.println("==================================");
        System.out.println(" SISTEMA DE SALAS DE CINE");
        System.out.println("==================================");

        while (continuar) {
            System.out.println();
            System.out.println("MENÚ PRINCIPAL");
            System.out.println("1. Crear y consultar películas");
            System.out.println("2. Asignar funciones");
            System.out.println("3. Venta de entradas");
            System.out.println("4. Salir");

            int opcion = leerEntero("Seleccione una opción: ", 1, 4);

            switch (opcion) {
                case 1:
                    menuPeliculas();
                    break;
                case 2:
                    asignarFuncion();
                    break;
                case 3:
                    menuVentas();
                    break;
                case 4:
                    continuar = false;
                    System.out.println("Aplicación finalizada.");
                    break;
                default:
                    // La validación de leerEntero impide llegar aquí.
                    break;
            }
        }

        SCANNER.close();
    }

    private static void menuPeliculas() {
        boolean volver = false;

        while (!volver) {
            System.out.println();
            System.out.println("PELÍCULAS");
            System.out.println("1. Mostrar películas");
            System.out.println("2. Registrar película");
            System.out.println("3. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ", 1, 3);

            switch (opcion) {
                case 1:
                    mostrarPeliculas();
                    break;
                case 2:
                    registrarPelicula();
                    break;
                case 3:
                    volver = true;
                    break;
                default:
                    break;
            }
        }
    }

    private static void mostrarPeliculas() {
        System.out.println();
        System.out.println("PELÍCULAS REGISTRADAS");

        if (cantidadPeliculas == 0) {
            System.out.println("Todavía no hay películas registradas.");
            return;
        }

        for (int i = 0; i < cantidadPeliculas; i++) {
            System.out.println((i + 1) + ". " + PELICULAS[i]);
        }
    }

    private static void registrarPelicula() {
        if (cantidadPeliculas >= MAX_PELICULAS) {
            System.out.println("Se alcanzó el límite de películas registradas.");
            return;
        }

        System.out.println();
        System.out.println("REGISTRAR PELÍCULA");

        String nombre = leerTexto("Nombre: ");
        String idioma = leerTexto("Idioma: ");

        System.out.println("Tipo de película:");
        System.out.println("1. 35mm");
        System.out.println("2. 3D");
        int opcionTipo = leerEntero("Seleccione el tipo: ", 1, 2);

        int duracion = leerEntero("Duración en minutos: ", 1, 1000);

        PELICULAS[cantidadPeliculas] =
                new Pelicula(nombre, idioma, opcionTipo == 2, duracion);

        cantidadPeliculas++;
        System.out.println("Película registrada correctamente.");
    }

    private static void asignarFuncion() {
        if (cantidadPeliculas == 0) {
            System.out.println("Primero debe registrar al menos una película.");
            return;
        }

        System.out.println();
        System.out.println("ASIGNAR FUNCIÓN");

        mostrarHorarios();
        int horario = leerEntero("Seleccione el horario: ", 1, 3) - 1;

        mostrarSalas();
        int numeroSala = leerEntero("Seleccione la sala: ", 1, 3);
        Sala sala = SALAS[numeroSala - 1];

        if (sala.getFuncion(horario) != null) {
            System.out.println("Esa sala ya tiene una película asignada en ese horario.");
            return;
        }

        mostrarPeliculas();

        int indicePelicula = leerEntero(
                "Seleccione el número de la película: ",
                1,
                cantidadPeliculas
        ) - 1;

        Pelicula pelicula = PELICULAS[indicePelicula];

        if (!sala.acepta(pelicula)) {
            if (numeroSala == 3) {
                System.out.println("La sala 3 solamente permite películas 3D.");
            } else {
                System.out.println("Las salas 1 y 2 solamente permiten películas 35mm.");
            }
            return;
        }

        sala.asignarFuncion(horario, pelicula);

        System.out.println("Función asignada correctamente:");
        System.out.println("Sala " + numeroSala);
        System.out.println("Horario: " + HORARIOS[horario]);
        System.out.println("Película: " + pelicula.getNombre());
    }

    private static void menuVentas() {
        boolean volver = false;

        while (!volver) {
            System.out.println();
            System.out.println("VENTA DE ENTRADAS");

            mostrarEstadoFunciones();

            System.out.println();
            System.out.println("1. Vender entradas");
            System.out.println("2. Volver al menú principal");

            int opcion = leerEntero("Seleccione una opción: ", 1, 2);

            if (opcion == 2) {
                volver = true;
            } else {
                venderEntradas();
            }
        }
    }

    private static void mostrarEstadoFunciones() {
        System.out.println();
        System.out.println("FUNCIONES DEL DÍA");

        for (int s = 0; s < SALAS.length; s++) {
            Sala sala = SALAS[s];

            for (int h = 0; h < HORARIOS.length; h++) {
                Funcion funcion = sala.getFuncion(h);

                System.out.print("Sala " + sala.getNumero()
                        + " | " + HORARIOS[h] + " | ");

                if (funcion == null) {
                    System.out.println("Sin película asignada");
                } else {
                    int disponibles =
                            funcion.contarSillasDisponibles(sala.getNumero());

                    System.out.println(funcion.getPelicula().getNombre()
                            + " | Sillas disponibles: " + disponibles);
                }
            }
        }
    }

    private static void venderEntradas() {
        mostrarSalas();
        int numeroSala = leerEntero("Seleccione la sala: ", 1, 3);
        Sala sala = SALAS[numeroSala - 1];

        mostrarHorarios();
        int horario = leerEntero("Seleccione el horario: ", 1, 3) - 1;

        Funcion funcion = sala.getFuncion(horario);

        if (funcion == null) {
            System.out.println("No hay una película asignada a esa función.");
            return;
        }

        int disponibles = funcion.contarSillasDisponibles(numeroSala);

        if (disponibles == 0) {
            System.out.println("La función está agotada.");
            return;
        }

        System.out.println();
        System.out.println("Película: " + funcion.getPelicula().getNombre());
        System.out.println("Sala: " + numeroSala);
        System.out.println("Horario: " + HORARIOS[horario]);
        System.out.println("Sillas disponibles: " + disponibles);

        mostrarPlanoSala(numeroSala, funcion);

        System.out.println();
        System.out.println("Ingrese los identificadores de silla separados por coma.");
        System.out.println("Ejemplo: A3,B8,D9");
        String seleccion = leerTexto("Sillas: ");

        String[] sillas = seleccion.split(",");
        int cantidadComprada = 0;
        int total = 0;

        for (int i = 0; i < sillas.length; i++) {
            String identificador = sillas[i].trim().toUpperCase();

            if (identificador.length() < 2) {
                System.out.println("Identificador inválido: " + identificador);
                continue;
            }

            char letraFila = identificador.charAt(0);
            int numeroSilla;

            try {
                numeroSilla = Integer.parseInt(identificador.substring(1));
            } catch (NumberFormatException e) {
                System.out.println("Identificador inválido: " + identificador);
                continue;
            }

            int fila = letraFila - 'A';

            if (fila < 0 || fila >= 8) {
                System.out.println("La fila " + letraFila + " no existe.");
                continue;
            }

            int sillasEnFila = cantidadSillasEnFila(numeroSala, fila);

            if (sillasEnFila == 0 || numeroSilla < 1 || numeroSilla > sillasEnFila) {
                System.out.println("La silla " + identificador + " no existe en esta sala.");
                continue;
            }

            int columna = numeroSilla - 1;

            if (funcion.estaVendida(fila, columna)) {
                System.out.println("La silla " + identificador + " ya está vendida.");
                continue;
            }

            funcion.venderSilla(fila, columna);
            int precio = calcularPrecio(numeroSala, fila);
            total += precio;
            cantidadComprada++;

            System.out.println("Silla " + identificador
                    + " comprada por $" + precio + ".");
        }

        if (cantidadComprada == 0) {
            System.out.println("No se compraron entradas.");
        } else {
            System.out.println();
            System.out.println("RESUMEN DE COMPRA");
            System.out.println("Entradas compradas: " + cantidadComprada);
            System.out.println("Total a pagar: $" + total);
            System.out.println("Sillas disponibles ahora: "
                    + funcion.contarSillasDisponibles(numeroSala));
        }
    }

    private static void mostrarPlanoSala(int numeroSala, Funcion funcion) {
        System.out.println();
        System.out.println("PLANO DE LA SALA");
        System.out.println("XX = vendida; número = disponible");

        for (int fila = 0; fila < 8; fila++) {
            int cantidad = cantidadSillasEnFila(numeroSala, fila);

            if (cantidad == 0) {
                continue;
            }

            char letra = (char) ('A' + fila);
            System.out.print(letra + ": ");

            for (int columna = 0; columna < cantidad; columna++) {
                if (funcion.estaVendida(fila, columna)) {
                    System.out.print(" XX ");
                } else {
                    System.out.printf("%3d ", columna + 1);
                }
            }

            System.out.println();
        }
    }

    private static int calcularPrecio(int numeroSala, int fila) {
        if (numeroSala == 3) {
            return 10000;
        }

        // En salas 1 y 2, las filas G y H son preferenciales.
        if (fila == 6 || fila == 7) {
            return 12000;
        }

        return 8000;
    }

    private static int cantidadSillasEnFila(int numeroSala, int fila) {
        if (fila < 0 || fila >= 8) {
            return 0;
        }

        // Todas las salas tienen filas A-F con 12 sillas.
        if (fila <= 5) {
            return 12;
        }

        // Las filas G-H existen solamente en las salas 1 y 2.
        if (numeroSala == 1 || numeroSala == 2) {
            return 9;
        }

        return 0;
    }

    private static void mostrarHorarios() {
        System.out.println();
        System.out.println("Horarios:");

        for (int i = 0; i < HORARIOS.length; i++) {
            System.out.println((i + 1) + ". " + HORARIOS[i]);
        }
    }

    private static void mostrarSalas() {
        System.out.println();
        System.out.println("Salas:");
        System.out.println("1. Sala 1");
        System.out.println("2. Sala 2");
        System.out.println("3. Sala 3 (solo películas 3D)");
    }

    private static String leerTexto(String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = SCANNER.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("El valor no puede estar vacío.");
            }
        } while (texto.isEmpty());

        return texto;
    }

    private static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String entrada = SCANNER.nextLine().trim();

            try {
                int numero = Integer.parseInt(entrada);

                if (numero < minimo || numero > maximo) {
                    System.out.println("Ingrese un número entre "
                            + minimo + " y " + maximo + ".");
                } else {
                    return numero;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }
}