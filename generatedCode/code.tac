
test:
	t0 = 1
	return t0

fibonacci:
	readParam 0 t1
	if t1 >= 2 goto L0
	return t1
L0:
	t2 = 0
	t3 = 0
	t4 = 0
	t5 = t1 - 1
	t4 = t5
	t6 = 0
	t7 = t1 - 2
	t6 = t7
	writeParam 0 t4
	t2 = call fibonacci
	writeParam 0 t6
	t3 = call fibonacci
	t8 = t2 + t3
	return t8

principal:
	t9 = 0
	writeParam 0 13
	t9 = call fibonacci
