package programacion3.taller9;
import java.util.ArrayList;
import java.util.List;

public class listtaller {

    // Aqui vamos a crear una lista donde guardaremos las compras
        List<String> compras = new ArrayList<>();

        // Ahora agregamos los productos que necesitamos comprar
        compras.add("Leche");
        compras.add("Pan");
        compras.add("Huevos");
        compras.add("Arroz");

        // Aqui mostramos un mensaje para saber que viene la lista
        System.out.println("Lista de compras:");

        // Luego recorremos la lista para mostrar cada producto
        for (String producto : compras) {
            System.out.println(producto);
        }

        // Ahora quitamos el pan de la lista porque ya no lo necesitamos
        compras.remove("Pan");

        // Aqui contamos cuantos productos quedan en la lista
        System.out.println("Cantidad de productos: "
                + compras.size());

        // Finalmente mostramos como quedo la lista despues de quitar el pan
        System.out.println("Lista actualizada: " + compras);
    }
}