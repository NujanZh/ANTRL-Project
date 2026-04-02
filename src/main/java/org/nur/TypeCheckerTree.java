package org.nur;

import java.util.HashMap;
import java.util.Map;

public class TypeCheckerTree extends ProjectLangBaseVisitor<String> {

    private final Map<String, String> symbolMap = new HashMap<>();

    @Override
    public String visitDeclStmt(ProjectLangParser.DeclStmtContext ctx) {
        String type = ctx.typeDecl().getText();

        for (var idNode : ctx.ID()) {
            String varName = idNode.getText();

            if (symbolMap.containsKey(varName)) {
                System.err.println("Error: Variable '" + varName + "' already declared.");
                System.exit(1);
            }
            symbolMap.put(varName, type);
        }
        return null;
    }

    @Override
    public String visitIdExpr(ProjectLangParser.IdExprContext ctx) {
        String varName = ctx.ID().getText();

        if (!symbolMap.containsKey(varName)) {
            System.err.println("Error: Using undeclared variable'" + varName + "'.");
            System.exit(1);
        }
        return symbolMap.get(varName);
    }

    @Override
    public String visitAssignExpr(ProjectLangParser.AssignExprContext ctx) {
        String varName = ctx.ID().getText();

        if (!symbolMap.containsKey(varName)) {
            System.err.println("Error: Attempt to assign a value to an undeclared variable '" + varName + "'.");
            System.exit(1);
        }

        String varType = symbolMap.get(varName);
        String exprType = visit(ctx.expr());

        if (!varType.equals(exprType)) {
            System.err.println("Type error: Cannot assign a value of type " + exprType + " to a variable of type " + varType + " ('" + varName + "').");
            System.exit(1);
        }

        return varType;
    }

    @Override
    public String visitIntExpr(ProjectLangParser.IntExprContext ctx) { return "int"; }

    @Override
    public String visitFloatExpr(ProjectLangParser.FloatExprContext ctx) { return "float"; }

    @Override
    public String visitBoolExpr(ProjectLangParser.BoolExprContext ctx) { return "bool"; }

    @Override
    public String visitStringExpr(ProjectLangParser.StringExprContext ctx) { return "string"; }
}
