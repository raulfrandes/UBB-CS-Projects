import unittest

from Domain.student import Student
from Errors.valid_error import ValidError
from Validation.student_validator import StudentValidator


class TestCaseStudent(unittest.TestCase):
    def setUp(self) -> None:
        self.__validator = StudentValidator()

    def test_create_student(self):
        student_1 = Student(13, "John")
        self.assertEqual(student_1.get_id_student(), 13)
        self.assertEqual(student_1.get_name(), "John")

        student_1.set_name("Jane")
        self.assertEqual(student_1.get_name(), "Jane")

    def test_eq(self):
        student_1 = Student(13, "John")
        student_2 = Student(13, "John")
        self.assertEqual(student_1, student_2)
        student_3 = Student(14, "John")
        self.assertNotEqual(student_1, student_3)

    def test_validator(self):
        student_1 = Student(1, "John")
        self.__validator.validate(student_1)
        student_2 = Student(-3, "Jane")
        self.assertRaises(ValidError, self.__validator.validate, student_2)
        student_3 = Student(3, "")
        self.assertRaises(ValidError, self.__validator.validate, student_3)


if __name__ == '__main__':
    unittest.main()
