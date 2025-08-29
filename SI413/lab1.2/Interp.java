import java.io.*;
import java.util.*;

public class Interp {
    
    private static ArrayDeque stack;

    /*
     * Constructor
     */
    public void Interp() {
        ArrayDeque<String> stack = new ArrayDeque<String>();
    }

    /*
     * File processing
     */
    public void fileProcess(String fname) throws IOException {
        
        Reader rdr = null;
        try { rdr = new FileReader(fname); } 
        catch (Exception e) { System.err.println("File Not Found: " + e);}
        
        int x = rdr.read();
        char c = (char) x;
        while (x != -1) {
            System.out.println(x);
            if (c == '/') {
                System.out.println("found slash");
                StringBuilder sb = new StringBuilder();
                c = rdr.read();
                while (c != '/') {
                    sb.append(c);
                    c = (char)rdr.read();
                }
                System.out.println(sb.toString());
            }
            c = (char)rdr.read();
        }

        rdr.close();
    }
    

    public static void main(String[] args) throws IOException {
        String fname = args[0];
        System.out.format("literals in %s:\n", fname);
        System.out.println(stack);
        
    }
}