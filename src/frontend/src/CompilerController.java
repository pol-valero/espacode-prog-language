package frontend.src;

public class CompilerController {



    public void executeCompiler(String codeFilePath) {


        Lexer lexer = new Lexer(codeFilePath);
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexer);

        syntaxAnalyzer.syntaxAnalysis();


    }




}
