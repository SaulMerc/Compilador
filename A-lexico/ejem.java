import java . io . IOException ;
import java . util . Scanner ;

public class ejem {
    public static leer read = new leer() ;
    /**
     * @param args
     * @throws IOException
     */
    public static void main (String[]args) throws IOException { 
        //Variables que se utilizarán para realizar la suma
        int a , b ;
        //Scaner que servirá para leer la entrada del teclado
        Scanner in = new Scanner (System.in) ;
        //Mensaje al usuario
        System . out . print ("Ingrese el primer número: " ) ;
        //Guardar la entrada del teclado en la variable a
        a = in . nextInt() ;
        //Salto de línea
        System . out . println() ;

        System . out . print ("Ingrese el segundo número: ") ;
        b = in . nextInt() ;
        System . out . println() ;

        /*
         * Esta parte del código imprimirá en la consola
         * el resultado de la suma de las variables
         */
        System . out . println ("El resultado es: " + (a+b)) ;

        switch (a) {
            case 2:
                System.out.println("Ingresaste un 2");
                break;
        
            default:
                break;
        }

        if(a<10){
            a=0;
        }else if(a>10){
            a=9;
        }else{
            a=-1;
        }

    }

}