package frontend.src;

import frontend.src.model.ParseTree;

import java.util.LinkedList;
import java.util.Queue;

public class TACGenerator {
    int tempCounter = 0;
    Queue<String> labels = new LinkedList<>();
    Queue<String> functionLabels = new LinkedList<>();
    //TODO: Change the function below to generate TAC code
    public void generateTAC(ParseTree parseTree) {
        generateCode(parseTree);
    }
    // <CODIGO> ::= <FUNCIONES> <PRINCIPAL>
    private void generateCode(ParseTree parseTree) {
        generateFunctions(parseTree.getChildren().get(0));
        generateMain(parseTree.getChildren().get(1));
    }
    // <FUNCIONES> ::= <FUNCION> <FUNCIONES> | e
    private void generateFunctions(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        generateFunction(parseTree.getChildren().get(0));
        generateFunctions(parseTree.getChildren().get(1));
    }

    // <FUNCION> ::= <TIPO_FUNCION> ID PARENTESIS_ABRIR  <PARAMETROS_DECLARACION_FUNCION> PARENTESIS_CERRAR <BLOQUE>
    private void generateFunction(ParseTree parseTree) {
        String functionType = generateFunctionType(parseTree.getChildren().get(0));
        String functionName = parseTree.getChildren().get(1).getLexeme();
        System.out.println("\n" + functionName + ":");
        generateBlock(parseTree.getChildren().get(5));
    }
    // <TIPO_FUNCION> ::= <TIPO> | VACIO
    private String generateFunctionType(ParseTree parseTree) {
        if (parseTree.getChildren().get(0).getToken().equals("VACIO")) {
            return "void";
        }
        return generateType(parseTree.getChildren().get(0));
    }
    // <TIPO> ::= TIPO_ENTERO | TIPO_DECIMAL | TIPO_CARACTER
    private String generateType(ParseTree parseTree) {
        return parseTree.getChildren().get(0).getLexeme();
    }
    //<PARAMETROS_DECLARACION_FUNCION> ::= <TIPO> ID <PARAMETROS_DECLARACION_FUNCION’> | e

    // <PARAMETROS_DECLARACION_FUNCION’> ::= COMA <TIPO> ID <PARAMETROS_DECLARACION_FUNCION’> | e

    // <BLOQUE> ::= inicio <SUBBLOQUE> fin
    private void generateBlock(ParseTree parseTree) {
        generateSubBlock(parseTree.getChildren().get(1));
    }
    // <SUBBLOQUE> ::=  <SENTENCIA_SUBBLOQUE> <SUBBLOQUE> | e
    private void generateSubBlock(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        generateStatement(parseTree.getChildren().get(0));
        generateSubBlock(parseTree.getChildren().get(1));
    }
    // <SENTENCIA_SUBBLOQUE> ::= <DECLARACION_VARIABLE> | ID <SENTENCIA_ID_SUBBLOQUE> | <MIENTRAS_EXPRESION> | <SI_EXPRESION> | <RETORNO_EXPRESION>
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
            case "MIENTRAS_EXPRESION":
                generateWhileStatement(parseTree); // TODO: Check because it is not implemented in the grammar
                break;
            case "SI_EXPRESION":
                generateIfStatement(parseTree);
                break;
            default:
                break;
        }
    }
    // <DECLARACION_VARIABLE> ::= <TIPO> ID  <DECLARACION_VARIABLE’>
    private void generateVariable(ParseTree parseTree) {
        String id = parseTree.getChildren().get(1).getLexeme();
        String result = generateVariablePrime(parseTree.getChildren().get(2));
        System.out.println(id + result);
    }
    // <DECLARACION_VARIABLE’> ::=  <ASIGNACION_VARIABLE> | PUNTO_Y_COMA
    private String generateVariablePrime(ParseTree parseTree) {
        if (parseTree.getChildren().get(0).getToken().equals("ASIGNACION_VARIABLE")) {
            return generateNewAssignment(parseTree.getChildren().get(0));
        }
        return ";";
    }

    // <SENTENCIA_ID_SUBBLOQUE> ::= <LLAMADA_FUNCION> PUNTO_Y_COMA | <ASIGNACION_VARIABLE>
    // <ASIGNACION_VARIABLE> ::= IGUAL_ASIGNACION <ASIGNACION_VARIABLE’> PUNTO_Y_COMA
    private String generateNewAssignment(ParseTree parseTree) {
        String result = generateAssignmentVariablePrime(parseTree.getChildren().get(1));
        return " = " + result;
    }
    // <ASIGNACION_VARIABLE’> ::= <EXPRESION> | <CARACTER>
    private String generateAssignmentVariablePrime(ParseTree parseTree) {
        if (parseTree.getChildren().get(0).getToken().equals("EXPRESION")) {
            return generateExpression(parseTree.getChildren().get(0));
        } else {
            return generateCharacter(parseTree.getChildren().get(0));
        }
    }
    // <CARACTER> ::= COMILLA <CARACTER'> COMILLA
    private String generateCharacter(ParseTree parseTree) {
        return generateCharacterPrime(parseTree.getChildren().get(1));
    }
    // <CARACTER'> ::= ID | VALOR_ENTERO
    private String generateCharacterPrime(ParseTree parseTree) {
        if (parseTree.getChildren().get(0).getToken().equals("ID")) {
            return parseTree.getChildren().get(0).getLexeme();
        } else {
            return parseTree.getChildren().get(0).getLexeme();
        }
    }
    // <EXPRESION> ::= <EXPRESION_RESTA> <EXPRESION’>
    public String generateExpression(ParseTree parseTree) {
        String Expression = generateExpressionResta(parseTree.getChildren().get(0));
        String SecondExpression = generateExpression_1(parseTree.getChildren().get(1));
        if (SecondExpression == null) {
            return Expression;
        }
        System.out.println("t" + tempCounter++ + " = " + Expression + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondExpression);

        return "t" + (tempCounter - 1);
    }
    // <EXPRESION’> ::= MAS < EXPRESION_RESTA> <EXPRESION’> | e
    public String generateExpression_1(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return null;
        }else{
            String Expression = generateExpressionResta(parseTree.getChildren().get(1));
            String SecondExpression = generateExpression_1(parseTree.getChildren().get(2));
            if (SecondExpression == null) {
                return Expression;
            }
            System.out.println("t" + tempCounter++ + " = " + Expression + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondExpression);
            return "t" + (tempCounter - 1);
        }
    }
    // <EXPRESION_RESTA> ::= <TERMINO> <EXPRESION_RESTA’>
    public String generateExpressionResta(ParseTree parseTree) {
        String Expression = generateTerm(parseTree.getChildren().get(0));
        String SecondExpression = generateExpressionResta_1(parseTree.getChildren().get(1));
        if (SecondExpression == null) {
            return Expression;
        }
        System.out.println("t" + tempCounter++ + " = " + Expression + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondExpression);

        return "t" + (tempCounter - 1);
    }
    // <EXPRESION_RESTA’> ::= MENOS <TERMINO> <EXPRESION_RESTA’> | e
    public String generateExpressionResta_1(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return null;
        }else{
            String Expression = generateTerm(parseTree.getChildren().get(1));
            String SecondExpression = generateExpressionResta_1(parseTree.getChildren().get(2));
            if (SecondExpression == null) {
                return Expression;
            }
            System.out.println("t" + tempCounter++ + " = " + Expression + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondExpression);
            return "t" + (tempCounter - 1);
        }
    }
    // <TERMINO> ::= < TERMINO_DIV > <TERMINO’>
    public String generateTerm(ParseTree parseTree) {
        String Term = generateFactor(parseTree.getChildren().get(0));
        String SecondTerm = generateTerm_1(parseTree.getChildren().get(1));

        if (SecondTerm == null) {
            return Term;
        }
        System.out.println("t" + tempCounter++ + " = " + Term + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondTerm);

        return "t" + (tempCounter - 1);
    }
    // <TERMINO’> ::= MULTIPLICACION < TERMINO_DIV > <TERMINO’> | e
    public String generateTerm_1(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return null;
        } else {
            String Term = generateTermDiv(parseTree.getChildren().get(1));
            String SecondTerm = generateTerm_1(parseTree.getChildren().get(2));
            if (SecondTerm == null) {
                return Term;
            }
            System.out.println("t" + tempCounter + " = " + Term + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondTerm); //TODO: Check
            tempCounter++;
            return "t" + (tempCounter - 1);
        }
    }
    // <TERMINO_DIV> ::= <FACTOR> <TERMINO _DIV’>
    public String generateTermDiv(ParseTree parseTree) {
        String Term = generateFactor(parseTree.getChildren().get(0));
        String SecondTerm = generateTermDiv_1(parseTree.getChildren().get(1));

        if (SecondTerm == null) {
            return Term;
        }
        System.out.println("t" + tempCounter++ + " = " + Term + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondTerm);

        return "t" + (tempCounter - 1);
    }
    // <TERMINO_DIV’> ::= DIVISION <FACTOR> <TERMINO_DIV’> | e
    public String generateTermDiv_1(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return null;
        } else {
            String Term = generateFactor(parseTree.getChildren().get(1));
            String SecondTerm = generateTermDiv_1(parseTree.getChildren().get(2));
            if (SecondTerm == null) {
                return Term;
            }
            System.out.println("t" + tempCounter + " = " + Term + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondTerm); //TODO: Check
            tempCounter++;
            return "t" + (tempCounter - 1);
        }
    }
    // <FACTOR> ::= ID <LLAMADA_FUNCION'> | VALOR_ENTERO | VALOR_DECIMAL | PARENTESIS_ABRIR <EXPRESION> PARENTESIS_CERRAR
    public String generateFactor(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return parseTree.getLexeme();
        } else {
            //<FACTOR> ::= ID <LLAMADA_FUNCION'> | VALOR_ENTERO | VALOR_DECIMAL | PARENTESIS_ABRIR <EXPRESION> PARENTESIS_CERRAR
            if (parseTree.getChildren().get(0).getToken().equals("ID")) {
                return parseTree.getChildren().get(0).getLexeme();
            } else if (parseTree.getChildren().get(0).getToken().equals("VALOR_ENTERO")) {
                return parseTree.getChildren().get(0).getLexeme();
            } else if (parseTree.getChildren().get(0).getToken().equals("VALOR_DECIMAL")) {
                return parseTree.getChildren().get(0).getLexeme();
            } else if (parseTree.getChildren().get(0).getToken().equals("PARENTESIS_ABRIR")) {
                return generateExpression(parseTree.getChildren().get(1));
            }
            return generateFactor(parseTree.getChildren().get(0));
        }
    }
    // <LLAMADA_FUNCION> ::=  PARENTESIS_ABRIR <PARAMETROS_LLAMADA_FUNCION> PARENTESIS_CERRAR
    // <LLAMADA_FUNCION'> ::=  <LLAMADA_FUNCION> | e
    // <PARAMETROS_LLAMADA_FUNCION> ::= <EXPRESION> <PARAMETROS_LLAMADA_FUNCION’> | e
    // <PARAMETROS_LLAMADA_FUNCION’> ::= COMA <EXPRESION> <PARAMETROS_LLAMADA_FUNCION’> | e
    // <RETORNO_EXPRESION> ::= RETORNO <EXPRESION> PUNTO_Y_COMA
    // <MIENTRAS_EXPRESION> ::= MIENTRAS PARENTESIS_ABRIR <COMPARACION> PARENTESIS_CERRAR <BLOQUE>
    private void generateWhileStatement(ParseTree parseTree) {
        System.out.println("L" + labels.size() + ":");
        String Comparation = generateComparation(parseTree.getChildren().get(0).getChildren().get(2));
        System.out.println("if " + Comparation + " goto " + "L" + labels.size());
        labels.add("L" + labels.size());
        generateBlock(parseTree.getChildren().get(0).getChildren().get(4));
        System.out.println("goto " + "L" + (labels.size() - 1));
        System.out.println(labels.poll() + ":");
    }
    // <COMPARACION> ::= ID <COMPARACION’>
    private String generateComparation(ParseTree parseTree) {
        String ID = parseTree.getChildren().get(0).getLexeme();
        String SecondExpression = generateComparationPrime(parseTree.getChildren().get(1));
        return ID + " " + SecondExpression;
    }
    // <COMPARACION’> ::= MAYOR <FACTOR> | MENOR <FACTOR> | MAYOR_O_IGUAL <FACTOR> | MENOR_O_IGUAL <FACTOR> | IGUAL_COMPARACION <FACTOR>
    private String generateComparationPrime(ParseTree parseTree){
        return parseTree.getChildren().get(0).getLexeme()  + " " + generateFactor(parseTree.getChildren().get(1));
    }
    // <SI_EXPRESION> ::= SI PARENTESIS_ABRIR <COMPARACION> PARENTESIS_CERRAR <BLOQUE> <SINO_EXPRESION>
    private void generateIfStatement(ParseTree parseTree) {
        String Comparation = generateComparation(parseTree.getChildren().get(0).getChildren().get(2));
        System.out.println("if " + Comparation + " goto " + "L" + labels.size());
        labels.add("L" + labels.size());
        generateBlock(parseTree.getChildren().get(0).getChildren().get(4));
        System.out.println(labels.poll() + ":");
    }
    // <SINO_EXPRESION> ::= SINO <BLOQUE> | e
    // <PRINCIPAL> ::= PRINCIPAL PARENTESIS_ABRIR PARENTESIS_CERRAR <BLOQUE>
    private void generateMain(ParseTree parseTree) {
        System.out.println("\nprincipal:");
        generateBlock(parseTree.getChildren().get(3));
    }
}



