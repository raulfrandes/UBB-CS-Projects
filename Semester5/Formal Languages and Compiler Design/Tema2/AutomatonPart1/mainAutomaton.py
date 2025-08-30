from FiniteAutomaton import FiniteAutomaton


def read_automaton_from_file(filename):
    automaton = FiniteAutomaton()
    with open(filename, "r") as file:
        automaton.states = set(file.readline().strip().split())
        automaton.alphabet = set(file.readline().strip().split())
        automaton.initial_state = file.readline().strip()
        automaton.final_states = set(file.readline().strip().split())

        for line in file:
            line = line.strip()
            if line.startswith("f("):
                start = line[2]
                symbol = line[4]
                end = line[-1]
                automaton.add_transition(start, symbol, end)

    return automaton


def read_automaton_from_keyboard():
    automaton = FiniteAutomaton()
    automaton.states = set(input("States: ").strip().split())
    automaton.alphabet = set(input("Alphabet: ").strip().split())
    automaton.initial_state = input("Initial state: ").strip()
    automaton.final_states = set(input("Final states: ").strip().split())

    print("Transitions in format f(state, symbol)=state. Enter empty line to stop.")
    while True:
        line = input().strip()
        if not line:
            break
        start = line[2]
        symbol = line[4]
        end = line[-1]
        automaton.add_transition(start, symbol, end)

    return automaton


def display_menu(automaton):
    while True:
        print("\nMenu:")
        print("1. Display states")
        print("2. Display alphabet")
        print("3. Display transitions")
        print("4. Display final states")

        if automaton.is_deterministic:
            print("5. Verify sequence")
            print("6. Longest accepted prefix")
        
        print("7. Exit")

        choice = input("Choose an option: ").strip()

        if choice == "1":
            print()
            automaton.display_states()
        elif choice == "2":
            print()
            automaton.display_alphabet()
        elif choice == "3":
            print()
            automaton.display_transitions()
        elif choice == "4":
            print()
            automaton.display_final_states()
        elif choice == "5" and automaton.is_deterministic:
            print()
            sequence = input("Sequence to verify: ").strip()
            automaton.verify_sequence(sequence)
        elif choice == "6" and automaton.is_deterministic:
            print()
            sequence = input("Sequence to find longest accepted prefix: ").strip()
            automaton.longest_accepted_prefix(sequence)
        elif choice == "7":
            print()
            print("Exiting...")
            break
        else:
            print()
            print("Invalid option. Try again.")


def main():
    choice = input("Read automaton from file (f) or from keyboard (k)? ").strip()

    if choice == "f":
        automaton = read_automaton_from_file("activ2.txt")
    elif choice == "k":
        automaton = read_automaton_from_keyboard()
    else:
        print("Invalid choice")
        return
    
    display_menu(automaton)


if __name__ == "__main__":
    main()