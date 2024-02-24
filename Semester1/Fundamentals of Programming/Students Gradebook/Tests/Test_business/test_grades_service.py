import unittest

from Business.subjects_service import SubjectsService
from Business.grades_service import GradesService
from Business.students_service import StudentsService
from Domain.grade import Grade
from Domain.subject import Subject
from Domain.averageDTO import AverageDTO
from Domain.student import Student
from Domain.studentDTO import StudentDTO
from Errors.repo_error import RepoError
from Errors.valid_error import ValidError
from Infrastructure.subjects_repo import SubjectsRepo
from Infrastructure.grades_repo import GradesRepo
from Infrastructure.students_repo import StudentsRepo
from Validation.subject_validator import SubjectValidator
from Validation.grade_validator import GradeValidator
from Validation.student_validator import StudentValidator


class TestCaseGradesService(unittest.TestCase):

    def setUp(self) -> None:
        self.__students_repo = StudentsRepo()
        self.__subjects_repo = SubjectsRepo()
        self.__repo = GradesRepo()
        self.__add_predefined_grades()
        val = GradeValidator()
        student_val = StudentValidator()
        subject_val = SubjectValidator()
        self.__serv = GradesService(val, self.__repo, self.__students_repo, self.__subjects_repo, student_val,
                                    subject_val)

    def __add_predefined_grades(self):
        student_1 = Student(1, "John")
        student_2 = Student(2, "Jane")
        student_3 = Student(3, "Alice")
        student_4 = Student(4, "Bob")

        self.__students_repo.add_student(student_1)
        self.__students_repo.add_student(student_2)
        self.__students_repo.add_student(student_3)
        self.__students_repo.add_student(student_4)

        subject_1 = Subject(1, "Math", "Mr. Johnson")
        subject_2 = Subject(2, "History", "Ms. Williams")
        subject_3 = Subject(3, "Computer Science", "Dr. Brown")
        subject_4 = Subject(4, "Physics", "Mrs. Smith")

        self.__subjects_repo.add_subject(subject_1)
        self.__subjects_repo.add_subject(subject_2)
        self.__subjects_repo.add_subject(subject_3)
        self.__subjects_repo.add_subject(subject_4)

        grade_1 = Grade(1, student_1, subject_1, 1)
        grade_2 = Grade(2, student_2, subject_2, 2)
        grade_3 = Grade(3, student_3, subject_3, 3)
        grade_4 = Grade(4, student_4, subject_4, 4)

        self.__repo.add_grade(grade_1)
        self.__repo.add_grade(grade_2)
        self.__repo.add_grade(grade_3)
        self.__repo.add_grade(grade_4)

    def test_add_grade(self):
        student_1 = Student(1, "John")
        subject_2 = Subject(2, "History", "Ms. Williams")
        self.__serv.add_grade(31, 1, 2, 10)
        grade = self.__serv.search_grade_by_id(31)
        self.assertEqual(grade.get_id_grade(), 31)
        self.assertEqual(grade.get_student(), student_1)
        self.assertEqual(grade.get_subject(), subject_2)
        self.assertEqual(grade.get_value(), 10)
        self.assertRaises(ValidError, self.__serv.add_grade, -3, 4, 3, 0)
        self.assertRaises(RepoError, self.__serv.add_grade, 1, 1, 2, 8)

    def test_delete_grade_by_id(self):
        self.__serv.add_grade(32, 1, 1, 10)
        self.__serv.delete_grade_by_id(32)
        self.assertEqual(len(self.__serv.get_all()), 4)
        self.assertRaises(RepoError, self.__serv.delete_grade_by_id, 32)

    def test_search_grade_by_id(self):
        grade = self.__serv.search_grade_by_id(2)
        self.assertEqual(grade.get_value(), 2)
        self.assertRaises(RepoError, self.__serv.search_grade_by_id, 50)

    def test_get_all(self):
        self.assertIsInstance(self.__serv.get_all(), list)
        self.assertEqual(len(self.__serv.get_all()), 4)

    def test_get_number_of_grades(self):
        self.assertEqual(self.__serv.get_number_of_grades(), 4)
        self.__serv.delete_grade_by_id(1)
        self.__serv.delete_grade_by_id(2)
        self.assertEqual(self.__serv.get_number_of_grades(), 2)

    def test_delete_student_by_id(self):
        student_val = StudentValidator()
        students_serv = StudentsService(student_val, self.__students_repo)
        self.__serv.delete_student_by_id(1)
        self.assertEqual(students_serv.get_number_of_students(), 3)
        self.assertEqual(self.__serv.get_number_of_grades(), 3)
        self.assertRaises(RepoError, self.__serv.delete_student_by_id, 8)

    def test_delete_subject_by_id(self):
        subject_val = SubjectValidator()
        subjects_serv = SubjectsService(subject_val, self.__subjects_repo)
        self.__serv.delete_subject_by_id(1)
        self.assertEqual(subjects_serv.get_number_of_subjects(), 3)
        self.assertEqual(self.__serv.get_number_of_grades(), 3)
        self.assertRaises(RepoError, self.__serv.delete_subject_by_id, 7)

    def test_sort_students_by_name(self):
        self.__serv.add_grade(10, 2, 1, 8)
        self.__serv.add_grade(11, 3, 1, 4)
        self.__serv.add_grade(12, 4, 1, 7)
        studentDTO_1 = StudentDTO("John", [1])
        studentDTO_2 = StudentDTO("Jane", [2])
        studentDTO_3 = StudentDTO("Alice", [3])
        studentDTO_4 = StudentDTO("Bob", [4])
        students = self.__serv.sort_students_by_name(1)
        self.assertIsInstance(students, list)
        self.assertEqual(len(students), 4)
        self.assertEqual(students[0], studentDTO_3)
        self.assertEqual(students[1], studentDTO_4)
        self.assertEqual(students[2], studentDTO_2)
        self.assertEqual(students[3], studentDTO_1)

    def test_students_by_grades(self):
        self.__serv.add_grade(10, 2, 1, 8)
        self.__serv.add_grade(11, 3, 1, 4)
        self.__serv.add_grade(12, 4, 1, 7)
        studentDTO_1 = StudentDTO("John", [1])
        studentDTO_2 = StudentDTO("Jane", [8])
        studentDTO_3 = StudentDTO("Alice", [4])
        studentDTO_4 = StudentDTO("Bob", [7])
        students = self.__serv.sort_students_by_grades(1)
        self.assertIsInstance(students, list)
        self.assertEqual(len(students), 4)
        self.assertEqual(students[0], studentDTO_2)
        self.assertEqual(students[1], studentDTO_4)
        self.assertEqual(students[2], studentDTO_3)
        self.assertEqual(students[3], studentDTO_1)

    def test_get_first_20percents_of_students_with_highest_grades(self):
        student = Student(5, "Stefan")
        subject = Subject(5, "Biology", "Mr. Smith")
        self.__students_repo.add_student(student)
        self.__subjects_repo.add_subject(subject)
        self.__serv.add_grade(10, 5, 5, 5)
        averageDTO = AverageDTO("Stefan", 5)
        students = self.__serv.get_first_20percents_of_students_with_highest_grades()
        self.assertIsInstance(students, list)
        self.assertEqual(len(students), 1)
        self.assertEqual(students[0], averageDTO)

    def test_bubble_sort(self):
        studentDTO_1 = StudentDTO("John", [1])
        studentDTO_2 = StudentDTO("Jane", [2])
        studentDTO_3 = StudentDTO("Alice", [3])
        studentDTO_4 = StudentDTO("Bob", [4])
        students_list = [studentDTO_1, studentDTO_2, studentDTO_3, studentDTO_4]
        sorted_list = self.__serv.bubble_sort(students_list, key=lambda x: (x.get_name()))
        self.assertEqual(sorted_list[0], studentDTO_3)
        self.assertEqual(sorted_list[1], studentDTO_4)
        self.assertEqual(sorted_list[2], studentDTO_2)
        self.assertEqual(sorted_list[3], studentDTO_1)

    def test_shell_sort(self):
        studentDTO_1 = StudentDTO("John", [1])
        studentDTO_2 = StudentDTO("Jane", [2])
        studentDTO_3 = StudentDTO("Alice", [3])
        studentDTO_4 = StudentDTO("Bob", [4])
        students_list = [studentDTO_1, studentDTO_2, studentDTO_3, studentDTO_4]
        sorted_list = self.__serv.shell_sort(students_list, len(students_list) // 2, key=lambda x: (x.get_name()))
        self.assertEqual(sorted_list[0], studentDTO_3)
        self.assertEqual(sorted_list[1], studentDTO_4)
        self.assertEqual(sorted_list[2], studentDTO_2)
        self.assertEqual(sorted_list[3], studentDTO_1)


if __name__ == "__main__":
    unittest.main()
