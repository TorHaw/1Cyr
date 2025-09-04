import java.io.*;
import java.util.*;

public class Compiler {
    // declare print stack and epilogue list
    private static ArrayDeque<String> stack;
    private static ArrayList<String> elist;
    private static int sCount = 0;
    private static PrintWriter pw;

    /*
     * Constructor
     */
    public Compiler() {
        stack = new ArrayDeque<String>();
    }

    /*
     * File processing
     */
    public ArrayDeque<String> compile(String f1, String f2) throws IOException {
        Reader src = new FileReader(f1);
        Scanner stdin = new Scanner(System.in);
        FileWriter fw = new FileWriter(f2, false);
        pw = new PrintWriter(fw);
        this.init();

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
            else if (got == 'i') //stack.push(stdin.nextLine());
                throw new UnsupportedOperationException("this doesn't work yet");
            // Concatenation case
            else if (got == 's' ) throw new UnsupportedOperationException("this doesn't work yet");
                //if (src.read() == 's') this.concat();
                //else System.exit(7);

            // Reverse case
            else if (got == 'r') //this.stringRev();
                throw new UnsupportedOperationException("this doesn't work yet");
            // White space case
            else if (got == ' ' || got == '\t' || got == '\n') continue;

            // error case
            else System.exit(7);

        }
        this.close();
        src.close();
        stdin.close();
        return stack;
    }

    private void init() {
        pw.println("target triple = \"x86_64-pc-linux-gnu\"\n");
        pw.println("declare i32 @puts(ptr noundef) #1");
        pw.println();
        pw.println("define i32 @main() { ");
    }

    private void close() {
        pw.println("\tret i32 0");
        pw.println("}");
        pw.close();
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
    private void stackPrint() {
        if (!stack.isEmpty()) {
            elist.add("@lit"++ " = constant ["+ +" x i8] c\"" +stack.remove() + "\00");
            lcount++;
        }
    }

    /*
     * Handles concatenation case
     */
    private void concat() {
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
    private void stringRev() {
        if (!stack.isEmpty()) {
            StringBuilder rev = new StringBuilder();
            rev.append(stack.remove());
            stack.push(rev.reverse().toString());
        } else System.exit(7);
    }

    public static void main(String[] args) throws IOException {
        String f1 = args[0];
        String f2 = args[1];
        Compiler comp = new Compiler();
        comp.compile(f1, f2);
    }
}