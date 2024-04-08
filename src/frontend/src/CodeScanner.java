package frontend.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class CodeScanner {
    private static Scanner scanner;
    private static final LinkedList<String> queue = new LinkedList<>();

    public CodeScanner(String filePath) {
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // TODO : Handle with errorHandler
        }

    }

    public String getNextLexeme() {
        if (queue.isEmpty()) {
            if (scanner.hasNext()) {
                String token = scanner.next();
                // next identify '(a' as a word. To adapt into our system we use a queue to get all tokens split.
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

}
