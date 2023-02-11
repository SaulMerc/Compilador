package Analizadores;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {
    hmap to = new hmap();

    /**
     * Método para eliminar los comentarios del código fuente
     * 
     * @throws IOException
     */
    public void rmComentario() throws IOException {
        PrintWriter out = null;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("arcleer.txt"));
            String linea;
            // crear el txt result
            out = new PrintWriter(new FileWriter("resul.txt"));
            while (in.hasNextLine()) {
                linea = in.nextLine();

                if (linea.contains("//") || linea.contains("/*") || linea.contains("*") || linea.contains("*/")
                        || linea.equals(" ") || linea.equals("")) {
                } else {

                    out.println(replac(linea));
                }

            }
        } finally {
            if (out != null && in != null) {
                out.close();
                in.close();
            }
        }
    }

    /**
     * Método para analizar analizar las instruciones
     * 
     * @throws IOException
     */
    public void formato() throws IOException {
        PrintWriter out = null;
        Scanner in = null;
        boolean ban = true;
        int contar = 0;
        to.lectura("P-reservada.txt");

        try {
            in = new Scanner(new FileReader("resul.txt"));
            String linea;
            out = new PrintWriter(new FileWriter("formato.txt"));
            // Leer el txt result
            while (in.hasNext()) {
                linea = in.next();

                if (linea.contains("\"") && contar == 0) {
                    ban = false;
                    contar++;
                } else if (linea.contains("\"") && contar == 1) {
                    ban = true;
                    contar = 0;
                }

                if (!ban) {
                    out.print(linea + " ");
                } else {
                    out.println(linea);
                }
            }
        } finally {
            if (out != null && in != null) {
                out.close();
                in.close();
                eliminar("resul.txt");
            }
        }
    }

    /**
     * Método para analizar analizar las instruciones
     * 
     * @throws IOException
     */
    public void resultado() throws IOException {
        //Variables inicializadas
        PrintWriter out = null;
        Scanner in = null;
        to.lectura("P-reservada.txt");

        try {
            in = new Scanner(new FileReader("formato.txt"));
            String linea;
            out = new PrintWriter(new FileWriter("final.txt"));

            // Leer el txt result
            while (in.hasNext()) {
                linea = in.nextLine();

                // comparar con hashmap
                if (to.vals.containsKey(linea)) {
                    int op = to.vals.get(linea);

                    switch (op) {
                        case 1:
                            out.println(linea + "   |Palabra reservada|");
                            break;
                        case 2:
                            out.println(linea + "   |Operadores Aritméticos|");
                            break;
                        case 3:
                            out.println(linea + "   |Operadores lógicos|");
                            break;
                        case 4:
                            out.println(linea + "   |Tokens|");
                            break;

                        default:
                            break;
                    }
                } else {
                    // Expresiones para identificar palabras
                    Pattern identificador;
                    Matcher buscador1;

                    Pattern enteros;
                    Matcher buscador2;

                    Pattern flotantes;
                    Matcher buscador3;

                    Pattern literales;
                    Matcher buscador4;

                    identificador = Pattern.compile("^[a-zA-ZñÑ][\\wñÑ]*$");
                    buscador1 = identificador.matcher(linea);
                    boolean encontrado = buscador1.find();

                    enteros = Pattern.compile("^\\-?\\d*$");
                    buscador2 = enteros.matcher(linea);
                    boolean encontrado2 = buscador2.find();

                    flotantes = Pattern.compile("^\\d*.\\d*$");
                    buscador3 = flotantes.matcher(linea);
                    boolean encontrado3 = buscador3.find();

                    literales = Pattern.compile("^\".*\"$");
                    buscador4 = literales.matcher(linea);
                    boolean encontrado4 = buscador4.find();

                    if(encontrado){
                        out.println(linea + "   |Identificador|");
                    }else if(encontrado2){
                        out.println(linea + "   |Entero|");
                    }else if(encontrado3){
                        out.println(linea + "   |flotante|");
                    }else if(encontrado4){
                        out.println(linea + "   |Literal|");
                        
                    }else{
                        out.println(linea + "   |Error de sintaxis|");
                    }
                }

            }
        } finally {
            if (out != null && in != null) {
                out.close();
                in.close();
                eliminar("formato.txt");
            }
        }
    }

    /**
     * Metodo para separar los tokens
     * 
     * @param frase linea de texto a evaluarse
     */
    public String replac(String frase) {
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
        String result17 = result16.replaceAll("\\[+", " [ ");
        String result18 = result17.replaceAll("\\]+", " ] ");
        String result19 = result18.replaceAll("\\++", " + ");
        String result20 = result19.replaceAll("\\-+", " - ");
        String result21 = result20.replaceAll("\\*+", " * ");
        String result22 = result21.replaceAll("\\/+", " / ");
        return result22;
    }

    /**
    *   Metodo para eliminar los archivos
     */
    private void eliminar(String nomarch){
        File arc = new File(nomarch);

        if (arc.exists()) {
            arc.delete();
        }
    }
}
