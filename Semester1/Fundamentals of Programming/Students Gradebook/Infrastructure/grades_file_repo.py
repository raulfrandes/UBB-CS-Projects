from Domain.grade import Grade
from Domain.student import Student
from Domain.subject import Subject
from Infrastructure.grades_repo import GradesRepo


class GradesFileRepo(GradesRepo):

    def __init__(self, filename):
        GradesRepo.__init__(self)
        self.__filename = filename

    def __read_all_from_file(self):
        """
        reads all grades from a file
        """
        with open(self.__filename, "r") as f:
            lines = f.readlines()
            self._grades.clear()
            for line in lines:
                line = line.strip()
                if line != "":
                    parts = line.split(", ")
                    id_grade = int(parts[0])
                    id_student = int(parts[1])
                    id_subject = int(parts[2])
                    value = float(parts[3])
                    grade = Grade(id_grade, Student(id_student, None),
                                  Subject(id_subject, None, None), value)
                    self._grades[id_grade] = grade

    def __write_all_to_file(self):
        """
        writes all subjects to a file
        """
        with open(self.__filename, "w") as f:
            for grade in self._grades.values():
                f.write(str(grade.get_id_grade()) + ", " + str(grade.get_student().get_id_student()) + ", " +
                        str(grade.get_subject().get_id_subject()) + ", " + str(grade.get_value()) + "\n")

    def add_grade(self, grade):
        """
        adds a grade to the dictionary
        :param grade: Grade
        :raises: RepoError - if the grade already exists throw a RepoError exception with the error message
                             "grade already exists!\n"
        """
        self.__read_all_from_file()
        GradesRepo.add_grade(self, grade)
        self.__write_all_to_file()

    def delete_grade_by_id(self, id_grade):
        """
        deletes a grade from the dictionary
        :param id_grade: int
        :raises: RepoError - if the grade with the given id does not exist throw a RepoError exception with
                             the error message "grade does not exist!\n"
        """
        self.__read_all_from_file()
        GradesRepo.delete_grade_by_id(self, id_grade)
        self.__write_all_to_file()

    def search_grade_by_id(self, id_grade):
        """
        search for a grade from the dictionary
        :param id_grade: int
        :return: Grade - the grade with the given id
        :raises: RepoError - if the grade with the given id does not exist throw a RepoError exception with
                             the error message "grade does not exist!\n"
        """
        self.__read_all_from_file()
        return GradesRepo.search_grade_by_id(self, id_grade)

    def update_grade(self, updated_grade):
        """
        update a grade from the dictionary
        :param updated_grade: Grade
        :raises: RepoError - if the grade with the given id does not exist throw a RepoError exception with
                             the error message "grade does not exist!\n"
        """
        self.__read_all_from_file()
        GradesRepo.update_grade(self, updated_grade)
        self.__write_all_to_file()

    def get_all(self):
        """
        returns a list with all the grades from the dictionary
        :return: list - list of grades
        """
        self.__read_all_from_file()
        return GradesRepo.get_all(self)

    def delete_all(self):
        """
        deletes all grades from the dictionary
        """
        GradesRepo.delete_all(self)
        self.__write_all_to_file()

    def __len__(self):
        """
        the number of grades in the dictionary
        :return: int - the number of grades in the dictionary
        """
        self.__read_all_from_file()
        return GradesRepo.__len__(self)
