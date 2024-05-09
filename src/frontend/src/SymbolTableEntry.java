package frontend.src;

class SymbolTableEntry {
    private String key;
    private String type; 
    private boolean isFunction;
    private SymbolTable symbolTable;

    public SymbolTableEntry(String key, String type) {
        this.key = key;
        this.type = type;
        this.isFunction = false;
    }

    public SymbolTableEntry(String key, String returnType, Boolean isFunction) {
        this.key = key;
        this.type = returnType;
        this.isFunction = isFunction;
        this.symbolTable = new SymbolTable();
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
}