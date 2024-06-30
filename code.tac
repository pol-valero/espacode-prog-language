fibonacci:
    readParam 0 t0
    if t0 >= 2 goto L0
    return t0
L0:
    t1 = t0 - 1
    t2 = t0 - 2
    writeParam 0 t1
    t3 = call fibonacci
    writeParam 0 t2
    t4 = call fibonacci
    t5 = t3 + t4
    return t5

main:
    writeParam 0 13
    t6 = call fibonacci
    call fibonacci