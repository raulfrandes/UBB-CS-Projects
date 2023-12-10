; Given 4 bytes, compute in AX the sum of the integers represented by the bits 4-6 of the 4 bytes.

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data

    a db 10110101b
    b db 01101010b
    c db 10100101b
    d db 11110011b

; our code starts here
segment code use32 class=code
    start:
        MOV AX, 0         ; AX = 0
        MOV BL, [a]       ; BL = 1011 101b
        AND BL, 01110000b ; BL = 0011 0000b - isolate bits 4-6
        MOV CL, 4         ; CL = 4
        ROR BL, CL        ; BL = 0000 0011b = 3
        ADD AL, BL        ; AL = 0 + 3 = 3
        
        MOV BL, [b]       ; BL = 0110 1010b
        AND BL, 01110000b ; BL = 0110 0000b - isolate bits 4-6
        MOV CL, 4         ; CL = 4
        ROR BL, CL        ; BL = 0000 0110b = 6
        ADD AL, BL        ; AL = 3 + 6 = 9
        
        MOV BL, [c]       ; BL = 1010 0101b
        AND BL, 01110000b ; BL = 0010 0000b - isolate bits 4-6
        MOV CL, 4         ; CL = 4
        ROR BL, CL        ; BL = 0000 0010b = 2
        ADD AL, BL        ; AL = 9 + 2 = 11
        
        MOV BL, [d]       ; BL = 1111 0011b
        AND BL, 01110000b ; BL = 0111 0000b - isolate bits 4-6
        MOV CL, 4         ; CL = 4
        ROR BL, CL        ; BL = 0000 0111b = 7
        ADD AL, BL        ; AL = 11 + 7 = 18 - the result will be in AX
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
