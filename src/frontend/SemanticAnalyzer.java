package frontend;

import errors.ErrorHandler;
import frontend.model.ParseTree;
import symbols.SymbolTable;
import symbols.SymbolTableEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SemanticAnalyzer {
    private SymbolTable symbolTable;
    int line = 0;

    int callingFunctionParametersNum = 0;

    // Constructor
    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
        //this.symbolTable.addFunctionEntry(null, "main", 0);
    }
    public String addPrincipal(int line){
        String type= "VACIO";
        String key = "PRINCIPAL";

        symbolTable.addFunctionEntry(type,key, line);
        return key;
    }

    public void incrementFunctionParametersNum(String functionName) {
        SymbolTableEntry entry = symbolTable.find(functionName);
        if (entry != null) {
            //We save the number of parameters of a function so that when calling the function we can check if the number of parameters is correct
           entry.setNumParams(entry.getNumParams() + 1);

           if (entry.getNumParams() > 4) {
               ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error: La funcion " + functionName + " tiene mas de 4 parametros, MIPS esta implementado con un maximo de 4 parametros\n");
           }
        }

    }
    public String addFunction(ParseTree parseTree, int line ) {
        String type = "";
        if (parseTree.getChildren().get(0).getChildren().get(0).getChildren().isEmpty()){
            type = parseTree.getChildren().get(0).getChildren().get(0).getToken();
        }else{
            type = parseTree.getChildren().get(0).getChildren().get(0).getChildren().get(0).getToken();
        }

        if (type.equals("TIPO_DECIMAL") || type.equals("TIPO_CARACTER")) {
            ErrorHandler.addError("Aviso en linia " + line + ":\n\t" + "Actualmente no estan implementados los " + type + " en TAC ni MIPS. Se procede a no generar codigo intermedio ni codigo maquina.\n");
        }

        String key = parseTree.getChildren().get(1).getLexeme();

        symbolTable.addFunctionEntry(type, key, line);
        return key;
    }
    public void addEntry(ParseTree parseTree, String scope, int line){
        String type = parseTree.getChildren().get(0).getChildren().get(0).getToken();
        String key = parseTree.getChildren().get(1).getLexeme();

        if (type.equals("TIPO_DECIMAL") || type.equals("TIPO_CARACTER")) {
            ErrorHandler.addError("Aviso en linia " + line + ":\n\t" + "Actualmente no estan implementados los " + type + " en TAC ni MIPS. Se procede a no generar codigo intermedio ni codigo maquina.\n");
        }

       symbolTable.find(scope).addEntryToScope(type, key, line);
    }
    public static List<String> findTokensContainingValue(ParseTree node) {
        List<String> tokensContainingValue = new ArrayList<>();
        findTokensContainingValueHelper(node, "VALOR_ENTERO", tokensContainingValue);
        findTokensContainingValueHelper(node, "VALOR_DECIMAL", tokensContainingValue);
        findTokensContainingValueHelper(node, "CARACTER'", tokensContainingValue);
        //If it finds VALOR_ENTERO, VALOR_CARACTER or CARACTER', it will add them to the list
        return tokensContainingValue;
    }

    private static void findTokensContainingValueHelper(ParseTree node, String value, List<String> result) {

        if (node == null) {
            return;
        }

        if (node.getToken() != null && node.getToken().equals(value)) {
            result.add(node.getToken());
        }
        for (ParseTree child : node.getChildren()) {
            findTokensContainingValueHelper(child, value, result);
        }
    }


    public List<String> findVariables(ParseTree node) {

        List<String> variables = new ArrayList<>();

        findVariablesHelper(node, variables);

        return variables;
    }

    private void findVariablesHelper(ParseTree node, List<String> variables) {

        if (node == null) {
            return;
        }

        if (node.getToken() != null && node.getToken().equals("ID")) {
            // Check if the variable is a function
            variables.add(node.getLexeme());
        }

        List<ParseTree> children = node.getChildren();

        if (children.size() < 2 || !children.get(1).getToken().equals("LLAMADA_FUNCION'") || children.get(1).getChildren().size() == 0) {
            for (ParseTree child : children) {
                findVariablesHelper(child, variables);
            }
        }else{
            checkFunctionExists(children.get(0).getLexeme());
        }
    }

    public void checkAssignation(ParseTree parseTree, String scope, int line){
        this.line = line;
        List<String> variables = findVariables(parseTree);

        if (parseTree.getToken().equals("DECLARACION_VARIABLE")) {
            if (parseTree.getChildren().get(0).getChildren().get(0).getToken().equals("TIPO_CARACTER")) {
                return;
                //We do not call checkIfVariableExists, to avoid mistaking a character with a variable
            }
        }

        checkIfVariableExists(variables, scope, line);

        String key;


        //Variable assignation will always have children "ID" and "SENTENCIA_ID_SUBBLOQUE"
        //Variable declaration with assignation will always have children "TIPO", "ID" and "DECLARACION_VARIABLE_PRIME"
        if (parseTree.getChildren().size() == 2){
            //checkAssignationVariable
            key = parseTree.getChildren().get(0).getLexeme();
        } else {
            //checkDeclarationAndAssignationVariable
            key = parseTree.getChildren().get(1).getLexeme();
        }

        // IS a variable
        SymbolTableEntry functionScope = symbolTable.find(scope);
        if(functionScope != null){
            SymbolTableEntry entry = functionScope.getSymbolTable().find(key);
            if(entry != null){

                //Check if the constant values (VALOR_ENTERO, VALOR_DECIMAL, CARACTER') in the assignation are the same type as the variable that is left of the assignation
                List<String> valors = findTokensContainingValue(parseTree);
                String type = entry.getType();
                for(String token : valors) {
                    if(type.equals("TIPO_ENTERO")){
                        if(!token.equals("VALOR_ENTERO")){
                            ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En la asignacion de '" + key + "' " + "los tipos de valor no coinciden \n");
                            break;
                        }
                    }
                    if(type.equals("TIPO_DECIMAL")){
                        if(!token.equals("VALOR_DECIMAL")){
                            ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En la asignacion de '" + key + "' " + "los tipos de valor no coinciden \n");
                            break;
                        }
                    }
                    if(type.equals("TIPO_CARACTER")){
                        if(!token.equals("CARACTER'")){
                            ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En la asignacion de '" + key + "' " + "los tipos de valor no coinciden \n");
                            break;
                        }
                    }
                }

                //Check if the variables in the assignation have the same type as the variable that is left of the assignation


                for(String variable : variables){
                    SymbolTableEntry entry2 = functionScope.getSymbolTable().find(variable);
                    if(entry2 != null){
                        if(!entry2.getType().equals(type)){
                            ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En la asignacion de '" + key + "' " + "los tipos de valor no coinciden \n");
                            break;
                        }
                    }
                }

            } else {
                ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: " + key + " " +"no se ha declarado\n");
            }
        } else {
            ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: " + key + " " +"no se ha declarado\n");

        }
    }

    public boolean checkIfVariableExists(List<String> variables ,String scope, int line){
        SymbolTableEntry functionScope = symbolTable.find(scope);
        for(String variable : variables){
            SymbolTableEntry entry = functionScope.getSymbolTable().find(variable);
            if(entry == null){
                ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: " + variable + " " +"no se ha declarado\n");
                return false;
            }
        }
        return true;
    }

    public void checkIfStatement (ParseTree parseTree, String scope, int line){
        List<String> variables = findVariables(parseTree);
        checkIfVariableExists(variables, scope, line);
        // Check if the condition it's all the same type
        String type = "";
        SymbolTableEntry functionScope = symbolTable.find(scope);
        if (!variables.isEmpty()){
            SymbolTableEntry entry = functionScope.getSymbolTable().find(variables.get(0));
            type = entry.getType();
            for(String variable : variables){
                entry = functionScope.getSymbolTable().find(variable);
                if(!entry.getType().equals(type)){
                    ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En la condicion del if " + "los tipos de valor no coinciden \n");
                    break;
                }
            }
        }
        // Check if the condition it's all the same type
        List<String> valors = findTokensContainingValue(parseTree);
        if (valors.size() > 0){
            if (type.equals("")){
                type = functionScope.getSymbolTable().find(valors.get(0)).getType();
            }else{
                if (type.equals("TIPO_ENTERO")){
                    type = "VALOR_ENTERO";
                }else if (type.equals("TIPO_DECIMAL")){
                    type = "VALOR_DECIMAL";
                }
            }
            for(String token : valors) {
                if(!token.equals(type)){
                    ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En la condicion del if " + "los tipos de valor no coinciden \n");
                    break;
                }
            }
        }
    }
    public void checkReturnStatment(ParseTree parseTree, String scoope, int line){
        //Check if the return is the same type as the function
        SymbolTableEntry functionScope = symbolTable.find(scoope);
        String type = functionScope.getType();
        List<String> valors = findTokensContainingValue(parseTree);

        if (type.equals("VACIO")) {
            ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: No se admite RETORNO si el tipo de la funcion es VACIO \n");
            return;
        }

        for(String token : valors) {
            if(type.equals("TIPO_ENTERO")){
                if(!token.equals("VALOR_ENTERO")){
                    ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En el return " + "los tipos de valor no coinciden \n");
                    break;
                }
            }
            if(type.equals("TIPO_DECIMAL")){
                if(!token.equals("VALOR_DECIMAL")){
                    ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En el return " + "los tipos de valor no coinciden \n");
                    break;
                }
            }
            if(type.equals("TIPO_CARACTER")){
                if(!token.equals("CARACTER'")){
                    ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: En el return " + "los tipos de valor no coinciden \n");
                    break;
                }
            }
        }
        functionScope.setHasReturn();
    }
    public void checkReturns(){
        //get all scopes
        Map<String, SymbolTableEntry> scopes = symbolTable.getTable();
        for (Map.Entry<String, SymbolTableEntry> entry : scopes.entrySet()) {
            SymbolTableEntry scope = entry.getValue();
            if (!scope.hasReturn()){
                ErrorHandler.addError("Error de semantica: La funcion " + scope.getKey() + " no tiene un return\n");
            }
        }
    }
    public boolean checkFunctionExists(String key){
        SymbolTableEntry entry = symbolTable.find(key);
        if(entry == null){
            ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: La funcion " + key + " no se ha declarado\n");
            return false;
        }
        return true;
    }
    public void checkFunctionCall(ParseTree parseTree, String scope, int line){
        this.line = line;
        //Check if the function exists
        String key = parseTree.getChildren().get(0).getLexeme();
        checkFunctionExists(key);
    }

    public void incrementCallingFunctionParametersNum (String functionName) {
        callingFunctionParametersNum++;
    }

    public void resetCallingFunctionParametersNum(String functionName) {
        callingFunctionParametersNum = 0;
    }

    public void compareCallingFunctionParametersNumWithFunctionDeclaration(String functionName, int line) {
        SymbolTableEntry entry = symbolTable.find(functionName);
        if (entry != null) {
            if (entry.getNumParams() != callingFunctionParametersNum) {
                ErrorHandler.addError("Error Linia " + line + ":\n\t" + "Error de semantica: El numero de parametros de la funcion " + functionName + " no coincide con el numero de parametros de la llamada\n");
            }
        }
    }
}

