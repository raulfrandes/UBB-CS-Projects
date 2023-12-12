; A file name and a text (defined in the data segment) are given. The text contains lowercase letters, uppercase letters, digits and special
; characters. 
; Replace all the special characters from the given text with the character 'X'. Create a file with the given name and write the generated text to file.

bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, fopen, fprintf, fclose ; tell nasm that exit, fopen, fprintf, fclose exist even if we won't be defining them
import exit msvcrt.dll              ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import fopen msvcrt.dll             ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions
import fprintf msvcrt.dll
import fclose msvcrt.dll

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    filename db 'text.txt', 0
    modf db 'w', 0
    text db '@n@ gave me, 10 apples.', 0
    len equ $-text
    dest times len db 0
    descriptor dd -1

; our code starts here
segment code use32 class=code
    start:
        mov ecx, len - 1                 ; initialize ECX with the number of elements
        mov esi, text                    ; initialize ESI with the first element of the string
        mov edi, dest                    ; initialize EDI with the destination string
        CLD                              ; DF = 0
        jecxz final                      ; if the string is "" exit the program
        repeat_loop:                     ; the replacement
            lodsb                        ; AL = text[i]
            cmp al, 32                   ; if the current character is " ", we place it in the destination, else we jump to next1
                jne next1
                    stosb
                    jmp next
            next1:                       ; if the current character is a number, we place it in the destination, else we jump to next2
                cmp al, 48 
                jb next2
                    cmp al, 57
                    ja next2
                        stosb
                        jmp next
                next2:                   ; if the current character is an uppercase letter, we place it in the destination, else we jump to next3
                    cmp al, 65
                    jb next3
                        cmp al, 90
                        ja next3
                            stosb
                            jmp next
                    next3:               ; if the current character is a lowercase letter, we place it in the destination, else we jump to next4
                        cmp al, 97
                        jb next4
                            cmp al, 122
                            ja next4
                                stosb
                                jmp next
                        next4:           ; we replace the special characters with 'X'            
                            mov al, 'X'
                            stosb
            next:
                loop repeat_loop
        
        ;fopen(filename, modf)
        push dword modf
        push dword filename
        call [fopen]
        add esp, 4*2                     ; we reset the stack
        cmp eax,0
        je final                         ; if the file cannot be opened we jump to final
        mov [descriptor], eax            ; we retain the file descriptor
        
        ;fprintf(descriptor, dest)
        push dword dest
        push dword[descriptor]
        call [fprintf]
        add esp, 4*2                     ; we reset the stack
        
        ;fclose(descriptor)
        push dword[descriptor]
        call [fclose]
        add esp, 4                       ; we reset the stack
        
        final:
                    
                
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
