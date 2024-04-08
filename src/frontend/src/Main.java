package frontend.src;

public class Main {

    public static void main(String[] args) {

        Dictionary dictionary = new Dictionary();

        CodeScanner cs = new CodeScanner("ejemplo.co");

        String lexeme;
        String token;
        while ((lexeme = cs.getNextLexeme()) != null) {
            if (!lexeme.isEmpty()) {
                token = dictionary.findToken(lexeme);
                // TODO: Send the token and the value(lexeme) to backend now? :/
            }
        }

    }

}
