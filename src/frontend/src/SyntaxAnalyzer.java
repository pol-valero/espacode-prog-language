package frontend.src;

import frontend.src.exceptions.SyntaxException;
import frontend.src.model.ParseTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyntaxAnalyzer {
    private LexicAnalyzer lexicAnalyzer;
    TokenData currentToken;

    private StringBuilder errors = new StringBuilder();

    public SyntaxAnalyzer(LexicAnalyzer lexicAnalyzer) {
        this.lexicAnalyzer = lexicAnalyzer;
    }

    public ParseTree syntaxAnalysis() {

        currentToken = lexicAnalyzer.getNextToken();
        codigo(List.of("EOF"));
        match("EOF");
        //return codigo();
        return null;
    }

    // Production for <CODIGO>
    private void codigo(List<String> followers) {

        //copy followers to new list
        List<String> funcionesFollowers = new ArrayList<>(followers);
        funcionesFollowers.add("PRINCIPAL");

        funciones(funcionesFollowers);
        principal(followers);

    }

    // Production for <FUNCIONES>
    private void funciones(List<String> followers) {
        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER") || currentToken.equals("VACIO")) {
            funcion(followers);
            funciones(followers);
        }
    }

    // Production for <FUNCION>
    private void funcion(List<String> followers) {
        tipoFuncion();
        match("ID");
        match("PARENTESIS_ABRIR");
        parametrosDeclaracionFuncion();
        match("PARENTESIS_CERRAR");
        bloque(followers);
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
    private void bloque(List<String> followers) {

        List<String> subbloqueFollowers = new ArrayList<>(followers);
        subbloqueFollowers.add("FIN");

        match("INICIO");
        subbloque(subbloqueFollowers);
        match("FIN");
    }

    // Production for <SUBBLOQUE>
    private void subbloque(List<String> followers) {

        List<String> sentenciaSubbloqueFollowers = new ArrayList<>(followers);
        sentenciaSubbloqueFollowers.addAll(Arrays.asList("TIPO_ENTERO", "TIPO_DECIMAL", "TIPO_CARACTER", "ID", "SI", "MIENTRAS", "RETORNO"));

        handleSubbloqueSpecificCases();

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER") ||
                currentToken.equals("ID") || currentToken.equals("SI") || currentToken.equals("MIENTRAS") || currentToken.equals("RETORNO")) {
            sentenciaSubbloque(sentenciaSubbloqueFollowers);
            subbloque(followers);
        }
    }

    // Production for <SENTENCIA_SUBBLOQUE>
    private void sentenciaSubbloque(List<String> followers) {

        if (currentToken.equals("TIPO_ENTERO") || currentToken.equals("TIPO_DECIMAL") || currentToken.equals("TIPO_CARACTER")) {
            declaracionVariable(followers);
        } else if (currentToken.equals("ID")) {
            match("ID");
            sentenciaIDsubbloque(followers);
        } else if (currentToken.equals("MIENTRAS")) {
            mientrasExpresion(followers);
        } else if (currentToken.equals("SI")) {
            siExpresion(followers);
        } else if (currentToken.equals("RETORNO")) {
            retornoExpresion();
        }
    }

    // Production for <DECLARACION_VARIABLE>
    private void declaracionVariable(List<String> followers) {
        tipo();
        match("ID");
        declaracionVariablePrime(followers);
    }

    // Production for <DECLARACION_VARIABLE'>
    private void declaracionVariablePrime(List<String> followers) {


        if (currentToken.equals("IGUAL_ASIGNACION")) {
            asignacionVariable(followers);
        } else {
            match("PUNTO_Y_COMA", followers);
        }
    }

    // Production for <SENTENCIA_ID_SUBBLOQUE>
    private void sentenciaIDsubbloque(List<String> followers) {
        if (currentToken.equals("IGUAL_ASIGNACION")) {
            asignacionVariable(followers);
        } else if (currentToken.equals("PARENTESIS_ABRIR")) {
            llamadaFuncion();
            match("PUNTO_Y_COMA");
        } else {
            error("IGUAL_ASIGNACION o PARENTESIS_ABRIR", followers);
        }
    }

    // Production for <ASIGNACION_VARIABLE>
    private void asignacionVariable(List<String> followers) {
        match("IGUAL_ASIGNACION", followers);
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
    private void mientrasExpresion(List<String> followers) {

        List<String> comparacionFollowers = new ArrayList<>(followers);
        comparacionFollowers.add("PARENTESIS_CERRAR");

        match("MIENTRAS");
        match("PARENTESIS_ABRIR");
        comparacion(comparacionFollowers);
        match("PARENTESIS_CERRAR");
        bloque(followers);
    }

    // Production for <COMPARACION>
    private void comparacion(List<String> followers) {
        match("ID", followers);
        comparacionPrime(followers);
    }

    // Production for <COMPARACION'>
    private void comparacionPrime(List<String> followers) {
        if (currentToken.equals("MAYOR") || currentToken.equals("MENOR") || currentToken.equals("MAYOR_O_IGUAL") || currentToken.equals("MENOR_O_IGUAL") || currentToken.equals("IGUAL_COMPARACION")) {
            match(currentToken.getToken(), followers);
            factor();
        } else {
            error("MAYOR, MENOR, MAYOR_O_IGUAL, MENOR_O_IGUAL o IGUAL_COMPARACION", followers);
        }

    }

    // Production for <SI_EXPRESION>
    private void siExpresion(List<String> followers) {

        List<String> bloqueFollowers = new ArrayList<>(followers);
        bloqueFollowers.add("SINO");

        //List<String> comparacionFollowers = new ArrayList<>(followers);
        //comparacionFollowers.add("PARENTESIS_CERRAR");

        match("SI");
        match("PARENTESIS_ABRIR");
        comparacion(List.of("PARENTESIS_CERRAR"));
        match("PARENTESIS_CERRAR");
        bloque(bloqueFollowers);
        sinoExpresion(followers);
    }

    // Production for <SINO_EXPRESION>
    private void sinoExpresion(List<String> followers) {
        if (currentToken.equals("SINO")) {
            match("SINO");
            bloque(followers);
        }
    }

    // Production for <PRINCIPAL>
    private void principal(List<String> followers) {

        match("PRINCIPAL", followers);
        match("PARENTESIS_ABRIR");
        match("PARENTESIS_CERRAR");
        bloque(followers);
    }

    private void match(String expectedToken) {

        if (currentToken.equals(expectedToken)){
            currentToken = lexicAnalyzer.getNextToken();
        } else {
            error(expectedToken);
        }

    }

    private void match(String expectedToken, List<String> followers) {

        if (currentToken.equals(expectedToken)){
            currentToken = lexicAnalyzer.getNextToken();
        } else {
            error(expectedToken, followers);
        }

    }

    private void error(String expectedToken, List<String> followers) {

        errors.append("Error Line " + currentToken.getLine() + ":\n\t" + "Error de sintaxis1: Se esperaba '" + expectedToken + "' pero se encontró '" + currentToken.getToken() + "'\n");

        skipTo(List.of(expectedToken), followers);

        if (currentToken.equals(expectedToken)) {
            currentToken = lexicAnalyzer.getNextToken();
        }

    }

    private void error(String expectedToken) {

        errors.append("Error Line " + currentToken.getLine() + ":\n\t" + "Error de sintaxis2: Se esperaba '" + expectedToken + "' pero se encontró '" + currentToken.getToken() + "'\n");

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

    public StringBuilder getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors.length() > 0;
    }








}
