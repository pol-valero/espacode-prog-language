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

        //If the match method finds an error and return null, we will not add the child to the tree. TODO: Check if this is the correct approach
        //if (child != null) {
            children.add(child);
        //}
        //REMARK: instead of not adding the child if it is null, we will add it because when we pass a tree to semantic analyzer we want to check if a node is null
        //and if it is, we will know that we don't have to do semantic analysis on that node because it has a syntax error.
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
            if(child != null){  //If a child is null, there has been a syntax error and the tree will be partially incomplete, so we will not print the child that contains the syntax error
                sb.append(child.toStringHelper(indent + 1));
            }
        }
        return sb.toString();
    }
}

