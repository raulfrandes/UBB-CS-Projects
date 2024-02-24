class Student:

    def __init__(self, id_student, name):
        """
        create a new object of type Student
        :param id_student: int
        :param name: string
        """
        self.__id_student = id_student
        self.__name = name

    def get_id_student(self):
        """
        return the id of the student
        :return: int - the id of the student
        """
        return self.__id_student

    def get_name(self):
        """
        return the name of the student
        :return: string - the name of the student
        """
        return self.__name

    def set_name(self, new_name):
        """
        update the name of the student
        :param new_name: string
        """
        self.__name = new_name

    def __eq__(self, other):
        """
        verify if two student objects are equal
        :param other: Student
        :return: bool - return True if both objects are equal or False otherwise
        """
        return self.__id_student == other.__id_student

    def __str__(self):
        """
        return string representation of the student
        :return: string - student information
        """
        return f"ID: {self.__id_student}, Name: {self.__name}"
