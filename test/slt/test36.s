.globl main

.text

main:
  li x1, 0xffffffff
  slt x2, x1, x0
  li x29, 0x00000001
  bne x2, x29, fail

sucess:
  li a0, 10
  ecall

fail:
  li a0, 17
  li a1, 1
  ecall