package frontend.src;

import frontend.src.model.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {
    private SymbolTable symbolTable;


    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
        this.symbolTable.addFunctionEntry(null, "main", 0);
    }

    public String addPrincipal(int line){
        String type= "VACIO";
        String key = "PRINCIPAL";

        symbolTable.addFunctionEntry(type,key, line);
        return key;
    }

    public String addFunction(ParseTree parseTree, int line ) {
        String type = parseTree.getChildren().get(0).getChildren().get(0).getChildren().get(0).getToken();
        String key = parseTree.getChildren().get(1).getLexeme();

        symbolTable.addFunctionEntry(type, key, line);
        return key;
    }

    public void addEntry(ParseTree parseTree, String scope, int line){
        String type = parseTree.getChildren().get(0).getChildren().get(0).getToken();
        String key = parseTree.getChildren().get(1).getLexeme();

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
            variables.add(node.getLexeme());
        }
        for (ParseTree child : node.getChildren()) {
            findVariablesHelper(child, variables);
        }

    }



    public void checkAssignation(ParseTree parseTree, String scope, int line){

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
                List<String> variables = findVariables(parseTree);

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


}

