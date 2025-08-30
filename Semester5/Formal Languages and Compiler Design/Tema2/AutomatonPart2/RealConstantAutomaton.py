from AutomatonPart1.FiniteAutomaton import FiniteAutomaton


class RealConstantAutomaton(FiniteAutomaton):
    def __init__(self):
        super().__init__()
        self.states = set("pqrstuvx")
        self.alphabet = set("0123456789+-eE.")
        self.initial_state = "p"
        self.final_states = set("stx")
        self.add_transition("p", "-", "q")
        self.add_transition("p", "+", "q")
        for digit in "0123456789":
            self.add_transition("p", digit, "r")
            self.add_transition("q", digit, "r")
            self.add_transition("r", digit, "r")
            self.add_transition("s", digit, "t")
            self.add_transition("t", digit, "t")
            self.add_transition("u", digit, "x")
            self.add_transition("v", digit, "x")
            self.add_transition("x", digit, "x")
        self.add_transition("r", ".", "s")
        self.add_transition("t", "e", "u")
        self.add_transition("t", "E", "u")
        self.add_transition("u", "-", "v")
        self.add_transition("u", "+", "v")