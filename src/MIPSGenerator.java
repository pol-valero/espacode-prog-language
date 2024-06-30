import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class MIPSGenerator {


    public void TACtoMIPS(String MIPSfilepath, String TACfilepath) {

        LinkedList<String> TACsentences;
        String MIPScode = "";

        if (!TACfileIsValid(TACfilepath)) {
            return;
        }

        TACsentences = loadTACsentences(TACfilepath);

        MIPScode = generateMIPS(TACsentences);

        System.out.println("\n" + "MIPS CODE\n\n" + MIPScode);


        //createMIPSfile(MIPScode, MIPSfilepath); //Create the code.asm file
    }

    private String generateMIPS(LinkedList<String> TACsentences) {
        StringBuilder MIPScode = new StringBuilder();

        MIPScode.append(".text\nj $main\n\n");

        for (String sentence : TACsentences) {
            String[] words = sentence.trim().split("\\s+"); //We remove tabs and spaces

            if (words.length == 1) {
                if (words[0].contains(":")) {
                    addLabel(MIPScode, words[0]);
                } else {
                    MIPScode.append("Error: MIPS word not recognized\n");
                    //TODO: Add all errors like this to ErrorHandler?
                }
            } else if (words.length == 2) {
                if (words[0].equals("return")) {
                    addReturn(MIPScode, words[1]);
                } else if (words[0].equals("goto")) {
                    addGoto(MIPScode, words[1]);
                } else if (words[0].equals("call")) {
                    //addStandaloneFunctionCall(MIPScode, words[1]); //Function call that is not assigned, and which return value is not used
                } else {
                    MIPScode.append("Error: MIPS word not recognized\n");
                }
            } else if (words.length == 3) {

            }


        }

        return MIPScode.toString();
    }

    private void addLabel(StringBuilder MIPScode, String label) {
        MIPScode.append("$" + label + "\n");
    }

    private void addReturn(StringBuilder MIPScode, String value) {
        if (value.contains("t")) {
            MIPScode.append("\tmove $v0, $" + value + "\n");
        } else {
            MIPScode.append("\tli $v0, " + value + "\n");
        }
        MIPScode.append("\tjr $ra\n");
    }

    private void addGoto(StringBuilder MIPScode, String label) {
        MIPScode.append("\tj $" + label + "\n");
    }

    private boolean TACfileIsValid(String TACfilepath) {

        Scanner scanner;

        //We check that the file exists and is not empty
        try {
            scanner = new Scanner(new File(TACfilepath));

            if (!scanner.hasNext()) {
                System.out.println("The TAC file is empty");    //TODO: Add this to ErrorHandler?
                return false;
            }

        } catch (FileNotFoundException e) {
            System.out.println("The TAC file does not exist");  //TODO: Add this to ErrorHandler?
            return false;
        }

        return true;
    }

    private LinkedList<String> loadTACsentences(String TACfilepath) {

        LinkedList<String> TACsentences = new LinkedList<>();
        Scanner scanner;

        String aux;

        try {
            scanner = new Scanner(new File(TACfilepath));

            while (scanner.hasNext()) {
                aux = scanner.nextLine();

                if (!aux.isEmpty()) {
                    TACsentences.add(aux);
                }
            }

        } catch (FileNotFoundException e) {
            return null;
        }

        return TACsentences;

    }


}
