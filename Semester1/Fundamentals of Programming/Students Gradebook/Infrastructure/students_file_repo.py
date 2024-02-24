from Domain.student import Student
from Infrastructure.students_repo import StudentsRepo


class StudentsFileRepo(StudentsRepo):

    def __init__(self, filename):
        StudentsRepo.__init__(self)
        self.__filename = filename

    def __read_all_from_file(self):
        """
        reads all students from a file
        """
        with open(self.__filename, "r") as f:
            lines = f.readlines()
            self._students.clear()
            for line in lines:
                line = line.strip()
                if line != "":
                    parts = line.split(", ")
                    id_student = int(parts[0])
                    name = parts[1]
                    student = Student(id_student, name)
                    self._students[id_student] = student

    def __write_all_to_file(self):
        """
        writes all students to a file
        """
        with open(self.__filename, "w") as f:
            for student in self._students.values():
                f.write(str(student.get_id_student()) + ", " + student.get_name() + "\n")

    def add_student(self, student):
        """
        adds a student from the file to the dictionary
        :param student: Student
        :raises: RepoError - if the student already exists throw a RepoError exception with the error message
                             "student already exists!\n"
        """
        self.__read_all_from_file()
        StudentsRepo.add_student(self, student)
        self.__write_all_to_file()

    def delete_student_by_id(self, id_student):
        """
        deletes a student from the file
        :param id_student: int
        :raises: RepoError - if the student with the given id does not exist throw a RepoError exception with
                             the error message "student does not exist!\n"
        """
        self.__read_all_from_file()
        StudentsRepo.delete_student_by_id(self, id_student)
        self.__write_all_to_file()

    def search_student_by_id(self, id_student):
        """
        search for a student from the file
        :param id_student: int
        :return: Student - the student with the given id
        :raises: RepoError - if the student with the given id does not exist throw a RepoError exception with
                             the error message "student does not exist!\n"
        """
        self.__read_all_from_file()
        return StudentsRepo.search_student_by_id(self, id_student)

    def update_student(self, updated_student):
        """
        update a student from the file
        :param updated_student: Student
        :raises: RepoError - if the student with the given id does not exist throw a RepoError exception with
                             the error message "student does not exist!\n"
        """
        self.__read_all_from_file()
        StudentsRepo.update_student(self, updated_student)
        self.__write_all_to_file()

    def get_all(self):
        """
        returns a list with all the students from the file
        :return: list - list of students
        """
        self.__read_all_from_file()
        return StudentsRepo.get_all(self)

    def delete_all(self):
        """
        deletes all students from the file
        """
        StudentsRepo.delete_all(self)
        self.__write_all_to_file()

    def __len__(self):
        """
        the number of students in the file
        :return: int - the number of students in the file
        """
        self.__read_all_from_file()
        return StudentsRepo.__len__(self)
