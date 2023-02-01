import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class leer {
    static String nom = "arcleer.txt";
    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        rmComentario();
        resultado();
    }

    
    /**
     * @throws IOException
     */
    public static void rmComentario() throws IOException{
        int puntoycoma = 0;
        PrintWriter out = null;
        Scanner in = null;

        try{
            in = new Scanner(new FileReader("arcleer.txt"));
            String linea;
            out = new PrintWriter(new FileWriter("resul.txt"));
            while(in.hasNextLine()){
                linea = in.nextLine();
                
                if(linea.contains("//") || linea.contains("/*") || linea.contains("*") || linea.contains("*/")
                || linea.equals(" ") || linea.equals("")){
                    //System.out.println("comentario");
                }else{
                    
                    //System.out.println("instrucción");
                    out.println(replac(linea));

                    if(linea.contains(";")){
                        puntoycoma++;
                    }
                    
                }

            }
        }finally{
            if(out != null && in != null){
                out.close();
                in.close();
            }
        }
        System.out.println(puntoycoma);
    }

    public static void resultado() throws IOException{
        PrintWriter out = null;
        Scanner in = null;
        boolean ban = true;
        int contar = 0;

        try{
            in = new Scanner(new FileReader("resul.txt"));
            String linea;
            out = new PrintWriter(new FileWriter("final.txt"));
            while(in.hasNext()){
                linea = in.next();

                if(linea.contains("\"") && contar == 0){
                    ban = false;
                    contar ++;
                } else if(linea.contains("\"") && contar == 1){
                    ban = true;
                    contar = 0;
                }

                if(!ban){
                    out.print(linea+" ");
                }else{
                    out.println(linea);
                }

            }
        }finally{
            if(out != null && in != null){
                out.close();
                in.close();
            }
        }
    }

    /**
     * Metodo para eliminar los espacios en blanco
     * 
     * @param frase linea de texto a evaluarse
     */
    public static String replac(String frase) {
        String result = frase.replaceAll("\\.+", " . ");
        String result1 = result.replaceAll("\\;+", " ; ");
        String result2 = result1.replaceAll("\\(+", " ( ");
        String result3 = result2.replaceAll("\\=+", " = ");
        String result4 = result3.replaceAll("\\,+", " , ");
        String result5 = result4.replaceAll("\\)+", " ) ");
        String result6 = result5.replaceAll("\"+", " \" ");
        String result7 = result6.replaceAll("\\|\\|+", " || ");
        String result8 = result7.replaceAll("\\&\\&+", " && ");
        String result9 = result8.replaceAll("\\!+", " ! ");
        String result10 = result9.replaceAll("\\>+", " > ");
        String result11 = result10.replaceAll("\\<+", " < ");
        String result12 = result11.replaceAll("\\<=+", " <= ");
        String result13 = result12.replaceAll("\\>=+", " >= ");
        String result14 = result13.replaceAll("\\:+", " : ");
        String result15 = result14.replaceAll("\\}+", " } ");
        String result16 = result15.replaceAll("\\{+", " { ");
        return result16;

    }
}
