package org.nur;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String testProgram = """
                int a;
                float b;
                string c;
                read a;
                b = a + 10.5;
                c = \"Result: \";
                write c, b;
                """;

        CharStream input = CharStreams.fromString(testProgram);
        ProjectLangLexer lexer = new ProjectLangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProjectLangParser parser = new ProjectLangParser(tokens);
        ParseTree tree = parser.program();

        TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();
        typeChecker.visit(tree);

        System.out.println("Generating code...");
        CodeGenVisitor codeGen = new CodeGenVisitor();
        codeGen.visit(tree);

        String outputCode = codeGen.getGeneratedCode();
        System.out.println("Generated code:\n" + outputCode);

        try (FileWriter writer = new FileWriter("output.txt")) {
            writer.write(outputCode);
            System.out.println("Generated code was saved in file output.txt");
        } catch (IOException e) {
            System.err.println("Error while saving to file: " + e.getMessage());
        }

        List<String> instructionList = Arrays.asList(outputCode.split("\n"));

        Interpreter interpreter = new Interpreter();
        interpreter.execute(instructionList);
    }
}
