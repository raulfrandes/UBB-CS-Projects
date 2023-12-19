#include <stdio.h>

/*
 * Read an integer (positive number) n from keyboard. Then read n sentences containing at least n words (no validation needed).
 * Print the string containing the concatenation of the word i of the sentence i, for i=1,n (separated by a space).
 * Example: n=5
 * We read the following 5 sentences:
 *   We read the following 5 sentences.
 *   Today is monday and it is raining.
 *   My favorite book is the one I just showed you.
 *   It is pretty cold today.
 *   Tomorrow I am going shopping.

 * The string printed on the screen should be:
 *   We is book cold shopping.
 */

int get_resulted_sentence(int, int, char []);

int main(){
    int n, countResult = 0;
    char firstLine[1], resultedSentence[100];
    
    printf("Enter the number of sentences: \n");
    scanf("%d", &n);
    gets(firstLine);
    
    printf("Enter the sentences: \n");
    countResult = get_resulted_sentence(n, countResult, resultedSentence);
    
    for(int i = 0; i < countResult; i++)
        printf("%c", resultedSentence[i]);
    
    return 0;
}