package frontend.src;

import frontend.src.model.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {
    private SymbolTable symbolTable;

    private StringBuilder errors = new StringBuilder();

    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
    }

    public void addFunction(String type, String key, int line){
        symbolTable.addFunctionEntry(key, type, line);
    }

    public void addEntry(String type, String key, String scope, int line){
       symbolTable.find(scope).addEntryToScope(type, key, line);
    }

    public void checkAssignation(String id, String value){
        SymbolTableEntry entry = symbolTable.find(id);
        if (entry == null){
            errors.append("ERROR: " + id +" is not declared\n");
            return;
        }

        if (!matchType(entry.getType(), value)){
               errors.append("ERROR: the type and the value of " + id + " is not matching\n");
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

