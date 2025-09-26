package si413;

import java.nio.file.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.antlr.v4.runtime.TokenStream;

/** Interpreter for basic calculator language.
 * The tokens and grammar come from src/main/resource/si413/tokenSpec.txt
 * and src/main/antlr4/si413/ParseRules.g4 respectively.
 */
public class Interpreter {

    Map<String, String> vars = new HashMap<String, Object>();

    /** Methods in this class will execute statements.
     * Return type is Void because statements do not return anything.
     * Note that this is Void and not void, so we still have to return null
     * in each function. (This is a consequence of Java generics.)
     */
    private class StatementVisitor extends ParseRulesBaseVisitor<Void> {
        @Override
        public Void visitRegularProg(ParseRules.RegularProgContext ctx) {
            visit(ctx.stmt());
            visit(ctx.prog());
            return null;
        }

        @Override
        public Void visitPrintStmt(ParseRules.PrintStmtContext ctx) {
            Object value = evisitor.visit(ctx.expr());
            if (value instanceof String)
                System.out.println((String)value);
            else if (value instanceof Boolean)
                System.out.println((Boolean)value);
            return null;
        }

        @Override
        public Void visitSaveStmt(ParseRules.SaveStmtContext ctx) {
            String varName = ctx.ID().getText();
            Object value = evisitor.visit(ctx.expr());
            vars.put(varName, value);
            return null;
        }
    }

    /** Methods in this class will execute expressions and return the result.
     */
    private class ExpressionVisitor extends ParseRulesBaseVisitor<Object> {
        @Override
        public String visitLiteralExpr(ParseRules.LiteralExprContext ctx) {
            return ctx.LIT().getText();
        }

        @Override
        public Boolean visitBoolExpr(ParseRules.BoolExprContext ctx) {
            return visit(ctx.expr()) == 1;
        }

        @Override
        public String visitInputExpr(ParseRules.InputExprContext ctx) {
            return ctx.INPUT().getText();
        }

        @Override
        public Object visitVarExpr(ParseRules.VarExprContext ctx) {
            String varName = ctx.ID().getText();
            Object savedValue = vars.get(varName);
            if (savedValue == null)
                return Errors.error(varName+" is referenced before being saved");
            return savedValue;
        }

        @Override
        public Object visitEvalExpr(ParseRules.EvalExprContext ctx) {
            Object lhs = visit(ctx.expr(0));
            Object rhs = visit(ctx.expr(1));
            if (lhs instanceof String && rhs instanceof String) {
                lhs = (String) lhs;
                rhs = (String)rhs;
                if (ctx.OP().getText().equals("<")) return lhs.compareTo(rhs) < 0;
                else if (ctx.OP().getText().equals(">")) return llhs.compareTo(rhs) >= 0;
                else if (ctx.OP().getText().equals("?")) return rhs.contains(lhs);
                else if (ctx.OP().getText().equals("+")) return (String)rhs + (String)lhs;
                else return Errors.error("Undefined operation for given types");

            } else if (lhs instanceof Boolean && rhs instanceof Boolean) {         
                if (ctx.OP().getText().equals("&")) return (Boolean)rhs && (Boolean)lhs;
                else if (ctx.OP().getText().equals("|")) return (Boolean)rhs || (Boolean)lhs;
                else return Errors.error("Undefined operation for given types");

            } else
                return Errors.error("Mismatch types in operation");
        }

        @Override
        public Integer visitRevExpr(ParseRules.RevExprContext ctx) {
            Object str = visit(ctx.expr());
            
            if (str instanceof String) {
                StringBuilder sb = new StringBuilder(str);
                return sb.reverse().toString();
            } else return Errors.error("Undefined operation for given type");
        }
    }

    private Scanner stdin = new Scanner(System.in);
    private StatementVisitor svisitor = new StatementVisitor();
    private ExpressionVisitor evisitor = new ExpressionVisitor();
    private Tokenizer tokenizer;

    public Interpreter() throws IOException {
        this.tokenizer = new Tokenizer(
            getClass().getResourceAsStream("tokenSpec.txt"),
            ParseRules.VOCABULARY
        );
    }

    public ParseRules.ProgContext parse(Path sourceFile) throws IOException {
        TokenStream tokenStream = tokenizer.streamFrom(sourceFile);
        ParseRules parser = new ParseRules(tokenStream);
        Errors.register(parser);
        return parser.prog();
    }

    public void execute(ParseRules.ProgContext parseTree) {
        // to execute the whole program, we just call visit() on the  root
        // node of the parse tree!
        svisitor.visit(parseTree);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            Errors.error("need 1 command-line arg: input source file");
        }
        Path sourceFile = Path.of(args[0]);
        Interpreter interp = new Interpreter();
        ParseRules.ProgContext parseTree = interp.parse(sourceFile);
        interp.execute(parseTree);
    }
}
