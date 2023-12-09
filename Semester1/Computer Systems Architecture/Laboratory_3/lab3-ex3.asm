; Write a program in assembly language which computes x - (a * b * 25 + c * 3) / (a + b) + e, considering:
; a, b, c - byte; e - doubleword; x - qword - Unsigned representation


bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a db 1 
    b db 9
    c db 5
    e dw 10
    x dq 1033

; our code starts here
segment code use32 class=code
    start:
        MOV AL,[a]         ; AL = 1
        MUL byte[b]        ; AX = 1 * 9 = 9 (a * b)

        MOV BX,25          ; BX = 25
        MUL BX             ; DX:AX = 9 * 25 = 225 (a * b * 25)

        PUSH DX
        PUSH AX
        POP EBX            ; EBX = 225
        MOV AL,3           ; AL = 3
        MUL byte[c]        ; AX = 3 * 5 = 15 (c * 3)

        MOV ECX,0          ; ECX = 0
        MOV CX,AX          ; convert (c * 3) to double => ECX = 15
        ADD EBX,ECX        ; EBX = 225 + 15 = 240 (a * b * 25 + c * 3)

        PUSH EBX
        POP AX
        POP DX             ; DX:AX = 240
        MOV BL,[a]         ; BL = 1
        ADD BL,[b]         ; BL = 1 + 9 = 10 (a + b)

        MOV BH,0           ; convert (a + b) to word => BX = 10
        DIV BX             ; AX = 240 / 10 = 24 ((a * b * 25 + c * 3) / (a + b))

        ADD AX,[e]         ; AX = 24 + 10 = 34 ((a * b * 25 + c * 3) / (a + b) + e)

        MOV EBX,dword[x]
        MOV ECX,dword[x+4] ; ECX:EBX = 1033
        MOV DX,AX          ; DX = 34
        MOV EAX,0
        MOV AX,DX          ; convert ((a * b * 25 + c * 3) / (a + b) + e) to double => EAX = 34
        MOV EDX,0          ; convert ((a * b * 25 + c * 3) / (a + b) + e) to qword => EDX:EAX = 34
        SUB EBX,EAX
        SBB ECX,EDX        ; ECX:EBX = 1033 - 34 = 999 (x - (a * b * 25 + c * 3) / (a + b) + e)
        
        
        
    
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
