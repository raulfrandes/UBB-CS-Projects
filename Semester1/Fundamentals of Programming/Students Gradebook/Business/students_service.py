from Domain.student import Student
import random


class StudentsService:
    """
        Responsible for implementing operations related to students
        Coordinates the operations necessary to perform the actions triggered by the user
    """

    def __init__(self, student_validator, students_repo):
        """
        Initializes the StudentsService class
        :param student_validator: StudentValidator
        :param students_repo: StudentsRepo
        """
        self.__student_validator = student_validator
        self.__students_repo = students_repo

    def add_student(self, id_student, name):
        """
        adds a student
        :param id_student: int
        :param name: string
        :raises: ValidError - if the student has invalid information
                 RepoError - if the student already exists in the repository
        """
        student = Student(id_student, name)
        self.__student_validator.validate(student)
        self.__students_repo.add_student(student)

    def search_student_by_id(self, id_student):
        """
        searches a student by id
        :param id_student: int
        :return: student - the student with the given id
        :raises: RepoError - if the student does not exist in the repository
        """
        return self.__students_repo.search_student_by_id(id_student)

    def update_student(self, id_student, new_name):
        """
        updates a student
        :param id_student: int
        :param new_name: string
        :raises: ValidError - if the student has invalid information
                 RepoError - if the student does not exist in the repository
        """
        student = Student(id_student, new_name)
        self.__student_validator.validate(student)
        self.__students_repo.update_student(student)

    def get_all(self):
        """
        returns a list of all students
        :return: list - list of students
        """
        return self.__students_repo.get_all()

    def get_number_of_students(self):
        """
        returns the number of students in the repository
        :return: int - the number of students
        """
        return self.__students_repo.__len__()

    def get_random_name(self):
        """
        generates a random name for a student
        :return: string - a random name
        """
        first_names = ['John', 'Jane', 'Alice', 'Bob', 'Emily', 'Michael', 'Emma', 'William']
        last_names = ['Smith', 'Johnson', 'Williams', 'Jones', 'Brown', 'Davis', 'Miller', 'Wilson']
        return random.choice(first_names) + ' ' + random.choice(last_names)

    def get_random_id(self):
        """
        generates a random id for a student
        :return: int - a random id
        """
        return random.randint(0, 345)

    def get_random_student(self):
        """
        generates a random student
        :return: Student - the generated student
        """
        student = Student(self.get_random_id(), self.get_random_name())
        self.add_student(student.get_id_student(), student.get_name())
        return student
