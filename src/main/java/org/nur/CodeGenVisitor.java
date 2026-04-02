package org.nur;

import org.nur.exception.CodeGenException;

public class CodeGenVisitor extends ProjectLangBaseVisitor<Void> {

    private final StringBuilder code = new StringBuilder();

    private int labelCounter = 0;

    private String newLabel() {
        return "L" + (labelCounter++);
    }

    public String getGeneratedCode() {
        return code.toString();
    }

    @Override
    public Void visitIntExpr(ProjectLangParser.IntExprContext ctx) {
        emit("PUSH I " + ctx.getText());
        return null;
    }

    @Override
    public Void visitIdExpr(ProjectLangParser.IdExprContext ctx) {
        emit("LOAD " + ctx.getText());
        return null;
    }

    @Override
    public Void visitAddSubConcatExpr(ProjectLangParser.AddSubConcatExprContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        String op = ctx.op.getText();
        switch (op) {
            case "+" -> emit("ADD");
            case "-" -> emit("SUB");
            case "." -> emit("CONCAT");
            default -> throw new CodeGenException("Unknown operator: " + op);
        }

        return null;
    }

    @Override
    public Void visitMulDivModExpr(ProjectLangParser.MulDivModExprContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        String op = ctx.op.getText();
        switch (op) {
            case "*" -> emit("MUL");
            case "/" -> emit("DIV");
            case "%" -> emit("MOD");
            default -> throw new CodeGenException("Unknown operator: " + op);
        }

        return null;
    }

    @Override
    public Void visitAssignExpr(ProjectLangParser.AssignExprContext ctx) {
        visit(ctx.expr());
        emit("STORE " + ctx.ID().getText());
        return null;
    }

    @Override
    public Void visitWriteStmt(ProjectLangParser.WriteStmtContext ctx) {
        for (var exprNode : ctx.expr()) {
            visit(exprNode);
            emit("WRITE");
        }
        return null;
    }

    @Override
    public Void visitIfStmt(ProjectLangParser.IfStmtContext ctx) {
        String labelFalse = newLabel();
        String labelEnd = newLabel();

        visit(ctx.expr());
        emit("JUMPF " + labelFalse);

        visit(ctx.statement(0));

        if (ctx.statement(1) != null) {
            emit("JMP " + labelEnd);
            emit("LABEL " + labelFalse);
            visit(ctx.statement(1));
            emit("LABEL " + labelEnd);
        } else {
            emit("LABEL " + labelFalse);
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(ProjectLangParser.WhileStmtContext ctx) {
        String labelStart = newLabel();
        String labelEnd = newLabel();

        emit("LABEL " + labelStart);

        visit(ctx.expr());
        emit("JUMPF " + labelEnd);

        visit(ctx.statement());

        emit("JMP " + labelStart);
        emit("LABEL " + labelEnd);

        return null;
    }

    private void emit(String instruction) {
        code.append(instruction).append("\n");
    }
}
