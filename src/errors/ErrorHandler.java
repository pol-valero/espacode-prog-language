package errors;

public class ErrorHandler {

    private static StringBuilder errors = new StringBuilder();
    private static StringBuilder TACgenErrors = new StringBuilder();
    private static StringBuilder MIPSgenErrors = new StringBuilder();


    public static void addError(String error){
        errors.append(error);
        errors.append("\n");
    }

    public static StringBuilder getErrors() {
        return errors;
    }

    public static void addTACgenErrors(String error) {
        TACgenErrors.append(error + "\n");
    }

    public static void addMIPSgenErrors(String error) {
        MIPSgenErrors.append(error + "\n");
    }

    public static StringBuilder getTACgenErrors() {
        return TACgenErrors;
    }

    public static StringBuilder getMIPSgenErrors() {
        return MIPSgenErrors;
    }

    public static boolean hasErrors() {
        return errors.length() > 0;
    }

    public static boolean hasTACgenErrors() {
        return TACgenErrors.length() > 0;
    }

    public static boolean hasMIPSgenErrors() {
        return MIPSgenErrors.length() > 0;
    }

}
