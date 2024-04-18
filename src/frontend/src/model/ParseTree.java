package frontend.src.model;

import java.util.ArrayList;
import java.util.List;

public class ParseTree {
    private String type;
    private String value;
    private List<ParseTree> children;

    public ParseTree(String type) {
        this.type = type;
        this.children = new ArrayList<>();
    }

    public ParseTree(String type, String value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }

    public void addChild(ParseTree child) {
        children.add(child);
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
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
        sb.append(type);
        if (value != null) {
            sb.append(": ");
            sb.append(value);
        }
        sb.append("\n");
        for (ParseTree child : children) {
            sb.append(child.toStringHelper(indent + 1));
        }
        return sb.toString();
    }
}

