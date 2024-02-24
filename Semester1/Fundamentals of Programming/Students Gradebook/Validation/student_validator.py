from Errors.valid_error import ValidError


class StudentValidator:

    def __init__(self):
        pass

    def validate(self, student):
        """
        validate an object of class Student
        :param student: Student
        :raises: ValidError - if the id of the student is <= 0, add the string "invalid id!\n" to the error message
                            - if the name of the student is null, add the string "invalid name!\n" to the error message
                            - if the error message is not null, throw ValidError exception with the error message
        """
        errors = ""
        if student.get_id_student() <= 0:
            errors += "invalid id!\n"
        if student.get_name() == "":
            errors += "invalid name!\n"
        if len(errors):
            raise ValidError(errors)
