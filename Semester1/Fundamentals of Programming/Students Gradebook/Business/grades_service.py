from statistics import mean

from Domain.averageDTO import AverageDTO
from Domain.grade import Grade
from Domain.studentDTO import StudentDTO


class GradesService:
    """
        Responsible for implementing operations related to grades
        Coordinates the operations necessary to perform the actions triggered by the user
    """

    def __init__(self, grade_validator, grades_repo, students_repo, subjects_repo, student_validator,
                 subject_validator):
        """
        Initializes the GradesService class
        :param grade_validator: GradeValidator
        :param grades_repo: GradesRepo
        :param students_repo: StudentsRepo
        :param subjects_repo: SubjectsRepo
        :param student_validator: StudentValidator
        :param subject_validator: SubjectValidator
        """
        self.__grade_validator = grade_validator
        self.__grades_repo = grades_repo
        self.__students_repo = students_repo
        self.__subjects_repo = subjects_repo
        self.__student_validator = student_validator
        self.__subject_validator = subject_validator

    def add_grade(self, id_grade, id_student, id_subject, value):
        """
        adds a new grade
        :param id_grade: int
        :param id_student: int
        :param id_subject: int
        :param value: float
        :raises: ValidError - if the grade has invalid information
                 RepoError - if the grade already exists in the repository
        """
        student = self.__students_repo.search_student_by_id(id_student)
        subject = self.__subjects_repo.search_subject_by_id(id_subject)
        grade = Grade(id_grade, student, subject, value)
        self.__grade_validator.validate(grade)
        self.__grades_repo.add_grade(grade)

    def delete_grade_by_id(self, id_grade):
        """
        deletes a grade
        :param id_grade: int
        :raises: RepoError - if the grade does not exist in the repository
        """
        self.__grades_repo.delete_grade_by_id(id_grade)

    def search_grade_by_id(self, id_grade):
        """
        search for a grade by id
        :param id_grade: int
        :raises: RepoError - if the grade does not exist in the repository
        """
        return self.__grades_repo.search_grade_by_id(id_grade)

    def get_all(self):
        """
        returns a list of all grades
        :return: list - list of grades
        """
        initialized_grades = []
        grades = self.__grades_repo.get_all()
        for grade in grades:
            student = self.__students_repo.search_student_by_id(grade.get_student().get_id_student())
            subject = self.__subjects_repo.search_subject_by_id(grade.get_subject().get_id_subject())
            initialized_grades.append(Grade(grade.get_id_grade(), student, subject, grade.get_value()))
        return initialized_grades

    def get_number_of_grades(self):
        """
        returns the number of grades in the repository
        :return: int - the number of grades
        """
        return self.__grades_repo.__len__()

    def delete_student_by_id(self, id_student):
        """
        deletes a student and his grades from repository
        :param id_student: int
        :raises: RepoError - if the student does not exist in the repository
        """
        grades = self.get_all()
        for grade in grades:
            if grade.get_student().get_id_student() == id_student:
                self.delete_grade_by_id(grade.get_id_grade())
        self.__students_repo.delete_student_by_id(id_student)

    def delete_subject_by_id(self, id_subject):
        """
        deletes a subject and the grades associated with it from repository
        :param id_subject: int
        :raises: RepoError - if the subject does not exist in the repository
        """
        grades = self.get_all()
        for grade in grades:
            if grade.get_subject().get_id_subject() == id_subject:
                self.delete_grade_by_id(grade.get_id_grade())
        self.__subjects_repo.delete_subject_by_id(id_subject)

    def bubble_sort(self, arr, key=None, reverse=False):
        """
        bubble sort recursive implementation
        """
        if len(arr) <= 1:
            return arr

        swapped = False
        for i in range(len(arr) - 1):
            if (not reverse and key(arr[i]) > key(arr[i + 1])) or (reverse and key(arr[i]) < key(arr[i + 1])):
                arr[i], arr[i + 1] = arr[i + 1], arr[i]
                swapped = True

        if not swapped:
            return arr

        return self.bubble_sort(arr, key=key, reverse=reverse)

    def shell_sort(self, arr, gap, key=None, reverse=False):
        """
        shell sort implementation
        """
        if gap <= 0:
            return arr

        for i in range(gap, len(arr)):
            temp = arr[i]
            j = i
            while j >= gap and ((not reverse and key(arr[j - gap]) > key(temp)) or
                                (reverse and key(arr[j - gap]) < key(temp))):
                arr[j] = arr[j - gap]
                j -= gap
            arr[j] = temp

        return self.shell_sort(arr, gap // 2, key=key, reverse=reverse)

    def sort_students_by_name(self, id_subject):
        """
        sort the students from a given subject by their names in alphabetical order
        :param id_subject: int
        :return: list - list of sorted students
        """
        students_grades = {}

        grades = self.get_all()
        for grade in grades:
            id_student = grade.get_student().get_id_student()
            value = grade.get_value()
            if grade.get_subject().get_id_subject() == id_subject:
                if id_student not in students_grades:
                    students_grades[id_student] = []
                students_grades[id_student].append(value)

        students_grades_list = list(map(lambda id_student:
                                        StudentDTO(
                                            self.__students_repo.search_student_by_id(id_student).get_name(),
                                            students_grades[id_student]
                                        ), students_grades))

        return self.bubble_sort(students_grades_list, key=lambda x: (x.get_name()))

    def sort_students_by_grades(self, id_subject):
        """
        sort the students from a given subject by their grades in descending order
        :param id_subject: int
        :return: list - list of sorted students
        """
        students_grades = {}

        grades = self.get_all()
        for grade in grades:
            id_student = grade.get_student().get_id_student()
            value = grade.get_value()
            if grade.get_subject().get_id_subject() == id_subject:
                if id_student not in students_grades:
                    students_grades[id_student] = []
                students_grades[id_student].append(value)

        students_grades_list = list(map(lambda id_student:
                                        StudentDTO(
                                            self.__students_repo.search_student_by_id(id_student).get_name(),
                                            students_grades[id_student]
                                        ), students_grades))

        return self.shell_sort(students_grades_list, len(students_grades_list) // 2,
                               key=lambda x: mean(x.get_grades()), reverse=True)

    def get_first_20percents_of_students_with_highest_grades(self):
        """
        returns first 20 percent of students with the highest grades
        :return: list - list of students
        """
        students_grades = {}

        grades = self.__grades_repo.get_all()
        for grade in grades:
            id_student = grade.get_student().get_id_student()
            value = grade.get_value()
            if id_student not in students_grades:
                students_grades[id_student] = []
            students_grades[id_student].append(value)

        students_grades_list = list(map(lambda id_student:
                                        AverageDTO(
                                            self.__students_repo.search_student_by_id(id_student).get_name(),
                                            mean(students_grades[id_student])
                                        ), students_grades))
        students_grades_list.sort(reverse=True)

        n = int(len(students_grades_list) * 0.2)
        return students_grades_list[:n]
