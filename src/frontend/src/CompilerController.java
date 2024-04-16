package frontend.src;

public class CompilerController {



    public void executeCompiler(String codeFilePath) {

        TokenData tokenData;

        Lexer lexer = new Lexer(codeFilePath);

        while (lexer.peekNextToken() != null) {

            tokenData = lexer.getNextToken();

            System.out.print(tokenData.getLexeme() + ", ");
        }

    }




}
