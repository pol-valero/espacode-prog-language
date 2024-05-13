package frontend.src;

import frontend.src.exceptions.SyntaxException;
import frontend.src.model.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        ParseTree parseTree;

        currentToken = lexicAnalyzer.getNextToken();
        parseTree = codigo(List.of("EOF"));
        match("EOF");

        return parseTree;
        //return null;
    }

    // Production for <CODIGO>
    private ParseTree codigo(List<String> followers) {

        ParseTree codigo = new ParseTree("CODIGO");


        //copy followers to new list
        List<String> funcionesFollowers = new ArrayList<>(followers);
        funcionesFollowers.add("PRINCIPAL");

        codigo.addChild(funciones(funcionesFollowers));
        codigo.addChild(principal(followers));

        return codigo;

    }

    // Production for <FUNCIONES>
    private ParseTree funciones(List<String> followers) {

        ParseTree funciones = new ParseTree("FUNCIONES");

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER") || currentToken.equals("VACIO")) {
            funciones.addChild(funcion(followers));
            funciones.addChild(funciones(followers));
        }

        return funciones;
    }

    // Production for <FUNCION>
    private ParseTree funcion(List<String> followers) {

        ParseTree funcion = new ParseTree("FUNCION");

        funcion.addChild(tipoFuncion());
        funcion.addChild(match("ID"));
        funcion.addChild(match("PARENTESIS_ABRIR"));
        funcion.addChild(parametrosDeclaracionFuncion());
        funcion.addChild(match("PARENTESIS_CERRAR"));
        funcion.addChild(bloque(followers));

        return funcion;
    }

    // Production for <TIPO_FUNCION>
    private ParseTree tipoFuncion() {

        ParseTree tipoFuncion = new ParseTree("TIPO_FUNCION");

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            tipoFuncion.addChild(tipo());
        } else if (currentToken.equals("VACIO")){
            tipoFuncion.addChild(match("VACIO"));
        } else {
            error("TIPO_ENTERO, TIPO_DECIMAL, TIPO_CARACTER o VACIO");
        }

        return tipoFuncion;
    }

    // Production for <TIPO>
    private ParseTree tipo() {

        ParseTree tipo = new ParseTree("TIPO");

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            tipo.addChild(match(currentToken.getToken()));
        } else {
            error("TIPO_ENTERO, TIPO_DECIMAL o TIPO_CARACTER");
        }

        return tipo;
    }

    // Production for <PARAMETROS_DECLARACION_FUNCION>
    private ParseTree parametrosDeclaracionFuncion() {

        ParseTree parametrosDeclaracionFuncion = new ParseTree("PARAMETROS_DECLARACION_FUNCION");

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            parametrosDeclaracionFuncion.addChild(tipo());
            parametrosDeclaracionFuncion.addChild(match("ID"));
            parametrosDeclaracionFuncion.addChild(parametrosDeclaracionFuncionPrime());
        }

        return parametrosDeclaracionFuncion;
    }

    // Production for <PARAMETROS_DECLARACION_FUNCION'>
    private ParseTree parametrosDeclaracionFuncionPrime() {

        ParseTree parametrosDeclaracionFuncionPrime = new ParseTree("PARAMETROS_DECLARACION_FUNCION'");

        if (currentToken.equals("COMA")) {
            parametrosDeclaracionFuncionPrime.addChild(match("COMA"));
            parametrosDeclaracionFuncionPrime.addChild(tipo());
            parametrosDeclaracionFuncionPrime.addChild(match("ID"));
            parametrosDeclaracionFuncionPrime.addChild(parametrosDeclaracionFuncionPrime());
        }

        return parametrosDeclaracionFuncionPrime;
    }

    // Production for <BLOQUE>
    private ParseTree bloque(List<String> followers) {

        ParseTree bloque = new ParseTree("BLOQUE");

        List<String> subbloqueFollowers = new ArrayList<>(followers);
        subbloqueFollowers.add("FIN");

        bloque.addChild(match("INICIO"));
        bloque.addChild(subbloque(subbloqueFollowers));
        bloque.addChild(match("FIN"));

        return bloque;
    }

    // Production for <SUBBLOQUE>
    private ParseTree subbloque(List<String> followers) {

        ParseTree subbloque = new ParseTree("SUBBLOQUE");

        List<String> sentenciaSubbloqueFollowers = new ArrayList<>(followers);
        sentenciaSubbloqueFollowers.addAll(Arrays.asList("TIPO_ENTERO", "TIPO_DECIMAL", "TIPO_CARACTER", "ID", "SI", "MIENTRAS", "RETORNO"));

        handleSubbloqueSpecificCases();

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER") ||
                currentToken.equals("ID") || currentToken.equals("SI") || currentToken.equals("MIENTRAS") || currentToken.equals("RETORNO")) {

            subbloque.addChild(sentenciaSubbloque(sentenciaSubbloqueFollowers));
            subbloque.addChild(subbloque(followers));

        }

        return subbloque;
    }

    // Production for <SENTENCIA_SUBBLOQUE>
    private ParseTree sentenciaSubbloque(List<String> followers) {

        ParseTree sentenciaSubbloque = new ParseTree("SENTENCIA_SUBBLOQUE");

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            sentenciaSubbloque.addChild(declaracionVariable(followers));
        } else if (currentToken.equals("ID")) {
            sentenciaSubbloque.addChild(match("ID"));
            sentenciaSubbloque.addChild(sentenciaIDsubbloque(followers));
        } else if (currentToken.equals("MIENTRAS")) {
            sentenciaSubbloque.addChild(mientrasExpresion(followers));
        } else if (currentToken.equals("SI")) {
            sentenciaSubbloque.addChild(siExpresion(followers));
        } else if (currentToken.equals("RETORNO")) {
            sentenciaSubbloque.addChild(retornoExpresion());
        }

        return sentenciaSubbloque;
    }

    // Production for <DECLARACION_VARIABLE>
    private ParseTree declaracionVariable(List<String> followers) {

        ParseTree declaracionVariable = new ParseTree("DECLARACION_VARIABLE");

        declaracionVariable.addChild(tipo());
        declaracionVariable.addChild(match("ID"));
        declaracionVariable.addChild(declaracionVariablePrime(followers));

        return declaracionVariable;
    }

    // Production for <DECLARACION_VARIABLE'>
    private ParseTree declaracionVariablePrime(List<String> followers) {

        ParseTree declaracionVariablePrime = new ParseTree("DECLARACION_VARIABLE'");

        if (currentToken.equals("IGUAL_ASIGNACION")) {
            declaracionVariablePrime.addChild(asignacionVariable(followers));
        } else {
            declaracionVariablePrime.addChild(match("PUNTO_Y_COMA", followers));
        }

        return declaracionVariablePrime;
    }

    // Production for <SENTENCIA_ID_SUBBLOQUE>
    private ParseTree sentenciaIDsubbloque(List<String> followers) {

        ParseTree sentenciaIDsubbloque = new ParseTree("SENTENCIA_ID_SUBBLOQUE");

        if (currentToken.equals("IGUAL_ASIGNACION")) {
            sentenciaIDsubbloque.addChild(asignacionVariable(followers));
        } else if (currentToken.equals("PARENTESIS_ABRIR")) {
            sentenciaIDsubbloque.addChild(llamadaFuncion());
            sentenciaIDsubbloque.addChild(match("PUNTO_Y_COMA"));
        } else {
            error("IGUAL_ASIGNACION o PARENTESIS_ABRIR", followers);
        }

        return sentenciaIDsubbloque;
    }

    // Production for <ASIGNACION_VARIABLE>
    private ParseTree asignacionVariable(List<String> followers) {

        ParseTree asignacionVariable = new ParseTree("ASIGNACION_VARIABLE");

        asignacionVariable.addChild(match("IGUAL_ASIGNACION", followers));
        asignacionVariable.addChild(asignacionVariablePrime());
        asignacionVariable.addChild(match("PUNTO_Y_COMA"));

        return asignacionVariable;
    }

    // Production for <ASIGNACION_VARIABLE'>
    private ParseTree asignacionVariablePrime() {

        ParseTree asignacionVariablePrime = new ParseTree("ASIGNACION_VARIABLE'");

        if (currentToken.equals("ID") || currentToken.equals("VALOR_ENTERO") || currentToken.equals("VALOR_DECIMAL") || currentToken.equals("PARENTESIS_ABRIR")) {
            asignacionVariablePrime.addChild(expresion());
        } else if (currentToken.equals("COMILLA")) {
            asignacionVariablePrime.addChild(caracter());
        } else {
            error("ID, VALOR_ENTERO, VALOR_DECIMAL, VALOR_CARACTER, PARENTESIS_ABRIR o COMILLA");
        }

        return asignacionVariablePrime;
    }

    // Production for <CARACTER>
    private ParseTree caracter() {

        ParseTree caracter = new ParseTree("CARACTER");

        caracter.addChild(match("COMILLA"));
        caracter.addChild(caracterPrime());
        caracter.addChild(match("COMILLA"));

        return caracter;
    }

    // Production for <CARACTER'>
    private ParseTree caracterPrime() {

        ParseTree caracterPrime = new ParseTree("CARACTER'");

        if (currentToken.equals("ID")) {
            caracterPrime.addChild(match("ID"));
        } else if(currentToken.equals("VALOR_ENTERO")) {
            caracterPrime.addChild(match("VALOR_ENTERO"));
        } else {
            error("letras o numeros");
        }

        return caracterPrime;
    }



    // Production for <EXPRESION>
    private ParseTree expresion() {

        ParseTree expresion = new ParseTree("EXPRESION");

        expresion.addChild(termino());
        expresion.addChild(expresionPrime());

        return expresion;
    }

    // Production for <TERMINO>
    private ParseTree termino() {

        ParseTree termino = new ParseTree("TERMINO");

        termino.addChild(factor());
        termino.addChild(terminoPrime());

        return termino;
    }

    // Production for <TERMINO'>
    private ParseTree terminoPrime() {

        ParseTree terminoPrime = new ParseTree("TERMINO'");

        if (currentToken.equals("MULTIPLICACION") || currentToken.equals("DIVISION")) {
            terminoPrime.addChild(match(currentToken.getToken()));
            terminoPrime.addChild(factor());
            terminoPrime.addChild(terminoPrime());
        }

        return terminoPrime;
    }

    // Production for <EXPRESION'>
    private ParseTree expresionPrime() {

        ParseTree expresionPrime = new ParseTree("EXPRESION'");

        if (currentToken.equals("MAS") || currentToken.equals("MENOS")) {
            expresionPrime.addChild(match(currentToken.getToken()));
            expresionPrime.addChild(termino());
            expresionPrime.addChild(expresionPrime());
        }

        return expresionPrime;
    }

    // Production for <FACTOR>
    private ParseTree factor() {

        ParseTree factor = new ParseTree("FACTOR");

        if (currentToken.equals("ID")) {
            factor.addChild(match("ID"));
            factor.addChild(llamadaFuncionPrime());
        } else if (currentToken.equals("VALOR_ENTERO") || currentToken.equals("VALOR_DECIMAL")) {
            factor.addChild(match(currentToken.getToken()));
        } else if (currentToken.equals("PARENTESIS_ABRIR")) {
            factor.addChild(match("PARENTESIS_ABRIR"));
            factor.addChild(expresion());
            factor.addChild(match("PARENTESIS_CERRAR"));
        } else {
            error("ID, VALOR_ENTERO, VALOR_DECIMAL o PARENTESIS_ABRIR");
        }

        return factor;
    }

    // Production for <LLAMADA_FUNCION>
    private ParseTree llamadaFuncion() {

        ParseTree llamadaFuncion = new ParseTree("LLAMADA_FUNCION");

        llamadaFuncion.addChild(match("PARENTESIS_ABRIR"));
        llamadaFuncion.addChild(parametrosLlamadaFuncion());
        llamadaFuncion.addChild(match("PARENTESIS_CERRAR"));

        return llamadaFuncion;
    }

    // Production for <LLAMADA_FUNCION_PRIME>
    private ParseTree llamadaFuncionPrime() {

        ParseTree llamadaFuncionPrime = new ParseTree("LLAMADA_FUNCION'");

        if (currentToken.equals("PARENTESIS_ABRIR")) {
            llamadaFuncionPrime.addChild(match("PARENTESIS_ABRIR"));
            llamadaFuncionPrime.addChild(parametrosLlamadaFuncion());
            llamadaFuncionPrime.addChild(match("PARENTESIS_CERRAR"));
        }

        return llamadaFuncionPrime;
    }

    // Production for <PARAMETROS_LLAMADA_FUNCION>
    private ParseTree parametrosLlamadaFuncion() {

        ParseTree parametrosLlamadaFuncion = new ParseTree("PARAMETROS_LLAMADA_FUNCION");

        if (currentToken.equals("ID") || currentToken.equals("VALOR_ENTERO") || currentToken.equals("VALOR_DECIMAL") ||
                currentToken.equals("PARENTESIS_ABRIR")) {
            parametrosLlamadaFuncion.addChild(expresion());
            parametrosLlamadaFuncion.addChild(parametrosLlamadaFuncionPrime());
        }

        return parametrosLlamadaFuncion;
    }

    // Production for <PARAMETROS_LLAMADA_FUNCION'>
    private ParseTree parametrosLlamadaFuncionPrime() {

        ParseTree parametrosLlamadaFuncionPrime = new ParseTree("PARAMETROS_LLAMADA_FUNCION'");

        if (currentToken.equals("COMA")) {
            parametrosLlamadaFuncionPrime.addChild(match("COMA"));
            parametrosLlamadaFuncionPrime.addChild(expresion());
            parametrosLlamadaFuncionPrime.addChild(parametrosLlamadaFuncionPrime());
        }

        return parametrosLlamadaFuncionPrime;
    }

    // Production for <RETORNO_EXPRESION>
    private ParseTree retornoExpresion() {

        ParseTree retornoExpresion = new ParseTree("RETORNO_EXPRESION");

        retornoExpresion.addChild(match("RETORNO"));
        retornoExpresion.addChild(expresion());
        retornoExpresion.addChild(match("PUNTO_Y_COMA"));

        return retornoExpresion;
    }

    // Production for <MIENTRAS_EXPRESION>
    private ParseTree mientrasExpresion(List<String> followers) {

        ParseTree mientrasExpresion = new ParseTree("MIENTRAS_EXPRESION");

        List<String> comparacionFollowers = new ArrayList<>(followers);
        comparacionFollowers.add("PARENTESIS_CERRAR");

        mientrasExpresion.addChild(match("MIENTRAS"));
        mientrasExpresion.addChild(match("PARENTESIS_ABRIR"));
        mientrasExpresion.addChild(comparacion(comparacionFollowers));
        mientrasExpresion.addChild(match("PARENTESIS_CERRAR"));
        mientrasExpresion.addChild(bloque(followers));

        return mientrasExpresion;
    }

    // Production for <COMPARACION>
    private ParseTree comparacion(List<String> followers) {

        ParseTree comparacion = new ParseTree("COMPARACION");

        comparacion.addChild(match("ID", followers));
        comparacion.addChild(comparacionPrime(followers));

        return comparacion;
    }

    // Production for <COMPARACION'>
    private ParseTree comparacionPrime(List<String> followers) {

        ParseTree comparacionPrime = new ParseTree("COMPARACION'");

        if (currentToken.equals("MAYOR") || currentToken.equals("MENOR") || currentToken.equals("MAYOR_O_IGUAL") || currentToken.equals("MENOR_O_IGUAL") || currentToken.equals("IGUAL_COMPARACION")) {
            comparacionPrime.addChild(match(currentToken.getToken(), followers));
            comparacionPrime.addChild(factor());
        } else {
            error("MAYOR, MENOR, MAYOR_O_IGUAL, MENOR_O_IGUAL o IGUAL_COMPARACION", followers);
        }

        return comparacionPrime;
    }

    // Production for <SI_EXPRESION>
    private ParseTree siExpresion(List<String> followers) {

        ParseTree siExpresion = new ParseTree("SI_EXPRESION");

        List<String> bloqueFollowers = new ArrayList<>(followers);
        bloqueFollowers.add("SINO");

        //List<String> comparacionFollowers = new ArrayList<>(followers);
        //comparacionFollowers.add("PARENTESIS_CERRAR");

        siExpresion.addChild(match("SI"));
        siExpresion.addChild(match("PARENTESIS_ABRIR"));
        siExpresion.addChild(comparacion(List.of("PARENTESIS_CERRAR")));
        siExpresion.addChild(match("PARENTESIS_CERRAR"));
        siExpresion.addChild(bloque(bloqueFollowers));
        siExpresion.addChild(sinoExpresion(followers));

        return siExpresion;
    }

    // Production for <SINO_EXPRESION>
    private ParseTree sinoExpresion(List<String> followers) {

        ParseTree sinoExpresion = new ParseTree("SINO_EXPRESION");

        if (currentToken.equals("SINO")) {
            sinoExpresion.addChild(match("SINO"));
            sinoExpresion.addChild(bloque(followers));
        }

        return sinoExpresion;
    }

    // Production for <PRINCIPAL>
    private ParseTree principal(List<String> followers) {

        ParseTree principal = new ParseTree("PRINCIPAL");

        principal.addChild(match("PRINCIPAL", followers));
        principal.addChild(match("PARENTESIS_ABRIR"));
        principal.addChild(match("PARENTESIS_CERRAR"));
        principal.addChild(bloque(followers));

        return principal;
    }

    private ParseTree match(String expectedToken) {

        String currentTokenName = currentToken.getToken();
        String currentTokenLexeme = currentToken.getLexeme();

        if (currentToken.equals(expectedToken)){
            currentToken = lexicAnalyzer.getNextToken();
            return new ParseTree(currentTokenName, currentTokenLexeme);
        } else {
            error(expectedToken);
        }

        return null;

    }

    private ParseTree match(String expectedToken, List<String> followers) {

        String currentTokenName = currentToken.getToken();
        String currentTokenLexeme = currentToken.getLexeme();

        if (currentToken.equals(expectedToken)){
            currentToken = lexicAnalyzer.getNextToken();
            return new ParseTree(currentTokenName, currentTokenLexeme);
        } else {
            error(expectedToken, followers);
        }

        return null;

    }

    private void error(String expectedToken, List<String> followers) /*throws SyntaxException*/ {

        ErrorHandler.addError("Error Linia " + currentToken.getLine() + ":\n\t" + "Error de sintaxis: Se esperaba '" + expectedToken + "' pero se encontró '" + currentToken.getToken());

        skipTo(List.of(expectedToken), followers);

        if (currentToken.equals(expectedToken)) {
            currentToken = lexicAnalyzer.getNextToken();
        }

        //throw new SyntaxException();

    }

    private void error(String expectedToken) /*throws SyntaxException*/ {

        ErrorHandler.addError("Error Linia " + currentToken.getLine() + ":\n\t" + "Error de sintaxis: Se esperaba '" + expectedToken + "' pero se encontró '" + currentToken.getToken());

        //throw new SyntaxException();

    }


    private void handleSubbloqueSpecificCases() {
        //Possible cases to handle when a statement in a subbloque is finished and the next one is about to start (if it exists)
        //Specific case to handle extra ";" at the end of the subbloque, and continue with the next subbloque if it exists.
        //Specific case to handle extra independent numbers at the end of the subbloque, and continue with the next subbloque if it exists...

        //Here we only handle the errors that happen when just starting a new subbloque (if an error happens in the middle of a subbloque, it will be handled elsewhere more appropriately)

        List<String> tokensToCheck = Arrays.asList("PUNTO_Y_COMA", "VALOR_ENTERO", "VALOR_DECIMAL");

        for (String token : tokensToCheck) {
            // Check if currentToken matches the token
            while (currentToken.equals(token)) {
                error("TIPO_ENTERO, TIPO_DECIMAL, TIPO_CARACTER, ID, SI, MIENTRAS o RETORNO");
                match(token);
            }
        }

    }

    private void skipTo(List<String> starters, List<String> follows) {
        while (!follows.contains(currentToken.getToken()) && !starters.contains(currentToken.getToken())) {

            currentToken = lexicAnalyzer.getNextToken();

        }
    }








}
