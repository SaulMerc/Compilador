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
    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> tipos = new ArrayList<String>();
    ArrayList<String> cadenaCopia;
    int contador = -1;

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
        boolean uso = false, tipoI = false, tipoS = false, tipoB = false;
        String usoAux = "";
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
                    if (linea.equals("int")) {
                        tipoI = true;
                    } else if (linea.equals("String")) {
                        tipoS = true;
                    } else if (linea.equals("float")) {
                        tipoB = true;
                    } else if (linea.equals(";") || linea.equals(")")) {
                        tipoI = false;
                        tipoS = false;
                        tipoB = false;
                        uso = false;
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

                    if (encontrado) {// identificador
                        cadena.add("0");

                        if (tipoI) {
                            if (existe(linea)) {
                                System.out.println(linea + "|Error, el identificador ya ha sido declarado|");
                            } else {
                                tipos.add("int");
                                ids.add(linea);
                            }
                        } else if (tipoS) {
                            if (existe(linea)) {
                                System.out.println(linea + " |Error, el identificador ya ha sido declarado|");
                            } else {
                                tipos.add("String");
                                ids.add(linea);
                            }
                        } else if (tipoB) {
                            if (existe(linea)) {
                                System.out.println(linea + " |Error, el identificador ya ha sido declarado|");
                            } else {
                                tipos.add("float");
                                ids.add(linea);
                            }
                        }
                        // uso
                        else {
                            if (existe(linea)) {
                                uso = true;
                                usoAux = linea;
                            } else {
                                System.out.println(linea + " |Error, el identificador no ha sido declarado|");
                            }

                        }

                    } else if (encontrado2) {// entero
                        cadena.add("1");
                        if (uso && !valTipo("int", usoAux)) {
                            System.out.println(linea + " |Error, el valor no coincide con el tipo de la variable|");
                        }
                    } else if (encontrado3) {// Flotantes
                        cadena.add("2");
                        if (uso && !valTipo("float", usoAux)) {
                            System.out.println(linea + " |Error, el valor no coincide con el tipo de la variable|");
                        }
                    } else if (encontrado4) {// Literales
                        cadena.add("3");
                        if (uso && !valTipo("String", usoAux)) {
                            System.out.println(linea + " |Error, el valor no coincide con el tipo de la variable|");
                        }
                    } else {
                        System.out.println(linea + "|Error de sintaxis|");
                    }
                }
            }
            cadena.add("23");
            cadenaCopia = new ArrayList<>(cadena);
            pila.add("0");
            // System.out.println(ids.toString());
            // System.out.println(tipos.toString());
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
                    contador++;
                }

            } else if (ver.contains("r")) {
                // Realizar busqueda de los valores a quitar y el valora a agregar

                if (ver.equals("r0")) {
                    accept = true;
                    System.out.println("La cadena ha sido aceptada");
                    // System.out.println(arbolito.toString());
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
                System.out.println("Error -------" + (contador + 1));
                if (Errores(contador)) {
                    contador++;
                }
                // accept = true;
            } else {
                pila.add(ver);
            }
        }
        // arbolote();
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
                        // System.out.print(pila.toString());
                        // System.out.print(" ");
                        // System.out.print(cadena.toString());
                        // System.out.print(" ");
                        // System.out.println(rsl);
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
                        // System.out.print(pila.toString());
                        // System.out.print(" ");
                        // System.out.print(cadena.toString());
                        // System.out.print(" ");
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
        int i = arbolito.size() - 1;
        // El padre toma el valor de la ultima posicion
        int padre = Integer.parseInt(arbolito.get(arbolito.size() - 1));
        System.out.println("Padre padre we:" + padre);
        while (i >= 0) {
            if (Integer.parseInt(arbolito.get(i)) <= 23) {
                // Creo variable hijo
                int hijo = Integer.parseInt(arbolito.get(i));
                // La añado al padre que no ha cambiado
                arbol.addNode(hijo, padre);
                System.out.println("Hijo:" + hijo);
            } else {
                // Se crea el ultimo nodo hijo
                arbol.addNode(Integer.parseInt(arbolito.get(i)), padre);
                // El valor de la posicion lo hago padre
                padre = Integer.parseInt(arbolito.get(i));
                System.out.println("Padre:" + padre);
            }
            i--;
        }
        // Prueba con otras impresiones
        arbol.preOrderTraversal(arbol.root);

    }

    /**
     * Metodo para arreglar los errores
     */
    public boolean Errores(int contador) {
        boolean cadR = false;
        if (contador == -1) {
            cadena.add(0, "4");
            System.out.println("Faltó un tipo");
        } else {
            switch (cadena.get(0)) {
                case "12":
                    if (cadenaCopia.get(contador).equals("12")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un ; de más");
                    } else {
                        cadena.add(0, "0");
                        System.out.println("Faltó un identificador");
                    }
                    break;
                case "19":
                    if (cadenaCopia.get(contador).equals("0")) {
                        cadena.add(0, "12");
                        System.out.println("Faltó un ;");
                    } else if (cadenaCopia.get(contador).equals("19")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un if de más");
                    } else if (cadenaCopia.get(contador).equals("1")) {
                        cadena.add(0, "12");
                        System.out.println("Faltó un ;");
                    } else if (cadenaCopia.get(contador).equals("4")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un tipo de más");
                    } else {
                        cadena.add(0, "16");
                        System.out.println("Faltó un {");
                    }
                    break;
                case "17":
                case "4":

                    break;
                case "20":
                    if (cadenaCopia.get(contador).equals("0")) {
                        cadena.add(0, "12");
                        System.out.println("Faltó un ;");
                    } else if (cadenaCopia.get(contador).equals("1")) {
                        cadena.add(0, "12");
                        System.out.println("Faltó un ;");
                    } else {
                        cadena.add(0, "16");
                        System.out.println("Faltó un {");
                    }
                    break;
                case "22":

                    cadena.add(0, "17");
                    System.out.println("Faltó un }");

                    break;
                case "0":
                    if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("4")) {
                        cadena.add(0, "13");
                        System.out.println("Faltó una ,");
                    } else if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("18")) {
                        cadena.add(0, "5");
                        System.out.println("Faltó un operador");
                    } else if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("16")) {
                        cadena.add(0, "18");
                        System.out.println("Faltó un =");
                    } else if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("1")) {
                        cadena.add(0, "5");
                        System.out.println("Faltó un operador");
                    } else if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("12")) {
                        cadena.add(0, "18");
                        System.out.println("Faltó un =");
                    } else if (cadenaCopia.get(contador).equals("16") && cadenaCopia.get(contador - 1).equals("15")
                            && cadenaCopia.get(contador - 2).equals("14")
                            && cadenaCopia.get(contador - 3).equals("0")) {
                        cadena.add(0, "4");
                        System.out.println("Faltó un tipo");
                    } else if (cadenaCopia.get(contador).equals("19") && cadenaCopia.get(contador + 2).equals("14")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un identificador de más");
                    } else if (cadenaCopia.get(contador).equals("19") || cadenaCopia.get(contador).equals("20")) {
                        cadena.add(0, "14");
                        System.out.println("Faltó un (");
                    } else if (cadenaCopia.get(contador).equals("12")) {
                        cadena.add(0, "4");
                        System.out.println("Faltó un tipo");
                    } else if (cadenaCopia.get(contador).equals("0")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un identificador de más");
                    } else if (cadenaCopia.get(contador).equals("4")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un tipo de más");
                    }
                    break;
                case "16":
                    if (cadenaCopia.get(contador).equals("17")) {
                        cadena.add(0, "22");
                        System.out.println("Faltó un else");
                    } else if (cadenaCopia.get(contador).equals("16")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un { de más");
                    } else if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("15")) {
                        pila.remove(pila.size() - 1);
                        pila.remove(pila.size() - 1);
                        System.out.println("Hay un identificador de más");
                    } else if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("22")) {
                        pila.remove(pila.size() - 1);
                        pila.remove(pila.size() - 1);
                        System.out.println("Hay un identificador de más");
                    } else {
                        cadena.add(0, "15");
                        System.out.println("Faltó un (");
                    }
                    break;
                case "13":
                case "18":
                    if (cadenaCopia.get(contador).equals("18")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un = de más");
                    } else {
                        cadena.add(0, "0");
                        System.out.println("Faltó un identificador");
                    }
                    break;
                case "7":
                case "8":
                case "9":
                case "11":
                    cadena.add(0, "0");
                    System.out.println("Faltó un identificador");
                    break;
                case "21":
                    if (cadenaCopia.get(contador).equals("0") ||
                            cadenaCopia.get(contador).equals("1") ||
                            cadenaCopia.get(contador).equals("2") ||
                            cadenaCopia.get(contador).equals("3")) {
                        cadena.add(0, "12");
                        System.out.println("Faltó un ;");
                    } else {
                        cadena.add(0, "17");
                        System.out.println("Faltó un }");
                    }
                    break;
                case "5":
                case "6":
                    cadena.add(0, "0");
                    System.out.println("Faltó un identificador");
                    break;
                case "1":
                case "2":
                case "3":
                    if (cadenaCopia.get(contador).equals("0") && cadenaCopia.get(contador - 1).equals("18")) {
                        cadena.add(0, "5");
                        System.out.println("Faltó un operador");
                    } else if (cadenaCopia.get(contador).equals("0")) {
                        cadena.add(0, "18");
                        System.out.println("Faltó un =");
                    } else if (cadenaCopia.get(contador).equals("17")) {
                        cadena.add(0, "21");
                        System.out.println("Faltó un return");
                    } else {
                        cadena.add(0, "5");
                        System.out.println("Faltó un +");
                    }
                    break;
                case "14":
                    if (cadenaCopia.get(contador).contains("4")) {
                        cadena.add(0, "0");
                        System.out.println("Faltó un identificador");
                    } else if (cadenaCopia.get(contador).contains("17") ||
                            cadenaCopia.get(contador).contains("12")) {
                        cadena.add(0, "19");
                        System.out.println("Faltó un if");
                    }
                    break;
                case "15":
                    if (cadenaCopia.get(contador).equals("14") ||
                            cadenaCopia.get(contador).equals("11") ||
                            cadenaCopia.get(contador).equals("10") ||
                            cadenaCopia.get(contador).equals("7")) {
                        cadena.add(0, "0");
                        System.out.println("Faltó un identificador");
                    } else if (cadenaCopia.get(contador).equals("15") && cadenaCopia.get(contador - 1).equals("14")) {
                        cadena.remove(0);
                        cadR = true;
                        System.out.println("Hay un ) de más");
                    } else if (cadenaCopia.get(contador).equals("0")) {
                        cadena.add(0, "14");
                        System.out.println("Faltó un (");
                    }
                    break;
                case "10":
                    cadena.add(0, "14");
                    System.out.println("Faltó un (");
                    break;
                case "23":
                    cadena.add(0, "17");
                    System.out.println("Faltó un }");
                    break;
                default:
                    cadena.remove(0);
                    cadR = true;
                    System.out.println("Pánico");
                    break;
            }
        }
        return cadR;
    }

    /**
     * @param id
     * @return
     */
    public boolean existe(String id) {
        boolean ver = false;
        if (ids.size() == 0) {
            ver = false;
        } else {
            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).equals(id)) {
                    ver = true;
                }
            }
        }

        return ver;
    }

    /**
     * @param id
     * @return
     */
    public boolean valTipo(String tipo, String id) {
        boolean ver = false;
        if (ids.size() == 0) {
            ver = false;
        } else {
            for (int i = 0; i < ids.size(); i++) {
                if (ids.get(i).equals(id)) {
                    if (tipos.get(i).equals(tipo)) {
                        ver = true;
                    }
                }
            }
        }

        return ver;
    }

}
