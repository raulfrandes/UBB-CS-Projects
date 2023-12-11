; Given an array S of doublewords, build the array of bytes D formed from bytes of doublewords sorted as unsigned numbers in descending order.
; Example:
;   s DD 12345607h, 1A2B3C15h
;   d DB 56h, 3Ch, 34h, 2Bh, 1Ah, 15h, 12h, 07h

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    
    s dd 12345607h, 1A2B3C15h 
    ls equ $-s 
    d times ls db 0

; our code starts here
segment code use32 class=code
    start:
        MOV ECX, ls                 ; the length of s
        MOV ESI, s                  ; position ESI on the first element of s 
        MOV EDI, d                  ; position EDI on d 
        CLD                         ; DF = 0
        jecxz sort                  ; jump to the sorting step if length is 0
        repeat_loop:
            MOVSB                   ; [EDI] = [ESI], EDI++, ESI++
            loop repeat_loop
        sort:                       ; sort in descending order
            MOV BL, 1               ; BL = 1
            etwhile:
                MOV BL, 0           ; BL = 0
                MOV ESI, d          ; position ESI on the first element of d
                MOV ECX, ls-1       ; the length of d-1
                etfor:
                    MOV AL, [ESI]   ; AL = [ESI] = [d]
                    MOV AH, [ESI+1] ; AH = [ESI+1] = [d+1]
                    INC ESI         ; ESI++
                    CMP AH, AL      ; compare AH and AL
                    jb continue
                    MOV [ESI-1], AH ; [ESI-1] = AH = [d+1]
                    MOV [ESI], AL   ; [ESI] = AL = [d], interchange
                    MOV BL, 1       ; BL = 1
                    continue: 
                        loop etfor 
                        CMP BL, 1   ; compare BL with 1
                        je etwhile 
                    
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
