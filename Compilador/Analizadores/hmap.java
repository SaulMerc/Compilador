package Analizadores;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class hmap {
    public HashMap<String, Integer> vals = new HashMap<>();

    public void lectura(String name) throws FileNotFoundException {
        File arch = new File(name);
        Scanner lect = new Scanner(arch);
        int token;
        String val;

        try {
            while (lect.hasNextLine()) {
                val = lect.next();
                token = lect.nextInt();

                vals.put(val, token);
            }
        } finally {
            if (lect != null) {
                lect.close();
            }
        }
    }
}