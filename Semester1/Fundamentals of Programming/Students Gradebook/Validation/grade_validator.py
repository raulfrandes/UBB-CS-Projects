from Errors.valid_error import ValidError


class GradeValidator:

    def __init__(self):
        pass

    def validate(self, grade):
        """
        validate an object of class Grade
        :param grade: Grade
        :raises: ValidError - if the id of the grade is <= 0, add the string "invalid id!\n" to the error message
                            - if the value of the grade is < 1 and > 10, add the string "invalid value!\n" to
                             the error message
                            - if the error message is not null, throw ValidError exception with the error message
        """
        errors = ""
        if grade.get_id_grade() <= 0:
            errors += "invalid id!\n"
        if grade.get_value() < 1 or grade.get_value() > 10:
            errors += "invalid value!\n"
        if len(errors):
            raise ValidError(errors)
