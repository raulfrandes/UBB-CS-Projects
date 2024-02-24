import unittest

from Domain.subject import Subject
from Domain.grade import Grade
from Domain.student import Student
from Errors.valid_error import ValidError
from Validation.grade_validator import GradeValidator


class TestCaseGrade(unittest.TestCase):

    def setUp(self) -> None:
        self.__validator = GradeValidator()

    def test_create_grade(self):
        student_1 = Student(13, "John")
        subject_1 = Subject(1, "Math", "Mr. Johnson")
        grade_1 = Grade(1, student_1, subject_1, 10)
        self.assertEqual(grade_1.get_id_grade(), 1)
        self.assertEqual(grade_1.get_student(), student_1)
        self.assertEqual(grade_1.get_subject(), subject_1)
        self.assertEqual(grade_1.get_value(), 10)

        student_2 = Student(1, "Jane")
        subject_2 = Subject(2, "Computer Science", "Mrs. Smith")
        grade_1.set_student(student_2)
        grade_1.set_subject(subject_2)
        grade_1.set_value(1)
        self.assertEqual(grade_1.get_student(), student_2)
        self.assertEqual(grade_1.get_subject(), subject_2)
        self.assertEqual(grade_1.get_value(), 1)

    def test_eq(self):
        student_1 = Student(13, "John")
        subject_1 = Subject(1, "Math", "Mr. Johnson")
        grade_1 = Grade(1, student_1, subject_1, 10)
        grade_2 = Grade(1, student_1, subject_1, 10)
        self.assertEqual(grade_1, grade_2)
        grade_3 = Grade(2, student_1, subject_1, 10)
        self.assertNotEqual(grade_1, grade_3)

    def test_validator(self):
        student_1 = Student(13, "John")
        subject_1 = Subject(1, "Math", "Mr. Johnson")
        grade_1 = Grade(1, student_1, subject_1, 10)
        self.__validator.validate(grade_1)
        grade_2 = Grade(0, student_1, subject_1, 10)
        self.assertRaises(ValidError, self.__validator.validate, grade_2)
        grade_3 = Grade(1, student_1, subject_1, 11)
        self.assertRaises(ValidError, self.__validator.validate, grade_3)


if __name__ == "__main__":
    unittest.main()