# Sistema de Gestión Universitaria (Java)

Proyecto académico de **Programación 3** — estructuras de datos con Java.
Implementa un sistema completo de gestión universitaria aplicando programación
orientada a objetos (herencia, encapsulamiento, abstracción y polimorfismo) y
estructuras de datos implementadas a mano (listas enlazadas, pilas, colas, HashMap,
arreglos y matrices nativas).

---

## 1. Cómo ejecutarlo

Necesitas un JDK (Java 8 o superior). Desde la carpeta `parcial_final`:

```bash
# Compilar todo el proyecto (crea la carpeta build/)
javac -encoding UTF-8 -d build src/estructuras/*.java src/excepciones/*.java src/modelo/*.java src/servicios/*.java src/utilidades/*.java src/principal/*.java src/pruebas/*.java

# Ejecutar el programa (menú interactivo)
java -cp build principal.Main

# Ejecutar las pruebas automáticas
java -cp build pruebas.PruebasSistema
```

> En este equipo el JDK está en `C:\Program Files\Java\jdk1.8.0_111\bin`.
> Si `javac` no está en el PATH, usa la ruta completa de ese `javac.exe` y `java.exe`.

---

## 2. Estructura del proyecto

```
parcial_final/
├── src/
│   ├── estructuras/        → estructuras de datos genéricas
│   │   ├── Nodo.java
│   │   ├── ListaEnlazada.java
│   │   ├── Pila.java
│   │   └── Cola.java
│   ├── excepciones/        → 7 excepciones personalizadas
│   │   ├── PreRequisitoNoAprobadoException.java
│   │   ├── HorarioConflictivoException.java
│   │   ├── CupoLlenoException.java
│   │   ├── EstudianteNoEncontradoException.java
│   │   ├── ColaDeEsperaVaciaException.java
│   │   ├── PilaDeshacerVaciaException.java
│   │   └── ArchivoInvalidoException.java
│   ├── modelo/             → clases del dominio (POO)
│   │   ├── Persona.java            (abstracta)
│   │   ├── Estudiante.java
│   │   ├── Docente.java
│   │   ├── HistorialAcademico.java (double[10][20])
│   │   ├── Materia.java
│   │   └── Aula.java               (boolean[7][24])
│   ├── servicios/          → lógica de negocio
│   │   ├── Accion.java             (comando deshacer/rehacer)
│   │   ├── SistemaDeshacerRehacer.java
│   │   ├── GestorEstudiantes.java
│   │   ├── GestorDocentes.java
│   │   ├── RegistroPersonas.java   (demuestra polimorfismo)
│   │   ├── GestorMaterias.java
│   │   ├── GestorHorarios.java
│   │   ├── GestorNotas.java
│   │   ├── GestorRutas.java        (Dijkstra)
│   │   └── ProcesadorInscripciones.java (lotes CSV)
│   ├── utilidades/
│   │   └── Consola.java
│   ├── principal/
│   │   ├── Main.java
│   │   └── MenuPrincipal.java
│   └── pruebas/
│       └── PruebasSistema.java
├── datos/
│   └── inscripciones.csv   → ejemplo de archivo por lotes
└── README.md
```

---

## 3. Archivo CSV de ejemplo (procesamiento por lotes)

Cada línea tiene el formato `idEstudiante,codigoMateria` (sin espacios):

```
101,MAT101
102,MAT101
101,MAT201
103,MAT101
104,INF101
```

Para probarlo: crea estudiantes con IDs 101-104, crea las materias `MAT101`,
`MAT201` (con prerrequisito `MAT101`) e `INF101`, y desde el menú **7.
Inscripciones por lotes (CSV)** escribe la ruta completa del archivo
(por ejemplo `datos/inscripciones.csv`). El sistema valida el archivo, encola
todas las solicitudes y las procesa en orden, mostrando un resumen de exitosas,
en cola de espera y fallidas.

---

## 4. Explicación de cada clase

### estructuras
- **Nodo**: guarda un dato y la referencia al siguiente nodo. Es la pieza base de
  la lista enlazada.
- **ListaEnlazada**: lista simple genérica (agregar al inicio/final, eliminar,
  buscar, recorrer). Se usa para prerrequisitos, inscritos y materias del estudiante.
- **Pila**: LIFO. Se usa en el sistema deshacer/rehacer y para los reportes consultados.
- **Cola**: FIFO. Se usa para la cola de espera de las materias y para el lote CSV.

### excepciones
Siete excepciones propias que se lanzan y se manejan en su situación correspondiente:
prerrequisito no aprobado, conflicto de horario, cupo lleno, estudiante no encontrado,
cola de espera vacía, pila deshacer vacía y archivo inválido.

### modelo
- **Persona** (abstracta): atributos comunes ID, nombre y correo. Declara el método
  abstracto `obtenerRol()`.
- **Estudiante** (hereda de Persona): semestre actual, historial académico, materias
  inscritas y materias aprobadas.
- **Docente** (hereda de Persona): especialidad y materias asignadas.
- **HistorialAcademico**: guarda las notas en un arreglo `double[10][20]` (10 semestres
  × 20 materias) y calcula promedios y materias reprobadas (nota < 3.0).
- **Materia**: código, nombre, cupos, créditos, prerrequisitos (lista enlazada),
  inscritos y cola de espera.
- **Aula**: matriz `boolean[7][24]` (7 días × 24 horas) para reservar y liberar franjas.

### servicios
- **Accion**: representa una operación que sabe deshacerse y rehacerse.
- **SistemaDeshacerRehacer**: dos pilas (`pilaDeshacer` y `pilaRehacer`) con los métodos
  `deshacer()` y `rehacer()`.
- **GestorEstudiantes**: registra, busca (HashMap), elimina y lista estudiantes, y
  muestra el historial.
- **GestorDocentes**: registra docentes y les asigna materias.
- **RegistroPersonas**: guarda estudiantes y docentes juntos como `Persona` para
  demostrar el polimorfismo.
- **GestorMaterias**: crea materias, agrega prerrequisitos, inscribe, cancela, maneja
  la cola de espera y libera cupos.
- **GestorHorarios**: reserva, libera, cambia y consulta horarios de las aulas.
- **GestorNotas**: registra notas, calcula promedios, genera reportes y guarda los
  últimos reportes en una pila (opción "Atrás").
- **GestorRutas**: grafo de edificios (no dirigido) con el algoritmo de Dijkstra.
- **ProcesadorInscripciones**: lee un CSV, valida, encola y procesa las solicitudes.

### utilidades
- **Consola**: títulos, separadores, mensajes de éxito/error y lectura de datos con
  validación.

### principal
- **Main**: punto de entrada. Arranca el menú.
- **MenuPrincipal**: menú principal y todos los submenús, con manejo de excepciones.

---

## 5. Cómo se aplicó la POO

- **Abstracción**: `Persona` es abstracta; define qué tienen en común estudiantes y
  docentes y obliga a implementar `obtenerRol()`.
- **Herencia**: `Estudiante` y `Docente` usan `extends Persona` y aprovechan sus
  atributos y métodos con `super(...)`.
- **Encapsulamiento**: todos los atributos son `private` y se acceden con getters y setters.
- **Polimorfismo**:
  1. Sobrescritura con `@Override` (`obtenerRol()` devuelve "Estudiante" o "Docente",
     y `toString()` cambia en cada clase).
  2. `RegistroPersonas` guarda referencias de tipo `Persona` y, al llamar
     `p.obtenerRol()`, cada objeto responde con su propio comportamiento.
  3. La clase abstracta `Accion` se implementa con clases anónimas distintas en cada
     operación (cada una con su propio `deshacer()` y `rehacer()`).

---

## 6. Estructuras de datos usadas

| Estructura | Dónde se usa |
|---|---|
| Lista enlazada | prerrequisitos de materia, inscritos, materias del estudiante/docente |
| Pila | deshacer/rehacer, últimos reportes consultados |
| Cola | cola de espera de materias, solicitudes del lote CSV |
| HashMap | búsqueda de estudiantes por ID, materias por código, aulas por nombre |
| Arreglo nativo `double[10][20]` | notas del historial académico |
| Matriz nativa `boolean[7][24]` | ocupación de cada aula |
| Matriz de adyacencia `int[][]` | distancias entre edificios (Dijkstra) |

---

## 7. Pruebas realizadas

`PruebasSistema` ejecuta **43 verificaciones automáticas** que cubren:

1. Registro, búsqueda por ID (HashMap) y `EstudianteNoEncontradoException`.
2. Eliminación de estudiante con deshacer y rehacer.
3. Prerrequisitos y `PreRequisitoNoAprobadoException`.
4. `CupoLlenoException`, cola de espera y liberación de cupo.
5. `ColaDeEsperaVaciaException`.
6. Reserva/liberación de horarios, `HorarioConflictivoException` y disponibilidad.
7. Registro de notas, promedio de semestre, promedio acumulado y reprobadas.
8. Ruta más corta con Dijkstra (origen, destino, camino y distancia).
9. Procesamiento por lotes CSV (exitosas, en espera, fallidas) y `ArchivoInvalidoException`.
10. `PilaDeshacerVaciaException` al deshacer/rehacer con las pilas vacías.
11. Deshacer y rehacer de las 5 operaciones obligatorias (inscribir, cancelar
    inscripción, cambiar horario, registrar nota, eliminar estudiante).

Resultado: **43/43 pruebas pasaron, 0 fallaron.**
