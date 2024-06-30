package frontend.src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class LexicAnalyzer {
    private static Scanner scanner;
    private static final LinkedList<String> queue = new LinkedList<>();
    private int lineNumCounter;
    private int currentWordLineNum;

    public LexicAnalyzer(String codeFilePath) {

        Dictionary dictionary = new Dictionary();
        dictionary.readDictionary();

        lineNumCounter = 1;
        try {
            scanner = new Scanner(new File(codeFilePath));
            scanner.useDelimiter("");   //We are going to read char by char

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // TODO : Handle with errorHandler
        }

    }

    //Reads next word (a word is separated by spaces or new lines). If a new line is found while reading the word, we increase the line counter. It returns "" if there are no more words.
    private String getScannerNext() {

        String next;
        StringBuilder token = new StringBuilder();
        boolean wordRead = false;
        boolean newLineInWordFound = false;

        do {

            if (!scanner.hasNext()) {
                break;
            }

            next = scanner.next();  //We are reading char by char (in order to be able to read the \n char)

            if (next.contains("\n")) {
                lineNumCounter++;

                //System.out.println("Line: " + line);
                if (wordRead) {
                    newLineInWordFound = true;
                    break;
                }

            }else if (next.equals(" ") || next.equals("\t")  || next.equals("\r")) {
                if (wordRead) {
                    break;
                }
            } else {
                wordRead = true;
                token.append(next);

            }

        } while (true);


        if (newLineInWordFound) {
            currentWordLineNum = lineNumCounter - 1;

        } else {
            currentWordLineNum = lineNumCounter;
        }

        //System.out.println("Line: " + currentWordLineNum + " Token: " + token.toString());
        return token.toString();

    }

    //We return the next lexeme and we remove it from the queue
    private String getNextLexeme() {
        if (queue.isEmpty()) {
            if (scanner.hasNext()) {
                //String token = scanner.next();
                String token = getScannerNext();

                //Check if token is a negative number. If it is, we avoid splitting the token by the "-" sign.
                if (token.contains("-") && token.length() > 1 && Character.isDigit(token.charAt(1)) ) {
                    String[] splitChars = token.split("(?=[{}();+*/',])|(?<=[{}();+*/',])");    //We avoid splitting by "-"
                    Collections.addAll(queue, splitChars);
                } else {
                    //To adapt into our system we use a queue to get all tokens split.
                    String[] splitChars = token.split("(?=[{}();+\\-*/',])|(?<=[{}();+\\-*/',])");
                    Collections.addAll(queue, splitChars);
                }
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
                //String token = scanner.next();
                String token = getScannerNext();

                //Check if token is a negative number. If it is, we avoid splitting the token by the "-" sign.
                if (token.contains("-") && token.length() > 1 && Character.isDigit(token.charAt(1)) ) {
                    String[] splitChars = token.split("(?=[{}();+*/',])|(?<=[{}();+*/',])");    //We avoid splitting by "-"
                    Collections.addAll(queue, splitChars);
                } else {
                    //To adapt into our system we use a queue to get all tokens split.
                    String[] splitChars = token.split("(?=[{}();+\\-*/',])|(?<=[{}();+\\-*/',])");
                    Collections.addAll(queue, splitChars);
                }
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

        if (lexeme != null && !lexeme.isEmpty()) {
            token = Dictionary.findToken(lexeme);

            if (token.equals("UNKNOWN")) {
                //If an unknown token is found, we add an error, and we continue searching for the next known token by calling getNextToken() recursively.
                ErrorHandler.addError("Error Linia " + currentWordLineNum + ":\n\t" + "Error de lexico: Token '" + lexeme + "' no conocido");
                return getNextToken();
            }

            return new TokenData(lexeme, token, currentWordLineNum);
        } else {
            //return null;
            return new TokenData("#", "EOF", currentWordLineNum); //# is the EOF token
        }

    }

    //TODO: Remove at the end. Probably no longer necessary
    /*public TokenData peekNextToken() {
        String lexeme = peekNextLexeme();
        String token;

        if (lexeme != null && !lexeme.equals("")) {
            token = Dictionary.findToken(lexeme);

            if (token.equals("UNKNOWN")) {
                ErrorHandler.addError("Error Linia " + currentWordLineNum + ":\n\t" + "Error de lexico: Token '" + lexeme + "' no conocido");
                return getNextToken();
            }

            return new TokenData(lexeme, token, currentWordLineNum);
        } else {
            //return null;
            return new TokenData("#", "EOF", currentWordLineNum); //# is the EOF token
        }
    }*/

    //TODO: Remove. Substituted by "line" field in TokenData
    public int getLineNumber() {
        return lineNumCounter;
    }

}
