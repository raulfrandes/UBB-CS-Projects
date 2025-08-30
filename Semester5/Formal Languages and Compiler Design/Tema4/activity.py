def parse_grammar(file_path):
    with open(file_path, 'r') as file:
        lines = file.readlines()

    non_terminals = set()
    terminals = set()
    production_rules = []
    start_symbol = None

    for line in lines:
        line = line.strip()
        if not line:
            continue

        line_elem = line.split(' -> ')
        if start_symbol is None:
            start_symbol = line_elem[0]

        if line_elem[1] != 'epsilon':
            for el in line_elem[1]:
                if not el.isupper():
                    terminals.add(el)

        non_terminals.add(line_elem[0])

        production_rules.append(line)
    return {
        "non_terminals": sorted(non_terminals),
        "terminals": sorted(terminals),
        "production_rules": production_rules,
        "start_symbol": start_symbol,
    }


def main():
    grammar_file = 'activity_input.txt'
    grammar = parse_grammar(grammar_file)

    print("Non-terminals:", ", ".join(grammar['non_terminals']))
    print("Terminals:", ", ".join(grammar['terminals']))
    print("Production rules:")
    for rule in grammar['production_rules']:
        print(" ", rule)
    print("Start symbol:", grammar['start_symbol'])


if __name__ == "__main__":
    main()
