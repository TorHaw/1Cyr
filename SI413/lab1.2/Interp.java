import java.io.*;
import java.util.*;

public class Interp {
    
    private static ArrayDeque<String> stack;

    /*
     * Constructor
     */
    public Interp() {
        stack = new ArrayDeque<String>();
    }

    /*
     * File processing
     */
    public ArrayDeque<String> parseFile(String fname) throws IOException {
        Reader src = new FileReader(fname);
        Scanner stdin = new Scanner(System.in);

        // note, -1 is returned at EOF
        for (int gotRaw = src.read(); gotRaw != -1; gotRaw = src.read()) {
            char got = (char)gotRaw;
            
            // String literal case
            if (got == '/') stack.push(this.stringFinder(src)); 

            // Print case
            else if (got == 'p') this.stackPrint();

            // Comment case
            else if (got == '~') this.commentHandler(src);

            // Input case
            else if (got == 'i') stack.push(stdin.nextLine());

            // Concatenation case
            else if (got == 's' && src.read() == 's') this.concat();

        }
        src.close();
        stdin.close();
        return stack;
    }

    /*
     * Handle strings even with escape sequences
     */
    private String stringFinder(Reader src) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int innerRaw = src.read(); innerRaw != -1; innerRaw = src.read()) {
            char inner = (char)innerRaw;

            // End string case
            if (inner == '/') break;

            // Escape character case
            else if (inner == '\\') {
                inner = (char)src.read();
                if (inner == 'n') inner = '\n';
            }
            // Normal character case
            sb.append(inner);
        }
        return sb.toString();
    }
    
    /*
     * Handles print case
     */
    public void stackPrint() {
        if (!stack.isEmpty()) System.out.format("%s\n", stack.remove());
    }

    /*
     * Handle comment case
     */
    private void commentHandler(Reader src) throws IOException {
        for (int inRaw = src.read(); inRaw != -1 ; inRaw = src.read()) 
            if ((char)inRaw == '~' || (char)inRaw == '\n') return;
    }

    /*
     * Handles concatenation case
     */
    public void concat() {
        if (!stack.isEmpty()) {
            String s1 = stack.remove();
            String s2 = stack.remove();
            String ss = s2 + s1;
            stack.push(ss);
        }
    }

    public static void main(String[] args) throws IOException {
        String fname = "some_file.txt";
        Interp interpreter = new Interp();
        interpreter.parseFile(fname);
    }
}