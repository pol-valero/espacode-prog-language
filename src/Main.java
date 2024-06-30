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

        String codeFilePath = "fibonacciRecSimp.ps";

        String TACfilepath = "generatedCode/code.tac";
        String MIPSfilepath = "generatedCode/code.asm";

        //testLexer(codeFilePath);

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(codeFilePath);

        ParseTree parseTree = syntaxAnalyzer.syntaxAnalysis();

        //System.out.println(parseTree);

        if (ErrorHandler.hasErrors()){
            System.out.println(ErrorHandler.getErrors());
        } else {
            TACGenerator tacGenerator = new TACGenerator();
            tacGenerator.generateTAC(parseTree, TACfilepath);

            MIPSGenerator mipsGenerator = new MIPSGenerator();
            mipsGenerator.TACtoMIPS(MIPSfilepath, TACfilepath);

        }

    }

}

