import java.io.*;

public class Extractor {
    String fname = "Example.java";
    Reader rdr = new FileReader(fname);
    int x = rdr.read();
    while (x != -1) {
        if (x == '"') {
            System.out.println("found quote");
            StringBuilder sb = new StringBuilder();
            x = rdr.read();
            while ((char)x != '"') {
                sb.append((char)x);
                x = rdr.read();
            }
        }
        x = rdr.read();
    }
}