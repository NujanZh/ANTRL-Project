package org.nur;

import org.nur.exception.InterpreterException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Interpreter {

    private final Stack<Object> stack = new Stack<>();
    private final Map<String, Object> memory = new HashMap<>();
    private final Map<String, Integer> labels = new HashMap<>();

    public void execute(List<String> instructions) {
        scanLabels(instructions);
        runInstruction(instructions);
    }

    private void scanLabels(List<String> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            String line = instructions.get(i).trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" ");
            if (parts[0].equals("LABEL")) {
                labels.put(parts[1], i);
            }
        }
    }

    private void runInstruction(List<String> instructions) {
        int ip = 0;

        while (ip < instructions.size()) {
            String line = instructions.get(ip).trim();
            if (line.isEmpty()) {
                ip++;
                continue;
            }

            String[] parts = line.split(" ", 3);
            String opcode = parts[0];

            switch (opcode) {
                case "PUSH"          -> executePush(parts[1], parts[2]);
                case "LOAD"          -> stack.push(memory.get(parts[1]));
                case "STORE"         -> memory.put(parts[1], stack.pop());
                case "WRITE"         -> executeWrite();
                case "ADD", "SUB",
                     "MUL", "DIV"    -> executeMathOp(opcode);
                case "MOD"           -> executeModOp();
                case "CONCAT"        -> executeConcatOp();
                case "LT", "GT"      -> executeRelationalOp(opcode);
                case "EQ", "NEQ"     -> executeEqualityOp(opcode);
                case "AND", "OR"     -> executeLogicalOp(opcode);
                case "NOT", "UMINUS" -> executeUnaryOp(opcode);
                case "LABEL" -> {}
                case "JMP" -> ip = labels.get(parts[1]) - 1;
                case "JUMPF" -> {
                    boolean condition = (Boolean) stack.pop();
                    if (!condition) {
                        ip = labels.get(parts[1]) - 1;
                    }
                }

                default -> throw new InterpreterException("Unknown instruction '" + opcode + "'");
            }

            ip++;
        }
    }

    private void executePush(String type, String value) {
        switch (type) {
            case "I" -> stack.push(Integer.parseInt(value));
            case "F" -> stack.push(Float.parseFloat(value));
            case "B" -> stack.push(Boolean.parseBoolean(value));
            case "S" -> stack.push(value.substring(1, value.length() - 1));
            default -> throw new InterpreterException("Unknown type prefix: " + type);
        }
    }

    private void executeMathOp(String opcode) {
        Object right = stack.pop();
        Object left = stack.pop();

        if (left instanceof Float || right instanceof Float) {
            float l = toFloat(left);
            float r = toFloat(right);
            switch (opcode) {
                case "ADD" -> stack.push(l + r);
                case "SUB" -> stack.push(l - r);
                case "MUL" -> stack.push(l * r);
                case "DIV" -> {
                    if (r == 0f) throw new InterpreterException("Division by zero.");
                    stack.push(l / r);
                }
                default -> throw new InterpreterException("Unknown math operator: " + opcode);
            }
        } else {
            int l = (Integer) left;
            int r = (Integer) right;
            switch (opcode) {
                case "ADD" -> stack.push(l + r);
                case "SUB" -> stack.push(l - r);
                case "MUL" -> stack.push(l * r);
                case "DIV" -> {
                    if (r == 0) throw new InterpreterException("Division by zero.");
                    stack.push(l / r);
                }
                default -> throw new InterpreterException("Unknown math operator: " + opcode);
            }
        }
    }

    private void executeRelationalOp(String opcode) {
        Object right = stack.pop();
        Object left = stack.pop();

        if (left instanceof Float || right instanceof Float) {
            float l = toFloat(left);
            float r = toFloat(right);
            switch (opcode) {
                case "LT" -> stack.push(l < r);
                case "GT" -> stack.push(l > r);
                default   -> throw new InterpreterException("Unknown relational operator: " + opcode);
            }
        } else {
            int l = (Integer) left;
            int r = (Integer) right;
            switch (opcode) {
                case "LT" -> stack.push(l < r);
                case "GT" -> stack.push(l > r);
                default   -> throw new InterpreterException("Unknown relational operator: " + opcode);
            }
        }
    }

    private void executeEqualityOp(String opcode) {
        Object right = stack.pop();
        Object left = stack.pop();

        boolean isEqual;
        if ((left instanceof Float || left instanceof Integer) &&
                (right instanceof Float || right instanceof Integer)) {
            isEqual = toFloat(left).equals(toFloat(right));
        } else {
            isEqual = left.equals(right);
        }

        switch (opcode) {
            case "EQ"  -> stack.push(isEqual);
            case "NEQ" -> stack.push(!isEqual);
            default    -> throw new InterpreterException("Unknown equality operator: " + opcode);
        }
    }

    private void executeModOp() {
        int right = (Integer) stack.pop();
        int left = (Integer) stack.pop();
        if (right == 0) throw new InterpreterException("Modulo by zero.");
        stack.push(left % right);
    }

    private void executeConcatOp() {
        String right = (String) stack.pop();
        String left = (String) stack.pop();
        stack.push(left + right);
    }

    private void executeLogicalOp(String opcode) {
        boolean right = (Boolean) stack.pop();
        boolean left = (Boolean) stack.pop();
        switch (opcode) {
            case "AND" -> stack.push(left && right);
            case "OR"  -> stack.push(left || right);
            default    -> throw new InterpreterException("Unknown logical operator: " + opcode);
        }
    }

    private void executeUnaryOp(String opcode) {
        Object val = stack.pop();
        switch (opcode) {
            case "NOT"    -> stack.push(!(Boolean) val);
            case "UMINUS" -> stack.push(val instanceof Float floatVal ? -floatVal : -(Integer) val);
            default       -> throw new InterpreterException("Unknown unary operator: " + opcode);
        }
    }

    private void executeWrite() {
        System.out.println(stack.pop());
    }

    private static Float toFloat(Object obj) {
        if (obj instanceof Float floatObject) return floatObject;
        if (obj instanceof Integer integerObject) return integerObject.floatValue();
        throw new InterpreterException("Cannot cast " + obj.getClass().getSimpleName() + " to Float.");
    }
}