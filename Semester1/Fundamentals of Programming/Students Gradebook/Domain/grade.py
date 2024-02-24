class Grade:

    def __init__(self, id_grade, student, subject, value):
        """
        create a new object of type Grade
        :param id_grade: int
        :param student: student
        :param subject: subject
        :param value: float
        """
        self.__id_grade = id_grade
        self.__student = student
        self.__subject = subject
        self.__value = value

    def get_id_grade(self):
        """
        return the id of the grade
        :return: int - the id of the grade
        """
        return self.__id_grade

    def get_student(self):
        """
        return the student of the grade
        :return: student - the student of the grade
        """
        return self.__student

    def get_subject(self):
        """
        return the subject of the grade
        :return: subject - the subject of the grade
        """
        return self.__subject

    def get_value(self):
        """
        return the value of the grade
        :return: float - the value of the grade
        """
        return self.__value

    def set_student(self, new_student):
        """
        update the student of the grade
        :param new_student: student
        """
        self.__student = new_student

    def set_subject(self, new_subject):
        """
        update the subject of the grade
        :param new_subject: subject
        """
        self.__subject = new_subject

    def set_value(self, new_value):
        """
        update the value of the grade
        :param new_value: float
        """
        self.__value = new_value

    def __eq__(self, other):
        """
        verify if two grade objects are equal
        :param other: Grade
        :return: bool - True if the two grades are equal or False otherwise
        """
        return self.__id_grade == other.__id_grade

    def __str__(self):
        '''
        return string representation of the grade
        :return: string - grade information
        '''
        return f"ID: {self.__id_grade}, Student: {self.__student.get_id_student()} - {self.__student.get_name()}, Subject: " \
               f"{self.__subject.get_id_subject()} - {self.__subject.get_name()}, Grade: {self.__value}"
