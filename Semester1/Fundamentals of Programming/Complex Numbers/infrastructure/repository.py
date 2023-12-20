def addComplexNumberToList(numbers_list, complex_number):
    """
    adds the complex number complex_number to the end of the list numbers_list
    :param numbers_list: list
    :param complex_number: complex_number
    """
    numbers_list.append(complex_number)


def insertComplexNumberInList(numbers_list, complex_number, position):
    """
    inserts the complex number complex_number into the list numbers_list at the specified position
    :param numbers_list: list
    :param complex_number: complex_number
    :param position: int
    :raises: ValueError - if position < 0 or position > len(numbers_list) throw ValueError exception with
                            the error message "invalid position!\n"
    """
    if position < 0 or position > len(numbers_list):
        raise ValueError("invalid position!\n")

    numbers_list.append(0)
    for i in range(len(numbers_list) - 1, position, -1):
        numbers_list[i] = numbers_list[i - 1]

    numbers_list[position] = complex_number


def deleteElement(numbers_list, position):
    """
    deletes the element at the specified position from the list numbers_list
    :param numbers_list: list
    :param position: int
    :raises: ValueError -  if position < 0 or position > len(numbers_list) throw ValueError exception with
                            the error message "invalid position!\n"
    """
    if position < 0 or position > len(numbers_list):
        raise ValueError("invalid position!\n")

    del numbers_list[position]


def deleteRangeOfElements(numbers_list, start, end):
    """
    deletes the elements from the list numbers_list between the specified start and end positions
    :param numbers_list: list
    :param start: int
    :param end: int
    :raises: ValueError - if start < 0 or end > len(numbers_list) or start > end throw ValueError exception with
                            the error message "invalid range!\n"
    """
    if start < 0 or end > len(numbers_list) or start > end:
        raise ValueError("invalid range!\n")

    for i in range(start, end + 1):
        del numbers_list[i]


def replaceElements(numbers_list, initial_value, new_value):
    """
    replaces all elements with the initial_value value from the list numbers_list with the new_value value
    :param numbers_list: list
    :param initial_value: complex_number
    :param new_value: complex_number
    :raises: ValueError - if the element initial_value is not in the list numbers_list throw ValueError exception with
                            the error message "invalid value!\n"
    """
    if initial_value not in numbers_list:
        raise ValueError("invalid value!\n")

    for i in range(0, len(numbers_list)):
        if numbers_list[i] == initial_value:
            deleteElement(numbers_list, i)
            insertComplexNumberInList(numbers_list, new_value, i)


def copyList(numbers_list):
    """
    creates a copy of the numbers_list list
    :param numbers_list: list
    :return: res: list - the copy of the list numbers_list
    """
    return [i for i in numbers_list]
