
test:
	readParam 0 t0
	readParam 1 t1
	t2 = t0 + 1
	t0 = t2
	t3 = t1 + 2
	t1 = t3
	t4 = 0
L0:
	if t4 >= 3 goto L1
	t5 = t0 + 1
	t0 = t5
	t6 = t1 + 2
	t1 = t6
	t7 = t4 + 1
	t4 = t7
	goto L0
L1:
	t8 = t0 + t1
	return t8

principal:
	writeParam 0 1
	writeParam 1 2
	t9 = call test
