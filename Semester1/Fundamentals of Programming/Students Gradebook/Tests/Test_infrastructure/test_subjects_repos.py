import unittest

from Domain.subject import Subject
from Errors.repo_error import RepoError
from Infrastructure.subjects_file_repo import SubjectsFileRepo


class TestCaseSubjectsRepos(unittest.TestCase):

    def setUp(self) -> None:
        self.__repo = SubjectsFileRepo("test_subjects.txt")
        self.__add_predefined_subjects()

    def __add_predefined_subjects(self):
        subject_1 = Subject(1, "Math", "Mr. Johnson")
        subject_2 = Subject(2, "History", "Ms. Williams")
        subject_3 = Subject(3, "Computer Science", "Dr. Brown")
        subject_4 = Subject(4, "Physics", "Mrs. Smith")

        self.__repo.add_subject(subject_1)
        self.__repo.add_subject(subject_2)
        self.__repo.add_subject(subject_3)
        self.__repo.add_subject(subject_4)

    def test_add_subject(self):
        initial_len = self.__repo.__len__()
        subject_1 = Subject(5, "Biology", "Mrs. Johnson")
        self.__repo.add_subject(subject_1)
        self.assertEqual(self.__repo.__len__(), initial_len + 1)
        subject_2 = Subject(123, "Physics", "Mr. Smith")
        self.__repo.add_subject(subject_2)
        self.assertEqual(self.__repo.__len__(), initial_len + 2)
        self.assertRaises(RepoError, self.__repo.add_subject, subject_2)

    def test_delete_subject(self):
        initial_len = self.__repo.__len__()
        self.__repo.delete_subject_by_id(1)
        self.assertEqual(self.__repo.__len__(), initial_len - 1)
        self.assertRaises(RepoError, self.__repo.delete_subject_by_id, 23)

    def test_search_subject(self):
        subject = self.__repo.search_subject_by_id(1)
        self.assertEqual(subject.get_name(), "Math")
        self.assertEqual(subject.get_teacher(), "Mr. Johnson")
        self.assertRaises(RepoError, self.__repo.search_subject_by_id, 23)

    def test_update_subject(self):
        subject = Subject(2, "History", "Ms. Williams")
        self.__repo.update_subject(subject)
        self.assertEqual(self.__repo.search_subject_by_id(2).get_name(), "History")
        self.assertEqual(self.__repo.search_subject_by_id(2).get_teacher(), "Ms. Williams")
        self.assertRaises(RepoError, self.__repo.update_subject, Subject(225, "Biology", "Ms. Williams"))

    def test_get_all(self):
        subjects = self.__repo.get_all()
        self.assertIsInstance(subjects, list)
        self.assertEqual(len(subjects), 4)
        self.__repo.delete_subject_by_id(1)
        self.__repo.delete_subject_by_id(2)
        subjects = self.__repo.get_all()
        self.assertEqual(len(subjects), 2)

    def tearDown(self) -> None:
        self.__repo.delete_all()


if __name__ == "__main__":
    unittest.main()