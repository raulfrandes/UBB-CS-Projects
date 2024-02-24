from Errors.valid_error import ValidError


class SubjectValidator:

    def __init__(self):
        pass

    def validate(self, subject):
        """
        validate an object of class Subject
        :param subject: Subject
        :raises: ValidError - if the id of the subject is <= 0, add the string "invalid id!\n" to the error message
                            - if the name of the subject is null, add the string "invalid name!\n" to the error message
                            - if the teacher of the subject is null, add the string "invalid teacher!\n" to
                              the error message
                            - if the error message is not null, throw ValidError exception with the error message
        """
        errors = ""
        if subject.get_id_subject() <= 0:
            errors += "invalid id!\n"
        if subject.get_name() == "":
            errors += "invalid name!\n"
        if subject.get_teacher() == "":
            errors += "invalid teacher!\n"
        if len(errors):
            raise ValidError(errors)
