package frontend.src;

import frontend.src.model.ParseTree;

public class CompilerController {



    public void executeCompiler(String codeFilePath) {


        Lexer lexer = new Lexer(codeFilePath);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexer);

        ParseTree parseTree = syntaxAnalyzer.syntaxAnalysis();
        if (syntaxAnalyzer.hasErrores()){
            System.out.println("\nErrores de sintaxis:\n");
            System.out.println(syntaxAnalyzer.getErrores());
        } else {
            System.out.println("\nSintaxis correcta\n");
            System.out.println(parseTree);
        }

    }




}
