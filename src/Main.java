import frontend.src.CompilerController;
import frontend.src.Dictionary;

//Comment

public class Main {

    public static void main(String[] args) {

        String codeFilePath = "fibonacci.ps";

        Dictionary dictionary = new Dictionary();
        dictionary.readDictionary();

        CompilerController compilerController = new CompilerController();

        compilerController.executeCompiler(codeFilePath);

    }

}
