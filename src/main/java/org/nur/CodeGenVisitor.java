package org.nur;

import org.nur.exception.CodeGenException;

import java.util.HashMap;
import java.util.Map;

public class CodeGenVisitor extends ProjectLangBaseVisitor<String> {

    private static final String TYPE_INT = "int";
    private static final String TYPE_FLOAT = "float";
    private static final String TYPE_BOOL = "bool";
    private static final String TYPE_STRING = "string";

    private final StringBuilder code = new StringBuilder();
    private int labelCounter = 0;

    private final Map<String, String> symbolMap = new HashMap<>();

    private int newLabel() {
        return labelCounter++;
    }

    public String getGeneratedCode() {
        return code.toString();
    }

    private void emit(String instruction) {
        code.append(instruction).append("\n");
    }
    
    @Override
    public String visitDeclStmt(ProjectLangParser.DeclStmtContext ctx) {
        String type = ctx.typeDecl().getText();
        for (var idNode : ctx.ID()) {
            symbolMap.put(idNode.getText(), type);
        }
        return null;
    }

    @Override
    public String visitIntExpr(ProjectLangParser.IntExprContext ctx) {
        emit("push I " + ctx.getText());
        return TYPE_INT;
    }

    @Override
    public String visitFloatExpr(ProjectLangParser.FloatExprContext ctx) {
        emit("push F " + ctx.getText());
        return TYPE_FLOAT;
    }

    @Override
    public String visitBoolExpr(ProjectLangParser.BoolExprContext ctx) {
        emit("push B " + ctx.getText());
        return TYPE_BOOL;
    }

    @Override
    public String visitStringExpr(ProjectLangParser.StringExprContext ctx) {
        emit("push S " + ctx.getText());
        return TYPE_STRING;
    }

    @Override
    public String visitExprStmt(ProjectLangParser.ExprStmtContext ctx) {
        visit(ctx.expr());
        emit("pop");
        return null;
    }

    @Override
    public String visitIdExpr(ProjectLangParser.IdExprContext ctx) {
        String varName = ctx.ID().getText();
        emit("load " + varName);
        return symbolMap.get(varName);
    }

    @Override
    public String visitAddSubConcatExpr(ProjectLangParser.AddSubConcatExprContext ctx) {
        String op = ctx.op.getText();
        if (op.equals(".")) {
            visit(ctx.expr(0));
            visit(ctx.expr(1));
            emit("concat");
            return TYPE_STRING;
        }

        String targetType = visitNumericOperands(ctx.expr(0), ctx.expr(1));
        String letter = typeToLetter(targetType);

        switch (op) {
            case "+" -> emit("add " + letter);
            case "-" -> emit("sub " + letter);
            default -> throw new CodeGenException("Unknown operator: " + op);
        }

        return targetType;
    }

    @Override
    public String visitMulDivModExpr(ProjectLangParser.MulDivModExprContext ctx) {
        String op = ctx.op.getText();
        if (op.equals("%")) {
            visit(ctx.expr(0));
            visit(ctx.expr(1));
            emit("mod");
            return TYPE_INT;
        }

        String targetType = visitNumericOperands(ctx.expr(0), ctx.expr(1));
        String letter = typeToLetter(targetType);

        switch (op) {
            case "/" -> emit("div " + letter);
            case "*" -> emit("mul " + letter);
            default -> throw new CodeGenException("Unknown operator: " + op);
        }

        return targetType;
    }

    @Override
    public String visitAssignExpr(ProjectLangParser.AssignExprContext ctx) {
        String varName = ctx.ID().getText();
        String varType = symbolMap.get(varName);

        String exprType = visit(ctx.expr());

        if (varType.equals(TYPE_FLOAT) && exprType.equals(TYPE_INT)) {
            emit("itof");
        }

        emit("save " + varName);
        return varType;
    }

    @Override
    public String visitWriteStmt(ProjectLangParser.WriteStmtContext ctx) {
        int count = ctx.expr().size();
        for (var exprNode : ctx.expr()) {
            visit(exprNode);
        }
        emit("print " + count);
        return null;
    }

    @Override
    public String visitReadStmt(ProjectLangParser.ReadStmtContext ctx) {
        for (var idNode : ctx.ID()) {
            String varName = idNode.getText();
            String type = symbolMap.get(varName);
            emit("read " + typeToLetter(type));
            emit("save " + varName);
        }
        return null;
    }

    @Override
    public String visitIfStmt(ProjectLangParser.IfStmtContext ctx) {
        boolean hasElse = ctx.statement(1) != null;
        int labelFalse = newLabel();
        int labelEnd = hasElse ? newLabel() : labelFalse;

        visit(ctx.expr());
        emit("fjmp " + labelFalse);

        visit(ctx.statement(0));

        if (ctx.statement(1) != null) {
            emit("jmp " + labelEnd);
            emit("label " + labelFalse);
            visit(ctx.statement(1));
            emit("label " + labelEnd);
        } else {
            emit("label " + labelFalse);
        }
        return null;
    }

    @Override
    public String visitWhileStmt(ProjectLangParser.WhileStmtContext ctx) {
        int labelStart = newLabel();
        int labelEnd = newLabel();

        emit("label " + labelStart);

        visit(ctx.expr());
        emit("fjmp " + labelEnd);

        visit(ctx.statement());

        emit("jmp " + labelStart);
        emit("label " + labelEnd);

        return null;
    }

    @Override
    public String visitRelationalExpr(ProjectLangParser.RelationalExprContext ctx) {
        String targetType = visitNumericOperands(ctx.expr(0), ctx.expr(1));
        String letter = typeToLetter(targetType);

        switch (ctx.op.getText()) {
            case "<" -> emit("lt " + letter);
            case ">" -> emit("gt " + letter);
            default -> throw new CodeGenException("Unknown operator: " + ctx.op.getText());
        }
        return TYPE_BOOL;
    }

    @Override
    public String visitEqualityExpr(ProjectLangParser.EqualityExprContext ctx) {
        String leftType = inferType(ctx.expr(0));
        String rightType = inferType(ctx.expr(1));
        String targetType = leftType;

        if ((leftType.equals(TYPE_INT) || leftType.equals(TYPE_FLOAT)) &&
                (rightType.equals(TYPE_INT) || rightType.equals(TYPE_FLOAT))) {
            targetType = (leftType.equals(TYPE_FLOAT) || rightType.equals(TYPE_FLOAT)) ? TYPE_FLOAT : TYPE_INT;
        }

        visit(ctx.expr(0));
        if (leftType.equals(TYPE_INT) && targetType.equals(TYPE_FLOAT)) emit("itof");
        visit(ctx.expr(1));
        if (rightType.equals(TYPE_INT) && targetType.equals(TYPE_FLOAT)) emit("itof");

        emit("eq " + typeToLetter(targetType));

        if (ctx.op.getText().equals("!=")) {
            emit("not");
        }
        return TYPE_BOOL;
    }

    @Override
    public String visitAndExpr(ProjectLangParser.AndExprContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        emit("and");
        return TYPE_BOOL;
    }

    @Override
    public String visitOrExpr(ProjectLangParser.OrExprContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        emit("or");
        return TYPE_BOOL;
    }

    @Override
    public String visitNotExpr(ProjectLangParser.NotExprContext ctx) {
        visit(ctx.expr());

        emit("not");
        return TYPE_BOOL;
    }

    @Override
    public String visitUnaryMinusExpr(ProjectLangParser.UnaryMinusExprContext ctx) {
        String type = visit(ctx.expr());

        emit("uminus " + typeToLetter(type));
        return type;
    }

    private String visitNumericOperands(ProjectLangParser.ExprContext left, ProjectLangParser.ExprContext right) {
        String leftType = inferType(left);
        String rightType = inferType(right);
        String targetType = (leftType.equals(TYPE_FLOAT) || rightType.equals(TYPE_FLOAT)) ? TYPE_FLOAT : TYPE_INT;

        visit(left);
        if (leftType.equals(TYPE_INT) && targetType.equals(TYPE_FLOAT)) emit("itof");

        visit(right);
        if (rightType.equals(TYPE_INT) && targetType.equals(TYPE_FLOAT)) emit("itof");

        return targetType;
    }

    private String inferType(ProjectLangParser.ExprContext ctx) {
        if (ctx instanceof ProjectLangParser.IntExprContext) return TYPE_INT;
        if (ctx instanceof ProjectLangParser.FloatExprContext) return TYPE_FLOAT;
        if (ctx instanceof ProjectLangParser.BoolExprContext) return TYPE_BOOL;
        if (ctx instanceof ProjectLangParser.StringExprContext) return TYPE_STRING;
        if (ctx instanceof ProjectLangParser.IdExprContext idExprCtx)
            return symbolMap.get(idExprCtx.ID().getText());
        if (ctx instanceof ProjectLangParser.ParensExprContext parensExprCtx)
            return inferType(parensExprCtx.expr());
        if (ctx instanceof ProjectLangParser.AssignExprContext assignExprCtx)
            return symbolMap.get(assignExprCtx.ID().getText());
        if (ctx instanceof ProjectLangParser.AddSubConcatExprContext addSubConcatExprCtx)
            return inferAddSubConcatType(addSubConcatExprCtx);
        if (ctx instanceof ProjectLangParser.MulDivModExprContext mulDivModExprCtx)
            return inferMulDivModType(mulDivModExprCtx);

        throw new CodeGenException("Cannot infer type for expression: " + ctx.getClass().getSimpleName());
    }

    private String inferAddSubConcatType(ProjectLangParser.AddSubConcatExprContext ctx) {
        if (ctx.op.getText().equals(".")) return TYPE_STRING;
        return resolveNumericType(inferType(ctx.expr(0)), inferType(ctx.expr(1)));
    }

    private String inferMulDivModType(ProjectLangParser.MulDivModExprContext ctx) {
        if (ctx.op.getText().equals("%")) return TYPE_INT;
        return resolveNumericType(inferType(ctx.expr(0)), inferType(ctx.expr(1)));
    }

    private String resolveNumericType(String leftType, String rightType) {
        return leftType.equals(TYPE_FLOAT) || rightType.equals(TYPE_FLOAT) ? TYPE_FLOAT : TYPE_INT;
    }

    private String typeToLetter(String type) {
        return switch (type) {
            case TYPE_INT -> "I";
            case TYPE_FLOAT -> "F";
            case TYPE_BOOL -> "B";
            case TYPE_STRING -> "S";
            default -> throw new CodeGenException("Unknown type: " + type);
        };
    }
}