import unittest

from Domain.subject import Subject
from Errors.valid_error import ValidError
from Validation.subject_validator import SubjectValidator


class TestCaseSubject(unittest.TestCase):

    def setUp(self) -> None:
        self.__validator = SubjectValidator()

    def test_create_subject(self):
        subject_1 = Subject(1, "Math", "Mr. Johnson")
        self.assertEqual(subject_1.get_id_subject(), 1)
        self.assertEqual(subject_1.get_name(), "Math")
        self.assertEqual(subject_1.get_teacher(), "Mr. Johnson")

        subject_1.set_name("Computer Science")
        subject_1.set_teacher("Mrs. Smith")
        self.assertEqual(subject_1.get_name(), "Computer Science")
        self.assertEqual(subject_1.get_teacher(), "Mrs. Smith")

    def test_eq(self):
        subject_1 = Subject(1, "Math", "Mr. Johnson")
        subject_2 = Subject(1, "Math", "Mr. Johnson")
        self.assertEqual(subject_1, subject_2)
        subject_3 = Subject(2, "Computer Science", "Mrs. Smith")
        self.assertNotEqual(subject_1, subject_3)

    def test_validator(self):
        subject_1 = Subject(1, "Math", "Mr. Johnson")
        self.__validator.validate(subject_1)
        subject_2 = Subject(-3, "Math", "Mr. Johnson")
        self.assertRaises(ValidError, self.__validator.validate, subject_2)
        subject_3 = Subject(3, "", "Mr. Johnson")
        self.assertRaises(ValidError, self.__validator.validate, subject_3)
        subject_4 = Subject(3, "Math", "")
        self.assertRaises(ValidError, self.__validator.validate, subject_4)


if __name__ == "__main__":
    unittest.main()
