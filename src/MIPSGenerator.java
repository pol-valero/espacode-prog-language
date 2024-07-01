import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class MIPSGenerator {

    private void createMIPSfile(String MIPScode, String MIPSfilepath) {
        try {
            FileWriter fileWriter = new FileWriter(MIPSfilepath);
            fileWriter.write(MIPScode);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while opening or creating " + MIPSfilepath);
        }
    }
    public void TACtoMIPS(String MIPSfilepath, String TACfilepath) {

        LinkedList<String> TACsentences;
        String MIPScode = "";

        if (!TACfileIsValid(TACfilepath)) {
            return;
        }

        TACsentences = loadTACsentences(TACfilepath);

        MIPScode = generateMIPS(TACsentences);

        System.out.println("\n" + "MIPS CODE\n\n" + MIPScode);


        createMIPSfile(MIPScode, MIPSfilepath); //Create the code.asm file
    }

    private String generateMIPS(LinkedList<String> TACsentences) {
        StringBuilder MIPScode = new StringBuilder();

        MIPScode.append(".text\nj $principal\n\n");

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
                    addStandaloneFunctionCall(MIPScode, words[1]); //Function call that is not assigned, and which return value is not used
                } else {
                    MIPScode.append("Error: MIPS word not recognized\n");
                }
            } else if (words.length == 3) {
                if (words[0].equals("writeParam")) {
                    addFunctionParamInsert(MIPScode, words[1], words[2]);
                } else if (words[0].equals("readParam")) {
                    addFunctionParamRetrieval(MIPScode, words[1], words[2]);
                } else if (words[1].equals("=")) {
                    addAssignation(MIPScode, words[0], words[2]);
                } else {
                    MIPScode.append("Error: MIPS word not recognized\n");
                }
            } else if (words.length == 4) {
                if (words[1].equals("=") && words[2].equals("call")) {
                    addFunctionCallAssignation(MIPScode, words[0], words[3]);
                } else {
                    MIPScode.append("Error: MIPS word not recognized\n");
                }
            } else if (words.length == 5) {
                if (words[1].equals("=")) {
                    addOperationAssignation(MIPScode, words[0], words[2], words[3], words[4]);
                } else {
                    MIPScode.append("Error: MIPS word not recognized\n");
                }
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

    private void addStandaloneFunctionCall(StringBuilder MIPScode, String function) {

        int i;
        int j;

        for (i = 0; i < 10; i++) {
            //We save in the RAM the 10 registers (t0-t9) that can be used to store temp info in the function
            MIPScode.append("\tsw $t" + i + ", -" + i * 4 + "($sp)\n");
        }

        for (j = 0; j < 5; j++) {
            //We save in the RAM the 4 registers (a0-a3) that can be used to pass arguments to the functions
            MIPScode.append("\tsw $a" + j + ", -" + i * 4 + "($sp)\n");
            i++;
        }

        //We save in the RAM the return address
        MIPScode.append("\tsw $ra, -" + i * 4 + "($sp)\n");


        //The stack pointer is decremented by 4 bytes for each register (60 bytes total), as the stack grows downwards.
        //Although the "subi" operation does not exist in MIPS, the program MARS4.5 allows it (it is a pseudo-instruction)
        MIPScode.append("\tsubi $sp, $sp, 60\n");


        //Make the function call
        MIPScode.append("\tjal $" + function + "\n");

        //Restore the registers, loading from RAM
        MIPScode.append("\taddi $sp, $sp, 60\n");

        for (i = 0; i < 10; i++) {
            //We load from the RAM the 10 registers (t0-t9) that can be used to store temp info in the function
            MIPScode.append("\tlw $t" + i + ", -" + i * 4 + "($sp)\n");
        }

        for (j = 0; j < 5; j++) {
            //We load from the RAM the 4 registers (a0-a3) that can be used to pass arguments to the functions
            MIPScode.append("\tlw $a" + j + ", -" + i * 4 + "($sp)\n");
            i++;
        }

        //We load from the RAM the return address
        MIPScode.append("\tlw $ra, -" + i * 4 + "($sp)\n");

    }

    private void addFunctionCallAssignation(StringBuilder MIPScode, String tempVar, String function) {
            addStandaloneFunctionCall(MIPScode, function);
            MIPScode.append("\tmove $" + tempVar + ", $v0\n");
    }

    private void addFunctionParamInsert(StringBuilder MIPScode, String paramNum, String value) {

        //Depending on whether the value is a constant or a temp variable, we use "li" or "move" respectively
        if (value.contains("t")) {
            MIPScode.append("\tmove $a" + paramNum + ", $" + value + "\n");
        } else {
            MIPScode.append("\tli $a" + paramNum + ", " + value + "\n");
        }

    }

    private void addFunctionParamRetrieval(StringBuilder MIPScode, String paramNum, String tempVar) {
        MIPScode.append("\tmove $" + tempVar + ", $a" + paramNum + "\n");
    }

    private void addAssignation(StringBuilder MIPScode, String tempVar, String value) {

        //Depending on whether the value is a constant or a temp variable, we use "li" or "move" respectively
        if (value.contains("t")) {
            MIPScode.append("\tmove $" + tempVar + ", $" + value + "\n");
        } else {
            MIPScode.append("\tli $" + tempVar + ", " + value + "\n");
        }

    }

    private void addOperationAssignation(StringBuilder MIPScode, String tempVar, String op1, String operator, String op2) {

        switch (operator) {
            case "+":

                if (op1.contains("t") && op2.contains("t")) {
                    MIPScode.append("\tadd $" + tempVar + ", $" + op1 + ", $" + op2 + "\n");
                } else if (op1.contains("t") && !op2.contains("t")) {
                    MIPScode.append("\taddi $" + tempVar + ", $" + op1 + ", " + op2 + "\n");
                } else if (!op1.contains("t") && op2.contains("t")) {
                    //As the sum is commutative, we can swap the operands and use the same "addi" instruction
                    MIPScode.append("\taddi $" + tempVar + ", $" + op2 + ", " + op1 + "\n");
                } else {
                    //This is the case where both operands are constants, we cannot generate the MIPS instructions directly
                    //We load the constants into registers s0 and s1 and then add them
                    MIPScode.append("\tli $s0, " + op1 + "\n");
                    MIPScode.append("\tli $s1, " + op2 + "\n");
                    MIPScode.append("\tadd $" + tempVar + ", $s0, $s1"+ "\n");
                }

                break;

            case "-":

                if (op1.contains("t") && op2.contains("t")) {
                    MIPScode.append("\tsub $" + tempVar + ", $" + op1 + ", $" + op2 + "\n");
                } else if (op1.contains("t") && !op2.contains("t")) {
                    MIPScode.append("\tsubi $" + tempVar + ", $" + op1 + ", " + op2 + "\n");
                } else {
                    //This is the case where the first operand is a constant or both operands are constants, we cannot generate the MIPS instructions directly
                    //We load the constants into registers s0 and s1 and then subtract them
                    MIPScode.append("\tli $s0, " + op1 + "\n");
                    MIPScode.append("\tli $s1, " + op2 + "\n");
                    MIPScode.append("\tsub $" + tempVar + ", $s0, $s1"+ "\n");
                }

                break;

            case "/":

                if (op1.contains("t") && op2.contains("t")) {
                    //We can only generate the MIPS instructions directly if both operands are temp variables
                    MIPScode.append("\tdiv $" + op1 + ", $" + op2 + "\n");
                    MIPScode.append("\tmflo $" + tempVar + "\n");
                } else {
                    //This is the case where one operand is a constant or both operands are constants, we cannot generate the MIPS instructions directly
                    //We load the constants into registers s0 and s1 and then divide them
                    MIPScode.append("\tli $s0, " + op1 + "\n");
                    MIPScode.append("\tli $s1, " + op2 + "\n");
                    MIPScode.append("\tdiv $s0, $s1\n");
                    MIPScode.append("\tmflo $" + tempVar + "\n");
                }

                break;

            case "*":

                if (op1.contains("t") && op2.contains("t")) {
                    //We can only generate the MIPS instructions directly if both operands are temp variables
                    MIPScode.append("\tmult $" + op1 + ", $" + op2 + "\n");
                    MIPScode.append("\tmflo $" + tempVar + "\n");
                } else {
                    //This is the case where one operand is a constant or both operands are constants, we cannot generate the MIPS instructions directly
                    //We load the constants into registers s0 and s1 and then multiply them
                    MIPScode.append("\tli $s0, " + op1 + "\n");
                    MIPScode.append("\tli $s1, " + op2 + "\n");
                    MIPScode.append("\tmult $s0, $s1\n");
                    MIPScode.append("\tmflo $" + tempVar + "\n");
                }

                break;

            default:
                MIPScode.append("Error: Operator not recognized\n");
        }

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
            System.out.println("The TAC file does not exist in " + TACfilepath);  //TODO: Add this to ErrorHandler?
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
