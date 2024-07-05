
fibonacci:
	readParam 0 t0
	if t0 >= 2 goto L0
	return t0
L0:
	t1 = 0
	t2 = 0
	t3 = 0
	t4 = t0 - 1
	t3 = t4
	t5 = 0
	t6 = t0 - 2
	t5 = t6
	writeParam 0 t3
	t1 = call fibonacci
	writeParam 0 t5
	t2 = call fibonacci
	t7 = t1 + t2
	return t7

principal:
	t0 = 0
	writeParam 0 13
	t0 = call fibonacci
