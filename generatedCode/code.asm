.text
j $principal

$test:
	move $t0, $a0
	move $t1, $a1
	addi $t2, $t0, 1
	move $t0, $t2
	addi $t3, $t1, 2
	move $t1, $t3
	li $t4, 0
$L0:
	li $s1, 3
	bge $t4, $s1, $L1
	addi $t5, $t0, 1
	move $t0, $t5
	addi $t6, $t1, 2
	move $t1, $t6
	addi $t7, $t4, 1
	move $t4, $t7
	j $L0
$L1:
	add $t8, $t0, $t1
	move $v0, $t8
	jr $ra
$principal:
	li $a0, 1
	li $a1, 2
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
	move $t0, $v0
