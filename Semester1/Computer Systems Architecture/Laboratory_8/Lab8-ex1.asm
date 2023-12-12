; Read two numbers a and b (in base 10) from the keyboard and calculate a+b. Display the result in base 16

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf ; tell nasm that exit, printf, scanf exist even if we won't be defining them
import exit msvcrt.dll     ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import printf msvcrt.dll   ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import scanf msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a dd 0
    b dd 0
    formats db '%d %d', 0
    formatp db '%d + %d = %x'

; our code starts here
segment code use32 class=code
    start:
    
        ;scanf("%d %d", &a, &b)
        push dword b
        push dword a
        push formats 
        call [scanf] 
        
        add esp, 4*3  ; reset the stack
        mov eax, [a]  ; EAX = a
        add eax, [b]  ; EAX = EAX + b
    
        ;printf("%d + %d = %x", a, b, EAX)
        push eax
        push dword[b]
        push dword[a]
        push formatp
        call [printf]
        add esp, 4*3  ; reset the stack
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
