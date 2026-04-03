package org.nur;

import org.nur.exception.InterpreterException;

import java.util.*;

public class Interpreter {

    private final Deque<Object> stack = new ArrayDeque<>();
    private final Map<String, Object> memory = new HashMap<>();
    private final Map<Integer, Integer> labels = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

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
                labels.put(Integer.parseInt(parts[1]), i);
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
                case "push"          -> executePush(parts[1], parts[2]);
                case "pop"           -> stack.pop();
                case "load"          -> stack.push(memory.get(parts[1]));
                case "save"          -> memory.put(parts[1], stack.pop());
                case "itof"          -> stack.push(((Integer) stack.pop()).floatValue());
                case "add", "sub",
                     "mul", "div"    -> executeMathOp(opcode, parts[1]);
                case "mod"           -> executeModOp();
                case "concat"        -> executeConcatOp();
                case "lt", "gt"      -> executeRelationalOp(opcode, parts[1]);
                case "eq"            -> executeEqualityOp();
                case "and", "or"     -> executeLogicalOp(opcode);
                case "not", "uminus" -> executeUnaryOp(opcode, parts[1]);
                case "print"         -> executePrint(Integer.parseInt(parts[1]));
                case "read"          -> executeRead(parts[1]);
                case "label"         -> {}
                case "jmp"           -> ip = labels.get(Integer.parseInt(parts[1])) - 1;
                case "fjmp"          -> {
                    boolean condition = (Boolean) stack.pop();
                    if (!condition) {
                        ip = labels.get(Integer.parseInt(parts[1])) - 1;
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
            case "S" -> {
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    stack.push(value.substring(1, value.length() - 1));
                }
                stack.push(value);
            }
            default -> throw new InterpreterException("Unknown type prefix: " + type);
        }
    }

    private void executeMathOp(String opcode, String type) {
        if (type.equals("F")) {
            float right = (Float) stack.pop();
            float left = (Float) stack.pop();
            switch (opcode) {
                case "add" -> stack.push(left + right);
                case "sub" -> stack.push(left - right);
                case "mul" -> stack.push(left * right);
                case "div" -> {
                    if (right == 0f) throw new InterpreterException("Division by zero.");
                    stack.push(left / right);
                }
                default -> throw new InterpreterException("Unknown math operator: " + opcode);
            }
        } else {
            float right = (Integer) stack.pop();
            float left = (Integer) stack.pop();
            switch (opcode) {
                case "add" -> stack.push(left + right);
                case "sub" -> stack.push(left - right);
                case "mul" -> stack.push(left * right);
                case "div" -> {
                    if (right == 0) throw new InterpreterException("Division by zero.");
                    stack.push(left / right);
                }
                default -> throw new InterpreterException("Unknown math operator: " + opcode);
            }
        }
    }

    private void executeRelationalOp(String opcode, String type) {
        if (type.equals("F")) {
            float left = (Float) stack.pop();
            float right = (Float) stack.pop();
            switch (opcode) {
                case "lt" -> stack.push(left < right);
                case "gt" -> stack.push(left > right);
                default   -> throw new InterpreterException("Unknown relational operator: " + opcode);
            }
        } else {
            int left = (Integer) stack.pop();
            int right = (Integer) stack.pop();
            switch (opcode) {
                case "lt" -> stack.push(left < right);
                case "gt" -> stack.push(left > right);
                default   -> throw new InterpreterException("Unknown relational operator: " + opcode);
            }
        }
    }

    private void executeEqualityOp() {
        Object right = stack.pop();
        Object left = stack.pop();
        stack.push(left.equals(right));
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
            case "and" -> stack.push(left && right);
            case "or"  -> stack.push(left || right);
            default    -> throw new InterpreterException("Unknown logical operator: " + opcode);
        }
    }

    private void executeUnaryOp(String opcode, String type) {
        Object val = stack.pop();
        switch (opcode) {
            case "not"    -> stack.push(!(Boolean) val);
            case "uminus" -> stack.push(type.equals("F") ? -((Float) val) : -((Integer) val));
            default       -> throw new InterpreterException("Unknown unary operator: " + opcode);
        }
    }

    private void executeRead(String type) {
        System.out.print("> ");
        switch (type) {
            case "I" -> stack.push(scanner.nextInt());
            case "F" -> stack.push(scanner.nextFloat());
            case "B" -> stack.push(scanner.nextBoolean());
            case "S" -> stack.push(scanner.next());
            default  -> throw new InterpreterException("Unknown type prefix: " + type);
        }
    }

    private void executePrint(int count) {
        List<Object> toPrint = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            toPrint.add(stack.pop());
        }
        Collections.reverse(toPrint);
        for (Object obj : toPrint) {
            System.out.print(obj);
        }
        System.out.println();
    }
}