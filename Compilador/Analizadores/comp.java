package Analizadores;

import java.io.IOException;

/**
@author Saul Mercado Pedroza
@author Isaac Ulises Ascencio Padilla
 */
public class comp {
    static String nom = "arcleer.txt";
    /**
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        tools sep = new tools();
        sep.rmComentario();
        sep.formato();
        sep.resultado();
    }
}