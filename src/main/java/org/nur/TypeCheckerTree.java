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

        if (varType.equals("float") && exprType.equals("int")) {
        } else if (!varType.equals(exprType)) {
            System.err.println("Type error: Cannot assign a value of type " + exprType + " to a variable of type " + varType + " ('" + varName + "').");
            System.exit(1);
        }

        return varType;
    }

    @Override
    public String visitAddSubConcatExpr(ProjectLangParser.AddSubConcatExprContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));
        String op = ctx.op.getText();

        if (op.equals(".")) {
            if (!leftType.equals("string") || !rightType.equals("string")) {
                System.err.println("Type Error: Operator '.' requires string operands.");
                System.exit(1);
            }
            return "string";
        } else {
            if ((leftType.equals("int") || leftType.equals("float")) &&
                (rightType.equals("int") || rightType.equals("float"))) {
                if (leftType.equals("float") || rightType.equals("float")) return "float";
                return "int";
            }
            System.err.println("Type Error: Operator '" + op + "' requires numeric operands.");
            System.exit(1);
            return null;
        }
    }

    @Override
    public String visitMulDivModExpr(ProjectLangParser.MulDivModExprContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));
        String op = ctx.op.getText();

        if (op.equals("%")) {
            if (!leftType.equals("int") || !rightType.equals("int")) {
                System.err.println("Type Error: Operator '%' requires integer operands.");
                System.exit(1);
            }
            return "int";
        } else {
            if ((leftType.equals("int") || leftType.equals("float")) &&
                (rightType.equals("int") || rightType.equals("float"))) {
                if (leftType.equals("float") || rightType.equals("float")) return "float";
                return "int";
            }
            System.err.println("Type Error: Operator '" + op + "' requires numeric operands.");
            System.exit(1);
            return null;
        }
    }

    @Override
    public String visitIntExpr(ProjectLangParser.IntExprContext ctx) { return "int"; }

    @Override
    public String visitFloatExpr(ProjectLangParser.FloatExprContext ctx) { return "float"; }

    @Override
    public String visitBoolExpr(ProjectLangParser.BoolExprContext ctx) { return "bool"; }

    @Override
    public String visitStringExpr(ProjectLangParser.StringExprContext ctx) { return "string"; }

    @Override
    public String visitAndExpr(ProjectLangParser.AndExprContext ctx) { return checkBool(ctx.expr(0), ctx.expr(1), "&&"); }

    @Override
    public String visitOrExpr(ProjectLangParser.OrExprContext ctx) { return checkBool(ctx.expr(0), ctx.expr(1), "||"); }

    private String checkBool(ProjectLangParser.ExprContext left, ProjectLangParser.ExprContext right, String op) {
        if (!visit(left).equals("bool") || !visit(right).equals("bool")) {
            System.err.println("Type Error: Operator '" + op + "' requires bool operands.");
            System.exit(1);
        }
        return "bool";
    }

    @Override
    public String visitParensExpr(ProjectLangParser.ParensExprContext ctx) {
        return visit(ctx.expr());
    }
}
