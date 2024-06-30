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


        //createMIPSfile(MIPScode, MIPSfilepath); //Create the code.asm file
    }

    private String generateMIPS(LinkedList<String> TACsentences) {
        String MIPScode = "";

        return MIPScode;
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
