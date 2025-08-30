#ifndef CODEASM_H
#define CODEASM_H

#define ASM_PROGRAM "\
bits 32 \n\
global start \n\
extern exit, printf, scanf \n\
import exit msvcrt.dll \n\
import printf msvcrt.dll \n\
import scanf msvcrt.dll \n\
segment data use32 class=data \n\
%s\
format db '%%d', 0 \n\
newline db 10, 0 \n\
segment code use32 class=code \n\
start: \n\
%s\
push dword 0 \n\
call [exit] \n\
"

#define VAR_DECLARATION_ASM_FORMAT "\
%s resw 2 \n\
"

#define VAR_DECLARATION_INIT_ASM_FORMAT "\
%s dd %s \n\
"

#define INPUT_ASM_FORMAT "\
push dword %s \n\
push dword format \n\
call [scanf] \n\
add esp, 4*2 \n\
"

#define OUTPUT_VAR_ASM_FORMAT "\
push dword %s \n\
push dword format \n\
call [printf] \n\
add esp, 4*2 \n\
"

#define OUTPUT_ENDL_ASM_FORMAT "\
push dword newline \n\
call [printf] \n\
add esp, 4*1 \n\
"

#define ASSIGN_ASM_FORMAT "\
mov eax, %s \n\
mov %s, eax \n\
"

#define OPERATION_ASM_FORMAT "\
mov eax, %s \n\
%s eax, %s \n\
mov %s, eax \n\
"

#define MUL_ASM_FORMAT "\
mov eax, %s \n\
mov ebx, %s \n\
mul ebx \n\
mov %s, eax \n\
"

#endif