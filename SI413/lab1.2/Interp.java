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

            // Comment case
            else if (got == '~') this.commentHandler(src);

            // Print case
            else if (got == 'p') this.stackPrint();

            // Input case
            else if (got == 'i') stack.push(stdin.nextLine());

            // Concatenation case
            else if (got == 's' )
                if (src.read() == 's') this.concat();
                else System.exit(7);

            // Reverse case
            else if (got == 'r') this.stringRev();

            // White space case
            else if (got == ' ' || got == '\t' || got == '\n') continue;

            // error case
            else System.exit(7);

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
                else if (inner == 't') inner = '\t';
            }
            // Normal character case
            sb.append(inner);
        }
        return sb.toString();
    }

    /*
     * Handle comment case
     */
    private void commentHandler(Reader src) throws IOException {
        for (int inRaw = src.read(); inRaw != -1 ; inRaw = src.read())
            if ((char)inRaw == '~' || (char)inRaw == '\n') return;
    }
    
    /*
     * Handles print case
     */
    public void stackPrint() {
        if (!stack.isEmpty()) System.out.format("%s\n", stack.remove());
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
        } else System.exit(7);
    }

    /*
     * Handles reverse case
     */
    public void stringRev() {
        if (!stack.isEmpty()) {
            StringBuilder rev = new StringBuilder();
            rev.append(stack.remove());
            stack.push(rev.reverse().toString());
        } else System.exit(7);
    }

    public static void main(String[] args) throws IOException {
        String fname = "blew_test.txt";
        Interp interpreter = new Interp();
        interpreter.parseFile(fname);
    }
}