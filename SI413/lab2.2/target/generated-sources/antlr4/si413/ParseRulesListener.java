// Generated from si413/ParseRules.g4 by ANTLR 4.13.1
package si413;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ParseRules}.
 */
public interface ParseRulesListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code RegularProg}
	 * labeled alternative in {@link ParseRules#prog}.
	 * @param ctx the parse tree
	 */
	void enterRegularProg(ParseRules.RegularProgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RegularProg}
	 * labeled alternative in {@link ParseRules#prog}.
	 * @param ctx the parse tree
	 */
	void exitRegularProg(ParseRules.RegularProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EmptyProg}
	 * labeled alternative in {@link ParseRules#prog}.
	 * @param ctx the parse tree
	 */
	void enterEmptyProg(ParseRules.EmptyProgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EmptyProg}
	 * labeled alternative in {@link ParseRules#prog}.
	 * @param ctx the parse tree
	 */
	void exitEmptyProg(ParseRules.EmptyProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrintStmt}
	 * labeled alternative in {@link ParseRules#stmt}.
	 * @param ctx the parse tree
	 */
	void enterPrintStmt(ParseRules.PrintStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrintStmt}
	 * labeled alternative in {@link ParseRules#stmt}.
	 * @param ctx the parse tree
	 */
	void exitPrintStmt(ParseRules.PrintStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SaveStmt}
	 * labeled alternative in {@link ParseRules#stmt}.
	 * @param ctx the parse tree
	 */
	void enterSaveStmt(ParseRules.SaveStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SaveStmt}
	 * labeled alternative in {@link ParseRules#stmt}.
	 * @param ctx the parse tree
	 */
	void exitSaveStmt(ParseRules.SaveStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SignExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterSignExpr(ParseRules.SignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SignExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitSignExpr(ParseRules.SignExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulExpr(ParseRules.MulExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulExpr(ParseRules.MulExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(ParseRules.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(ParseRules.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterVarExpr(ParseRules.VarExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitVarExpr(ParseRules.VarExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddExpr(ParseRules.AddExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExpr}
	 * labeled alternative in {@link ParseRules#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddExpr(ParseRules.AddExprContext ctx);
}