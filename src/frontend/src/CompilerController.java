package frontend.src;

import frontend.src.model.ParseTree;

public class CompilerController {



    public void executeCompiler(String codeFilePath) {


        LexicAnalyzer lexicAnalyzer = new LexicAnalyzer(codeFilePath);

        /*TokenData tokenData;

        while (lexicAnalyzer.peekNextToken() != null) {

            tokenData = lexicAnalyzer.getNextToken();

            System.out.println("Line: " + tokenData.getLine() + " - " + tokenData.getLexeme() + " -> " + tokenData.getToken());
         }*/

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicAnalyzer);

        ParseTree parseTree = syntaxAnalyzer.syntaxAnalysis();
        if (syntaxAnalyzer.hasErrors()){
            System.out.println("\nErrores de sintaxis:\n");
            System.out.println(syntaxAnalyzer.getErrors());
        } else {
            System.out.println("\nSintaxis correcta\n");
            System.out.println(parseTree);
        }

    }




}
