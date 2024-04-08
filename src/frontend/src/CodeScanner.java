package frontend.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CodeScanner {
    private Scanner scanner; // TODO: Implement custom one to handle ; ( { etc...

    public CodeScanner(String filePath) {
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); //TODO : Handle with errorHandler
        }

    }

    public String getNextLexeme() {
        if (scanner.hasNext()) {
            String lexeme = scanner.next();
            if (lexeme.contains(";")) lexeme = lexeme.substring(0, lexeme.indexOf(";"));
            return lexeme;
        } else {
            scanner.close();
            return null;
        }
    }

}
