from AutomatonPart1.FiniteAutomaton import FiniteAutomaton


class IntegerConstantAutomaton(FiniteAutomaton):
    def __init__(self):
        super().__init__()
        self.states = set("pqrstuv")
        self.alphabet = set("0123456789+-xXabcdefABCDEF")
        self.initial_state = "p"
        self.final_states = set("qstv")
        self.add_transition("p", "0", "q")
        self.add_transition("p", "+", "r")
        self.add_transition("p", "-", "r")
        for non_zero_digit in "123456789":
            self.add_transition("p", non_zero_digit, "s")
            self.add_transition("r", non_zero_digit, "s")
        for digit in "0123456789":
            self.add_transition("s", digit, "s")
            self.add_transition("u", digit, "v")
            self.add_transition("v", digit, "v")
        for octal_digit in "01234567":
            self.add_transition("q", octal_digit, "t")
            self.add_transition("t", octal_digit, "t")
        self.add_transition("q", "x", "u")
        self.add_transition("q", "X", "u")
        for hex_digit in "abcdefABCDEF":
            self.add_transition("u", hex_digit, "v")
            self.add_transition("v", hex_digit, "v")
