.globl main

.text

main:
  li x1, 0x80000000
  li x2, 0x00000007
  sra x30, x1, x2
  li x29, 0xff000000
  bne x30, x29, fail

sucess:
  li a0, 10
  ecall

fail:
  li a0, 17
  li a1, 1
  ecall