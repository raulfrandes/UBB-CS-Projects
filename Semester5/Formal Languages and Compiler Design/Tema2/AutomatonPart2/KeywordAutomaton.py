from AutomatonPart1.FiniteAutomaton import FiniteAutomaton


class KeywordAutomaton(FiniteAutomaton):
    def __init__(self, keywords):
        super().__init__()
        self.keywords = keywords
        self.construct_automaton()


    def construct_automaton(self):
        self.initial_state = "p0"
        self.states.add(self.initial_state)
        for keyword in self.keywords:
            current_state = self.initial_state
            for char in keyword:
                self.alphabet.add(char)
                next_state = current_state[0] + str(int(current_state[1]) + 1)
                self.states.add(next_state)
                self.add_transition(current_state, char, next_state)
                current_state = next_state
            self.final_states.add(current_state)