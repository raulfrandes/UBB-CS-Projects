; Read an integer (positive number) n from keyboard. Then read n sentences containing at least n words (no validation needed).
; Print the string containing the concatenation of the word i of the sentence i, for i=1,n (separated by a space).
; Example: n=5
;   We read the following 5 sentences:
;   We read the following 5 sentences.
;   Today is monday and it is raining.
;   My favorite book is the one I just showed you.
;   It is pretty cold today.
;   Tomorrow I am going shopping.

;   The string printed on the screen should be:
;   We is book cold shopping.


bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, scanf, printf, gets ; tell nasm that exit, scanf, printf, gets exists even if we won't be defining it
import exit msvcrt.dll           ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import scanf msvcrt.dll          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import printf msvcrt.dll
import gets msvcrt.dll
                                 
extern get_resulted_sentence
     
segment data use32 class=data
    format_print_n db "Enter the number of sentences: ", 10, 0
    format_print_sentences db "Enter the sentences: ", 10, 0
    n dd 0
    format_n db "%d", 0;
    len equ 100
    first_line db 0
    format_char db "%c", 0
    count_res dd 0
    result times len db 0
    
; our code starts here
segment code use32 class=code public
    start:
        ; printf("Enter the number of sentences: \n")
        push dword format_print_n
        call [printf]
        add esp, 4
        
        ; scanf("%d", &n)
        push dword n
        push dword format_n
        call [scanf]
        add esp, 4*2 
        
        ; read the line printed by the first printf for gets to read the first entered sentence
        ; gets(first_line)
        push dword first_line
        call [gets]
        add esp, 4
        
        ; printf("Enter the sentences: \n")
        push dword format_print_sentences
        call [printf]
        add esp, 4
 
        push dword result
        push dword [count_res]
        push dword [n]
        call get_resulted_sentence
        pop ecx
        pop esi 
        
        print:                     ; loop for printing the resulted sentence
            push ecx               ; save ECX in stack
            
            ; printf("%c", [ESI])
            push dword [esi]
            push dword format_char
            call [printf]
            add esp, 4*2
            
            inc esi                ; ESI++
            
            pop ecx                ; take ECX from stack
        loop print
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
