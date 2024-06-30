fibonacci:
    readParam 0 var_numero
    actual = 0
    siguiente = 1
    resultado = 0
    i = 2
    if var_numero > 1 goto L0
    retorno var_numero
L0:
    if i > var_numero goto L1
    resultado = actual + siguiente
    actual = siguiente
    siguiente = resultado
    i = i + 1
    goto L0
L1:
    retorno resultado

principal:
    numero = 10
    resultado = 0
    writeParam 0 numero
    resultado = call fibonacci
