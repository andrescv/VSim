######################
#     lbu TESTS      #
######################

.globl main

.data

tdat:
  tdat1:  .byte 0xff
  tdat2:  .byte 0x00
  tdat3:  .byte 0xf0
  tdat4:  .byte 0x0f

.text

main:

test01:
  li a1, 1
  la x1, tdat
  lbu x30, 0(x1)
  li x29, 0x000000ff
  bne x30, x29, fail

test02:
  li a1, 2
  la x1, tdat
  lbu x30, 1(x1)
  li x29, 0x00000000
  bne x30, x29, fail

test03:
  li a1, 3
  la x1, tdat
  lbu x30, 2(x1)
  li x29, 0x000000f0
  bne x30, x29, fail

test04:
  li a1, 4
  la x1, tdat
  lbu x30, 3(x1)
  li x29, 0x0000000f
  bne x30, x29, fail

test05:
  li a1, 5
  la x1, tdat4
  lbu x30, -3(x1)
  li x29, 0x000000ff
  bne x30, x29, fail

test06:
  li a1, 6
  la x1, tdat4
  lbu x30, -2(x1)
  li x29, 0x00000000
  bne x30, x29, fail

test07:
  li a1, 7
  la x1, tdat4
  lbu x30, -1(x1)
  li x29, 0x000000f0
  bne x30, x29, fail

test08:
  li a1, 8
  la x1, tdat4
  lbu x30, 0(x1)
  li x29, 0x0000000f
  bne x30, x29, fail

test09:
  li a1, 9
  la x1, tdat
  addi x1, x1, -32
  lbu x5, 32(x1)
  li x29, 0x000000ff
  bne x5, x29, fail

test10:
  li a1, 10
  la x1, tdat
  addi x1, x1, -6
  lbu x5, 7(x1)
  li x29, 0x00000000
  bne x5, x29, fail

test11:
  li a1, 11
  li x4, 0
label1_test11:
  la x1, tdat2
  lbu x30, 1(x1)
  addi x6, x30, 0
  li x29, 0x000000f0
  bne x6, x29, fail
  addi x4, x4, 1
  li x5, 2
  bne x4, x5, label1_test11

test12:
  li a1, 12
  li x4, 0
label1_test12:
  la x1, tdat3
  lbu x30, 1(x1)
  nop
  addi x6, x30, 0
  li x29, 0x0000000f
  bne x6, x29, fail
  addi x4, x4, 1
  li x5, 2
  bne x4, x5, label1_test12

test13:
  li a1, 13
  li x4, 0
label1_test13:
  la x1, tdat1
  lbu x30, 1(x1)
  nop
  nop
  addi x6, x30, 0
  li x29, 0x00000000
  bne x6, x29, fail
  addi x4, x4, 1
  li x5, 2
  bne x4, x5, label1_test13

test14:
  li a1, 14
  li x4, 0
label1_test14:
  la x1, tdat2
  lbu x30, 1(x1)
  li x29, 0x000000f0
  bne x30, x29, fail
  addi x4, x4, 1
  li x5, 2
  bne x4, x5, label1_test14

test15:
  li a1, 15
  li x4, 0
label1_test15:
  la x1, tdat3
  nop
  lbu x30, 1(x1)
  li x29, 0x0000000f
  bne x30, x29, fail
  addi x4, x4, 1
  li x5, 2
  bne x4, x5, label1_test15

test16:
  li a1, 16
  li x4, 0
label1_test16:
  la x1, tdat1
  nop
  nop
  lbu x30, 1(x1)
  li x29, 0x00000000
  bne x30, x29, fail
  addi x4, x4, 1
  li x5, 2
  bne x4, x5, label1_test16

test17:
  li a1, 17
  la x5, tdat
  lbu x2, 0(x4)
  li x2, 2
  li x29, 0x00000002
  bne x2, x29, fail

test18:
  li a1, 18
  la x5, tdat
  lbu x2, 0(x5)
  nop
  li x2, 2
  li x29, 0x00000002
  bne x2, x29, fail

success:
  li a0, 10
  ecall

fail:
  li a0, 17
  ecall
