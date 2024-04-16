import frontend.src.CompilerController;
import frontend.src.Dictionary;

public class Main {

    public static void main(String[] args) {

        String codeFilePath = "example1.ps";

        Dictionary dictionary = new Dictionary();
        dictionary.readDictionary();

        CompilerController compilerController = new CompilerController();

        compilerController.executeCompiler(codeFilePath);

    }

}
