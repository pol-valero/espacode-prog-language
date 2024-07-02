package frontend.src;

import java.util.HashMap;

class SymbolTable {
    private HashMap<String, SymbolTableEntry> symbolTable;


    public SymbolTable() {
        symbolTable = new HashMap<String, SymbolTableEntry>();
    }

    public void addVariableEntry(String key, String type, int line) {
        if (symbolTable.containsKey(key)) {
            String error = "Error Linia " + line + ":\n\t" + "Error de semantica: La variable " + key + " ya esta declarada\n";
            throwError(error);
        } else {
            SymbolTableEntry entry = new SymbolTableEntry(key, type, line);
            symbolTable.put(key, entry);
        }
    }

    public void addFunctionEntry(String type, String key, int line) {
        if (symbolTable.containsKey(key)) {
            String error = "Error Linia " + line + ":\n\t" + "Error de semantica: La funcion " + key + " ya esta declarada\n";
            throwError(error);
        } else {
            SymbolTableEntry entry = new SymbolTableEntry(key, type, true, line);
            symbolTable.put(key, entry);
        }
    }


    public SymbolTableEntry find(String key) {
        return symbolTable.get(key);
    }


    private void throwError(String error){
        ErrorHandler.addError(error);
    }
}
