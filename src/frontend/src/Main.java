package frontend.src;

public class Main {

    public static void main(String[] args) {

        Dictionary dictionary = new Dictionary();

        Lexer lexer = new Lexer("example1.ps");

        String lexeme;
        String token;

        while (lexer.peekNextLexeme() != null) {
                lexeme = lexer.getNextLexeme();
                token = dictionary.findToken(lexeme);
                //System.out.print(token + " " + lexeme + ", ");
                System.out.print(lexeme + ", ");
        }

    }

}
