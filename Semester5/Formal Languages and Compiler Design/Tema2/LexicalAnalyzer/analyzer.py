import re

from AutomatonPart2.IdentifierAutomaton import IdentifierAutomaton
from AutomatonPart2.IntegerConstantAutomaton import IntegerConstantAutomaton
from AutomatonPart2.KeywordAutomaton import KeywordAutomaton
from AutomatonPart2.OperatorDelimiterAutomaton import OperatorDelimiterAutomaton
from AutomatonPart2.RealConstantAutomaton import RealConstantAutomaton
from LexicalAnalyzer.HashTable import HashTable


def read_atoms_table():
    atoms = {}
    with open('LexicalAnalyzer/atoms.txt', 'r') as file:
        table = file.read()
        lines = table.splitlines()
        i = 0
        for line in lines:
            atoms[line] = i
            i += 1
    return atoms


def lexical_analyzer(program_text, atoms_table, symbol_table):
    fip = []

    identifier_automaton = IdentifierAutomaton()
    integer_automaton = IntegerConstantAutomaton()
    real_automaton = RealConstantAutomaton()
    keyword_automaton = KeywordAutomaton(['void', 'main', 'struct', 'int', 'float', 'cin', 'cout', 'endl', 'if', 'else', 'for'])
    operator_delimiter_automaton = OperatorDelimiterAutomaton(['(', ')', '{', '}', ';', ',', '=', '>>', '<<', '+', '-', '*', '/', '%', '<', '<=', '>', '>=', '==', '!='])

    lines = program_text.splitlines()
    for line_number, line in enumerate(lines, start=1):
        line = line.strip()
        if not line:
            continue

        while line:
            keyword_prefix = keyword_automaton.longest_accepted_prefix(line)
            operator_delimiter_prefix = operator_delimiter_automaton.longest_accepted_prefix(line)
            real_prefix = real_automaton.longest_accepted_prefix(line)
            integer_prefix = integer_automaton.longest_accepted_prefix(line)
            identifier_prefix = identifier_automaton.longest_accepted_prefix(line)

            longest_prefix = max(keyword_prefix, operator_delimiter_prefix, real_prefix, integer_prefix, identifier_prefix, key=len)
            
            if longest_prefix and (longest_prefix == keyword_prefix or longest_prefix == operator_delimiter_prefix):
                token_type = longest_prefix
            elif longest_prefix and longest_prefix == identifier_prefix:
                token_type = "ID"
            elif longest_prefix and (longest_prefix == integer_prefix or longest_prefix == real_prefix):
                token_type = "CONST"
            else:
                print(f"Eroare: Tokenul '{line[0]}' pe linia {line_number} nu este recunoscut!")
                line = line[1:]
                continue

            if token_type in ["ID", "CONST"]:
                index = symbol_table.put(longest_prefix)
                fip.append((atoms_table[token_type], index))
            else:
                fip.append((atoms_table[token_type], -1))

            line = line[len(longest_prefix):].lstrip()

    return fip, symbol_table


def display_fip(fip):
    print("\nFIP:")
    for entry in fip:
        print(f"{entry[0]} | {entry[1]}")


def display_ts(symbol_table):
    symbol_table.display()


def save_to_file(fip, symbol_table):
    with open('LexicalAnalyzer/fip_output.txt', 'w') as fip_file:
        for entry in fip:
            fip_file.write(f"{entry[0] + 1} | {entry[1] + 1}\n")
    
    with open('LexicalAnalyzer/ts_output.txt', 'w') as ts_file:
        for i in range(symbol_table.size):
            if symbol_table.table[i] is not None:
                ts_file.write(f"{symbol_table.table[i]}\n")
            else:
                ts_file.write("\n")


def read_program_from_file(filename):
    with open(filename, 'r') as file:
        return file.read()
    

def run_analyzer():
    atoms_table = read_atoms_table()
    symbol_table = HashTable(10)

    program_text = read_program_from_file('LexicalAnalyzer/program_input.txt')

    fip, symbol_table = lexical_analyzer(program_text, atoms_table, symbol_table)

    #display_fip(fip)
    #display_ts(symbol_table)

    save_to_file(fip, symbol_table)