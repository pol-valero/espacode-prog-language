.text
j $principal

$fibonacci:
	move $t0, $a0
	li $s1, 2
	bge $t0, $s1, $L0
	move $v0, $t0
	jr $ra
$L0:
	li $t1, 0
	li $t2, 0
	li $t3, 0
	subi $t4, $t0, 1
	move $t3, $t4
	li $t5, 0
	subi $t6, $t0, 2
	move $t5, $t6
	move $a0, $t3
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
	move $t1, $v0
	move $a0, $t5
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
	move $t2, $v0
	add $t7, $t1, $t2
	move $v0, $t7
	jr $ra
$principal:
	li $t0, 0
	li $a0, 13
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
	move $t0, $v0
