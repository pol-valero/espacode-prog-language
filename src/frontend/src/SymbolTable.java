package frontend.src;

import java.util.HashMap;

class SymbolTable {
    private HashMap<String, SymbolTableEntry> symbolTable;

    public SymbolTable() {
        symbolTable = new HashMap<String, SymbolTableEntry>();
    }

    public void addVariableEntry(String name, String type) {
        SymbolTableEntry entry = new SymbolTableEntry(name, type);
        symbolTable.put(name, entry);
    }

    public void addFunctionEntry(String name, String returnType) {
        SymbolTableEntry entry = new SymbolTableEntry(name, returnType, true);
        symbolTable.put(name, entry);
    }

    public SymbolTableEntry find(String name) {
        return symbolTable.get(name);
    }
}
