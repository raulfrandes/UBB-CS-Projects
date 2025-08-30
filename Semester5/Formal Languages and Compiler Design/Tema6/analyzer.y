%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "attrib.h"
#include "codeASM.h"

extern int line_number;
extern int yylex();
extern FILE *yyin;
extern char *yytext;

void yyerror(const char *s) {
    fprintf(stderr, "Syntax Error on line #%d: %s\n", line_number, s);
    fprintf(stderr, "At token: %s\n", yytext);
    exit(1);
};

char dataSegment[250];
char codeSegment[250];
char tempBuffer[250];

int tempNr = 1;
void newTempName(char* s) {
    sprintf(s, "temp%d", tempNr);
    sprintf(tempBuffer, VAR_DECLARATION_ASM_FORMAT, s);
    strcat(dataSegment, tempBuffer);
    tempNr++;
};

const char* format_var(const char* varn) {
    char* formatted_var = malloc(12);
    if (isdigit(varn[0]) || (varn[0] == '-' && isdigit(varn[1]))) {
        return varn;
    } else {
        sprintf(formatted_var, "[%s]", varn);
        return formatted_var;
    }
};
%}

%union {
    char varname[10];
    attributes attrib;
    char strCode[250];
}

%token VOID
%token MAIN 
%token INT
%token CIN
%token COUT
%token ENDL
%token PLUS
%token MINUS
%token MUL
%token ASSIGN
%token IN_OP
%token OUT_OP
%token LPAREN
%token RPAREN
%token LBRACE 
%token RBRACE
%token SEMICOLON
%token COMMA

%token <varname> ID
%token <varname> CONST
%type <attrib> expression
%type <attrib> term
%type <attrib> factor
%type <strCode> var_declaration
%type <strCode> input_chain
%type <strCode> output_chain
%type <strCode> output_chain_tail
%type <strCode> assignment_instr
%type <strCode> expression_operator

%%

program:
    VOID MAIN LPAREN RPAREN LBRACE var_declarations instructions RBRACE
    ;

var_declarations:
    var_declaration var_declarations
    {
      strcat(dataSegment, $1);
    }
    |
    ;

var_declaration:
    INT ID SEMICOLON
    {
      sprintf($$, VAR_DECLARATION_ASM_FORMAT, $2);
    }
    | INT ID ASSIGN CONST SEMICOLON
    {
      sprintf(tempBuffer, VAR_DECLARATION_INIT_ASM_FORMAT, $2, $4);
      strcpy($$, tempBuffer);
    }
    ;

instructions:
    instruction instructions
    |
    ;

instruction:
    in_instr SEMICOLON
    | out_instr SEMICOLON
    | assignment_instr SEMICOLON
    {
      strcat(codeSegment, $1);
    }
    | SEMICOLON
    ;

in_instr:
    CIN input_chain
    {
      strcat(codeSegment, $2);
    }
    ;

input_chain:
    IN_OP ID input_chain
    {
      sprintf($$, INPUT_ASM_FORMAT, $2);
      strcat($$, $3);
    }
    | IN_OP ID
    {
      sprintf($$, INPUT_ASM_FORMAT, $2);
    }
    ;

out_instr:
    COUT output_chain
    {
      strcat(codeSegment, $2);
    }
    ;

output_chain:
    OUT_OP output_chain_tail
    {
      strcpy($$, $2);
    }
    ;

output_chain_tail:
    expression output_chain
    {
      sprintf($$, OUTPUT_VAR_ASM_FORMAT, format_var($1.varn));
      strcat($$, $2);
    }
    | ENDL output_chain
    {
      strcpy($$, OUTPUT_ENDL_ASM_FORMAT);
      strcat($$, $2);
    }
    | expression
    {
      sprintf($$, OUTPUT_VAR_ASM_FORMAT, format_var($1.varn));
    }
    | ENDL
    {
      strcpy($$, OUTPUT_ENDL_ASM_FORMAT);
    }
    ;

assignment_instr:
    ID ASSIGN expression
    {
      strcpy($$, $3.cod);
      sprintf(tempBuffer, ASSIGN_ASM_FORMAT, format_var($3.varn), format_var($1));
      strcat($$, tempBuffer);
    }
    ;

expression:
    expression expression_operator term
    {
      newTempName($$.varn);
      sprintf($$.cod, "%s%s", $1.cod, $3.cod);
      sprintf(tempBuffer, OPERATION_ASM_FORMAT, format_var($1.varn), $2, format_var($3.varn), format_var($$.varn));
      strcat($$.cod, tempBuffer);
    }
    | term
    {
      strcpy($$.varn, $1.varn);
      strcpy($$.cod, $1.cod);
    }
    ;

term:
    term MUL factor
    {
      newTempName($$.varn);
      sprintf($$.cod, "%s%s", $1.cod, $3.cod);
      sprintf(tempBuffer, MUL_ASM_FORMAT, format_var($1.varn), format_var($3.varn), format_var($$.varn));
      strcat($$.cod, tempBuffer);
    }
    | factor
    {
      strcpy($$.varn, $1.varn);
      strcpy($$.cod, $1.cod);
    }
    ;

factor:
    ID
    {
      strcpy($$.varn, $1);
      strcpy($$.cod, "");
    }
    | CONST
    {
      strcpy($$.varn, $1);
      strcpy($$.cod, "");
    }
    | LPAREN expression RPAREN
    {
      strcpy($$.varn, $2.varn);
      strcpy($$.cod, $2.cod);
    }
    ;

expression_operator:
    PLUS
    {
        strcpy($$, "add");
    }
    | MINUS
    {
        strcpy($$, "sub");
    }
    ;

%%

int main(int argc, char **argv) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <input_file>\n", argv[0]);
        return EXIT_FAILURE;
    }

    FILE *input_file = fopen(argv[1], "r");
    if (!input_file) {
        perror("Failed to open input file");
        return EXIT_FAILURE;
    }

    yyin = input_file;
    int result = yyparse();

    fclose(input_file);

    if (result == 0) {
        printf("Parsing completed.\n");

        FILE *output_file = fopen("code.asm", "w");
        if (!output_file) {
            perror("Failed to open output file");
            return EXIT_FAILURE;
        }

        fprintf(output_file, ASM_PROGRAM, dataSegment, codeSegment);

        fclose(output_file);
    } else {
        printf("Parsing failed.\n");
    }

    return result;
}