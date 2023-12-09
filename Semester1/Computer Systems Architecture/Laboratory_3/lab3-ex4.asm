; Write a program in assembly language which computes x - (a * b * 25 + c * 3) / (a + b) + e, considering:
; a, b, c - byte; e - doubleword; x - qword - Signed representation


bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a db -1
    b db -9
    c db -5
    e dd 10
    x dq 1033

; our code starts here
segment code use32 class=code
    start:
        MOV AL,[a]         ; AL = -1
        IMUL byte[b]       ; AX = -1 * (-9) = 9 (a * b)

        MOV BX,25          ; BX = 25
        IMUL BX            ; DX:AX = 9 * 25 = 225 (a * b * 25)

        PUSH DX
        PUSH AX
        POP EBX            ; EBX = 225
        MOV AL,3           ; AL = 3
        IMUL byte[c]       ; AX = 3 * (-5) = -15 (c * 3)
        
        CWD                ; convert (c * 3) to double => DX:AX = 15
        PUSH DX
        PUSH AX
        POP EAX            ; EAX = 15
        ADD EBX,EAX        ; EBX = 225 + (-15) = 210 (a * b * 25 + c * 3)
        
        MOV AL,[a]         ; BL = -1
        ADD AL,[b]         ; BL = -1 + (-9) = -10 (a + b)
        
        CBW                ; convert (a + b) to word => AX = -10
        MOV CX,AX          ; CX = -10
        PUSH EBX
        POP AX
        POP DX             ; DX:AX = 210
        IDIV CX            ; AX = 210 / (-10) = -21 ((a * b * 25 + c * 3) / (a + b))
        
        ADD AX,[e]         ; AX = -21 + 10 = -11 ((a * b * 25 + c * 3) / (a + b) + e)
        
        MOV EBX,dword[x]
        MOV ECX,dword[x+4] ; ECX:EBX = 1033
        CWD                ; convert ((a * b * 25 + c * 3) / (a + b) + e) to double => DX:AX = -11
        CDQ                ; convert ((a * b * 25 + c * 3) / (a + b) + e) to qword => EDX:EAX = -11
        SUB EBX,EAX
        SBB ECX,EDX        ; ECX:EBX = 1033 - (-11) = 1044 (x - (a * b * 25 + c * 3) / (a + b) + e) 
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
