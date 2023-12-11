; Two character strings S1 and S2 are given. Obtain the string D by concatenating the elements found on odd positions in S2 and the elements found on ; even positions in S1.
; Example:
;   S1: 'a', 'b', 'c', 'b', 'e', 'f'
;   S2: '1', '2', '3', '4', '5'
;   D: '1', '3', '5', 'b', 'b', 'f'

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    
    s1 db 'a', 'b', 'c', 'b', 'e', 'f'
    l1 equ $-s1 
    s2 db '1', '2', '3', '4', '5'
    l2 equ $-s2 
    d times l1+l2 db 0

; our code starts here
segment code use32 class=code
    start:
        MOV AL, 01h
        TEST AL, l2 
        jz et
        MOV ECX, l2/2+1      ; the number of elements from odd positions if it has an odd number of elements
        jmp et2
        et:
        MOV ECX, l2/2        ; the number of elements from odd positions if it has an even number of elements
        et2:
        MOV ESI, s2          ; position ESI on the first element of s2
        MOV EDI, d           ; position EDI on d
        CLD                  ; DF = 0
        jecxz do_s1         ; jump on the next step if the length is 0
        repeat_loop:
            MOVSB            ; [EDI] = [ESI] = [s2], ESI++
            INC ESI          ; ESI++
            loop repeat_loop
        do_s1:
            MOV AL, 01h
            TEST AL, l2 
            jz et3
            MOV ECX, l2/2+1  ; the number of elements from even positions if it has an odd number of elements
            jmp et4
            et3:
            MOV ECX, l2/2    ; the number of elements from even positions if it has an even number of elements
            et4:
            MOV ESI, s1+1 
            jecxz final      ; jump on the next step if the length is 0
            repeat_loop2:
                MOVSB        ; [EDI] = [ESI] = [s1+1], ESI++
                INC ESI      ; ESI++
                loop repeat_loop2
        final:
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
