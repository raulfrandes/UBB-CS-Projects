class StudentDTO:

    def __init__(self, name, grades):
        """
        creates a DTO object with the name and grades of a Student object
        :param name: string
        :param grades: list
        """
        self.__name = name
        self.__grades = grades

    def get_name(self):
        """
        returns the name of the student
        :return: string - the name of the student
        """
        return self.__name

    def get_grades(self):
        """
        returns the grades of the student
        :return: list - list of grades
        """
        return self.__grades

    def __lt__(self, other):
        """
        compares two students by comparing their names
        :param other: StudentDTO
        :return: boolean - True if self's name is before other's name in alphabetical order or False otherwise
        """
        return self.__name < other.__name

    def __eq__(self, other):
        """
        verifies if two studentDTO objects are equal
        :param other: StudentDTO
        :return: boolean - True if both objects are equal or False otherwise
        """
        return self.__name == other.__name

    def __str__(self):
        """
        return string representation of the StudentDTO
        :return: string - studentDTO information
        """
        grades_str = ', '.join(map(str, self.__grades))
        return f"Name: {self.__name}, Grades: {grades_str}"
