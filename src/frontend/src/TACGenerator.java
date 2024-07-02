package frontend.src;

import frontend.src.model.ParseTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class TACGenerator {
    private Map<String, Integer> tempVariables = new HashMap<>();
    private int tempCounter = 0;
    private Queue<String> labels = new LinkedList<>();
    private int labelCounter = 0;
    private int paramCounter = 0;
    private static final Map<String, String> operatorMap = new HashMap<>();
    // Este esta todo negado
    static {
        operatorMap.put("MAYOR_O_IGUAL", "<");
        operatorMap.put("MENOR_O_IGUAL", ">");
        operatorMap.put("MAYOR", "<=");
        operatorMap.put("MENOR", ">=");
        operatorMap.put("IGUAL_COMPARACION", "!=");
        operatorMap.put("DISTINTO_DE", "==");
    }
    /*
    static {
        operatorMap.put("MAYOR_O_IGUAL", ">=");
        operatorMap.put("MENOR_O_IGUAL", "<=");
        operatorMap.put("MAYOR", ">");
        operatorMap.put("MENOR", "<");
        operatorMap.put("IGUAL_COMPARACION", "==");
        operatorMap.put("DISTINTO_DE", "!=");
    }*/
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
        tempVariables.clear();
        String functionType = generateFunctionType(parseTree.getChildren().get(0));
        String functionName = parseTree.getChildren().get(1).getLexeme();
        System.out.println("\n" + functionName + ":");
        generateFunctionParameters(parseTree.getChildren().get(3));
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
    //TODO:
    private void generateFunctionParameters(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        tempVariables.put(parseTree.getChildren().get(1).getLexeme(), tempCounter);
        System.out.println("\treadParam " + paramCounter++ + " t" + tempCounter++);
        generateFunctionParametersPrime(parseTree.getChildren().get(2));
        paramCounter = 0;
    }
    // <PARAMETROS_DECLARACION_FUNCION’> ::= COMA <TIPO> ID <PARAMETROS_DECLARACION_FUNCION’> | e
    private void generateFunctionParametersPrime(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        String type = generateType(parseTree.getChildren().get(1));
        String id = parseTree.getChildren().get(2).getLexeme();
        tempVariables.put(id, tempCounter);
        System.out.println("\treadParam " + paramCounter++ + " t" + tempCounter++);
        generateFunctionParametersPrime(parseTree.getChildren().get(3));
    }

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
                generateStatementIdSubBlock(parseTree.getChildren().get(1), parseTree.getChildren().get(0).getLexeme());
                break;
            case "MIENTRAS_EXPRESION":
                generateWhileStatement(parseTree.getChildren().get(0)); // TODO: Check because it is not implemented in the grammar
                break;
            case "SI_EXPRESION":
                generateIfStatement(parseTree.getChildren().get(0));
                break;
            case "RETORNO_EXPRESION":
                generateReturnStatement(parseTree.getChildren().get(0));
                break;
            default:
                break;
        }
    }
    // <DECLARACION_VARIABLE> ::= <TIPO> ID  <DECLARACION_VARIABLE’>
    private void generateVariable(ParseTree parseTree) {
        String id = parseTree.getChildren().get(1).getLexeme();
        String result = generateVariablePrime(parseTree.getChildren().get(2));
        tempVariables.put(id, tempCounter);
        System.out.println("\tt" + tempCounter++ + result);
    }
    // <DECLARACION_VARIABLE’> ::=  <ASIGNACION_VARIABLE> | PUNTO_Y_COMA
    private String generateVariablePrime(ParseTree parseTree) {
        if (parseTree.getChildren().get(0).getToken().equals("ASIGNACION_VARIABLE")) {
            return generateNewAssignment(parseTree.getChildren().get(0));
        }
        return " = 0";
    }

    // <SENTENCIA_ID_SUBBLOQUE> ::= <LLAMADA_FUNCION> PUNTO_Y_COMA | <ASIGNACION_VARIABLE>
    private void generateStatementIdSubBlock(ParseTree parseTree, String id) {
        if (parseTree.getChildren().get(0).getToken().equals("LLAMADA_FUNCION")) {
            generateFunctionCall(parseTree.getChildren().get(0));
            System.out.println("\tcall " + id);
        } else {
            String result = generateNewAssignment(parseTree.getChildren().get(0));
            id = tempVariables.containsKey(id) ? "t" + tempVariables.get(id) : id;
            System.out.println("\t" + id + result);
        }
    }
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
        System.out.println("\tt" + tempCounter + " = " + Expression + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondExpression);

        return "t" + tempCounter++;
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
            System.out.println("\tt" + tempCounter + " = " + Expression + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondExpression);
            return "t" + tempCounter++;
        }
    }
    // <EXPRESION_RESTA> ::= <TERMINO> <EXPRESION_RESTA’>
    public String generateExpressionResta(ParseTree parseTree) {
        String Expression = generateTerm(parseTree.getChildren().get(0));
        String SecondExpression = generateExpressionResta_1(parseTree.getChildren().get(1));
        if (SecondExpression == null) {
            return Expression;
        }
        System.out.println("\tt" + tempCounter + " = " + Expression + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondExpression);

        return "t" + tempCounter++;
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
            System.out.println("\tt" + tempCounter + " = " + Expression + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondExpression);
            return "t" + tempCounter++;
        }
    }
    // <TERMINO> ::= < TERMINO_DIV > <TERMINO’>
    public String generateTerm(ParseTree parseTree) {
        String Term = generateTermDiv(parseTree.getChildren().get(0));
        String SecondTerm = generateTerm_1(parseTree.getChildren().get(1));

        if (SecondTerm == null) {
            return Term;
        }
        System.out.println("\tt" + tempCounter + " = " + Term + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondTerm);

        return "t" + tempCounter++;
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
            System.out.println("\tt" + tempCounter + " = " + Term + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondTerm); //TODO: Check

            return "t" + tempCounter++;
        }
    }
    // <TERMINO_DIV> ::= <FACTOR> <TERMINO _DIV’>
    public String generateTermDiv(ParseTree parseTree) {
        String Term = generateFactor(parseTree.getChildren().get(0));
        String SecondTerm = generateTermDiv_1(parseTree.getChildren().get(1));

        if (SecondTerm == null) {
            return Term;
        }
        System.out.println("\tt" + tempCounter + " = " + Term + " " + parseTree.getChildren().get(1).getChildren().get(0).getLexeme() + " " + SecondTerm);

        return "t" + tempCounter++;
    }
    // <TERMINO_DIV’> ::= DIVISION <FACTOR> <TERMINO_DIV’> | e
    public String generateTermDiv_1(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return null;
        } else {
            String Factor = generateFactor(parseTree.getChildren().get(1));
            String SecondFactor = generateTermDiv_1(parseTree.getChildren().get(2));
            if (SecondFactor == null) {
                return Factor;
            }else{
                System.out.println("\tt" + tempCounter + " = " + Factor + " " + parseTree.getChildren().get(0).getLexeme() + " " + SecondFactor);
                return "t" + tempCounter++;
            }
        }
    }
    // <FACTOR> ::= ID <LLAMADA_FUNCION'> | VALOR_ENTERO | VALOR_DECIMAL | PARENTESIS_ABRIR <EXPRESION> PARENTESIS_CERRAR
    public String generateFactor(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return parseTree.getLexeme();
        } else {
            if (parseTree.getChildren().get(0).getToken().equals("ID")) {
                if (generateFunctionCallPrime(parseTree.getChildren().get(1))) {
                    return "call " + parseTree.getChildren().get(0).getLexeme();
                }else{
                    String ID = parseTree.getChildren().get(0).getLexeme();
                    ID = tempVariables.containsKey(ID) ? "t" + tempVariables.get(ID) : ID;
                    return ID;
                }
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
    // <LLAMADA_FUNCION> ::=  PARENTESIS_ABRIR <PARAMETROS_LLAMADA_FUNCION> PARENTESIS_CERRAR | e
    private boolean generateFunctionCall(ParseTree parseTree) {
        generateCallFunctionParameters(parseTree.getChildren().get(1));
        return true;
    }
    // <LLAMADA_FUNCION'> ::=  <LLAMADA_FUNCION> | e
    private boolean generateFunctionCallPrime(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return false;
        }
        generateFunctionCall(parseTree.getChildren().get(0));
        return true;
    }

    // <PARAMETROS_LLAMADA_FUNCION> ::= <EXPRESION> <PARAMETROS_LLAMADA_FUNCION’> | e
    private void generateCallFunctionParameters(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        String Expression = generateExpression(parseTree.getChildren().get(0));
        System.out.println("\twriteParam " + paramCounter++ + " " + Expression);
        generateCallFunctionParametersPrime(parseTree.getChildren().get(1));
        paramCounter = 0;
    }
    // <PARAMETROS_LLAMADA_FUNCION’> ::= COMA <EXPRESION> <PARAMETROS_LLAMADA_FUNCION’> | e
    private void generateCallFunctionParametersPrime(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        String Expression = generateExpression(parseTree.getChildren().get(1));
        System.out.println("\twriteParam " + paramCounter++ + " " + Expression);
        generateCallFunctionParametersPrime(parseTree.getChildren().get(2));
    }
    // <RETORNO_EXPRESION> ::= RETORNO <EXPRESION> PUNTO_Y_COMA
    private void generateReturnStatement(ParseTree parseTree) {
        String Expression = generateExpression(parseTree.getChildren().get(1));
        System.out.println("\treturn " + Expression);
    }
    // <MIENTRAS_EXPRESION> ::= MIENTRAS PARENTESIS_ABRIR <COMPARACION> PARENTESIS_CERRAR <BLOQUE>
    private void generateWhileStatement(ParseTree parseTree) {
        System.out.println("L" + labelCounter + ":");
        labels.add("L" + labelCounter++);
        String Comparation = generateComparation(parseTree.getChildren().get(2));
        System.out.println("\tif " + Comparation + " goto " + "L" + labelCounter);
        labels.add("L" + labelCounter++);
        generateBlock(parseTree.getChildren().get(4));
        String first = labels.poll();
        String second = labels.poll();
        System.out.println("\tgoto " + first);
        System.out.println(second + ":");
    }
    // <COMPARACION> ::= ID <COMPARACION’>
    private String generateComparation(ParseTree parseTree) {
        String ID = parseTree.getChildren().get(0).getLexeme();
        ID = tempVariables.containsKey(ID) ? "t" + tempVariables.get(ID) : ID;
        String SecondExpression = generateComparationPrime(parseTree.getChildren().get(1));
        return ID + " " + SecondExpression;
    }
    // <COMPARACION’> ::= MAYOR <FACTOR> | MENOR <FACTOR> | MAYOR_O_IGUAL <FACTOR> | MENOR_O_IGUAL <FACTOR> | IGUAL_COMPARACION <FACTOR>
    private String generateComparationPrime(ParseTree parseTree){
        return operatorMap.get(parseTree.getChildren().get(0).getToken())  + " " + generateFactor(parseTree.getChildren().get(1));
    }
    // <SI_EXPRESION> ::= SI PARENTESIS_ABRIR <COMPARACION> PARENTESIS_CERRAR <BLOQUE> <SINO_EXPRESION>
    private void generateIfStatement(ParseTree parseTree) {
        String Comparation = generateComparation(parseTree.getChildren().get(2));
        System.out.println("\tif " + Comparation + " goto " + "L" + labelCounter);
        labels.add("L" + labelCounter++);
        generateBlock(parseTree.getChildren().get(4));
        System.out.println(labels.poll() + ":");
        generateElseStatement(parseTree.getChildren().get(5));
    }
    // <SINO_EXPRESION> ::= SINO <BLOQUE> | e
    private void generateElseStatement(ParseTree parseTree) {
        if (parseTree.getChildren().size() == 0) {
            return;
        }
        generateBlock(parseTree.getChildren().get(1));
    }
    // <PRINCIPAL> ::= PRINCIPAL PARENTESIS_ABRIR PARENTESIS_CERRAR <BLOQUE>
    private void generateMain(ParseTree parseTree) {
        tempVariables.clear();
        System.out.println("\nprincipal:");
        generateBlock(parseTree.getChildren().get(3));
    }
}



