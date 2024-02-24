import unittest

from Business.subjects_service import SubjectsService
from Domain.subject import Subject
from Errors.valid_error import ValidError
from Infrastructure.subjects_repo import SubjectsRepo
from Validation.subject_validator import SubjectValidator


class TestCaseServiceDiscipline(unittest.TestCase):

    def setUp(self) -> None:
        repo = SubjectsRepo()
        validator = SubjectValidator()
        self.__serv = SubjectsService(validator, repo)

    def test_add_subject(self):
        subject = Subject(1, "Math", "Mr. Johnson")
        self.__serv.add_subject(1, "Math", "Mr. Johnson")
        self.assertEqual(subject.get_id_subject(), 1)
        self.assertEqual(subject.get_name(), "Math")
        self.assertEqual(subject.get_teacher(), "Mr. Johnson")
        self.assertEqual(len(self.__serv.get_all()), 1)
        self.assertRaises(ValidError, self.__serv.add_subject, 0, "Biology", "")

    def test_search_subject_by_id(self):
        self.__serv.add_subject(1, "Math", "Mr. Johnson")
        subject = self.__serv.search_subject_by_id(1)
        self.assertEqual(subject.get_name(), "Math")

    def test_get_all(self):
        self.__serv.add_subject(1, "Math", "Mr. Johnson")
        self.__serv.add_subject(2, "Biology", "Mrs. Smith")
        self.assertIsInstance(self.__serv.get_all(), list)
        self.assertEqual(len(self.__serv.get_all()), 2)

    def test_get_number_of_subjects(self):
        self.__serv.add_subject(1, "Math", "Mr. Johnson")
        self.__serv.add_subject(2, "Biology", "Mrs. Smith")
        self.__serv.add_subject(3, "Computer Science", "Dr. Brown")
        self.__serv.add_subject(4, "History", "Ms. Williams")
        self.assertEqual(self.__serv.get_number_of_subjects(), 4)


if __name__ == "__main__":
    unittest.main()
