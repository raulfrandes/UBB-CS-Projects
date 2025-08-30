import copy


class LL1Parser:
    def __init__(self, grammar_file, input_sequence):
        self.grammar = {}
        self.terminals = set()
        self.non_terminals = set()
        self.start_symbol = None
        self.first_sets = {}
        self.follow_sets = {}
        self.parsing_table = {}
        self.input_sequence = input_sequence
        self.stack = []

        self.load_grammar(grammar_file)

    def load_grammar(self, grammar_file):
        with open(grammar_file, 'r') as file:
            i = 1
            for line in file:
                if '->' not in line:
                    continue
                left, right = map(str.strip, line.split(' -> '))

                if right == 'Îµ':
                    right_symbols = ['ε']
                else:
                    right_symbols = [s for s in right.split()]

                if self.start_symbol is None:
                    self.start_symbol = left

                self.non_terminals.add(left)
                self.grammar.setdefault(left, []).append((i, right_symbols))
                for symbol in right_symbols:
                    if not symbol.isupper():
                        self.terminals.add(symbol)
                i += 1

        for non_terminal in self.non_terminals:
            self.first_sets[non_terminal] = set()
            self.follow_sets[non_terminal] = set()

    def add_of_one(self, *args):
        l1 = args[0]
        for l2 in args[1:]:
            if 'ε' in l1:
                l1 = l1.union(l2)
                l1.discard('ε')
            
            if 'ε' in l2 and 'ε' in l1:
                l1.add('ε') 

        return l1

    def compute_first(self):
        for terminal in self.terminals:
            self.first_sets[terminal] = {terminal}
        
        for non_terminal in self.non_terminals:
            for production_pair in self.grammar[non_terminal]:
                production = production_pair[1]
                if production[0] in self.terminals:
                    self.first_sets[non_terminal].add(production[0])
        
        f = copy.deepcopy(self.first_sets)

        while True:
            current_f = copy.deepcopy(f)
            for non_terminal in self.non_terminals:
                for production_pair in self.grammar[non_terminal]:
                    production = production_pair[1]
                    args = [f[symbol] for symbol in production]
                    current_f[non_terminal].update(self.add_of_one(*args))

            if f == current_f:
                break
            f = current_f.copy()

        self.first_sets = f
        # print(self.first_sets)

    def compute_follow(self):
        self.follow_sets[self.start_symbol].add('$')

        f = copy.deepcopy(self.follow_sets)

        while True:
            current_f = copy.deepcopy(f)
            for non_terminal in self.non_terminals:
                for production_pair in self.grammar[non_terminal]:
                    production = production_pair[1]
                    for i in range(len(production)):
                        if production[i] in self.non_terminals:
                            if i < len(production) - 1:
                                current_first = self.first_sets[production[i + 1]]
                                current_f[production[i]].update(current_first.difference({'ε'}))
                                if 'ε' in current_first:
                                    current_f[production[i]].update(f[non_terminal])
                            else:
                                current_f[production[i]].update(f[non_terminal])

            if f == current_f:
                break
            f = current_f.copy()

        self.follow_sets = current_f    
        # print(self.follow_sets) 

    def build_parsing_table(self):
        rows = self.non_terminals.union(self.terminals.difference({'ε'}), {'$'})
        columns = self.terminals.difference({'ε'}).union({'$'})
        for row in rows:
            for column in columns:
                if row in self.non_terminals:
                    for production_pair in self.grammar[row]:
                        production = production_pair[1]
                        if self.parsing_table.get((row, column)) is not None and column in self.first_sets[production[0]]:
                            print(f"Error: Multiple rules for ({row}, {column})")
                            print('The grammar is not LL(1)')
                            return False
                        if column in self.first_sets[production[0]] or ('ε' in self.first_sets[production[0]] and 'ε' not in production and column in self.follow_sets[production[0]]):
                            self.parsing_table[(row, column)] = (production, production_pair[0])
                            continue
                        if 'ε' in self.first_sets[production[0]] and column in self.follow_sets[row]:
                            self.parsing_table[(row, column)] = (production, production_pair[0])
                            continue
                elif row == column and row != '$':
                    self.parsing_table[(row, column)] = ('pop', None)
                elif row == '$' and column == '$':
                    self.parsing_table[(row, column)] = ('acc', None)
                else:
                    self.parsing_table[(row, column)] = ('err', None)

        # for key, value in self.parsing_table.items():
        #     print(key, value)
        return True

    def parse(self):
        input_stack = ['$'] + [s for s in self.input_sequence[::-1]]
        self.stack = ['$', self.start_symbol]
        output = []

        while True:
            print(f"({input_stack[::-1]}, {self.stack[::-1]}, {output})")
            top_input = input_stack.pop()
            top_stack = self.stack.pop()

            # push
            if top_stack in self.non_terminals:
                production, i = self.parsing_table[(top_stack, top_input)]
                if production != 'pop' or production != 'acc' or production != 'err':
                    input_stack.append(top_input)
                    if production != ['ε']:
                        self.stack.extend(production[::-1])
                    output.append(i)
                else:
                    print(f"Error: No rule for ({top_stack}, {top_input})")
            # pop
            elif top_stack == top_input and top_stack != '$':
                continue
            # accept
            elif top_stack == top_input == '$':
                print("Sequence accepted")
                print("The sequence is generated by the following productions: ", end='')
                for i in output:
                    print(i, end=' ')
                break
            # error
            else:
                print("Error: Sequence not accepted")
                break

    def run(self):
        self.compute_first()
        self.compute_follow()
        if not self.build_parsing_table():
            return
        self.parse()