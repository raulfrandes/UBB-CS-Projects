from business.service import getImaginaryParts, getModulusBelowTen, getModulusEqualToTen, filterRealPartPrime, \
    numbersSum, numbersProduct, sortByImaginaryParts, filterModulus, doUndo
from domain.complex_number import getRealPart, getImaginaryPart, createComplexNumber
from infrastructure.repository import addComplexNumberToList, insertComplexNumberInList, deleteElement, \
    deleteRangeOfElements, replaceElements


def printMenu():
    print("Menu:")
    print(" Read list: ")
    print("1. Enter the complex numbers.")
    print(" Add number: ")
    print("2. Add a complex number at the end of the list.")
    print("3. Insert a complex number at a specific position.")
    print(" Modify the list elements: ")
    print("4. Delete the element from a specific position.")
    print("5. Delete the elements from a specific range.")
    print("6. Replace a specific elements with a new value.")
    print(" Search numbers: ")
    print("7. Print all imaginary parts of the complex numbers from a specific range.")
    print("8. Print all numbers that have the modulus greater less than 10.")
    print("9. Print all numbers that have the modulus equal to 10.")
    print(" Operations with the complex numbers: ")
    print("10. The sum of the numbers from a specific range.")
    print("11. The product of the numbers from a specific range.")
    print("12. Print the list sorted in descending order by the imaginary parts of the complex numbers.")
    print(" Filters: ")
    print("13. Delete the elements where the real part is a prime number.")
    print("14. Delete the elements where the modulus is <, = or > than a given number.")
    print(" Undo: ")
    print("15. Undo")
    print(" Exit: ")
    print("16. Exit.")


def convertList(numbers_list):
    return [complex(getRealPart(x), getImaginaryPart(x)) for x in numbers_list]


def inputNumbers(numbers_list):
    n = int(input("Enter the number of elements: "))

    for i in range(n):
        print("number ", i + 1)
        real_part = int(input("Enter the real part: "))
        imaginary_part = int(input("Enter the imaginary part: "))
        complex_number = createComplexNumber(real_part, imaginary_part)
        numbers_list.append(complex_number)

    print(convertList(numbers_list))


def addComplexNumberToListUi(numbers_list, undo_list):
    print("What complex number do you want to add?")
    real_part = int(input("Enter the real part: "))
    imaginary_part = int(input("Enter the imaginary part: "))

    complex_number = createComplexNumber(real_part, imaginary_part)
    addComplexNumberToList(numbers_list, complex_number, undo_list)

    print(convertList(numbers_list))


def insertComplexNumberInListUi(numbers_list, undo_list):
    print("What complex number do you want to insert?")
    real_part = int(input("Enter the real part: "))
    imaginary_part = int(input("Enter the imaginary part: "))
    position = int(input("On which position? "))

    complex_number = createComplexNumber(real_part, imaginary_part)
    insertComplexNumberInList(numbers_list, complex_number, position - 1, undo_list)

    print(convertList(numbers_list))


def deleteElementUi(numbers_list, undo_list):
    position = int(input("The element from which position do you want to delete? "))

    deleteElement(numbers_list, position - 1, undo_list)

    print(convertList(numbers_list))


def deleteRangeOfElementsUi(numbers_list, undo_list):
    print("From which range do you want to delete?")
    range_start = int(input("Enter the start of the range: "))
    range_end = int(input("Enter the end of the range: "))

    deleteRangeOfElements(numbers_list, range_start - 1, range_end - 1, undo_list)

    print(convertList(numbers_list))


def replaceElementsUi(numbers_list, undo_list):
    print("What element do you want to be replaced?")
    real_part = int(input("Enter the real part: "))
    imaginary_part = int(input("Enter the imaginary part: "))

    initial_value = createComplexNumber(real_part, imaginary_part)

    print("With what value do you want to be replaced with?")
    new_real_part = int(input("Enter the real part: "))
    new_imaginary_part = int(input("Enter the imaginary part: "))

    new_value = createComplexNumber(new_real_part, new_imaginary_part)
    replaceElements(numbers_list, initial_value, new_value, undo_list)

    print(convertList(numbers_list))


def getImaginaryPartsUi(numbers_list):
    print("From which range do you want to print?")
    range_start = int(input("Enter the start of the range: "))
    range_end = int(input("Enter the end of the range: "))

    print(getImaginaryParts(numbers_list, range_start - 1, range_end - 1))


def getModulusBelowTenUi(numbers_list):
    print(convertList(getModulusBelowTen(numbers_list)))


def getModulusEqualToTenUi(numbers_list):
    print(convertList(getModulusEqualToTen(numbers_list)))


def filterRealPartPrimeUi(numbers_list):
    print(convertList(filterRealPartPrime(numbers_list)))


def numbersSumUi(numbers_list):
    result = []

    print("From which range do you want to print?")
    range_start = int(input("Enter the start of the range: "))
    range_end = int(input("Enter the end of the range: "))

    result.append(numbersSum(numbers_list, range_start - 1, range_end - 1))

    print(convertList(result))


def numbersProductUi(numbers_list):
    result = []

    print("From which range do you want to print?")
    range_start = int(input("Enter the start of the range: "))
    range_end = int(input("Enter the end of the range: "))

    result.append(numbersProduct(numbers_list, range_start - 1, range_end - 1))

    print(convertList(result))


def sortByImaginaryPartsUi(numbers_list):
    print(convertList(sortByImaginaryParts(numbers_list)))


def filterModulusUi(numbers_list):
    operator = input("Choose an operator (<, = or >): ")
    number = float(input("Enter the number compare the modulus to: "))

    print(convertList(filterModulus(numbers_list, operator, number)))


def doUndoUi(numbers_list, undo_list):
    doUndo(numbers_list, undo_list)
    print(convertList(numbers_list))


def addComplexNumberToListUiCommand(numbers_list, params, undo_list):
    if len(params) != 2:
        print("invalid number of arguments!")
        return

    real_part = int(params[0])
    imaginary_part = int(params[1])

    complex_number = createComplexNumber(real_part, imaginary_part)
    addComplexNumberToList(numbers_list, complex_number, undo_list)


def deleteElementUiCommand(numbers_list, params, undo_list):
    if len(params) != 1:
        print("invalid number of arguments!")
        return

    position = int(params[0])

    deleteElement(numbers_list, position - 1, undo_list)


def printCommand(numbers_list, params):
    print(convertList(numbers_list))


def start():
    numbers_list = []
    undo_list = []
    printMenu()
    while True:
        input_option = input("Enter the type of input (int or command): ")
        if input_option == "int":
            while True:
                option = int(input("Your option is: "))
                if option == 1:
                    inputNumbers(numbers_list)
                elif option == 2:
                    addComplexNumberToListUi(numbers_list, undo_list)
                elif option == 3:
                    insertComplexNumberInListUi(numbers_list, undo_list)
                elif option == 4:
                    deleteElementUi(numbers_list, undo_list)
                elif option == 5:
                    deleteRangeOfElementsUi(numbers_list, undo_list)
                elif option == 6:
                    replaceElementsUi(numbers_list, undo_list)
                elif option == 7:
                    getImaginaryPartsUi(numbers_list)
                elif option == 8:
                    getModulusBelowTenUi(numbers_list)
                elif option == 9:
                    getModulusEqualToTenUi(numbers_list)
                elif option == 10:
                    numbersSumUi(numbers_list)
                elif option == 11:
                    numbersProductUi(numbers_list)
                elif option == 12:
                    sortByImaginaryPartsUi(numbers_list)
                elif option == 13:
                    filterRealPartPrimeUi(numbers_list)
                elif option == 14:
                    filterModulusUi(numbers_list)
                elif option == 15:
                    doUndoUi(numbers_list, undo_list)
                elif option == 16:
                    return
        elif input_option == "command":
            while True:
                commands = {
                    "add": addComplexNumberToListUiCommand,
                    "delete": deleteElementUiCommand,
                    "undo": doUndoUi
                }
                commands_array = input(">>>")
                if commands_array == "":
                    continue
                elif commands_array == "exit":
                    return
                elif commands_array == "input":
                    inputNumbers(numbers_list)
                elif commands_array == "undo":
                    doUndoUi(numbers_list, undo_list)
                else:
                    command_array = commands_array.split(';')
                    for i in command_array:
                        command_element = i.split()
                        command_name = str(command_element[0])
                        params = command_element[1:]
                        if command_name in commands:
                            try:
                                commands[command_name](numbers_list, params, undo_list)
                            except ValueError as ve:
                                print(ve)
                    print(convertList(numbers_list))
