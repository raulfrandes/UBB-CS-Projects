class AverageDTO:

    def __init__(self, name, average):
        """
        creates a DTO object with the name and grades average of a Student object
        :param name: string
        :param average: float
        """
        self.__name = name
        self.__average = average

    def get_name(self):
        """
        returns the name of the student
        :return: string - the name of the student
        """
        return self.__name

    def get_average(self):
        """
        returns the grades average of the student
        :return: float - the grades average
        """
        return self.__average

    def __lt__(self, other):
        """
        compares two students by comparing their grades average
        :param other: AverageDTO
        :return: boolean - True if self's average is less than other's average or False otherwise
        """
        return self.__average < other.__average

    def __eq__(self, other):
        """
        verifies if two averageDTO objects are equal
        :param other: AverageDTO
        :return: boolean - True if both objects are equal or False otherwise
        """
        return self.__name == other.__name

    def __str__(self):
        """
        return string representation of the AverageDTO
        :return: string - averageDTO information
        """
        return f"Name: {self.__name}, Grades average: {self.__average}"
