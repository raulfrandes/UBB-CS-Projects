import re


TOKENS = {
    'void': r'\bvoid\b',
    'main': r'\bmain\b',
    'delimiter': r'\(|\)|\{|\}|;|,',
    'struct': r'\bstruct\b',
    'int': r'\bint\b',
    'float': r'\bfloat\b',
    'assignment': r'=',
    'cin': r'\bcin\b',
    'input_op': r'>>',
    'cout': r'\bcout\b',
    'output_op': r'<<',
    'endl': r'\bendl\b',
    'if': r'\bif\b',
    'else': r'\belse\b',
    'for': r'\bfor\b',
    'arithmetic_op': r'\+|-|\*|/|%',
    'relational_op': r'<|<=|>|>=|==|!=',
    'ID': r'[a-zA-Z][a-zA-Z0-9_]*',
    'CONST': r'(-?([1-9][0-9]*)|0)|(-?[1-9][0-9]*(.[0-9]+)?)|("[a-zA-Z]+")'
}


def detect_token(token):
    for token_type, pattern in TOKENS.items():
        if re.fullmatch(pattern, token):
            return token_type
    return 'UNKNOWN'


def parser(program_text):
    token_list_with_type = []
    token_list = []

    lines = program_text.splitlines()
    for line in lines:
        line = line.strip()
        if not line:
            continue
        
        parts = re.split(r'(\s+|;|,|\(|\)|\{|\}|>>|<<|==|!=|<=|>=|<|>|=|\+|\-|\*|/|%)', line)
        parts = [part for part in parts if part and not part.isspace()]

        for part in parts:
            token_type = detect_token(part)
            token_list.append(part)
            token_list_with_type.append((part, token_type))
    
    return token_list, token_list_with_type


def display_tokens(token_list):
    for token, token_type in token_list:
        print(f"Token: {token},\t\tType: {token_type}")


def save_tokens_to_file(token_list, filename):
    with open(filename, 'w') as file:
        for token in token_list:
            file.write(f'{token}\n')


def read_program_from_file(filename):
    with open(filename, 'r') as file:
        return file.read()


input_filename = 'ex4/program_input.txt'
output_filename = 'ex4/tokens_output.txt'

program_text = read_program_from_file(input_filename)

tokens, tokens_with_type = parser(program_text)

display_tokens(tokens_with_type)

save_tokens_to_file(tokens, output_filename)
