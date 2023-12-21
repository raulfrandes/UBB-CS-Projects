def addComplexNumberToList(numbers_list, complex_number, undo_list):
    """
    adds the complex number complex_number to the end of the list numbers_list
    :param numbers_list: list
    :param complex_number: complex_number
    :param undo_list: list
    """
    numbers_list.append(complex_number)

    undo_list.append({"operation": "add", "param": -1})


def insertComplexNumberInList(numbers_list, complex_number, position, undo_list):
    """
    inserts the complex number complex_number into the list numbers_list at the specified position
    :param numbers_list: list
    :param complex_number: complex_number
    :param position: int
    :param undo_list: list
    :raises: ValueError - if position < 0 or position > len(numbers_list) throw ValueError exception with
                            the error message "invalid position!\n"
    """
    if position < 0 or position > len(numbers_list):
        raise ValueError("invalid position!\n")

    numbers_list.append(0)
    for i in range(len(numbers_list) - 1, position, -1):
        numbers_list[i] = numbers_list[i - 1]

    numbers_list[position] = complex_number

    undo_list.append({"operation": "insert", "param": position})


def deleteElement(numbers_list, position, undo_list):
    """
    deletes the element at the specified position from the list numbers_list
    :param numbers_list: list
    :param position: int
    :param undo_list: list
    :raises: ValueError -  if position < 0 or position > len(numbers_list) throw ValueError exception with
                            the error message "invalid position!\n"
    """
    if position < 0 or position > len(numbers_list):
        raise ValueError("invalid position!\n")

    deleted_element = numbers_list[position]
    del numbers_list[position]

    undo_list.append({"operation": "deleteElement", "param": [deleted_element, position]})


def deleteRangeOfElements(numbers_list, start, end, undo_list):
    """
    deletes the elements from the list numbers_list between the specified start and end positions
    :param numbers_list: list
    :param start: int
    :param end: int
    :param undo_list: list
    :raises: ValueError - if start < 0 or end > len(numbers_list) or start > end throw ValueError exception with
                            the error message "invalid range!\n"
    """
    if start < 0 or end > len(numbers_list) or start > end:
        raise ValueError("invalid range!\n")

    deleted_elements = []
    for i in range(end, start - 1, -1):
        deleted_elements.append(numbers_list[i])
        del numbers_list[i]

    deleted_elements.reverse()
    undo_list.append({"operation": "deleteRange", "param": [deleted_elements, start, end]})


def replaceElements(numbers_list, initial_value, new_value, undo_list):
    """
    replaces all elements with the initial_value value from the list numbers_list with the new_value value
    :param numbers_list: list
    :param initial_value: complex_number
    :param new_value: complex_number
    :param undo_list: list
    :raises: ValueError - if the element initial_value is not in the list numbers_list throw ValueError exception with
                            the error message "invalid value!\n"
    """
    if initial_value not in numbers_list:
        raise ValueError("invalid value!\n")

    for i in range(0, len(numbers_list)):
        if numbers_list[i] == initial_value:
            numbers_list[i] = new_value

    undo_list.append({"operation": "replace", "param": [initial_value, new_value]})
