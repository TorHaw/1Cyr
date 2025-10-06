package si413;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Compiler {

    private static ArrayList<String> elist = new ArrayList<String>();
    Map<String, String> vars = new HashMap<String, String>();
    private StmtVisitor svisitor = new StmtVisitor();
    private ExprVisitor evisitor = new ExprVisitor();
    private static int sCount = 1;
    private static int rCount = 0;
    private static int pCount = 0;
    private static int bCount = 0;
    private static int oCount = 0;
    private PrintWriter pw;

    private class StmtVisitor extends ParseRulesBaseVisitor<Void> {
        @Override
        public Void visitRegularProg(ParseRules.RegularProgContext ctx) {
            visit(ctx.stmt());
            visit(ctx.prog());
            return null;
        }

        @Override
        public Void visitPrintStmt(ParseRules.PrintStmtContext ctx) {
            String value = evisitor.visit(ctx.expr());
            if (value.charAt(1) == 'r' || value.charAt(1) == 'l' || value.charAt(1) == 'o') { 
                pw.println("\tcall i32 (ptr, ...) @printf(ptr @fmt_str, ptr "+ value +")");
            } 
            else if (value.charAt(1) == 'b') {
                pw.println("\tcall i32 (ptr, ...) @printf(ptr @fmt_int, i32 " + value +")");
            }
            pw.println();
            return null;
        }

        @Override
        public Void visitSaveStmt(ParseRules.SaveStmtContext ctx) {
            String varName = ctx.ID().getText();
            String value = evisitor.visit(ctx.expr());
            vars.put(varName, value);
            return null;
        }
    }

    private class ExprVisitor extends ParseRulesBaseVisitor<String> {
        @Override
        public String visitLiteralExpr(ParseRules.LiteralExprContext ctx) {
            String lit = ctx.LIT().getText().substring(1, ctx.LIT().getText().length()-1);
            Pattern p = Pattern.compile("\\$\\[|\\$\\]|\\$\\$|\\$\\n|\\$\\t|\\$\\r");
            Matcher m = p.matcher(lit);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String r = m.group();
                if (r.equals("$$")) m.appendReplacement(sb, "\\$");
                else if (r.equals("$[")) m.appendReplacement(sb, "\\[");
                else if (r.equals("$]")) m.appendReplacement(sb, "\\]");
                else if (r.equals("$\n")) m.appendReplacement(sb, "\n");
                else if (r.equals("$\t")) m.appendReplacement(sb, "\t");
                else if (r.equals("$\r")) m.appendReplacement(sb, "\r");
            }
            m.appendTail(sb);
            String val = sb.toString();
            elist.add("@lit"+ sCount + " = constant ["+ (val.length()+1) +" x i8] c\"" + val + "\\00\"");
            return "@lit" + sCount++;
        }

        @Override
        public String visitBoolExpr(ParseRules.BoolExprContext ctx) {
            int val = Integer.parseInt(ctx.BOOL().getText());
            pw.println("\t%b" + bCount + " = add i32 0, " + val);
            return "%b" + bCount++;
        }

        @Override
        public String visitInputExpr(ParseRules.InputExprContext ctx) {
            elist.add("@ptr" + pCount + " = constant [6 x i8] c\"%255s\\00\"");

            //input capabilities
            pw.println();
            pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
            pw.println("\t%reg"+ rCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");
            pw.println("\tcall i32 (ptr, ...) @scanf(ptr @ptr"+ pCount++ +", ptr %reg"+ rCount +")");

            return "%reg"+ rCount++;
        }

        @Override
        public String visitVarExpr(ParseRules.VarExprContext ctx) {
            String varName = ctx.ID().getText();
            String savedValue = vars.get(varName);
            if (savedValue == null)
                return Errors.error(varName+" is referenced before being saved");
            return savedValue;
        }

        
        public Boolean isString(String value) {
            return (value.charAt(1) == 'r' || value.charAt(1) == 'l' || value.charAt(1) == 'o');
        }

        public Boolean isBool(String value) {
            return (value.charAt(1) == 'b');
        }

        public Void intToString(String val) {
           
            pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 16)");
            pw.println("\t%reg"+ rCount +" = getelementptr inbounds [16 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");
            
            pw.println("\tcall i32 (ptr, ...) @sprintf(ptr noundef %reg"+ (rCount++) +", ptr noundef @int_to_str, i32 "+ val +")");
            pw.println();
            
            return null;
        }

        @Override
        public String visitEvalExpr(ParseRules.EvalExprContext ctx) {
            String lhs = visit(ctx.expr(0));
            String rhs = visit(ctx.expr(1));
            if (isString(lhs) && isString(rhs)) {
                if (ctx.OP().getText().equals("<")) {
                    pw.println("\t%b"+ bCount++ +" = call i32 @strcmp(i8* noundef "+ lhs +", i8* noundef "+ rhs +")");
                    pw.println("\t%b"+ bCount++ +" = icmp slt i32 %b"+ (bCount-2) +", 0");
                    pw.println("\t%b"+ bCount + "= zext i1 %b" + (bCount-1) + " to i32");
                    return "%b" + bCount++;
                }
                else if (ctx.OP().getText().equals(">")) {
                    pw.println("\t%b"+ bCount++ +" = call i32 @strcmp(i8* noundef "+ lhs +", i8* noundef "+ rhs +")");
                    pw.println("\t%b"+ bCount++ +" = icmp sgt i32 %b"+ (bCount-2) +", 0");
                    pw.println("\t%b"+ bCount + "= zext i1 %b" + (bCount-1) + " to i32");
                    return "%b" + bCount++;
                }
                else if (ctx.OP().getText().equals("?")) {
                    pw.println("\t%reg"+ rCount +" = call ptr @strstr(i8* noundef "+ rhs +", i8* noundef "+ lhs +")");
                    pw.println("\t%b"+ bCount++ +" = icmp ne ptr %reg"+ rCount++ +", null");
                    pw.println("\t%b"+ bCount + "= zext i1 %b" + (bCount-1) + " to i32");
                    return "%b" + bCount++;
                }
                else if (ctx.OP().getText().equals("+")) {
                    pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
                    pw.println("\t%out"+ oCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");
                    
                    pw.println("\tcall void @concat(ptr noundef "+ lhs +", ptr noundef "+ rhs +", ptr noundef %out"+ oCount +")");
                    pw.println();
                    
                    return "%out" + oCount++;
                }
                else return Errors.error("Undefined operation for given types 1");

            } else if (isBool(lhs) && isString(rhs)) {  
                if (ctx.OP().getText().equals("+")) {
                    // convert value
                    intToString(lhs);

                    // concatenate the two strings
                    pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
                    pw.println("\t%out"+ oCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");
                    
                    pw.println("\tcall void @concat(ptr noundef "+ lhs +", ptr noundef "+ rhs +", ptr noundef %out"+ oCount +")");
                    pw.println();

                    return "%out" + oCount++;
                }
                else return Errors.error("Undefined operation for given types 2");

            } else if (isString(lhs) && isBool(rhs)) {  
                if (ctx.OP().getText().equals("+")) {
                    // convert value
                    intToString(rhs);

                    // concatenate the two strings
                    pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
                    pw.println("\t%out"+ oCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");
                    
                    pw.println("\tcall void @concat(ptr noundef "+ lhs +", ptr noundef "+ rhs +", ptr noundef %out"+ oCount +")");
                    pw.println();

                    return "%out" + oCount++;
                }
                else return Errors.error("Undefined operation for given types 3");

            } else if (isBool(lhs) && isBool(rhs)) {   
                if (ctx.OP().getText().equals("&")) { 
                    pw.println("\t%b"+ bCount +" = and i32 "+ lhs +", "+ rhs);
                    return "%b" + bCount++;
                }
                else if (ctx.OP().getText().equals("|")) {
                    pw.println("\t%b"+ bCount +" = or i32 "+ lhs +", "+ rhs);
                    return "%b" + bCount++;
                }
                else return Errors.error("Undefined operation for given types 4");

            } else
                return Errors.error("Mismatch types in operation");
        }

        @Override
        public String visitRevExpr(ParseRules.RevExprContext ctx) {
            String str = visit(ctx.expr());
            
            if (isString(str)) {
                pw.println("\t%reg"+ rCount++ +" = call ptr @malloc(i64 256)");
                pw.println("\t%out"+ oCount +" = getelementptr inbounds [256 x i8], ptr %reg"+ (rCount-1) +", i64 0, i64 0");

                pw.println("\tcall void @reverse_string(ptr noundef "+ str +",ptr noundef %out"+ oCount +")");
                pw.println();
                return "%out" + oCount++;
            } else if (isBool(str)) {
                pw.println("\t%b"+ bCount +" = call i32 @reverse_bool(i32 "+ str +")");
                pw.println();
                return "%b" + (bCount++);
            } else return Errors.error("Undefined operation for given type");
        }
    }

    public Compiler(Path destFile) throws IOException {
        pw = new PrintWriter(destFile.toFile());
    }

    public void compile(ParseTree ptree) throws IOException {
        // copy contents of preamble.ll in the resources directory
        try (BufferedReader preamble = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("preamble.ll"))))
        {
            while (true) {
                String line = preamble.readLine();
                if (line == null) break;
                pw.println(line);
            }
        }

        this.init();

        // this calls all of your visit methods to walk the parse tree
        // note that the code emitted goes inside main()
        svisitor.visit(ptree);

        this.close();
    }

    public static TokenStream getTokens(Path sourceFile) throws IOException {
        return new Tokenizer(
            Compiler.class.getResourceAsStream("tokenSpec.txt"),
            ParseRules.VOCABULARY
        ).streamFrom(sourceFile);
    }

    public static ParseTree parse(TokenStream tokens) throws IOException {
        ParseRules parser = new ParseRules(tokens);
        Errors.register(parser);
        return parser.prog();
    }

    private void init() {
        // Just initialize top of blank file
        pw.println();
        pw.println();
        pw.println("declare i32 @puts(ptr noundef)");
        pw.println("declare i32 @scanf(ptr, ...)");
        pw.println("declare i32 @printf(ptr, ...)");
        pw.println("declare ptr @malloc(i64)");
        pw.println("declare i32 @strcmp(i8* noundef, i8* noundef)");
        pw.println("declare i8* @strstr(i8* noundef, i8* noundef)");
        pw.println("declare i32 @sprintf(i8* noundef, i8* noundef, ...)");
        pw.println();
        pw.println();
        pw.println("define i32 @main() { ");
        
        // initialize string format for printf
        elist.add("@fmt_str = constant [4 x i8] c\"%s\n\\00\"");
        elist.add("@fmt_int = constant [4 x i8] c\"%d\n\\00\"");
        elist.add("@int_to_str = constant [4 x i8] c\"%d\n\\00\"");
    }

    private void close() {
        pw.println();
        pw.println("\tret i32 0");
        pw.println("}");
        pw.println();
        for (String s : elist) pw.println(s);
        pw.println();
        pw.close();
    }


    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            Errors.error("need 2 command-line args: source_file dest_file");
        }
        Path sourceFile = Path.of(args[0]);
        Path destFile = Path.of(args[1]);

        TokenStream tokens = getTokens(sourceFile);
        ParseTree ptree = parse(tokens);
        new Compiler(destFile).compile(ptree);
    }
}
