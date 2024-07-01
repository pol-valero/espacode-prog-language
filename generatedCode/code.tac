
fibonacci:
	readParam 0 t0
	t1 = 0
	t2 = 1
	t3 = 0
	t4 = 2
	if t0 > 1 goto L0
	return t0
L0:
L1:
	if t4 > t0 goto L2
	t5 = t1 + t2
	t3 = t5
	t1 = t2
	t2 = t3
	t6 = t4 + 1
	t4 = t6
	goto L1
L2:
	return t3

principal:
	t7 = 8
	t8 = 0
	writeParam 0 t7
	t8 = call fibonacci
