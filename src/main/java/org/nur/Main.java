package org.nur;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) throws Exception {
        String testProgram = """
                    int a, b;
                    a = 5;
                    b = a + 10;
                    write b;
                """;

        CharStream input = CharStreams.fromString(testProgram);

        ProjectLangLexer lexer = new ProjectLangLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ProjectLangParser parser = new ProjectLangParser(tokens);

        ParseTree tree = parser.program();

        System.out.println("Abstract Syntax Tree:");
        System.out.println(tree.toStringTree(parser));
        System.out.println();

        System.out.println("Type checking...");
        TypeCheckerTree typeChecker = new TypeCheckerTree();
        typeChecker.visit(tree);
        System.out.println("Done.");
    }
}
