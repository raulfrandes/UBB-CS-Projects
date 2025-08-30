bits 32 
global start 
extern exit, printf, scanf 
import exit msvcrt.dll 
import printf msvcrt.dll 
import scanf msvcrt.dll 
segment data use32 class=data 
result resw 2 
b resw 2 
a resw 2 
temp1 resw 2 
temp2 resw 2 
temp3 resw 2 
temp4 resw 2 
format db '%d', 0 
newline db 10, 0 
segment code use32 class=code 
start: 
push dword a 
push dword format 
call [scanf] 
add esp, 4*2 
push dword b 
push dword format 
call [scanf] 
add esp, 4*2 
mov eax, 2 
mov ebx, [a] 
mul ebx 
mov [temp1], eax 
mov eax, 5 
add eax, [temp1] 
mov [temp2], eax 
mov eax, [b] 
mov ebx, 3 
mul ebx 
mov [temp3], eax 
mov eax, [temp2] 
sub eax, [temp3] 
mov [temp4], eax 
mov eax, [temp4] 
mov [result], eax 
push dword [result] 
push dword format 
call [printf] 
add esp, 4*2 
push dword newline 
call [printf] 
add esp, 4*1 
push dword 0 
call [exit] 
