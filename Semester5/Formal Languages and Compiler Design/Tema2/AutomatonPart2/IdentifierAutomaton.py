from AutomatonPart1.FiniteAutomaton import FiniteAutomaton


class IdentifierAutomaton(FiniteAutomaton):
    def __init__(self):
        super().__init__()
        self.states = set("pq")
        self.alphabet = set("abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVXYZ_0123456789")
        self.initial_state = "p"
        self.final_states = set("q")
        for letter in "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVXYZ_":
            self.add_transition("p", letter, "q")
            self.add_transition("q", letter, "q")
        for digit in "0123456789":
            self.add_transition("q", digit, "q")