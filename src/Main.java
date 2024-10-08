import backend.MIPSGenerator;
import errors.ErrorHandler;
import frontend.*;
import frontend.model.ParseTree;

public class Main {

    public static void main(String[] args) {

        String codeFilePath = "examples/fibonacciRecSimp.ps";

        String TACfilepath = "generatedCode/code.tac";
        String MIPSfilepath = "generatedCode/code.asm";


        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(codeFilePath);

        ParseTree parseTree = syntaxAnalyzer.syntaxAnalysis();

        //System.out.println(parseTree);

        if (ErrorHandler.hasErrors()){

            System.out.println(ErrorHandler.getErrors());

        } else {

            TACGenerator tacGenerator = new TACGenerator();
            tacGenerator.generateTAC(parseTree, TACfilepath);



            if (ErrorHandler.hasTACgenErrors()){
                System.out.println(ErrorHandler.getTACgenErrors());
            } else {
                MIPSGenerator mipsGenerator = new MIPSGenerator();
                mipsGenerator.TACtoMIPS(MIPSfilepath, TACfilepath);
            }

            if (ErrorHandler.hasMIPSgenErrors()){
                System.out.println(ErrorHandler.getMIPSgenErrors());
            }

        }

    }

}

