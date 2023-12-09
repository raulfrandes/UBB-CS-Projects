; Write a program in assembly language which computes c + a + b + b + a, considering:
; a-byte, b-word, c-double word, d-qword - Signed representation


bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a db -10
    b dw -20
    c dd 22
    
; our code starts here
segment code use32 class=code
    start:
        MOV EBX,[c] ; EBX = 22
        
        MOV AL,[a]  ; AL = -10
        CBW         ; convert a to word => AX = -10
        CWD         ; convert a to double => DX:AX = -10
        PUSH DX
        PUSH AX
        POP EAX     ; EAX = -10
        ADD EBX,EAX ; EBX = 22 - 10 = 12 (c + a)
        
        MOV AX,[b]  ; AX = -20
        CWD         ; convert b  to double => DX:AX = -20
        PUSH DX
        PUSH AX
        POP EAX     ; EAX = -20
        ADD EBX,EAX ; EBX = 12 + (-20) = -8 (c + a + b)
        
        ADD EBX,EAX ; EBX = -8 + (-20) = -28 (c + a + b + b)
        
        MOV AL,[a]  ; AL = -10
        CBW         ; convert a to word => AX = -10
        CWD         ; convert a to double => DX:AX = -10
        PUSH DX
        PUSH AX
        POP EAX     ; EAX = -10
        ADD EBX,EAX ; EBX = -28 + (-10) = -38 (c + a + b + b + a)
        
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
