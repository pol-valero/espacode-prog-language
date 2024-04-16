package frontend.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Lexer {
    private static Scanner scanner;
    private static final LinkedList<String> queue = new LinkedList<>();

    public Lexer(String filePath) {
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // TODO : Handle with errorHandler
        }

    }

    //We return the next lexeme and we remove it from the queue
    public String getNextLexeme() {
        if (queue.isEmpty()) {
            if (scanner.hasNext()) {
                String token = scanner.next();
                //TODO: identify '(a' as a word. This is not a priority, first we will only implement basic stuff like integers, sum...
                //To adapt into our system we use a queue to get all tokens split.
                String[] splitChars = token.split("(?=[{}();+\\-*/])|(?<=[{}();+\\-*/])");
                Collections.addAll(queue, splitChars);
                if (!queue.isEmpty()) {
                    return queue.poll();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return queue.poll();
        }
    }

    //We return the next lexeme, but it stays in the queue
    public String peekNextLexeme() {
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
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return queue.peek();
        }
    }

}
