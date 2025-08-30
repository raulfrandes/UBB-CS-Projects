class FiniteAutomaton:
    def __init__(self):
        self.states = set()
        self.alphabet = set()
        self.initial_state = None
        self.final_states = set()
        self.transitions = {}
        self.is_deterministic = True

    def add_transition(self, start, symbol, end):
        if start not in self.transitions:
            self.transitions[start] = {}

        if symbol in self.transitions[start]:
            self.is_deterministic = False

        self.transitions[start][symbol] = end

    def display_states(self):
        print("States:", self.states)

    def display_alphabet(self):
        print("Alphabet:", self.alphabet)

    def display_transitions(self):
        print("Transitions:")
        for start, symbols in self.transitions.items():
            for symbol, end in symbols.items():
                print(f"f({start}, {symbol}) = {end}")

    def display_final_states(self):
        print("Final states:", self.final_states)

    def verify_sequence(self, sequence):
        current_state = self.initial_state
        for symbol in sequence:
            if symbol in self.alphabet and current_state in self.transitions and symbol in self.transitions[current_state]:
                current_state = self.transitions[current_state][symbol]
            else:
                print(f"Sequence {sequence} is not accepted.")
                return False
            
        if current_state in self.final_states:
            print(f"Sequence {sequence} is accepted.")
            return True
        else:
            print(f"Sequence {sequence} is not accepted.")
            return False
        
    def longest_accepted_prefix(self, sequence):
        current_state = self.initial_state
        longest_prefix_end = -1

        for i, symbol in enumerate(sequence):
            if symbol in self.alphabet and current_state in self.transitions and symbol in self.transitions[current_state]:
                current_state = self.transitions[current_state][symbol]
                if current_state in self.final_states:
                    longest_prefix_end = i
            else:
                break

        if longest_prefix_end != -1:
            accepted_prefix = sequence[:longest_prefix_end + 1]
            return accepted_prefix
        else:
            if self.initial_state in self.final_states:
                return "epsilon"
            else:
                return ""