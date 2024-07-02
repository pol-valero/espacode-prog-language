.text
j $principal

$test:
	move $t0, $a0
$principal:
	li $t1, 0
	li $t2, a
	li $t3, 0
	li $t3, a
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
	jal $test
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
