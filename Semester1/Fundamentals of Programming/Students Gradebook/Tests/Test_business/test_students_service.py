import unittest

from Business.students_service import StudentsService
from Errors.valid_error import ValidError
from Infrastructure.students_repo import StudentsRepo
from Validation.student_validator import StudentValidator


class TestCaseStudentsService(unittest.TestCase):

    def setUp(self) -> None:
        repo = StudentsRepo()
        validator = StudentValidator()
        self.__serv = StudentsService(validator, repo)

    def test_add_student(self):
        self.__serv.add_student(1, "John")
        student = self.__serv.search_student_by_id(1)
        self.assertEqual(student.get_id_student(), 1)
        self.assertEqual(student.get_name(), "John")
        self.assertEqual(len(self.__serv.get_all()), 1)
        self.assertRaises(ValidError, self.__serv.add_student, 0, "Jane")

    def test_search_student_by_id(self):
        self.__serv.add_student(1, "John")
        student = self.__serv.search_student_by_id(1)
        self.assertEqual(student.get_name(), "John")

    def test_get_all(self):
        self.__serv.add_student(1, "John")
        self.__serv.add_student(2, "Jane")
        self.assertIsInstance(self.__serv.get_all(), list)
        self.assertEqual(len(self.__serv.get_all()), 2)

    def test_get_number_of_students(self):
        self.__serv.add_student(1, "John")
        self.__serv.add_student(2, "Jane")
        self.__serv.add_student(3, "Alice")
        self.__serv.add_student(4, "Stefan")
        self.assertEqual(self.__serv.get_number_of_students(), 4)


if __name__ == "__main__":
    unittest.main()
