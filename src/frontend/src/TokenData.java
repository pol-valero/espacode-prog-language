package frontend.src;

public class TokenData {
    private String lexeme;
    private String token; //TODO: Create "Token" enum?

    private int line;

    public TokenData(String lexeme, String token) {
        this.lexeme = lexeme;
        this.token = token;
    }

    public TokenData(String lexeme, String token, int line) {
        this.lexeme = lexeme;
        this.token = token;
        this.line = line;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getToken() {
        return token;
    }

    public int getLine() {
        return line;
    }

}


