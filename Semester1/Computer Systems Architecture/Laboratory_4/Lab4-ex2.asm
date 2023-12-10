; Given the words A and B, compute the doubleword C as follows:
;   the bits 0-2 of C have the value 0
;   the bits 3-5 of C have the value 1
;   the bits 6-9 of C are the same as the bits 11-14 of A
;   the bits 10-15 of C are the same as the bits 1-6 of B
;   the bits 16-31 of C have the value 1

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
        
    a dw 1001001010110101b
    b dw 1010110110101010b
    c dd 0

; our code starts here
segment code use32 class=code
    start:
        MOV EBX, 0                                 ; EBX = 0
        AND EBX, 11111111111111111111111111111000b ; EBX = 0000 0000 0000 0000 0000 0000 0000 0000b - change bits 0-2 to 0
        
        OR EBX, 00000000000000000000000000111000b  ; EBX = 0000 0000 0000 0000 0000 0000 0011 1000b - change bits 3-5 to 1
        
        MOV EAX, 0                                 ; EAX = 0
        MOV AX, [a]                                ; AX = 1001 0010 1011 0101b and EAX = 0000 0000 0000 0000 1001 0010 1011 0101b
        AND EAX, 00000000000000000111100000000000b ; EAX = 0000 0000 0000 0000 0001 0000 0000 0000b - isolate bits 11-14
        MOV CL, 5                                  ; CL = 5
        ROR EAX, CL                                ; EAX = 0000 0000 0000 0000 0000 0000 1000 0000b
        OR EBX, EAX                                ; EBX = 0000 0000 0000 0000 0000 0000 1011 1000b - initialize bits 6-9 with bits 11-14 of A
        
        MOV EAX, 0                                 ; EAX = 0
        MOV AX, [b]                                ; AX = 1010 1101 1010 1010b and EAX = 0000 0000 0000 0000 1010 1101 1010 1010b 
        AND EAX, 00000000000000000000000001111110b ; EAX = 0000 0000 0000 0000 0000 0000 0010 1010b - isolate bits 1-6
        MOV CL, 9                                  ; CL = 4
        ROL EAX, CL                                ; EAX = 0000 0000 0000 0000 0101 0100 0000 0000b
        OR EBX, EAX                                ; EBX = 0000 0000 0000 0000 0101 0100 1011 1000b - initialize bits 10-15 with bits 1-6 of B
        
        OR EBX, 11111111111111110000000000000000b  ; EBX = 1111 1111 1111 1111 0101 0100 1011 1000b - change bits 16-31 to 1
        MOV [c], EBX                               ; C = 1111 1111 1111 1111 0101 0100 1011 1000b = 4294923448
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
