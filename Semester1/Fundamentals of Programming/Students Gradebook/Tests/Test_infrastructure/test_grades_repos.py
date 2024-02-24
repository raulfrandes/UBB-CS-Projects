import unittest

from Domain.subject import Subject
from Domain.grade import Grade
from Domain.student import Student
from Errors.repo_error import RepoError
from Infrastructure.grades_file_repo import GradesFileRepo


class TestCaseGradesRepos(unittest.TestCase):

    def setUp(self) -> None:
        self.__repo = GradesFileRepo("test_grades.txt")
        self.__add_predefined_grades()

    def __add_predefined_grades(self):
        student_1 = Student(1, None)
        student_2 = Student(2, None)
        student_3 = Student(3, None)
        student_4 = Student(4, None)
        subject_1 = Subject(1, None, None)
        subject_2 = Subject(2, None, None)
        subject_3 = Subject(3, None, None)
        subject_4 = Subject(4, None, None)
        grade_1 = Grade(1, student_1, subject_1, 1)
        grade_2 = Grade(2, student_2, subject_2, 2)
        grade_3 = Grade(3, student_3, subject_3, 3)
        grade_4 = Grade(4, student_4, subject_4, 4)

        self.__repo.add_grade(grade_1)
        self.__repo.add_grade(grade_2)
        self.__repo.add_grade(grade_3)
        self.__repo.add_grade(grade_4)

    def test_add_grade(self):
        initial_len = self.__repo.__len__()
        student_1 = Student(5, None)
        subject_1 = Subject(5, None, None)
        grade_1 = Grade(5, student_1, subject_1, 5)
        self.__repo.add_grade(grade_1)
        self.assertEqual(self.__repo.__len__(), initial_len + 1)
        student_2 = Student(123, None)
        subject_2 = Subject(123, None, None)
        grade_2 = Grade(124, student_2, subject_2, 7)
        self.__repo.add_grade(grade_2)
        self.assertEqual(self.__repo.__len__(), initial_len + 2)
        self.assertRaises(RepoError, self.__repo.add_grade, grade_2)

    def test_delete_grade(self):
        initial_len = self.__repo.__len__()
        self.__repo.delete_grade_by_id(1)
        self.assertEqual(self.__repo.__len__(), initial_len - 1)
        self.assertRaises(RepoError, self.__repo.delete_grade_by_id, 23)

    def test_search_grade(self):
        grade = self.__repo.search_grade_by_id(1)
        self.assertEqual(grade.get_value(), 1)
        self.assertRaises(RepoError, self.__repo.search_grade_by_id, 23)

    def test_update_grade(self):
        student = Student(2, None)
        subject = Subject(2, None, None)
        grade = Grade(2, student, subject, 2)
        self.__repo.update_grade(grade)
        self.assertEqual(self.__repo.search_grade_by_id(2).get_value(), 2)
        self.assertRaises(RepoError, self.__repo.update_grade, Grade(7, student, subject, 78))

    def test_get_all(self):
        grades = self.__repo.get_all()
        self.assertIsInstance(grades, list)
        self.assertEqual(len(grades), 4)
        self.__repo.delete_grade_by_id(1)
        self.__repo.delete_grade_by_id(2)
        grades = self.__repo.get_all()
        self.assertEqual(len(grades), 2)

    def tearDown(self) -> None:
        self.__repo.delete_all()


if __name__ == "__main__":
    unittest.main()
