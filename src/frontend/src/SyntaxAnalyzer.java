package frontend.src;

public class SyntaxAnalyzer {
    Lexer lexer;
    String currentToken;

    public SyntaxAnalyzer(Lexer lexer) {
        this.lexer = lexer;
    }

    public void syntaxAnalysis() {

        TokenData tokenData;

        while (lexer.peekNextToken() != null) {

            tokenData = lexer.getNextToken();

            //System.out.print(tokenData.getLexeme() + ", ");
            //System.out.println(tokenData.getLexeme() + " -> " + tokenData.getToken());

            //TODO: CALL ALL THE FUNCTIONS TO CHECK SYNTAX HERE

        }

    }

    private void match(String expectedToken) {

        if (currentToken.equals(expectedToken)) {
            currentToken = lexer.getNextToken().getToken();
        } else {
            System.out.println("Syntax error: expected " + expectedToken + ", found " + currentToken);
            System.exit(1);
        }

    }







}
