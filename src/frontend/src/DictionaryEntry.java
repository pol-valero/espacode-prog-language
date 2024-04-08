package frontend.src;

public class DictionaryEntry {
    private String token;
    private String regex;

    public DictionaryEntry(String token, String regex) {
        this.token = token;
        this.regex = regex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
