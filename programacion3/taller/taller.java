
import java.util.random.*;


//arreglos

public class taller {
    public static void main(String[] args) {
        
        //declaracoin y creacion
        int[] numeros = {7, 8, 9, 10, 11, 12, 13};
        int posici = 9;

        int resultado;

        if (posici >= 0 && posici < numeros.length) {
          resultado = numeros[posici];
        }else {
            random random = new Random();
            resultado = random.nextInt(30) +1;
            System.out.println("el numero aleatorio es: " + resultado);
        }
        System.out.println("el valor obtenido es: " + resultado);




        numeros[0] = 7; 
        numeros[1] = 8;
        numeros[2] = 9;
        numeros[3] = 10;
        numeros[4] = 11;
        numeros[5] = 12;
        numeros[6] = 13;
    

        System.out.println(numeros[0]);
        System.out.println(numeros[1]);
        System.out.println(numeros[2]);
        System.out.println(numeros[3]);
        System.out.println(numeros[4]);
        System.out.println(numeros[5]);
        System.out.println(numeros[6]);
       

    }
}
