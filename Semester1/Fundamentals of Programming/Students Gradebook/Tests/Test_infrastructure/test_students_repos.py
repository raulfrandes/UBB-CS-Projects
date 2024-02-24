import unittest

from Domain.student import Student
from Errors.repo_error import RepoError
from Infrastructure.students_file_repo import StudentsFileRepo


class TestCaseStudentsRepos(unittest.TestCase):

    def setUp(self) -> None:
        self.__repo = StudentsFileRepo("test_students.txt")
        self.__add_predefined_students()

    def __add_predefined_students(self):
        student_1 = Student(1, "John")
        student_2 = Student(2, "Jane")
        student_3 = Student(3, "Alice")
        student_4 = Student(4, "Bob")

        self.__repo.add_student(student_1)
        self.__repo.add_student(student_2)
        self.__repo.add_student(student_3)
        self.__repo.add_student(student_4)

    def test_add_student(self):
        initial_len = self.__repo.__len__()
        student_1 = Student(5, "Andy")
        self.__repo.add_student(student_1)
        self.assertEqual(self.__repo.__len__(), initial_len + 1)
        student_2 = Student(123, "Marcos")
        self.__repo.add_student(student_2)
        self.assertEqual(self.__repo.__len__(), initial_len + 2)
        self.assertRaises(RepoError, self.__repo.add_student, student_2)

    def test_delete_student(self):
        initial_len = self.__repo.__len__()
        self.__repo.delete_student_by_id(1)
        self.assertEqual(self.__repo.__len__(), initial_len - 1)
        self.assertRaises(RepoError, self.__repo.delete_student_by_id, 23)

    def test_search_student(self):
        student = self.__repo.search_student_by_id(1)
        self.assertEqual(student.get_name(), "John")
        self.assertRaises(RepoError, self.__repo.search_student_by_id, 23)

    def test_update_student(self):
        student = Student(2, "Charles")
        self.__repo.update_student(student)
        self.assertEqual(self.__repo.search_student_by_id(2).get_name(), "Charles")
        self.assertRaises(RepoError, self.__repo.update_student, Student(225, "Michael"))

    def test_get_all(self):
        students = self.__repo.get_all()
        self.assertIsInstance(students, list)
        self.assertEqual(len(students), 4)
        self.__repo.delete_student_by_id(1)
        self.__repo.delete_student_by_id(2)
        students = self.__repo.get_all()
        self.assertEqual(len(students), 2)

    def tearDown(self) -> None:
        self.__repo.delete_all()


if __name__ == "__main__":
    unittest.main()