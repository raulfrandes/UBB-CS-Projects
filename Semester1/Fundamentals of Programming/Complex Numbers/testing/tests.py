from infrastructure.repository import *
from business.service import *


def testCreateComplexNumber():
    real_part = 5
    imaginary_part = 7
    complex_number = createComplexNumber(real_part, imaginary_part)
    assert (real_part == getRealPart(complex_number))
    assert (imaginary_part == getImaginaryPart(complex_number))


def testAddComplexNumberToList():
    test_list = []
    undo_list = []
    complex_number = createComplexNumber(5, 6)
    addComplexNumberToList(test_list, complex_number, undo_list)
    assert (len(test_list) == 1)
    assert (getRealPart(test_list[0]) == 5)
    assert (getImaginaryPart(test_list[0]) == 6)
    assert (len(undo_list) == 1)
    assert (undo_list[-1] == {"operation": "add", "param": -1})

    doUndo(test_list, undo_list)
    assert (test_list == [])


def testInsertComplexNumberInList():
    test_list = []
    undo_list = []
    complex_number1 = createComplexNumber(5, 6)
    complex_number2 = createComplexNumber(7, 3)
    addComplexNumberToList(test_list, complex_number1, undo_list)
    addComplexNumberToList(test_list, complex_number2, undo_list)
    complex_number3 = createComplexNumber(4, 2)
    position = 1
    insertComplexNumberInList(test_list, complex_number3, position, undo_list)
    assert (len(test_list) == 3)
    assert (getRealPart(test_list[position]) == 4)
    assert (getImaginaryPart(test_list[position]) == 2)
    assert (undo_list[-1] == {"operation": "insert", "param": position})

    doUndo(test_list, undo_list)
    assert (test_list == [complex_number1, complex_number2])

    wrong_position = 6
    try:
        insertComplexNumberInList(test_list, complex_number3, wrong_position, [])
        assert False
    except ValueError as ve:
        assert (str(ve) == "invalid position!\n")


def testDeleteElement():
    test_list = []
    undo_list = []
    complex_number1 = createComplexNumber(13, 6)
    complex_number2 = createComplexNumber(19, 5)
    complex_number3 = createComplexNumber(1, 2)
    addComplexNumberToList(test_list, complex_number1, undo_list)
    addComplexNumberToList(test_list, complex_number2, undo_list)
    addComplexNumberToList(test_list, complex_number3, undo_list)
    position = 1
    deleteElement(test_list, position, undo_list)
    assert (len(test_list) == 2)
    assert (getRealPart(test_list[position]) == 1)
    assert (getImaginaryPart(test_list[position]) == 2)
    assert (undo_list[-1] == {"operation": "deleteElement", "param": [complex_number2, position]})

    doUndo(test_list, undo_list)
    assert (test_list == [complex_number1, complex_number2, complex_number3])

    wrong_position = 7
    try:
        deleteElement(test_list, wrong_position, [])
        assert False
    except ValueError as ve:
        assert (str(ve) == "invalid position!\n")


def testDeleteRangeOfElements():
    test_list = []
    undo_list = []
    complex_number1 = createComplexNumber(1, 3)
    complex_number2 = createComplexNumber(3, 7)
    complex_number3 = createComplexNumber(5, 9)
    complex_number4 = createComplexNumber(7, 10)
    complex_number5 = createComplexNumber(9, 11)
    addComplexNumberToList(test_list, complex_number1, undo_list)
    addComplexNumberToList(test_list, complex_number2, undo_list)
    addComplexNumberToList(test_list, complex_number3, undo_list)
    addComplexNumberToList(test_list, complex_number4, undo_list)
    addComplexNumberToList(test_list, complex_number5, undo_list)
    start = 1
    end = 3
    deleteRangeOfElements(test_list, start, end, undo_list)
    assert (len(test_list) == 2)
    assert (getRealPart(test_list[start]) == 9)
    assert (getImaginaryPart(test_list[start]) == 11)
    assert (undo_list[-1] == {"operation": "deleteRange",
                              "param": [[complex_number2, complex_number3, complex_number4, ], start, end]})

    doUndo(test_list, undo_list)
    assert (test_list == [complex_number1, complex_number2, complex_number3, complex_number4, complex_number5])

    wrong_start = -3
    wrong_end = 9
    try:
        deleteRangeOfElements(test_list, wrong_start, wrong_end, [])
        assert False
    except ValueError as ve:
        assert (str(ve) == "invalid range!\n")


def testReplaceElements():
    test_list = []
    undo_list = []
    complex_number1 = createComplexNumber(1, 2)
    complex_number2 = createComplexNumber(3, 4)
    complex_number3 = createComplexNumber(5, 6)
    complex_number4 = createComplexNumber(3, 4)
    complex_number5 = createComplexNumber(3, 4)
    addComplexNumberToList(test_list, complex_number1, undo_list)
    addComplexNumberToList(test_list, complex_number2, undo_list)
    addComplexNumberToList(test_list, complex_number3, undo_list)
    addComplexNumberToList(test_list, complex_number4, undo_list)
    addComplexNumberToList(test_list, complex_number5, undo_list)
    initial_value = createComplexNumber(3, 4)
    new_value = createComplexNumber(9, 7)
    replaceElements(test_list, initial_value, new_value, undo_list)
    assert (len(test_list) == 5)
    assert (test_list.count(new_value) == 3)
    assert (undo_list[-1] == {"operation": "replace", "param": [initial_value, new_value]})

    doUndo(test_list, undo_list)
    assert (test_list == [complex_number1, complex_number2, complex_number3, complex_number4, complex_number5])

    wrong_initial_value = createComplexNumber(15, 15)
    try:
        replaceElements(test_list, wrong_initial_value, new_value, [])
        assert False
    except ValueError as ve:
        assert (str(ve) == "invalid value!\n")


def testGetImaginaryParts():
    test_list = []
    complex_number1 = createComplexNumber(1, 2)
    complex_number2 = createComplexNumber(3, 4)
    complex_number3 = createComplexNumber(5, 6)
    complex_number4 = createComplexNumber(7, 8)
    complex_number5 = createComplexNumber(9, 10)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    addComplexNumberToList(test_list, complex_number4, [])
    addComplexNumberToList(test_list, complex_number5, [])
    start = 1
    end = 3
    list_imaginary = getImaginaryParts(test_list, start, end)
    assert (len(list_imaginary) == 3)
    assert (list_imaginary[0] == getImaginaryPart(complex_number2))
    assert (list_imaginary[1] == getImaginaryPart(complex_number3))
    assert (list_imaginary[2] == getImaginaryPart(complex_number4))

    wrong_start = -3
    wrong_end = 11
    try:
        getImaginaryParts(test_list, wrong_start, wrong_end)
        assert False
    except ValueError as ve:
        assert (str(ve) == "invalid range!\n")


def testModulus():
    complex_number = createComplexNumber(2, 5)
    complex_number_modulus = modulus(complex_number)
    assert (abs(complex_number_modulus - math.sqrt(29)) < 0.00001)


def testGetModulusBelowTen():
    test_list = []
    complex_number1 = createComplexNumber(1, 2)
    complex_number2 = createComplexNumber(3, 4)
    complex_number3 = createComplexNumber(5, 6)
    complex_number4 = createComplexNumber(7, 8)
    complex_number5 = createComplexNumber(9, 10)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    addComplexNumberToList(test_list, complex_number4, [])
    addComplexNumberToList(test_list, complex_number5, [])
    modulus_list = getModulusBelowTen(test_list)
    assert (len(modulus_list) == 3)
    assert (modulus_list[0] == complex_number1)
    assert (modulus_list[1] == complex_number2)
    assert (modulus_list[2] == complex_number3)


def testGetModulusEqualToTen():
    test_list = []
    complex_number1 = createComplexNumber(1, 2)
    complex_number2 = createComplexNumber(6, 8)
    complex_number3 = createComplexNumber(5, 6)
    complex_number4 = createComplexNumber(7, 8)
    complex_number5 = createComplexNumber(8, -6)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    addComplexNumberToList(test_list, complex_number4, [])
    addComplexNumberToList(test_list, complex_number5, [])
    modulus_list = getModulusEqualToTen(test_list)
    assert (len(modulus_list) == 2)
    assert (modulus_list[0] == complex_number2)
    assert (modulus_list[1] == complex_number5)


def testPrimeNumber():
    n = 5
    assert (primeNumber(n))


def testFilterRealPartPrime():
    test_list = []
    complex_number1 = createComplexNumber(5, 2)
    complex_number2 = createComplexNumber(3, 4)
    complex_number3 = createComplexNumber(8, 6)
    complex_number4 = createComplexNumber(7, 8)
    complex_number5 = createComplexNumber(9, 10)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    addComplexNumberToList(test_list, complex_number4, [])
    addComplexNumberToList(test_list, complex_number5, [])
    filtered_list = filterRealPartPrime(test_list)
    assert (len(filtered_list) == 2)
    assert (getRealPart(filtered_list[0]) == 8)
    assert (getImaginaryPart(filtered_list[0]) == 6)
    assert (getRealPart(filtered_list[1]) == 9)
    assert (getImaginaryPart(filtered_list[1]) == 10)


def testNumbersSum():
    test_list = []
    complex_number1 = createComplexNumber(5, 2)
    complex_number2 = createComplexNumber(3, 4)
    complex_number3 = createComplexNumber(8, 6)
    complex_number4 = createComplexNumber(7, 8)
    complex_number5 = createComplexNumber(9, 10)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    addComplexNumberToList(test_list, complex_number4, [])
    addComplexNumberToList(test_list, complex_number5, [])
    start = 1
    end = 3
    result_real_part = 18
    result_imaginary_part = 18
    result = createComplexNumber(result_real_part, result_imaginary_part)
    assert (numbersSum(test_list, start, end) == result)

    wrong_start = -3
    wrong_end = 80
    try:
        numbersSum(test_list, wrong_start, wrong_end)
        assert False
    except ValueError as ve:
        assert (str(ve) == "invalid range!\n")


def testNumbersProduct():
    test_list = []
    complex_number1 = createComplexNumber(5, 2)
    complex_number2 = createComplexNumber(3, 4)
    complex_number3 = createComplexNumber(8, 6)
    complex_number4 = createComplexNumber(7, 8)
    complex_number5 = createComplexNumber(9, 10)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    addComplexNumberToList(test_list, complex_number4, [])
    addComplexNumberToList(test_list, complex_number5, [])
    start = 1
    end = 3
    result_real_part = -400
    result_imaginary_part = 350
    result = createComplexNumber(result_real_part, result_imaginary_part)
    assert (numbersProduct(test_list, start, end) == result)

    wrong_start = -5
    wrong_end = 45
    try:
        numbersProduct(test_list, wrong_start, wrong_end)
        assert False
    except ValueError as ve:
        assert (str(ve) == "invalid range!\n")


def testSortByImaginaryParts():
    test_list = []
    complex_number1 = createComplexNumber(5, 2)
    complex_number2 = createComplexNumber(3, 6)
    complex_number3 = createComplexNumber(8, 3)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    sorted_list = sortByImaginaryParts(test_list)
    assert (getRealPart(sorted_list[0]) == 3)
    assert (getImaginaryPart(sorted_list[0]) == 6)
    assert (getRealPart(sorted_list[1]) == 8)
    assert (getImaginaryPart(sorted_list[1]) == 3)
    assert (getRealPart(sorted_list[2]) == 5)
    assert (getImaginaryPart(sorted_list[2]) == 2)


def testFilterModulus():
    test_list = []
    complex_number1 = createComplexNumber(5, 2)
    complex_number2 = createComplexNumber(3, 4)
    complex_number3 = createComplexNumber(8, 6)
    complex_number4 = createComplexNumber(7, 8)
    complex_number5 = createComplexNumber(9, 10)
    addComplexNumberToList(test_list, complex_number1, [])
    addComplexNumberToList(test_list, complex_number2, [])
    addComplexNumberToList(test_list, complex_number3, [])
    addComplexNumberToList(test_list, complex_number4, [])
    addComplexNumberToList(test_list, complex_number5, [])
    number = 9
    operator = '<'
    filtered_list = filterModulus(test_list, operator, number)
    assert (len(filtered_list) == 2)
    assert (getRealPart(filtered_list[0]) == 5)
    assert (getImaginaryPart(filtered_list[0]) == 2)
    assert (getRealPart(filtered_list[1]) == 3)
    assert (getImaginaryPart(filtered_list[1]) == 4)
    number = 11
    operator = '>'
    filtered_list = filterModulus(test_list, operator, number)
    assert (len(filtered_list) == 1)
    assert (getRealPart(filtered_list[0]) == 9)
    assert (getImaginaryPart(filtered_list[0]) == 10)
    number = 10
    operator = '='
    filtered_list = filterModulus(test_list, operator, number)
    assert (len(filtered_list) == 1)
    assert (getRealPart(filtered_list[0]) == 8)
    assert (getImaginaryPart(filtered_list[0]) == 6)


def runAllTests():
    testCreateComplexNumber()
    testAddComplexNumberToList()
    testInsertComplexNumberInList()
    testDeleteElement()
    testDeleteRangeOfElements()
    testReplaceElements()
    testGetImaginaryParts()
    testModulus()
    testGetModulusBelowTen()
    testGetModulusEqualToTen()
    testPrimeNumber()
    testFilterRealPartPrime()
    testNumbersSum()
    testNumbersProduct()
    testSortByImaginaryParts()
    testFilterModulus()
    print("(Passed all tests with success!)")
