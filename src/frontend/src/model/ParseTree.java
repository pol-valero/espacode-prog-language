package frontend.src.model;

import java.util.ArrayList;
import java.util.List;

public class ParseTree {
    private String token;
    private String lexeme;
    private List<ParseTree> children;

    //TODO: Add "node" class (order of the children is important. ex.- to do operations)?

    public ParseTree(String token) {
        this.token = token;
        this.children = new ArrayList<>();
    }

    public ParseTree(String token, String lexeme) {
        this.token = token;
        this.lexeme = lexeme;
        this.children = new ArrayList<>();
    }

    public void addChild(ParseTree child) {
        children.add(child);
    }

    public String getToken() {
        return token;
    }

    public String getLexeme() {
        return lexeme;
    }

    public List<ParseTree> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return toStringHelper(0);
    }

    private String toStringHelper(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ".repeat(Math.max(0, indent)));
        sb.append(token);
        if (lexeme != null) {
            sb.append(": ");
            sb.append(lexeme);
        }
        sb.append("\n");
        for (ParseTree child : children) {
            if(child != null){
                sb.append(child.toStringHelper(indent + 1));
            }
        }
        return sb.toString();
    }
}

