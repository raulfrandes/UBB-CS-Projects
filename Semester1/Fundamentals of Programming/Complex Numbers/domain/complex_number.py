def createComplexNumber(real_part, imaginary_part):
    """
    creates a complex number with the real part real_part and the imaginary part imaginary_part
    :param real_part: int
    :param imaginary_part: int
    :return: a complex number
    """
    return {"real": real_part, "imaginary": imaginary_part}


def getRealPart(complex_number):
    """
    returns the real part of a complex number
    :param complex_number: complex number
    :return: int - the real part of the given complex number
    """
    return complex_number["real"]


def getImaginaryPart(complex_number):
    """
    returns the imaginary part of a complex number
    :param complex_number: complex number
    :return: int - the imaginary part of the given complex number
    """
    return complex_number["imaginary"]
