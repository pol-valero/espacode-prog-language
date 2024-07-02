package errors;

public class ErrorHandler {

    private static StringBuilder errors = new StringBuilder();

    public static void addError(String error){
        errors.append(error);
        errors.append("\n");
    }

    public static StringBuilder getErrors() {
        return errors;
    }

    public static boolean hasErrors() {
        return errors.length() > 0;
    }

}
