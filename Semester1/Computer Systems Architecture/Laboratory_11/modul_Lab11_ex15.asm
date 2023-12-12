bits 32

extern gets
import gets msvcrt.dll

global get_resulted_sentence

segment data use32 public data
    len equ 100
    sentence times len db 0
    count dd 0
    count_res dd 0

segment code use32 public code

    ;|       ret      | ESP+0
    ;|        n       | ESP+4
    ;|    count_res   | ESP+8
    ;|     result     | ESP+12
    get_resulted_sentence:
        mov eax, [esp+8]
        mov [count_res], eax
        mov esi, [esp+12]                 ; in ESI we will have the words that will be in the final sentence
        mov ecx, [esp+4]                  ; ECX = n, the number of sentences
        read:
            push ecx                      ; save ECX in stack
            
            ;gets(sentence)
            push dword sentence
            call [gets]
            add esp, 4
            
            add dword [count], 1          ; we are counting which sentence we are at
            mov edi, sentence             ; EDI will have the final sentence
            mov ebx, [count]              ; EBX = count
            sub ebx, 1                    ; EBX = count - 1
            mov ecx, ebx                  ; ECX = count - 1, how many words we have to ignore
            mov al, ' '                   ; AL = ' ' 
            jecxz over_loop 
            words:                        ; EDI will increment until it will be on the word needed
                cld                       ; DF = 0
                space:
                    scasb                 ; cmp AL, EDI
                jnz space                 ; jump until we meet the character ' ' 
            loop words
            over_loop:
      
            set_result:                   ; we set the word needed in result
                mov dl, [edi]             ; DL = [EDI]
                cmp dl, ' '               ; if the ascii code of the current character is below space we can jump to final
                jb final
                cmp dl, 'z'               ; if the ascii code of the current character is above 'z' we can jump to final
                ja final
                mov [esi], dl             ; [ESI] = DL = [EDI]
                inc esi                   ; ESI++
                add dword [count_res], 1  ; count_res++, compute the length of result
                
                scasb                     ; cmp AL, EDI 
            jnz set_result                ; jump until we meet the character ' '
            
            pop ecx                       ; extract ECX from stack
        loop read
        
        final:
        pop ecx                           ; extract ECX from stack
        mov eax, [count_res]
        mov [esp+8], eax
        ret 4