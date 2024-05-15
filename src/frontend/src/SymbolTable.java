package frontend.src;

import java.util.HashMap;

class SymbolTable {
    private HashMap<String, SymbolTableEntry> symbolTable;

    public SymbolTable() {
        symbolTable = new HashMap<String, SymbolTableEntry>();
    }

    public void addVariableEntry(String key, String type, int line) {
        SymbolTableEntry entry = new SymbolTableEntry(key, type, line);
        try{
            symbolTable.put(key, entry);
        } catch (Exception e){
            String error = "ERROR line " + line + ": " + "the id \"" + key + "\" is already declared.\n";
            throwError(error);
        }
    }

    public void addFunctionEntry(String type, String key, int line) {
        SymbolTableEntry entry = new SymbolTableEntry(key, type, true, line);
       try{
           symbolTable.put(key, entry);
       } catch (Exception e){
           String error = "ERROR line " + line + ": " + "the function \"" + key + "\" is already declared.\n";
           throwError(error);
       }
    }

    public SymbolTableEntry find(String key) {
        return symbolTable.get(key);
    }


    private void throwError(String error){
        //TODO: Handle here

    }
}
