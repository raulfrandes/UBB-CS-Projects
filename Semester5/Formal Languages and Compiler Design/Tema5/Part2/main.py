import sys
sys.path.append('..')
from Part1.LL1Parser import LL1Parser


def load_atoms(file_path):
    with open(file_path, 'r') as file:
        atoms = [line.strip() for line in file.readlines()]
    return atoms

def process_fip(file_path, atoms):
    sequence = []
    with open(file_path, 'r') as file:
        for line in file:
            atom_code, index = map(int, line.strip().split('|'))
            atom_name = atoms[atom_code - 1]
            sequence.append(atom_name)
    return sequence

if __name__ == "__main__":
    atoms = load_atoms('atoms.txt')
    sequence = process_fip('fip.txt', atoms)

    grammar_file = 'grammar.txt'

    parser = LL1Parser(grammar_file, sequence)
    parser.run()