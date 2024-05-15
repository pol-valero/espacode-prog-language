package frontend.src;

import frontend.src.model.ParseTree;

public class TACGenerator {
    int tempCounter = 0;
    //TODO: Change the function below to generate TAC code
    public void generateTAC(ParseTree parseTree) {
        switch (parseTree.getToken()) {
            case "FUNCION":
                System.out.println(parseTree.getLexeme() + ":");
                for (ParseTree child : parseTree.getChildren()) {
                    generateTAC(child);
                }
                break;
            case "PRINCIPAL":
                if (parseTree.getLexeme() == null){
                    System.out.println("main:");
                    for (ParseTree child : parseTree.getChildren()) {
                        generateTAC(child);
                    }
                }
                break;
            case "DECLARACION_VARIABLE":
                generateVariable(parseTree);
                break;
            default:
                break;
        }
        for (ParseTree child : parseTree.getChildren()) {
            generateTAC(child);
        }
    }
    //!Below here it's the correct implementation of the TACGenerator
    private void generateVariable(ParseTree parseTree) {

        String id = parseTree.getChildren().get(1).getLexeme();

        if (parseTree.getChildren().get(2).getChildren() != null) {
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



