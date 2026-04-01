// Generated from ProjectLang.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ProjectLangParser}.
 */
public interface ProjectLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ProjectLangParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ProjectLangParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProjectLangParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ProjectLangParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EmptyStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStmt(ProjectLangParser.EmptyStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EmptyStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStmt(ProjectLangParser.EmptyStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeclStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterDeclStmt(ProjectLangParser.DeclStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeclStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitDeclStmt(ProjectLangParser.DeclStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExprStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(ProjectLangParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExprStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(ProjectLangParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ReadStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReadStmt(ProjectLangParser.ReadStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ReadStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReadStmt(ProjectLangParser.ReadStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WriteStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWriteStmt(ProjectLangParser.WriteStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WriteStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWriteStmt(ProjectLangParser.WriteStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlockStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmt(ProjectLangParser.BlockStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlockStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmt(ProjectLangParser.BlockStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(ProjectLangParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(ProjectLangParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(ProjectLangParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStmt}
	 * labeled alternative in {@link ProjectLangParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(ProjectLangParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link ProjectLangParser#typeDecl}.
	 * @param ctx the parse tree
	 */
	void enterTypeDecl(ProjectLangParser.TypeDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link ProjectLangParser#typeDecl}.
	 * @param ctx the parse tree
	 */
	void exitTypeDecl(ProjectLangParser.TypeDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(ProjectLangParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(ProjectLangParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpr(ProjectLangParser.BoolExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpr(ProjectLangParser.BoolExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStringExpr(ProjectLangParser.StringExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStringExpr(ProjectLangParser.StringExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFloatExpr(ProjectLangParser.FloatExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFloatExpr(ProjectLangParser.FloatExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIdExpr(ProjectLangParser.IdExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIdExpr(ProjectLangParser.IdExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelationalExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpr(ProjectLangParser.RelationalExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelationalExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpr(ProjectLangParser.RelationalExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(ProjectLangParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(ProjectLangParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssignExpr(ProjectLangParser.AssignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssignExpr(ProjectLangParser.AssignExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualityExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(ProjectLangParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualityExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(ProjectLangParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDivModExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDivModExpr(ProjectLangParser.MulDivModExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDivModExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDivModExpr(ProjectLangParser.MulDivModExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParensExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParensExpr(ProjectLangParser.ParensExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParensExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParensExpr(ProjectLangParser.ParensExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSubConcatExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSubConcatExpr(ProjectLangParser.AddSubConcatExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSubConcatExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSubConcatExpr(ProjectLangParser.AddSubConcatExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(ProjectLangParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(ProjectLangParser.NotExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntExpr(ProjectLangParser.IntExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntExpr(ProjectLangParser.IntExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryMinusExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinusExpr(ProjectLangParser.UnaryMinusExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryMinusExpr}
	 * labeled alternative in {@link ProjectLangParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinusExpr(ProjectLangParser.UnaryMinusExprContext ctx);
}