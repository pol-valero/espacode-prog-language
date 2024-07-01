.text
j $principal

$fibonacci:
	move $t0, $a0
	li $t1, 0
	li $t2, 1
	li $t3, 0
	li $t4, 2
	li $s1, 1
	bgt $t0, $s1, $L0
	move $v0, $t0
	jr $ra
$L0:
$L1:
	bgt $t4, $t0, $L2
	add $t5, $t1, $t2
	move $t3, $t5
	move $t1, $t2
	move $t2, $t3
	addi $t6, $t4, 1
	move $t4, $t6
	j $L1
$L2:
	move $v0, $t3
	jr $ra
$principal:
	li $t7, 8
	li $t8, 0
	move $a0, $t7
	sw $t0, -4($sp)
	sw $t1, -8($sp)
	sw $t2, -12($sp)
	sw $t3, -16($sp)
	sw $t4, -20($sp)
	sw $t5, -24($sp)
	sw $t6, -28($sp)
	sw $t7, -32($sp)
	sw $t8, -36($sp)
	sw $t9, -40($sp)
	sw $a0, -44($sp)
	sw $a1, -48($sp)
	sw $a2, -52($sp)
	sw $a3, -56($sp)
	sw $ra, -60($sp)
	subi $sp, $sp, 60
	jal $fibonacci
	addi $sp, $sp, 60
	lw $t0, -4($sp)
	lw $t1, -8($sp)
	lw $t2, -12($sp)
	lw $t3, -16($sp)
	lw $t4, -20($sp)
	lw $t5, -24($sp)
	lw $t6, -28($sp)
	lw $t7, -32($sp)
	lw $t8, -36($sp)
	lw $t9, -40($sp)
	lw $a0, -44($sp)
	lw $a1, -48($sp)
	lw $a2, -52($sp)
	lw $a3, -56($sp)
	lw $ra, -60($sp)
	move $t8, $v0
