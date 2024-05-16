package frontend.src;

import frontend.src.model.ParseTree;

public class TACGenerator {
    int tempCounter = 0;
    //TODO: Change the function below to generate TAC code
    public void generateTAC(ParseTree parseTree) {
        generateCode(parseTree);
    }
    private void generateCode(ParseTree parseTree) {
        generateFunctions(parseTree.getChildren().get(0));
        generateMain(parseTree.getChildren().get(1));
    }

    private void generateFunctions(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        generateFunction(parseTree.getChildren().get(0));
        generateFunctions(parseTree.getChildren().get(1));
    }
    private void generateFunction(ParseTree parseTree) {
        System.out.println(parseTree.getChildren().get(1).getLexeme() + ":");
        generateBlock(parseTree.getChildren().get(5));
    }
    private void generateMain(ParseTree parseTree) {
        System.out.println("\nprincipal:");
        generateBlock(parseTree.getChildren().get(3));
    }
    private void generateBlock(ParseTree parseTree) {
        generateSubBlock(parseTree.getChildren().get(1));
    }
    private void generateSubBlock(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        generateStatement(parseTree.getChildren().get(0));
        generateSubBlock(parseTree.getChildren().get(1));
    }
    private void generateStatement(ParseTree parseTree) {
        switch (parseTree.getChildren().get(0).getToken()) {
            case "DECLARACION_VARIABLE":
                generateVariable(parseTree.getChildren().get(0));
                break;
            case "ID":
                if (parseTree.getChildren().get(1).getChildren().get(0).getToken().equals("ASIGNACION_VARIABLE")) {
                    generateNewAssignment(parseTree);
                }else if (parseTree.getChildren().get(1).getChildren().get(0).getToken().equals("LLAMADA_FUNCION")) {
                    //generateFunctionCall(parseTree.getChildren().get(0));
                }
                break;
            default:
                break;
        }
    }
    //!Below here it's the correct implementation of the TACGenerator
    private void generateNewAssignment(ParseTree parseTree) {
        String id = parseTree.getChildren().get(0).getLexeme();
        String result = generateExpression(parseTree.getChildren().get(1).getChildren().get(0).getChildren().get(1).getChildren().get(0));
        System.out.println(id + " = " + result);
    }
    //TODO: Fix the function below
    private void generateVariable(ParseTree parseTree) {

        String id = parseTree.getChildren().get(1).getLexeme();
        //This
        if (parseTree.getChildren().get(2).getChildren().get(0).getToken().equals("ASIGNACION_VARIABLE")) {
            String result = generateExpression(parseTree.getChildren().get(2).getChildren().get(0).getChildren().get(1).getChildren().get(0));
            System.out.println(id + " = " + result);
        }
    }
    public String generateExpression(ParseTree parseTree) {
        String Expression = generateTerm(parseTree.getChildren().get(0));
        String SecondExpression = generateExpression_1(parseTree.getChildren().get(1));
        if (SecondExpression == null) {
            return Expression;
        }
        System.out.println("t" + tempCounter++ + " = " + Expression + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondExpression);

        return "t" + (tempCounter - 1);
    }

    public String generateExpression_1(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return null;
        }else{
            String Expression = generateTerm(parseTree.getChildren().get(1));
            String SecondExpression = generateExpression_1(parseTree.getChildren().get(2));
            if (SecondExpression == null) {
                return Expression;
            }
            System.out.println("t" + tempCounter++ + " = " + Expression + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondExpression);
            return "t" + (tempCounter - 1);
        }
    }

    public String generateTerm(ParseTree parseTree) {
        String Term = generateFactor(parseTree.getChildren().get(0));
        String SecondTerm = generateTerm_1(parseTree.getChildren().get(1));

        if (SecondTerm == null) {
            return Term;
        }
        System.out.println("t" + tempCounter++ + " = " + parseTree.getChildren().get(0).getChildren().get(0).getLexeme() + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondTerm);

        return "t" + (tempCounter - 1);
    }
    public String generateTerm_1(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return null;
        } else {
            String Term = generateFactor(parseTree.getChildren().get(1));
            String SecondTerm = generateTerm_1(parseTree.getChildren().get(2));
            if (SecondTerm == null) {
                return Term;
            }
            System.out.println("t" + tempCounter + " = " + generateFactor(parseTree.getChildren().get(3)) + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondTerm);
            tempCounter++;
            return "t" + (tempCounter - 1);
        }
    }

    public String generateFactor(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return parseTree.getLexeme();
        } else {
            return generateFactor(parseTree.getChildren().get(0));
        }
    }

}



