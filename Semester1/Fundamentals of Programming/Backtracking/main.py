def consistent(cand, S):
    if sum(cand) <= S:
        return True
    return False


def solution(cand, S):
    return sum(cand) == S


def solutionFound(cand):
    print(cand)


def backtracking(coins, S, candidate):
    candidate.append(0)
    for e in coins:
        candidate[-1] = e
        if consistent(candidate, S):
            if solution(candidate, S):
                solutionFound(candidate)
            backtracking(coins, S, candidate)
    candidate.pop()


if __name__ == "__main__":
    S = int(input("Amount: "))
    user_input = input("The coins: ")
    coins = list(map(int, user_input.split()))
    print("Backtracking:")
    backtracking(coins, S, [])
