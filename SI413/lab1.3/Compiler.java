import java.io.*;
import java.util.*;

public class Compiler {
    // declare print stack and epilogue list
    private static ArrayList<String> elist;
    private static ArrayDeque<String> stack;
    private static int sCount = 1;
    private static int rCount = 0;
    private static int pCount = 0;
    private static int oCount = 0;
    private static PrintWriter pw;

    /*
     * Constructor
     */
    public Compiler() {
        elist = new ArrayList<String>();
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
            if (got == '/') this.stringFinder(src); 

            // Comment case
            else if (got == '~') this.commentHandler(src);

            // Print case
            else if (got == 'p') this.stackPrint();
                
            // Input case
            else if (got == 'i') getInput(); 
                
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
        this.close();
        src.close();
        stdin.close();
        return stack;
    }

    /*
     * Handle strings even with escape sequences
     */
    private void stringFinder(Reader src) throws IOException {
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
        String val = sb.toString();
        elist.add("@lit"+ sCount + " = constant ["+ (val.length()+1) +" x i8] c\"" + val + "\\00\"");
        stack.push("@lit" + sCount++);
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
            String val = stack.remove();
            pw.println("\tcall i32 (ptr, ...) @printf(ptr @fmt, ptr "+ val +")");
        }
    }

    /*
     * Handles input case
     */
    private void getInput() {
        elist.add("@ptr" + pCount + " = constant [6 x i8] c\"%255s\\00\"");

        //input capabilities
        pw.println();
        pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
        pw.println("\t%reg"+ rCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");
        pw.println("\tcall i32 (ptr, ...) @scanf(ptr @ptr"+ pCount++ +", ptr %reg"+ rCount +")");

        stack.push("%reg"+ rCount++ +"");
    }

    /*
     * Handles concatenation case
     */
    private void concat() {
        if (!stack.isEmpty() && stack.size() > 1) {
            String s1 = stack.remove();
            String s2 = stack.remove();

            //elist.add("@out" + oCount + " = constant [6 x i8] c\"%255s\\00\"");
            
            pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
            pw.println("\t%out"+ oCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");
            
            pw.println("\tcall void @concat(ptr noundef "+ s2 +", ptr noundef "+ s1 +", ptr noundef %out"+ oCount +")");
            pw.println();
            
            stack.push("%out" + oCount++);
            
        } else System.exit(7);
    }

    /*
     * Handles reverse case
     */
    private void stringRev() {
        if (!stack.isEmpty()) {
            String val = stack.remove();

            pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
            pw.println("\t%out"+ oCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");

            pw.println("\tcall void @reverse(ptr noundef "+ val +",ptr noundef %out"+ oCount +")");
            pw.println();

            stack.push("%out" + oCount++);

        } else System.exit(7);
    }

    private void init() {
        // Just initialize top of blank file
        pw.println("target triple = \"x86_64-pc-linux-gnu\"\n");
        pw.println("declare i32 @puts(ptr noundef)");
        pw.println("declare i32 @scanf(ptr, ...)");
        pw.println("declare i32 @printf(ptr, ...)");
        pw.println("declare ptr @malloc(i64)");
        pw.println("declare i64 @strlen(ptr noundef)");
        pw.println();
        this.initFunctions();
        pw.println();
        pw.println("define i32 @main() { ");
        
        // initialize string format for printf
        elist.add("@fmt = constant [4 x i8] c\"%s\n\\00\"");
    }

    /*
     * Initialize concat function in LLVM
     */
    private void initFunctions() {
        pw.println("; Function Attrs: noinline nounwind optnone uwtable\r\n" + //
                        "define dso_local void @concat(ptr noundef %0, ptr noundef %1, ptr noundef %2) {\r\n" + //
                        "  %4 = alloca ptr, align 8\r\n" + //
                        "  %5 = alloca ptr, align 8\r\n" + //
                        "  %6 = alloca ptr, align 8\r\n" + //
                        "  store ptr %0, ptr %4, align 8\r\n" + //
                        "  store ptr %1, ptr %5, align 8\r\n" + //
                        "  store ptr %2, ptr %6, align 8\r\n" + //
                        "  br label %7\r\n" + //
                        "\r\n" + //
                        "7:                                                ; preds = %12, %3\r\n" + //
                        "  %8 = load ptr, ptr %4, align 8\r\n" + //
                        "  %9 = load i8, ptr %8, align 1\r\n" + //
                        "  %10 = sext i8 %9 to i32\r\n" + //
                        "  %11 = icmp ne i32 %10, 0\r\n" + //
                        "  br i1 %11, label %12, label %18\r\n" + //
                        "\r\n" + //
                        "12:                                               ; preds = %7\r\n" + //
                        "  %13 = load ptr, ptr %4, align 8\r\n" + //
                        "  %14 = getelementptr inbounds i8, ptr %13, i32 1\r\n" + //
                        "  store ptr %14, ptr %4, align 8\r\n" + //
                        "  %15 = load i8, ptr %13, align 1\r\n" + //
                        "  %16 = load ptr, ptr %6, align 8\r\n" + //
                        "  %17 = getelementptr inbounds i8, ptr %16, i32 1\r\n" + //
                        "  store ptr %17, ptr %6, align 8\r\n" + //
                        "  store i8 %15, ptr %16, align 1\r\n" + //
                        "  br label %7, !llvm.loop !6\r\n" + //
                        "\r\n" + //
                        "18:                                               ; preds = %7\r\n" + //
                        "  br label %19\r\n" + //
                        "\r\n" + //
                        "19:                                               ; preds = %24, %18\r\n" + //
                        "  %20 = load ptr, ptr %5, align 8\r\n" + //
                        "  %21 = load i8, ptr %20, align 1\r\n" + //
                        "  %22 = sext i8 %21 to i32\r\n" + //
                        "  %23 = icmp ne i32 %22, 0\r\n" + //
                        "  br i1 %23, label %24, label %30\r\n" + //
                        "\r\n" + //
                        "24:                                               ; preds = %19\r\n" + //
                        "  %25 = load ptr, ptr %5, align 8\r\n" + //
                        "  %26 = getelementptr inbounds i8, ptr %25, i32 1\r\n" + //
                        "  store ptr %26, ptr %5, align 8\r\n" + //
                        "  %27 = load i8, ptr %25, align 1\r\n" + //
                        "  %28 = load ptr, ptr %6, align 8\r\n" + //
                        "  %29 = getelementptr inbounds i8, ptr %28, i32 1\r\n" + //
                        "  store ptr %29, ptr %6, align 8\r\n" + //
                        "  store i8 %27, ptr %28, align 1\r\n" + //
                        "  br label %19, !llvm.loop !8\r\n" + //
                        "\r\n" + //
                        "30:                                               ; preds = %19\r\n" + //
                        "  %31 = load ptr, ptr %6, align 8\r\n" + //
                        "  store i8 0, ptr %31, align 1\r\n" + //
                        "  ret void\r\n" + //
                        "}\r\n" + //
                        "\r\n" + //
                        "; Function Attrs: noinline nounwind optnone uwtable\r\n" + //
                        "define dso_local void @reverse(ptr noundef %0, ptr noundef %1) {\r\n" + //
                        "  %3 = alloca ptr, align 8\r\n" + //
                        "  %4 = alloca ptr, align 8\r\n" + //
                        "  %5 = alloca i32, align 4\r\n" + //
                        "  store ptr %0, ptr %3, align 8\r\n" + //
                        "  store ptr %1, ptr %4, align 8\r\n" + //
                        "  %6 = load ptr, ptr %3, align 8\r\n" + //
                        "  %7 = call i64 @strlen(ptr noundef %6)\r\n" + //
                        "  %8 = sub i64 %7, 1\r\n" + //
                        "  %9 = trunc i64 %8 to i32\r\n" + //
                        "  store i32 %9, ptr %5, align 4\r\n" + //
                        "  br label %10\r\n" + //
                        "\r\n" + //
                        "10:                                               ; preds = %13, %2\r\n" + //
                        "  %11 = load i32, ptr %5, align 4\r\n" + //
                        "  %12 = icmp sge i32 %11, 0\r\n" + //
                        "  br i1 %12, label %13, label %22\r\n" + //
                        "\r\n" + //
                        "13:                                               ; preds = %10\r\n" + //
                        "  %14 = load ptr, ptr %3, align 8\r\n" + //
                        "  %15 = load i32, ptr %5, align 4\r\n" + //
                        "  %16 = add nsw i32 %15, -1\r\n" + //
                        "  store i32 %16, ptr %5, align 4\r\n" + //
                        "  %17 = sext i32 %15 to i64\r\n" + //
                        "  %18 = getelementptr inbounds i8, ptr %14, i64 %17\r\n" + //
                        "  %19 = load i8, ptr %18, align 1\r\n" + //
                        "  %20 = load ptr, ptr %4, align 8\r\n" + //
                        "  %21 = getelementptr inbounds i8, ptr %20, i32 1\r\n" + //
                        "  store ptr %21, ptr %4, align 8\r\n" + //
                        "  store i8 %19, ptr %20, align 1\r\n" + //
                        "  br label %10, !llvm.loop !9\r\n" + //
                        "\r\n" + //
                        "22:                                               ; preds = %10\r\n" + //
                        "  %23 = load ptr, ptr %4, align 8\r\n" + //
                        "  store i8 0, ptr %23, align 1\r\n" + //
                        "  ret void\r\n" + //
                        "}\r\n");
    }

    private void close() {
        pw.println();
        pw.println("\tret i32 0");
        pw.println("}");
        pw.println();
        for (String s : elist) pw.println(s);
        pw.println();
        pw.println("!0 = !{i32 1, !\"wchar_size\", i32 4}\r\n" + //
                        "!1 = !{i32 8, !\"PIC Level\", i32 2}\r\n" + //
                        "!2 = !{i32 7, !\"PIE Level\", i32 2}\r\n" + //
                        "!3 = !{i32 7, !\"uwtable\", i32 2}\r\n" + //
                        "!4 = !{i32 7, !\"frame-pointer\", i32 2}\r\n" + //
                        "!5 = !{!\"Ubuntu clang version 18.1.3 (1ubuntu1)\"}\r\n" + //
                        "!6 = distinct !{!6, !7}\r\n" + //
                        "!7 = !{!\"llvm.loop.mustprogress\"}\r\n" + //
                        "!8 = distinct !{!8, !7}\r\n" + //
                        "!9 = distinct !{!9, !7}");
        pw.close();
    }

    public static void main(String[] args) throws IOException {
        String f1 = args[0];
        String f2 = args[1];
        Compiler comp = new Compiler();
        comp.compile(f1, f2);
    }
}