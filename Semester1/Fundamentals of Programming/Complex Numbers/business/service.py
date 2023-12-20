import math

from domain.complex_number import getImaginaryPart, getRealPart, createComplexNumber


def getImaginaryParts(numbers_list, start, end):
    """
    returns the imaginary part of each complex number from the specified range
    :param numbers_list: list
    :param start: int
    :param end: int
    :return: res: list - the list with the corresponding imaginary parts from the specified range if it is valid
    :raises: ValueError - if start < 0 or end > len(numbers_list) or start > end throw ValueError exception with
                            the error message "invalid range!\n"
    """
    if start < 0 or end > len(numbers_list) or start > end:
        raise ValueError("invalid range!\n")

    imaginary_list = []
    for i in range(start, end + 1):
        imaginary_list.append(getImaginaryPart(numbers_list[i]))

    return imaginary_list


def modulus(complex_number):
    """
    returns the modulus of the complex number complex_number
    :param complex_number: complex_number
    :return: res: float - the modulus of the complex_number
    """
    a = getRealPart(complex_number)
    b = getImaginaryPart(complex_number)
    modul = math.sqrt(a ** 2 + b ** 2)

    return modul


def getModulusBelowTen(numbers_list):
    """
    returns all the complex numbers that have the modulus less than ten
    :param numbers_list: list
    :return: res: list - the list with complex numbers that have the modulus less than ten
    """
    result_list = []
    for i in numbers_list:
        if modulus(i) < 10:
            result_list.append(i)

    return result_list


def getModulusEqualToTen(numbers_list):
    """
    returns all the complex numbers that have the modulus equal to ten
    :param numbers_list: list
    :return: res: list - the list with complex numbers that have the modulus equal to ten
    """
    result_list = []
    for i in numbers_list:
        if modulus(i) == 10:
            result_list.append(i)

    return result_list


def primeNumber(n):
    """
    checks if the number n is a prime number
    :param n: int
    :return: res: bool - True if the number is prime or False otherwise
    """
    prime = True
    for i in range(2, int(n / 2)):
        if n % i == 0:
            prime = False

    return prime


def filterRealPartPrime(numbers_list):
    """
    deletes all the complex numbers where the real part is a prime number from the numbers_list list
    :param numbers_list: list
    """
    filtered_list = [x for x in numbers_list if not primeNumber(getRealPart(x))]
    numbers_list[:] = filtered_list


def numbersSum(numbers_list, start, end):
    """
    compute the sum of the complex numbers from the specified range
    :param numbers_list: list
    :param start: int
    :param end: int
    :return: res: complex_number - returns the sum of the complex numbers from the specified range if it is valid
    :raises: ValueError - if start < 0 or end > len(numbers_list) or start > end throw ValueError exception with
                            the error message "invalid range!\n"
    """
    if start < 0 or end > len(numbers_list) or start > end:
        raise ValueError("invalid range!\n")

    resulted_real_part = 0
    resulted_imaginary_part = 0
    for i in range(start, end + 1):
        resulted_real_part += getRealPart(numbers_list[i])
        resulted_imaginary_part += getImaginaryPart(numbers_list[i])
    resulted_sum = createComplexNumber(resulted_real_part, resulted_imaginary_part)

    return resulted_sum


def numbersProduct(numbers_list, start, end):
    """
    compute the product of the complex numbers from the specified range
    :param numbers_list: list
    :param start: int
    :param end: int
    :return: res: complex_number - returns the product of the complex numbers from the specified range if it is valid
    :raises: ValueError - if start < 0 or end > len(numbers_list) or start > end throw ValueError exception with
                            the error message "invalid range!\n"
    """
    if start < 0 or end > len(numbers_list) or start > end:
        raise ValueError("invalid range!\n")

    resulted_real_part = getRealPart(numbers_list[start])
    resulted_imaginary_part = getImaginaryPart(numbers_list[start])
    for i in range(start + 1, end + 1):
        first_number_real_part = resulted_real_part
        first_number_imaginary_part = resulted_imaginary_part
        resulted_real_part = (first_number_real_part * getRealPart(numbers_list[i])
                              - first_number_imaginary_part * getImaginaryPart(numbers_list[i]))
        resulted_imaginary_part = (first_number_real_part * getImaginaryPart(numbers_list[i])
                                   + first_number_imaginary_part * getRealPart(numbers_list[i]))

    resulted_product = createComplexNumber(resulted_real_part, resulted_imaginary_part)

    return resulted_product


def sortByImaginaryParts(numbers_list):
    """
    sort the numbers_list list in descending order by the imaginary part of the complex numbers
    :param numbers_list: list
    """
    numbers_list.sort(reverse=True, key=getImaginaryPart)


def filterModulus(numbers_list, operator, number):
    """
    delete all complex numbers from numbers_list list where the modulus is <, = or > then the given number,
    elimina din lista l numerele complexe la care modulul este <, =, > decat un numarul numar dat, depending on the
    chosen operator
    :param numbers_list: list
    :param operator: string
    :param number: float
    """
    if str(operator) == '<':
        filtered_list = [x for x in numbers_list if modulus(x) < number]
        numbers_list[:] = filtered_list
    elif str(operator) == '=':
        filtered_list = [x for x in numbers_list if abs(modulus(x) - number) < 0.00001]
        numbers_list[:] = filtered_list
    elif str(operator) == '>':
        filtered_list = [x for x in numbers_list if modulus(x) > number]
        numbers_list[:] = filtered_list
