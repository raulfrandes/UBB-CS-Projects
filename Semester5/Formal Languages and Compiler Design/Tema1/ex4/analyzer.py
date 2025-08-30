import re

from HashTable import HashTable

def read_atoms_table():
    atoms = {}
    with open('ex4/atoms.txt', 'r') as file:
        table = file.read()
        lines = table.splitlines()
        i = 0
        for line in lines:
            atoms[line] = i
            i += 1
    return atoms


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


def lexical_analyzer(program_text, atoms_table, symbol_table):
    fip = []

    lines = program_text.splitlines()
    for line_number, line in enumerate(lines, start=1):
        line = line.strip()
        if not line:
            continue

        parts = re.split(r'(\s+|;|,|\(|\)|\{|\}|>>|<<|==|!=|<=|>=|<|>|=|\+|\-|\*|/|%)', line)
        parts = [part for part in parts if part and not part.isspace()]

        for part in parts:
            token_type = detect_token(part)
            if token_type == 'UNKNOWN':
                print(f"Error: Token '{part} on line {line_number} is not recognized!")
                continue

            if token_type == 'ID' or token_type == 'CONST':
                symbol_table.put(part)
                fip.append((atoms_table[token_type], symbol_table._hash(part) + 1))
            else:
                fip.append((atoms_table[part], -1))

    return fip, symbol_table


def display_fip(fip):
    print("\nFIP:")
    for entry in fip:
        print(f"{entry[0]} | {entry[1]}")


def display_ts(symbol_table):
    symbol_table.display()


def save_to_file(fip, symbol_table):
    with open('ex4/fip_output.txt', 'w') as fip_file:
        for entry in fip:
            fip_file.write(f"{entry[0]} | {entry[1]}\n")
    
    with open('ex4/ts_output.txt', 'w') as ts_file:
        for i in range(symbol_table.size):
            if symbol_table.table[i] is not None:
                ts_file.write(f"{symbol_table.table[i]}\n")
            else:
                ts_file.write("\n")


def read_program_from_file(filename):
    with open(filename, 'r') as file:
        return file.read()
    

atoms_table = read_atoms_table()
symbol_table = HashTable(10)

program_text = read_program_from_file('ex4/program_input.txt')

fip, symbol_table = lexical_analyzer(program_text, atoms_table, symbol_table)

#display_fip(fip)
#display_ts(symbol_table)

save_to_file(fip, symbol_table)

