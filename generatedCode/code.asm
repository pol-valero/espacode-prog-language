.text
j $principal

$fibonacci:
	move $t0, $a0
	move $v0, $t0
	jr $ra
$L0:
	move $a0, $t3
	sw $t0, -0($sp)
	sw $t1, -4($sp)
	sw $t2, -8($sp)
	sw $t3, -12($sp)
	sw $t4, -16($sp)
	sw $t5, -20($sp)
	sw $t6, -24($sp)
	sw $t7, -28($sp)
	sw $t8, -32($sp)
	sw $t9, -36($sp)
	sw $a0, -40($sp)
	sw $a1, -44($sp)
	sw $a2, -48($sp)
	sw $a3, -52($sp)
	sw $a4, -56($sp)
	sw $ra, -60($sp)
	subi $sp, $sp, 60
	jal $fibonacci
	addi $sp, $sp, 60
	lw $t0, -0($sp)
	lw $t1, -4($sp)
	lw $t2, -8($sp)
	lw $t3, -12($sp)
	lw $t4, -16($sp)
	lw $t5, -20($sp)
	lw $t6, -24($sp)
	lw $t7, -28($sp)
	lw $t8, -32($sp)
	lw $t9, -36($sp)
	lw $a0, -40($sp)
	lw $a1, -44($sp)
	lw $a2, -48($sp)
	lw $a3, -52($sp)
	lw $a4, -56($sp)
	lw $ra, -60($sp)
	move $t1, $v0
	move $a0, $t5
	sw $t0, -0($sp)
	sw $t1, -4($sp)
	sw $t2, -8($sp)
	sw $t3, -12($sp)
	sw $t4, -16($sp)
	sw $t5, -20($sp)
	sw $t6, -24($sp)
	sw $t7, -28($sp)
	sw $t8, -32($sp)
	sw $t9, -36($sp)
	sw $a0, -40($sp)
	sw $a1, -44($sp)
	sw $a2, -48($sp)
	sw $a3, -52($sp)
	sw $a4, -56($sp)
	sw $ra, -60($sp)
	subi $sp, $sp, 60
	jal $fibonacci
	addi $sp, $sp, 60
	lw $t0, -0($sp)
	lw $t1, -4($sp)
	lw $t2, -8($sp)
	lw $t3, -12($sp)
	lw $t4, -16($sp)
	lw $t5, -20($sp)
	lw $t6, -24($sp)
	lw $t7, -28($sp)
	lw $t8, -32($sp)
	lw $t9, -36($sp)
	lw $a0, -40($sp)
	lw $a1, -44($sp)
	lw $a2, -48($sp)
	lw $a3, -52($sp)
	lw $a4, -56($sp)
	lw $ra, -60($sp)
	move $t2, $v0
	move $v0, $t7
	jr $ra
$principal:
	li $a0, 13
	sw $t0, -0($sp)
	sw $t1, -4($sp)
	sw $t2, -8($sp)
	sw $t3, -12($sp)
	sw $t4, -16($sp)
	sw $t5, -20($sp)
	sw $t6, -24($sp)
	sw $t7, -28($sp)
	sw $t8, -32($sp)
	sw $t9, -36($sp)
	sw $a0, -40($sp)
	sw $a1, -44($sp)
	sw $a2, -48($sp)
	sw $a3, -52($sp)
	sw $a4, -56($sp)
	sw $ra, -60($sp)
	subi $sp, $sp, 60
	jal $fibonacci
	addi $sp, $sp, 60
	lw $t0, -0($sp)
	lw $t1, -4($sp)
	lw $t2, -8($sp)
	lw $t3, -12($sp)
	lw $t4, -16($sp)
	lw $t5, -20($sp)
	lw $t6, -24($sp)
	lw $t7, -28($sp)
	lw $t8, -32($sp)
	lw $t9, -36($sp)
	lw $a0, -40($sp)
	lw $a1, -44($sp)
	lw $a2, -48($sp)
	lw $a3, -52($sp)
	lw $a4, -56($sp)
	lw $ra, -60($sp)
	move $t8, $v0
