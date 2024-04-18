package frontend.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Lexer {
    private static Scanner scanner;
    private static final LinkedList<String> queue = new LinkedList<>();
    private int line;
    public Lexer(String codeFilePath) {
        line = 0;
        try {
            scanner = new Scanner(new File(codeFilePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // TODO : Handle with errorHandler
        }

    }

    //We return the next lexeme and we remove it from the queue
    private String getNextLexeme() {
        if (queue.isEmpty()) {
            if (scanner.hasNext()) {
                String token = scanner.next();
                line += 1; //TODO: Fix this. Currently "line" is the number of tokens read, not the actual line number.

                //TODO: identify '(a' as a word. This is not a priority, first we will only implement basic stuff like integers, sum...
                //To adapt into our system we use a queue to get all tokens split.
                String[] splitChars = token.split("(?=[{}();+\\-*/])|(?<=[{}();+\\-*/])");
                Collections.addAll(queue, splitChars);
                if (!queue.isEmpty()) {
                    return queue.poll();
                } else {
                    ////////////////////////////////
                    if (scanner.hasNext()) {
                        return getNextLexeme();
                    } else {
                        return null;
                    }
                    ///////////////////////////////
                }
            } else {
                return null;
            }
        } else {
            return queue.poll();
        }
    }

    //We return the next lexeme, but it stays in the queue
    private String peekNextLexeme() {
        if (queue.isEmpty()) {
            if (scanner.hasNext()) {
                String token = scanner.next();
                //TODO: identify '(a' as a word. This is not a priority, first we will only implement basic stuff like integers, sum...
                //To adapt into our system we use a queue to get all tokens split.
                String[] splitChars = token.split("(?=[{}();+\\-*/])|(?<=[{}();+\\-*/])");
                Collections.addAll(queue, splitChars);
                if (!queue.isEmpty()) {
                    return queue.peek();
                } else {
                    ////////////////////////////////
                    if (scanner.hasNext()) {
                        return peekNextLexeme();
                    } else {
                        return null;
                    }
                    ///////////////////////////////
                }
            } else {
                return null;
            }
        } else {
            return queue.peek();
        }
    }

    public TokenData getNextToken() {
        String lexeme = getNextLexeme();
        String token;

        if (lexeme != null) {
            token = Dictionary.findToken(lexeme);
            //System.out.println(lexeme + " -> " + token);
            return new TokenData(lexeme, token);
        } else {
            return null;
        }

    }

    public TokenData peekNextToken() {
        String lexeme = peekNextLexeme();
        String token;

        if (lexeme != null) {
            token = Dictionary.findToken(lexeme);
            return new TokenData(lexeme, token);
        } else {
            return null;
        }
    }

    public int getLineNumber() {
        return line;
    }

}
