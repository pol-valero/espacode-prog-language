package frontend.src;

class SymbolTableEntry {
    private String key;
    private String type;
    private Integer line;
    private boolean isFunction;
    private SymbolTable symbolTable;

    public SymbolTableEntry(String key, String type, int line) {
        this.key = key;
        this.type = type;
        this.isFunction = false;
        this.line = line;
    }

    public SymbolTableEntry(String type, String key, Boolean isFunction, int line ) {
        this.key = key;
        this.type = type;
        this.isFunction = isFunction;
        this.line = line;
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

    public int getLine() {return line;}
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public void addEntryToScope(String type, String key, int line){
        this.symbolTable.addVariableEntry(key,type, line);
    }
    
}