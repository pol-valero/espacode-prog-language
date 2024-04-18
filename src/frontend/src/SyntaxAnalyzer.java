package frontend.src;

import frontend.src.exceptions.SyntaxException;
import frontend.src.model.ParseTree;

public class SyntaxAnalyzer {
    private Lexer lexer;
    //String currentToken;

    private StringBuilder errores = new StringBuilder(); //TODO: Just launch exceptions and handle them in the controller? So that it will only show one error at a time?

    public SyntaxAnalyzer(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseTree syntaxAnalysis() {

        /*TokenData tokenData;

        while (lexer.peekNextToken() != null) {

            tokenData = lexer.getNextToken();

            //System.out.print(tokenData.getLexeme() + ", ");
            //System.out.println(tokenData.getLexeme() + " -> " + tokenData.getToken());
         }*/

        return MAIN_Code();
    }

    private ParseTree MAIN_Code(){
        ParseTree program = new ParseTree("Program");
        try {
            program.addChild( match("VACIO"));
            program.addChild( match("PRINCIPAL"));
            program.addChild( match("PARENTESIS_ABRIR"));
            program.addChild( match("PARENTESIS_CERRAR"));
            program.addChild(BLOQUE());
            return program;
        }catch (SyntaxException e) {
            errores.append(e.getMessage());
            return null;
        }

    }

    private ParseTree BLOQUE() throws SyntaxException{
        ParseTree bloque = new ParseTree("Bloque");
        try {
            bloque.addChild( match("INICIO"));
            bloque.addChild(SUBBLOQUE());
            bloque.addChild( match("FIN"));
            return bloque;
        }catch (SyntaxException e) {
            errores.append(e.getMessage());
            return null;
        }
    }
    private ParseTree SUBBLOQUE() throws SyntaxException {
        ParseTree Subbloque =  new ParseTree("Subbloque");
        TokenData token = null;
        do {
            try {
                token = lexer.peekNextToken();
                if (token.getToken().equals("TIPO_ENTERO") || token.getToken().equals("TIPO_CARACTER")) {
                    Subbloque.addChild(DECLARACION_VARIABLE());
                }else if (token.getToken().equals("ID")) {
                    Subbloque.addChild(ASIGNACION());
                }else if(token.getToken().equals("FIN")) {
                    break;
                }else{
                    token = lexer.getNextToken();
                }
            } catch (SyntaxException e) {
                errores.append(e.getMessage());
            }
        }while (token != null);
        return Subbloque;
    }
    public ParseTree ASIGNACION() throws SyntaxException {
        ParseTree asignacion = new ParseTree("Asignacion");
        asignacion.addChild(match("ID"));
        asignacion.addChild(match("IGUAL_ASIGNACION"));
        asignacion.addChild(EXPRESION());
        asignacion.addChild(match("DELIMITADOR"));
        return asignacion;
    }
    public ParseTree DECLARACION_VARIABLE() throws SyntaxException {
        ParseTree declaracion = new ParseTree("Declaracion");
        declaracion.addChild(TIPO());
        declaracion.addChild(match("ID"));

        TokenData token = lexer.peekNextToken();
        if (token.getToken().equals("IGUAL_ASIGNACION")) {
            declaracion.addChild(IGUALACION());
            declaracion.addChild(match("DELIMITADOR"));
        }else if (token.getToken().equals("DELIMITADOR")) {
            declaracion.addChild(match("DELIMITADOR"));
        }
        return declaracion;
    }
    public ParseTree IGUALACION() throws SyntaxException{
        ParseTree igualacion = new ParseTree("Igualacion");
        igualacion.addChild(match("IGUAL_ASIGNACION"));
        igualacion.addChild(EXPRESION());
        return igualacion;
    }
    public ParseTree EXPRESION() throws SyntaxException{
        ParseTree expresion = new ParseTree("Expresion");
        expresion.addChild(PRODUCTO());
        TokenData token = lexer.peekNextToken();
        if (token.getToken().equals("MAS")) {
            expresion.addChild(match("MAS"));
            expresion.addChild(EXPRESION());
        }else if (token.getToken().equals("MENOS")) {
            expresion.addChild(match("MENOS"));
            expresion.addChild(EXPRESION());
        }
        return expresion;
    }
    public ParseTree PRODUCTO() throws SyntaxException{
        ParseTree producto = new ParseTree("Producto");
        producto.addChild(VALOR());
        TokenData token = lexer.peekNextToken();
        if (token.getToken().equals("MULTIPLICACION")){
            producto.addChild(match("MULTIPLICACION"));
            producto.addChild(PRODUCTO());
        }else if(token.getToken().equals("DIVISION")) {
            producto.addChild(match("DIVISION"));
            producto.addChild(PRODUCTO());
        }
        return producto;
    }
    public ParseTree VALOR() throws SyntaxException{
        TokenData token = lexer.peekNextToken();
        if (token.getToken().equals("ID")) {
            match("ID");
            return new ParseTree("Valor", token.getLexeme());
        } else if (token.getToken().equals("NUM_ENTERO")) {
            match("NUM_ENTERO");
            return new ParseTree("Valor", token.getLexeme());
        } else {
            throw new SyntaxException("Error Line " + lexer.getLineNumber() + ":\n\t" + "Error de sintaxis: Se esperaba 'IDENTIFICADOR' o 'NUM_ENTERO' pero se encontró '" + token.getToken() + "'\n");
        }
    }
    public ParseTree TIPO() throws SyntaxException{
        TokenData token = lexer.peekNextToken();
        if (token.getToken().equals("TIPO_ENTERO")) {
            match("TIPO_ENTERO");
            return new ParseTree("Tipo", "int");
        } else if(token.getToken().equals("TIPO_CARACTER")){
            match("TIPO_CARACTER");
            return new ParseTree("Tipo", "char");
        } else {
            errores.append("Error Line " + lexer.getLineNumber() + ":\n\t" + "Error de sintaxis: Se esperaba 'int' o 'char' pero se encontró '" + token.getToken() + "'\n");
            return null;
        }
    }
    private ParseTree match(String expectedToken) throws SyntaxException{
        TokenData tokenData = lexer.getNextToken();

        if (tokenData == null) {
            throw new SyntaxException("Error Line " + lexer.getLineNumber() + ":\n\t" + "Error de sintaxis: Se esperaba '" + expectedToken + "' pero se encontró fin de archivo\n");
        }

        if (tokenData.getToken().equals(expectedToken)) {
            return new ParseTree(expectedToken, tokenData.getLexeme());
        }else{
            throw new SyntaxException("Error Line " + lexer.getLineNumber() + ":\n\t" + "Error de sintaxis: Se esperaba '" + expectedToken + "' pero se encontró '" + tokenData.getToken() + "'\n");
        }
    }

    public StringBuilder getErrores() {
        return errores;
    }

    public boolean hasErrores() {
        return errores.length() > 0;
    }

    /*private void match(String expectedToken) {

        if (currentToken.equals(expectedToken)) {
            currentToken = lexer.getNextToken().getToken();
        } else {
            System.out.println("Syntax error: expected " + expectedToken + ", found " + currentToken);
            System.exit(1);
        }

    }*/







}
