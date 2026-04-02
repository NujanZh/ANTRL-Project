package org.nur;

import org.nur.exception.TypeCheckerException;

import java.util.HashMap;
import java.util.Map;

public class TypeCheckerTree extends ProjectLangBaseVisitor<String> {

    private static final String TYPE_INT = "int";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_BOOL = "bool";
    private static final String TYPE_STRING = "string";

    private final Map<String, String> symbolMap = new HashMap<>();

    @Override
    public String visitDeclStmt(ProjectLangParser.DeclStmtContext ctx) {
        String type = ctx.typeDecl().getText();

        for (var idNode : ctx.ID()) {
            String varName = idNode.getText();

            if (symbolMap.containsKey(varName)) {
                return reportRedeclaration(varName);
            }
            symbolMap.put(varName, type);
        }
        return null;
    }

    @Override
    public String visitIdExpr(ProjectLangParser.IdExprContext ctx) {
        String varName = ctx.ID().getText();

        if (!symbolMap.containsKey(varName)) {
            return reportUndefined(varName);
        }
        return symbolMap.get(varName);
    }

    @Override
    public String visitAssignExpr(ProjectLangParser.AssignExprContext ctx) {
        String varName = ctx.ID().getText();

        if (!symbolMap.containsKey(varName)) {
            return reportUndefined(varName);
        }

        String varType = symbolMap.get(varName);
        String exprType = visit(ctx.expr());

        boolean isWideningConversion = varType.equals(TYPE_FLOAT) && exprType.equals(TYPE_INT);
        if (!isWideningConversion && !varType.equals(exprType)) {
            return reportTypeMismatch(varType, exprType, varName);
        }

        return varType;
    }

    @Override
    public String visitAddSubConcatExpr(ProjectLangParser.AddSubConcatExprContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));
        String op = ctx.op.getText();

        if (op.equals(".")) {
            if (!leftType.equals(TYPE_STRING) || !rightType.equals(TYPE_STRING)) {
                return reportInvalidOperandTypes(op, TYPE_STRING);
            }
            return TYPE_STRING;
        } else {
            if (isNumeric(leftType) && isNumeric(rightType)) {
                return resolveNumericType(leftType, rightType);
            }
            return reportInvalidOperandTypes(op, "numeric");
        }
    }

    @Override
    public String visitMulDivModExpr(ProjectLangParser.MulDivModExprContext ctx) {
        String leftType = visit(ctx.expr(0));
        String rightType = visit(ctx.expr(1));
        String op = ctx.op.getText();

        if (op.equals("%")) {
            if (!leftType.equals(TYPE_INT) || !rightType.equals(TYPE_INT)) {
                return reportInvalidOperandTypes(op, "integer");
            }
            return TYPE_INT;
        } else {
            if (isNumeric(leftType) && isNumeric(rightType)) {
                return resolveNumericType(leftType, rightType);
            }
            return reportInvalidOperandTypes(op, "numeric");
        }
    }

    @Override
    public String visitIntExpr(ProjectLangParser.IntExprContext ctx) { return TYPE_INT; }

    @Override
    public String visitFloatExpr(ProjectLangParser.FloatExprContext ctx) { return TYPE_FLOAT; }

    @Override
    public String visitBoolExpr(ProjectLangParser.BoolExprContext ctx) { return TYPE_BOOL; }

    @Override
    public String visitStringExpr(ProjectLangParser.StringExprContext ctx) { return TYPE_STRING; }

    @Override
    public String visitAndExpr(ProjectLangParser.AndExprContext ctx) { return checkBool(ctx.expr(0), ctx.expr(1), "&&"); }

    @Override
    public String visitOrExpr(ProjectLangParser.OrExprContext ctx) { return checkBool(ctx.expr(0), ctx.expr(1), "||"); }

    @Override
    public String visitParensExpr(ProjectLangParser.ParensExprContext ctx) { return visit(ctx.expr()); }

    private String checkBool(ProjectLangParser.ExprContext left, ProjectLangParser.ExprContext right, String op) {
        if (!visit(left).equals(TYPE_BOOL) || !visit(right).equals(TYPE_BOOL)) {
            return reportInvalidOperandTypes(op, TYPE_BOOL);
        }
        return TYPE_BOOL;
    }

    private boolean isNumeric(String type) {
        return type.equals(TYPE_INT) || type.equals(TYPE_FLOAT);
    }

    private String resolveNumericType(String left, String right) {
        if (left.equals(TYPE_FLOAT) || right.equals(TYPE_FLOAT)) return TYPE_FLOAT;
        return TYPE_INT;
    }

    private String reportRedeclaration(String varName) {
        throw new TypeCheckerException("Variable '" + varName + "' already declared.");
    }

    private String reportUndefined(String varName) {
        throw new TypeCheckerException("Using undeclared variable '" + varName + "'.");
    }

    private String reportTypeMismatch(String varType, String exprType, String varName) {
        throw new TypeCheckerException("Cannot assign " + exprType + " to variable of type " + varType + " ('" + varName + "').");
    }

    private String reportInvalidOperandTypes(String op, String requiredType) {
        throw new TypeCheckerException("Operator '" + op + "' requires " + requiredType + " operands.");
    }
}
