%{
#include <stdio.h>
#include <stdlib.h>

int yylex();
void yyerror(const char *s);

extern int line_number;
extern FILE *yyin;
extern char *yytext;
extern int yydebug;
%}

%token ID CONST
%token VOID MAIN INT FLOAT STRUCT CIN COUT ENDL IF ELSE FOR
%token PLUS MINUS MUL DIV MOD ASSIGN IN_OP OUT_OP
%token LT LE GT GE EQ NE
%token LPAREN RPAREN LBRACE RBRACE SEMICOLON COMMA

%left PLUS MINUS
%left MUL DIV MOD
%nonassoc LT LE GT GE EQ NE

%start program

%%

program:
    struct_declarations VOID MAIN LPAREN RPAREN LBRACE var_declarations instructions RBRACE
    ;

struct_declarations:
    STRUCT ID LBRACE var_declarations RBRACE
    |
    ;

var_declarations:
    var_declaration var_declarations
    |
    ;

var_declaration:
    data_type ID SEMICOLON
    | data_type assignment_instr SEMICOLON
    ;

data_type:
    INT
    | FLOAT
    | STRUCT
    ;

instructions:
    instruction instructions
    |
    ;

instruction:
    in_instr SEMICOLON
    | out_instr SEMICOLON
    | assignment_instr SEMICOLON
    | control_instr
    | loop_instr
    | SEMICOLON
    ;

in_instr:
    CIN input_chain
    ;

input_chain:
    IN_OP ID input_chain
    |
    ;

out_instr:
    COUT output_chain
    ;

output_chain:
    OUT_OP expression output_chain
    | OUT_OP ENDL output_chain
    |
    ;

assignment_instr:
    ID ASSIGN expression
    ;

control_instr:
    IF LPAREN condition RPAREN LBRACE instructions RBRACE
    | IF LPAREN condition RPAREN LBRACE instructions RBRACE ELSE LBRACE instructions RBRACE
    ;

loop_instr:
    FOR LPAREN assignment_instr SEMICOLON condition SEMICOLON assignment_instr RPAREN LBRACE instructions RBRACE
    ;

condition:
    expression LT expression
    | expression LE expression
    | expression GT expression
    | expression GE expression
    | expression EQ expression
    | expression NE expression
    ;

expression:
    ID
    | CONST
    | expression PLUS expression
    | expression MINUS expression
    | expression MUL expression
    | expression DIV expression
    | expression MOD expression
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Syntax Error: %s on line %d\n", s, line_number);
}

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
    } else {
        printf("Parsing failed.\n");
    }

    return result;
}