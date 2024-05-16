import frontend.src.*;
import frontend.src.model.ParseTree;

public class Main {

    private static void testLexer(String codeFilePath) {
        LexicAnalyzer lexicAnalyzer = new LexicAnalyzer(codeFilePath);

        TokenData tokenData = lexicAnalyzer.getNextToken();

        while (!tokenData.equals("EOF")) {

            System.out.println("Line: " + tokenData.getLine() + " - " + tokenData.getLexeme() + " -> " + tokenData.getToken());
            tokenData = lexicAnalyzer.getNextToken();

        }
    }

    public static void main(String[] args) {

        String codeFilePath = "fibonacciRec.ps";

        //testLexer(codeFilePath);

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(codeFilePath);

        ParseTree parseTree = syntaxAnalyzer.syntaxAnalysis();

        System.out.println(parseTree);

        if (ErrorHandler.hasErrors()){
            System.out.println("\nErrores de sintaxis:\n");
            System.out.println(ErrorHandler.getErrors());
        } else {
            System.out.println("\nSintaxis correcta\n");
            System.out.println(parseTree);
        }

    }

}
