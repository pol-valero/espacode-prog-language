package frontend.src;

import frontend.src.model.ParseTree;

public class CompilerController {



    public void executeCompiler(String codeFilePath) {


        LexicAnalyzer lexicAnalyzer = new LexicAnalyzer(codeFilePath);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicAnalyzer);

        ParseTree parseTree = syntaxAnalyzer.syntaxAnalysis();
        if (syntaxAnalyzer.hasErrores()){
            System.out.println("\nErrores de sintaxis:\n");
            System.out.println(syntaxAnalyzer.getErrors());
        } else {
            System.out.println("\nSintaxis correcta\n");
            System.out.println(parseTree);
        }

    }




}
