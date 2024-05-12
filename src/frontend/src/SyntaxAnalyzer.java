package frontend.src;

import frontend.src.exceptions.SyntaxException;
import frontend.src.model.ParseTree;

public class SyntaxAnalyzer {
    private LexicAnalyzer lexicAnalyzer;
    private String scope;
    TokenData currentToken;

    private StringBuilder errors = new StringBuilder();
    private  SemanticAnalyzer semanticAnalyzer;

    public SyntaxAnalyzer(LexicAnalyzer lexicAnalyzer) {
        this.lexicAnalyzer = lexicAnalyzer;
        this.semanticAnalyzer = new SemanticAnalyzer();
        this.scope = null;
    }

    public ParseTree syntaxAnalysis() {

        currentToken = lexicAnalyzer.getNextToken();
        codigo();
        match("EOF");
        //return codigo();
        return null;
    }

    // Production for <CODIGO>
    private void codigo() {

        funciones();
        principal();

    }

    // Production for <FUNCIONES>
    private void funciones() {
        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER") || currentToken.equals("VACIO")) {
            funcion();
            funciones();
        }
    }

    // Production for <FUNCION>
    private void funcion() {
        String type = currentToken.getToken();
        tipoFuncion();
        String key = currentToken.getToken();

        match("ID");

        semanticAnalyzer.addFunction(type, key,currentToken.getLine());
        match("PARENTESIS_ABRIR");
        parametrosDeclaracionFuncion();
        match("PARENTESIS_CERRAR");
        bloque();
    }

    // Production for <TIPO_FUNCION>
    private void tipoFuncion() {
        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            tipo();
        } else if (currentToken.equals("VACIO")){
            match("VACIO");
        } else {
            error("TIPO_ENTERO, TIPO_DECIMAL, TIPO_CARACTER o VACIO");
        }
    }

    // Production for <TIPO>
    private void tipo() {
        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            match(currentToken.getToken());
        } else {
            error("TIPO_ENTERO, TIPO_DECIMAL o TIPO_CARACTER");
        }
    }

    // Production for <PARAMETROS_DECLARACION_FUNCION>
    private void parametrosDeclaracionFuncion() {
        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            tipo();
            match("ID");
            parametrosDeclaracionFuncionPrime();
        }
    }

    // Production for <PARAMETROS_DECLARACION_FUNCION'>
    private void parametrosDeclaracionFuncionPrime() {
        if (currentToken.equals("COMA")) {
            match("COMA");
            tipo();
            match("ID");
            parametrosDeclaracionFuncionPrime();
        }
    }

    // Production for <BLOQUE>
    private void bloque() {
        match("INICIO");
        subbloque();
        match("FIN");
    }

    // Production for <SUBBLOQUE>
    private void subbloque() {
        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER") ||
                currentToken.equals("ID") || currentToken.equals("SI") || currentToken.equals("MIENTRAS") || currentToken.equals("RETORNO") ) {
            sentenciaSubbloque();
            subbloque();
        }
    }

    // Production for <SENTENCIA_SUBBLOQUE>
    private void sentenciaSubbloque() {
        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            declaracionVariable();
        } else if (currentToken.equals("ID")) {
            match("ID");
            sentenciaIDsubbloque();
        } else if (currentToken.equals("MIENTRAS")) {
            mientrasExpresion();
        } else if (currentToken.equals("SI")) {
            siExpresion();
        } else if (currentToken.equals("RETORNO")) {
            retornoExpresion();
        }
    }

    // Production for <DECLARACION_VARIABLE>
    private void declaracionVariable() {
        String type = currentToken.getToken();
        tipo();
        String key = currentToken.getToken();
        match("ID");

        semanticAnalyzer.addEntry(type, key, scope, currentToken.getLine());
        declaracionVariablePrime();
    }

    // Production for <DECLARACION_VARIABLE'>
    private void declaracionVariablePrime() {
        if (currentToken.equals("IGUAL_ASIGNACION")) {
            asignacionVariable();
        } else {
            match("PUNTO_Y_COMA");
        }
    }

    // Production for <SENTENCIA_ID_SUBBLOQUE>
    private void sentenciaIDsubbloque() {
        if (currentToken.equals("IGUAL_ASIGNACION")) {
            asignacionVariable();
        } else {
            llamadaFuncion();
        }
    }

    // Production for <ASIGNACION_VARIABLE>
    private void asignacionVariable() {
        match("IGUAL_ASIGNACION");
        asignacionVariablePrime();
        match("PUNTO_Y_COMA");
    }

    // Production for <ASIGNACION_VARIABLE'>
    private void asignacionVariablePrime() {
        if (currentToken.equals("ID") || currentToken.equals("VALOR_ENTERO") || currentToken.equals("VALOR_DECIMAL") || currentToken.equals("PARENTESIS_ABRIR")) {
            expresion();
        } else if (currentToken.equals("COMILLA")) {
            caracter();
        } else {
            error("ID, VALOR_ENTERO, VALOR_DECIMAL, VALOR_CARACTER, PARENTESIS_ABRIR o COMILLA");
        }
    }

    // Production for <CARACTER>
    private void caracter() {
        match("COMILLA");
        caracterPrime();
        match("COMILLA");
    }

    // Production for <CARACTER'>
    private void caracterPrime() {
        if (currentToken.equals("ID")) {
            match("ID");
        } else if(currentToken.equals("VALOR_ENTERO")) {
            match("VALOR_ENTERO");
        } else {
            error("letras o numeros");
        }
    }



    // Production for <EXPRESION>
    private void expresion() {
        termino();
        expresionPrime();
    }

    // Production for <TERMINO>
    private void termino() {
        factor();
        terminoPrime();
    }

    // Production for <TERMINO'>
    private void terminoPrime() {
        if (currentToken.equals("MULTIPLICACION") || currentToken.equals("DIVISION")) {
            match(currentToken.getToken());
            factor();
            terminoPrime();
        }
    }

    // Production for <EXPRESION'>
    private void expresionPrime() {
        if (currentToken.equals("MAS") || currentToken.equals("MENOS")) {
            match(currentToken.getToken());
            termino();
            expresionPrime();
        }
    }

    // Production for <FACTOR>
    private void factor() {
        if (currentToken.equals("ID")) {
            match("ID");
            llamadaFuncionPrime();
        } else if (currentToken.equals("VALOR_ENTERO") || currentToken.equals("VALOR_DECIMAL")) {
            match(currentToken.getToken());
        } else if (currentToken.equals("PARENTESIS_ABRIR")) {
            match("PARENTESIS_ABRIR");
            expresion();
            match("PARENTESIS_CERRAR");
        } else {
            error("ID, VALOR_ENTERO, VALOR_DECIMAL o PARENTESIS_ABRIR");
        }
    }

    // Production for <LLAMADA_FUNCION>
    private void llamadaFuncion() {
        match("PARENTESIS_ABRIR");
        parametrosLlamadaFuncion();
        match("PARENTESIS_CERRAR");
    }

    // Production for <LLAMADA_FUNCION_PRIME>
    private void llamadaFuncionPrime() {
        if (currentToken.equals("PARENTESIS_ABRIR")) {
            match("PARENTESIS_ABRIR");
            parametrosLlamadaFuncion();
            match("PARENTESIS_CERRAR");
        }
    }

    // Production for <PARAMETROS_LLAMADA_FUNCION>
    private void parametrosLlamadaFuncion() {
        if (currentToken.equals("ID") || currentToken.equals("VALOR_ENTERO") || currentToken.equals("VALOR_DECIMAL") ||
                currentToken.equals("PARENTESIS_ABRIR")) {
            expresion();
            parametrosLlamadaFuncionPrime();
        }
    }

    // Production for <PARAMETROS_LLAMADA_FUNCION'>
    private void parametrosLlamadaFuncionPrime() {
        if (currentToken.equals("COMA")) {
            match("COMA");
            expresion();
            parametrosLlamadaFuncionPrime();
        }
    }

    // Production for <RETORNO_EXPRESION>
    private void retornoExpresion() {
        match("RETORNO");
        expresion();
        match("PUNTO_Y_COMA");
    }

    // Production for <MIENTRAS_EXPRESION>
    private void mientrasExpresion() {
        match("MIENTRAS");
        match("PARENTESIS_ABRIR");
        comparacion();
        match("PARENTESIS_CERRAR");
        bloque();
    }

    // Production for <COMPARACION>
    private void comparacion() {
        match("ID");
        comparacionPrime();
    }

    // Production for <COMPARACION'>
    private void comparacionPrime() {
        if (currentToken.equals("MAYOR") || currentToken.equals("MENOR") || currentToken.equals("MAYOR_O_IGUAL") || currentToken.equals("MENOR_O_IGUAL") || currentToken.equals("IGUAL_COMPARACION")) {
            match(currentToken.getToken());
            factor();
        } else {
            error("MAYOR, MENOR, MAYOR_O_IGUAL, MENOR_O_IGUAL o IGUAL_COMPARACION");
        }

    }

    // Production for <SI_EXPRESION>
    private void siExpresion() {
        match("SI");
        match("PARENTESIS_ABRIR");
        comparacion();
        match("PARENTESIS_CERRAR");
        bloque();
        sinoExpresion();
    }

    // Production for <SINO_EXPRESION>
    private void sinoExpresion() {
        if (currentToken.equals("SINO")) {
            match("SINO");
            bloque();
        }
    }

    // Production for <PRINCIPAL>
    private void principal() {
        match("PRINCIPAL");
        match("PARENTESIS_ABRIR");
        match("PARENTESIS_CERRAR");
        bloque();
    }


    private void match(String expectedToken) {

        if (currentToken.equals(expectedToken)){
            currentToken = lexicAnalyzer.getNextToken();
        } else {
            error(expectedToken);
        }

    }

    private void error(String expectedToken) {

        //throw new SyntaxException("Error Line " + currentToken.getLine() + ":\n\t" + "Error de sintaxis: Se esperaba '" + expectedToken + "' pero se encontró '" + currentToken.getToken() + "'\n");
        errors.append("Error Line " + currentToken.getLine() + ":\n\t" + "Error de sintaxis: Se esperaba '" + expectedToken + "' pero se encontró '" + currentToken.getToken() + "'\n");

        /*while (!currentToken.equals("PUNTO_Y_COMA") && !currentToken.equals("FIN") ) {
            currentToken = lexicAnalyzer.getNextToken();
        }*/

    }

    public StringBuilder getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors.length() > 0;
    }








}
