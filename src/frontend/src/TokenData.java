package frontend.src;

public class TokenData {
    private String lexeme;
    private String token; //TODO: Create "Token" enum?

    public TokenData(String lexeme, String token) {
        this.lexeme = lexeme;
        this.token = token;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getToken() {
        return token;
    }

}


