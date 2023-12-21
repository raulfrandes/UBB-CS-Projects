def undoAddComplexNumberToList(numbers_list):
    """
    undo the operation of adding complex numbers to the end of the list
    :param numbers_list: list
    """
    del numbers_list[-1]


def undoInsertComplexNumberInList(numbers_list, position):
    """
    undo the operation of inserting a complex number on a specific position
    :param numbers_list: list
    :param position: int
    """
    del numbers_list[position]


def undoDeleteElement(numbers_list, complex_number, position):
    """
    undo the operation of deleting a complex number from a specific position
    :param numbers_list: list
    :param complex_number: int
    :param position: int
    """
    numbers_list.append(0)
    for i in range(len(numbers_list) - 1, position, -1):
        numbers_list[i] = numbers_list[i - 1]

    numbers_list[position] = complex_number


def undoDeleteRangeOfElements(numbers_list, to_add, start, end):
    """
    undo the operation of deleting complex numbers from a range
    :param numbers_list: list
    :param to_add: list
    :param start: int
    :param end: int
    """
    for _ in range(end - start + 1):
        numbers_list.append(0)
        for i in range(len(numbers_list) - 1, start - 1, -1):
            numbers_list[i] = numbers_list[i - 1]

    j = 0
    for i in range(start, end + 1):
        numbers_list[i] = to_add[j]
        j += 1


def undoReplaceElements(numbers_list, initial_value, new_value):
    """
    undo the operation of replacing all elements with the given value with a new one
    :param numbers_list: list
    :param initial_value: complex number
    :param new_value: complex number
    """
    for i in range(0, len(numbers_list)):
        if numbers_list[i] == initial_value:
            numbers_list[i] = new_value
