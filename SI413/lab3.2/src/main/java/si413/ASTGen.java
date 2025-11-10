package si413;

import java.util.List;
import java.util.ArrayList;

/** This class is used to create the AST from a parse tree.
 * The static method ASTGen.gen(parseTree) is the specific function
 * to perform that conversion.
 */
public class ASTGen {
    /** Turns a parse tree Prog node into a complete AST.
     * This is the main external interface for the ASTGen class.
     */
    public static Stmt.Block gen(ParseRules.ProgContext ptreeRoot) {
        return new ASTGen().progVis.visit(ptreeRoot);
    }


    private class ProgVisitor extends Visitor<Stmt.Block> {
        @Override
        public Stmt.Block visitRegularProg(ParseRules.RegularProgContext ctx) {
            // recursively call visit to get the first statement and block for the rest
            Stmt first = stmtVis.visit(ctx.stmt());
            Stmt.Block rest = visit(ctx.prog());
            // combine those into a single block AST node
            List<Stmt> children = new ArrayList<>();
            children.add(first);
            children.addAll(rest.children());
            return new Stmt.Block(children);
        }

        @Override
        public Stmt.Block visitEmptyProg(ParseRules.EmptyProgContext ctx) {
            return new Stmt.Block(List.of());
        }
    }


    private class StmtVisitor extends Visitor<Stmt> {
        @Override
        public Stmt visitStrPrint(ParseRules.StrPrintContext ctx) {
            Expr<String> child = strExprVis.visit(ctx.strExpr());
            return new Stmt.PrintString(child);
        }

        @Override
        public Stmt visitSaveStr(ParseRules.SaveStrContext ctx) {
            String name = ctx.SID().getText();
            Expr<String> child = strExprVis.visit(ctx.strExpr());
            return new Stmt.AssignString(name, child);
        }

        @Override
        public Stmt visitBoolPrint(ParseRules.BoolPrintContext ctx) {
            Expr<Boolean> child = boolExprVis.visit(ctx.boolExpr());
            return new Stmt.PrintBool(child);
        }

        @Override
        public Stmt visitSaveBool(ParseRules.SaveBoolContext ctx) {
            String name = ctx.BID().getText();
            Expr<Boolean> child = boolExprVis.visit(ctx.boolExpr());
            return new Stmt.AssignBool(name, child);
        }

        @Override
        public Stmt visitIfStmt(ParseRules.IfStmtContext ctx) {
            Expr<Boolean> child = boolExprVis.visit(ctx.boolExpr());
            Stmt ifBody = stmtLstVis.visit(ctx.stmtlst());
            Stmt elseBody = new Stmt.EmptyStmt();
            return new Stmt.IfElse(child, ifBody, elseBody);
        }

        @Override
        public Stmt visitIfElseStmt(ParseRules.IfElseStmtContext ctx) {
            Expr<Boolean> child = boolExprVis.visit(ctx.boolExpr());
            Stmt ifBody = stmtLstVis.visit(ctx.stmtlst(0));
            Stmt elseBody = stmtLstVis.visit(ctx.stmtlst(1));
            return new Stmt.IfElse(child, ifBody, elseBody);
        }

        @Override
        public Stmt visitWhileStmt(ParseRules.WhileStmtContext ctx) {
            Expr<Boolean> child = boolExprVis.visit(ctx.boolExpr());
            Stmt body = stmtLstVis.visit(ctx.stmtlst());
            return new Stmt.While(child, body);
        }
    }

    private class StmtListVisitor extends Visitor<Stmt.Block> {
        
        @Override
        public Stmt.Block visitMultStmt(ParseRules.MultStmtContext ctx) {
            Stmt first = stmtVis.visit(ctx.stmt());
            Stmt.Block rest = visit(ctx.stmtlst());
            List<Stmt> children = new ArrayList<>();

            children.add(first);
            children.addAll(rest.children());
            return new Stmt.Block(children);
        }

        @Override
        public Stmt.Block visitOneStmt(ParseRules.OneStmtContext ctx) {
            return new Stmt.Block(List.of(stmtVis.visit(ctx.stmt())));
        }
    }


    private class StrExprVisitor extends Visitor<Expr<String>> {
        @Override
        public Expr<String> visitStrLit(ParseRules.StrLitContext ctx) {
            // extract the actual string literal without escapes
            StringBuilder sb = new StringBuilder();
            String raw = ctx.STRLIT().getText();
            for (int i = 1; i < raw.length()-1; ++i) {
                if (raw.charAt(i) == '$') { 
                    ++i;

                    if (raw.charAt(i) == 'n') sb.append('\n');
                    else if (raw.charAt(i) == 't') sb.append('\t');
                    else sb.append(raw.charAt(i));
                }
                else sb.append(raw.charAt(i));
            }
            return new Expr.StringLit(sb.toString());
        }

        @Override
        public Expr<String> visitInputLit(ParseRules.InputLitContext ctx) {
            return new Expr.Input();
        }

        @Override
        public Expr<String> visitRevStr(ParseRules.RevStrContext ctx) {
            Expr<String> child = visit(ctx.strExpr());
            return new Expr.Reverse(child);
        }

        @Override
        public Expr<String> visitStrVar(ParseRules.StrVarContext ctx) {
            String name = ctx.SID().getText();
            return new Expr.StrVar(name);
        }

        @Override
        public Expr<String> visitConcatStr(ParseRules.ConcatStrContext ctx) {
            Expr<String> l = visit(ctx.strExpr(0));
            Expr<String> r = visit(ctx.strExpr(1));
            return new Expr.Concat(l, r);
        }
    }


    private class BoolExprVisitor extends Visitor<Expr<Boolean>> {

        @Override
        public Expr<Boolean> visitBoolLit(ParseRules.BoolLitContext ctx) {
            return new Expr.BoolLit(ctx.BOOLLIT().getText().equals("1"));
        }

        @Override
        public Expr<Boolean> visitRevBool(ParseRules.RevBoolContext ctx) {
            Expr<Boolean> child = visit(ctx.boolExpr());
            return new Expr.Not(child);
        }

        @Override
        public Expr<Boolean> visitBoolVar(ParseRules.BoolVarContext ctx) {
            String name = ctx.BID().getText();
            return new Expr.BoolVar(name);
        }

        @Override
        public Expr<Boolean> visitCondExpr(ParseRules.CondExprContext ctx) {
            Expr<Boolean> l = visit(ctx.boolExpr(0));
            Expr<Boolean> r = visit(ctx.boolExpr(1));
            String cond = ctx.COND().getText();
    
            if (cond.equals("&")) return new Expr.And(l, r);
            else if (cond.equals("|")) return new Expr.Or(l, r);
            else return Errors.error("Undefined operation for given types");
        }

        @Override
        public Expr<Boolean> visitSubStr(ParseRules.SubStrContext ctx) {
            Expr<String> l = strExprVis.visit(ctx.strExpr(0));
            Expr<String> r = strExprVis.visit(ctx.strExpr(1));
            return new Expr.Contains(r, l);
        }

        @Override
        public Expr<Boolean> visitStrCompare(ParseRules.StrCompareContext ctx) {
            Expr<String> l = strExprVis.visit(ctx.strExpr(0));
            Expr<String> r = strExprVis.visit(ctx.strExpr(1));
            String op = ctx.COMPARE().getText();

            if (op.equals("<")) return new Expr.StrLess(l, r);
            else if (op.equals(">")) return new Expr.StrLess(r, l);
            else return Errors.error("Undefined operation for given types");
        }
    }


    private ProgVisitor progVis = new ProgVisitor();
    private StmtVisitor stmtVis = new StmtVisitor();
    private StmtListVisitor stmtLstVis = new StmtListVisitor();
    private StrExprVisitor strExprVis = new StrExprVisitor();
    private BoolExprVisitor boolExprVis = new BoolExprVisitor();


    /** Use this as the subclass for the visitor classes.
     * It overrides the default method to alert you if one of the
     * visit methods is missing.
     */
    private static class Visitor<T> extends ParseRulesBaseVisitor<T> {
        // This overrides the default behavior to alert if a visit method is missing.
        @Override
        public T visitChildren(org.antlr.v4.runtime.tree.RuleNode node) {
            return Errors.error(String.format(
                "class %s has no visit method for %s",
                getClass().getSimpleName(),
                node.getClass().getSimpleName()));
        }
    }
}
