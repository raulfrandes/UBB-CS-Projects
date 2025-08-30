from LL1Parser import LL1Parser


if __name__ == "__main__":
    grammar_file = 'grammar.txt'
    input_sequence = input('Enter the input sequence: ')

    parser = LL1Parser(grammar_file, input_sequence)
    parser.run()