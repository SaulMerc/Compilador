package Analizadores;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tools {
    hmap to = new hmap();
    Tree arbol = new Tree();
    ArrayList<String> pila = new ArrayList<String>();
    ArrayList<String> cadena = new ArrayList<String>();
    ArrayList<String> arbolito = new ArrayList<String>();

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
     * Método para separar los tokens por palabras y juntar las literales
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
     * y agregar a la pila la cadena que se va a analizar
     * 
     * @throws IOException
     */
    public void resultado() throws IOException {
        // Variables inicializadas
        Scanner in = null;
        to.lectura("P-reservada.txt");

        try {
            in = new Scanner(new FileReader("formato.txt"));
            String linea;

            // Leer el txt result
            while (in.hasNext()) {
                linea = in.nextLine();

                // comparar con hashmap
                if (to.vals.containsKey(linea)) {
                    int op = to.vals.get(linea);

                    cadena.add(Integer.toString(op));

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

                    if (encontrado) {
                        cadena.add("0");
                    } else if (encontrado2) {
                        cadena.add("1");
                    } else if (encontrado3) {
                        cadena.add("2");
                    } else if (encontrado4) {
                        cadena.add("3");
                    } else {
                        System.out.println(linea + "|Error de sintaxis|");
                    }
                }
            }
            cadena.add("23");
            pila.add("0");
        } finally {
            if (in != null) {
                in.close();
                eliminar("formato.txt");
            }
        }
    }

    /**
     * Método para recorrer en la pila
     */
    public void recorrerPila() throws FileNotFoundException {
        String ver = "";
        boolean accept = false;

        while (!accept) {
            ver = buscarReglas(ver);
            // contiene d - d12 r - 13 producciones(ver); - return q p
            // .split(d)

            if (ver.contains("d")) {
                // agregar a la pila
                String[] arr = ver.split("d");

                pila.add(cadena.get(0));
                pila.add(arr[1]);

                if (!cadena.get(0).equals("23")) {
                    arbolito.add(cadena.get(0));
                    cadena.remove(0);
                }

            } else if (ver.contains("r")) {
                // Realizar busqueda de los valores a quitar y el valora a agregar

                if (ver.equals("r0")) {
                    accept = true;
                    System.out.println("La cadena ha sido aceptada");
                    System.out.println(arbolito.toString());
                } else {
                    Scanner in = null;
                    boolean salir = false;
                    try {
                        in = new Scanner(new FileReader("prod.txt"));
                        String regla;
                        String quitar;
                        String poner;

                        while (in.hasNext() && !salir) {
                            regla = in.next();
                            quitar = in.next();
                            poner = in.next();

                            if (ver.equals(regla)) {
                                int i = 0;
                                while (i < Integer.parseInt(quitar)) {
                                    pila.remove(pila.size() - 1);
                                    i++;
                                }
                                pila.add(poner);
                                arbolito.add(poner);
                                salir = true;
                            }
                        }
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                }

            } else if (ver.equals("n")) {
                System.out.println("Error en la cadena");
                accept = true;
            } else {
                pila.add(ver);
            }
        }
        arbolote();
    }

    /**
     * Metodo para buscar en las reglas
     * 
     * @throws FileNotFoundException
     * 
     */
    public String buscarReglas(String ver) throws FileNotFoundException {
        PrintWriter out = null;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("reglas.txt"));
            String pil;
            String cad;
            String rsl = "n";
            // leer los valores y guardarlos en variables

            // Despues de insertar un valor de una regla, busca los ultimos valores en la
            // pila
            if (ver.contains("r")) {
                while (in.hasNext()) {
                    pil = in.next();
                    cad = in.next();
                    rsl = in.next();

                    if (pil.equals(pila.get(pila.size() - 2)) && cad.equals(pila.get(pila.size() - 1))) {
                        // System.out.println(rsl);
                        System.out.print(pila.toString());
                        System.out.print(" ");
                        System.out.print(cadena.toString());
                        System.out.print(" ");
                        System.out.println(rsl);
                        return rsl;
                    }
                }
                // Si el siguiente valor de la cadena es un vacio se lo salta
            } else {
                // En los desplazamientos solo se buscará en la primera posición de la cadena y
                // la última de la pila
                while (in.hasNext()) {
                    pil = in.next();
                    cad = in.next();
                    rsl = in.next();

                    if (pil.equals(pila.get(pila.size() - 1)) && cad.equals(cadena.get(0))) {
                        System.out.print(pila.toString());
                        System.out.print(" ");
                        System.out.print(cadena.toString());
                        System.out.print(" ");
                        System.out.println(rsl);

                        // System.out.println(rsl);
                        return rsl;
                    } else {
                        rsl = "n";
                    }
                }
            }
            return rsl;

        } finally {
            if (out != null && in != null) {
                out.close();
                in.close();
            }
        }
    }

    /**
     * Metodo para separar los tokens
     * 
     * @param frase linea de texto a evaluarse
     */
    public String replac(String frase) {
        String result = frase.replaceAll("==", " @@ ");
        String result1 = result.replaceAll("<=", " %% ");
        String result2 = result1.replaceAll(">=", " ## ");
        String result3 = result2.replaceAll("\\.", " . ");
        String result4 = result3.replaceAll("\\;", " ; ");
        String result5 = result4.replaceAll("\\(", " ( ");
        String result6 = result5.replaceAll("\\=", " = ");
        String result7 = result6.replaceAll("\\,", " , ");
        String result8 = result7.replaceAll("\\)", " ) ");
        String result9 = result8.replaceAll("\\|\\|", " || ");
        String result10 = result9.replaceAll("\\&\\&", " && ");
        String result11 = result10.replaceAll("\\!", " ! ");
        String result12 = result11.replaceAll("\\>", " > ");
        String result13 = result12.replaceAll("\\<", " < ");
        String result14 = result13.replaceAll("\\:", " : ");
        String result15 = result14.replaceAll("\\}", " } ");
        String result16 = result15.replaceAll("\\{", " { ");
        String result17 = result16.replaceAll("\\[", " [ ");
        String result18 = result17.replaceAll("\\]", " ] ");
        String result19 = result18.replaceAll("\\+", " + ");
        String result20 = result19.replaceAll("\\-", " - ");
        String result21 = result20.replaceAll("\\*", " * ");
        String result22 = result21.replaceAll("/", " / ");
        String result23 = result22.replaceAll("\"", " \" ");
        String result24 = result23.replaceAll("\\@\\@", " == ");   
        String result25 = result24.replaceAll("\\%\\%", " <= ");
        String result26 = result25.replaceAll("\\#\\#", " >= ");
        return result26;
    }

    /**
     * Metodo para eliminar los archivos
     */
    private void eliminar(String nomarch) {
        File arc = new File(nomarch);

        if (arc.exists()) {
            arc.delete();
        }
    }
    public void arbolote() {
        int i = arbolito.size()-1;
        //El padre toma el valor de la ultima posicion
        int padre = Integer.parseInt(arbolito.get(arbolito.size()-1));
        System.out.println("Padre padre we:"+padre);
        while ( i >= 0) {
            if(Integer.parseInt(arbolito.get(i)) <= 23){
                //Creo variable hijo
                int hijo = Integer.parseInt(arbolito.get(i)); 
                //La añado al padre que no ha cambiado
                arbol.addNode(hijo, padre);
                System.out.println("Hijo:"+hijo);
            }else{
                //Se crea el ultimo nodo hijo
                arbol.addNode(Integer.parseInt(arbolito.get(i)), padre);
                //El valor de la posicion lo hago padre
                padre = Integer.parseInt(arbolito.get(i));
                System.out.println("Padre:"+padre);
            }
            i--;
        }
        // Prueba con otras impresiones
        arbol.preOrderTraversal(arbol.root);
        
    }
}
