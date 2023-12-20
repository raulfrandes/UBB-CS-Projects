def createComplexNumber(real_part, imaginary_part):
    """
    create a complex number with the real part real_part of type int and the imaginary part imaginary_part of type int
    :param real_part: int
    :param imaginary_part: int
    :return: res: a complex number
    """
    return {"real": real_part, "imaginary": imaginary_part}


def getRealPart(complex_number):
    """
    returns the real part of type integer of the complex number complex_number
    :param complex_number: complex_number
    :return: res: int - the real part of the complex number complex_number
    """
    return complex_number["real"]


def getImaginaryPart(complex_number):
    """
    returns the imaginary part of type integer of the complex number complex_number
    :param complex_number: complex_number
    :return: res: int - the imaginary part of the complex number complex_number
    """
    return complex_number["imaginary"]
