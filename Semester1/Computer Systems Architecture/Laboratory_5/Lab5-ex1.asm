; Two byte strings A and B are given. Obtain the string R by concatenating the elements of B in reverse order and the odd elements of A.
; Example:
;   A: 2, 1, 3, 3, 4, 2, 6
;   B: 4, 5, 7, 6, 2, 1
;   R: 1, 2, 6, 7, 5, 4, 1, 3, 3

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data

    a db 2, 1, 3, 3, 4, 2, 6
    la equ $-a
    b db 4, 5, 7, 6, 2, 1
    lb equ $-b 
    r resb la+lb
    
; our code starts here
segment code use32 class=code
    start:
        MOV ECX, lb              ; length of b
        MOV ESI, b+la-2          ; position ESI on the last element of b
        MOV EDI, r               ; position EDI on r
        jecxz do_a              ; jump on the next step if the length of b is 0
        repeat_loop:
            STD                  ; DF = 1
            LODSB                ; AL = [b+5] = 1, ESI--
            CLD                  ; DF = 0
            STOSB                ; [EDI] = AL = 1, EDI++
            loop repeat_loop
        do_a:
            MOV ECX, la          ; length of a
            MOV ESI, a           ; position ESI on the first element of a
            CLD                  ; DF = 0
            jecxz final          ; jump to final if the length of a is 0
            repeat_loop2:
                LODSB            ; AL = [a+0] = 2
                TEST AL, 01h     ; test parity
                jz next          ; jump if ZF = 0 (if Al is even)
                STOSB            ; [EDI] = AL = 1
                next:
                    loop repeat_loop2
    final:
            
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
