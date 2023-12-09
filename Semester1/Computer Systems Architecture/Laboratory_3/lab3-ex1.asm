; Write a program in assembly language which computes a + b - c + (d - a), considering:
; a-byte, b-word, c-double word, d-qword - Unsigned representation.

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a db 19    
    b dw 21
    c dd 8
    d dq 27
; our code starts here
segment code use32 class=code
    start:
        MOV AL,[a]         ; AL = 19
        MOV AH,0           ; convert a to word => AX = 19
        ADD AX,[b]         ; AX = 19 + 21 = 40 (a + b)
        
        MOV DX,0           ; convert a + b to double word => DX:AX = 40
        SUB AX,[c] 
        SBB DX,[c+2]       ; DX:AX = 40 - 8 = 32 (a + b - c)
        
        PUSH DX
        PUSH AX
        POP EAX            ; EAX = 32
        MOV EDX,0          ; convert current result to quad => EDX:EAX = 32
        MOV EBX,dword[d]
        MOV ECX,dword[d+4] ; ECX:EBX = 27
        ADD EAX,EBX
        ADC EDX,ECX        ; EDX:EAX = 32 + 27 = 59 (a + b - c + d)
        
        MOV ECX,EDX
        MOV EBX,EAX        ; ECX:EBX = 59
        MOV EAX,0
        MOV AL,[a]         ; convert a to double => EAX = 19 
        MOV EDX,0          ; convert a to quad => EDX:EAX = 19
        SUB EBX,EAX
        SBB ECX,EDX        ; ECX:EBX = 59 - 19 = 40 (a + b - c + d - a)
        
    
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
