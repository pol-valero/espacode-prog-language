package frontend.src;

import frontend.src.model.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {
    private SymbolTable symbolTable;


    private StringBuilder errors = new StringBuilder();

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
        findTokensContainingValueHelper(node, "VALOR", tokensContainingValue);
        return tokensContainingValue;
    }

    private static void findTokensContainingValueHelper(ParseTree node, String value, List<String> result) {
        if (node.getToken() != null && node.getToken().contains(value)) {
            result.add(node.getToken());
        }
        for (ParseTree child : node.getChildren()) {
            findTokensContainingValueHelper(child, value, result);
        }
    }


    public void checkAssignation(ParseTree parseTree, String scope, int line){
        String key = parseTree.getChildren().get(1).getLexeme();


        // IS a funcition?
        SymbolTableEntry function = symbolTable.find(key);
        if(function != null){
            errors.append("In line " + line + ": can not assign a value to a function" + "\n");
        }

        // IS a variable
        SymbolTableEntry functionScope = symbolTable.find(scope);
        if(functionScope != null){
            SymbolTableEntry entry = functionScope.getSymbolTable().find(key);
            if(entry != null){
            List<String> valors = findTokensContainingValue(parseTree);
            String type = entry.getType();
            for(String token : valors){
                if(type.equals("TIPO_ENTERO")){
                    if(!token.equals("VALOR_ENTERO")){
                        errors.append("In line " + line + ": "+ key +"type do not match. \n");
                        break;
                    }
                }
                if(type.equals("TIPO_DECIMAL")){
                    if(!token.equals("VALOR_DECIMAL")){
                        errors.append("In line " + line + ": "+ key +"type do not match. \n");
                        break;
                    }
                }
                if(type.equals("TIPO_CARACTER")){
                    if(!token.equals("TIPO_CARACTER")){
                        errors.append("In line " + line + ": "+ key +"type do not match. \n");
                        break;
                    }
                }
            }
            } else {
                errors.append("In line " + line + ": "+ key +"do not exist. \n");
            }
        } else {
            errors.append("In line " + line + ": "+ key +"do not exist. \n");
        }
    }


    private boolean matchType(String expectedType, String value) {
        if(value == null || expectedType == null){
            return false;
        }

        switch (expectedType) {
            case "INT" -> {
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            case "FLOAT" -> {
                try {
                    Float.parseFloat(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            case "CHAR" -> {
                return value.length() == 1;
            }
            default -> {
                return false;
            }
        }
    
    }


    /*
    private void analyzeDeclarations(ParseTree parseTree) {
        if (parseTree.getType().equals("DECLARACION")) {
            List<ParseTree> children = parseTree.getChildren();

            if (children != null) {
                String type = children.get(0).getValue();
                String identifier = children.get(1).getValue();

                if (identifier != null && type != null) {
                    Declaration declaration = new Declaration();
                    declaration.identifier = identifier;
                    declaration.type = type;

                    //find if the identifier is already declared
                    boolean isDuplicate = false;
                    for (Declaration d : declarations) {
                        if (d.identifier.equals(identifier)) {
                            // TODO: Throw error; identifier already declared
                            System.out.println("Found duplicate declaration");
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        declarations.add(declaration);
                    }

                   // IGUALACIO?
                    if (children.get(2).getType().equals("Igualacion")){
                        // IGUALACION -> EXPRESION -> Assignation Expression
                        String expressionType = children.get(2).getChildren().get(1).getChildren().get(0).getType();

                        // CASES -> Operació aritmetica, unaltre declaració, valor

                        // Operació artimetica
                        if (expressionType.equals("Producto") || expressionType.equals("Suma")){

                        } else if (expressionType.equals("ID")){
                            String id = children.get(2).getChildren().get(1).getChildren().get(0).getValue();

                            for (Declaration d : declarations) {
                                if (d.identifier.equals(id)){
                                    if (d.type.equals(type)){
                                        // correcte next;
                                    } else{
                                        // TODO: throw error: Not matching types
                                        return;
                                    }
                                }
                            }
                        }
                    }

                } else {
                    //TODO: Throw error; invalid declaration Això ho hauriem de controlar? Ja ho controla el parser
                    return;
                }
            } else {
                //TODO: Throw error; invalid declaration Això ho hauriem de controlar? Ja ho controla el parser
                return;
            }

            for (ParseTree child : children) {
                analyzeDeclarations(child);
            }
        }
    }

    private void analyzeAssignations(ParseTree parseTree) {
        if (parseTree.getType().equals("ASSIGNATION")) {
            List<ParseTree> children = parseTree.getChildren();

            if (children != null) {
                String type = children.get(0).getValue();
                String identifier = children.get(1).getValue();

                if (identifier != null && type != null) {
                    Declaration declaration = new Declaration();
                    declaration.identifier = identifier;
                    declaration.type = type;

                    //find if the identifier is already declared
                    boolean isDuplicate = false;
                    for (Declaration d : declarations) {
                        if (d.identifier.equals(identifier)) {
                            // TODO: Throw error; identifier already declared
                            System.out.println("Found duplicate declaration");
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        declarations.add(declaration);
                    }

                    if (children.size() >= 3) {
                        String value = children.get(3).getValue();

                        if (!checkType(type, value)) {
                            // TODO: Throw error; invalid type
                            return;
                        }
                    }
                } else {
                    //TODO: Throw error; invalid declaration Això ho hauriem de controlar? Ja ho controla el parser
                    return;
                }
            } else {
                //TODO: Throw error; invalid declaration Això ho hauriem de controlar? Ja ho controla el parser
                return;
            }

            for (ParseTree child : children) {
                analyzeDeclarations(child);
            }
        }
    }

    */
}

