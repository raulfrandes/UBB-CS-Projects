from Domain.subject import Subject
from Infrastructure.subjects_repo import SubjectsRepo


class SubjectsFileRepo(SubjectsRepo):

    def __init__(self, filename):
        SubjectsRepo.__init__(self)
        self.__filename = filename

    def __read_all_from_file(self):
        """
        reads all subjects from a file
        """
        with open(self.__filename, "r") as f:
            lines = f.readlines()
            self._subjects.clear()
            for line in lines:
                line = line.strip()
                if line != "":
                    parts = line.split(", ")
                    id_subject = int(parts[0])
                    name = parts[1]
                    teacher = parts[2]
                    subject = Subject(id_subject, name, teacher)
                    self._subjects[id_subject] = subject

    def __write_all_to_file(self):
        """
        writes all subjects to a file
        """
        with open(self.__filename, "w") as f:
            for subject in self._subjects.values():
                f.write(str(subject.get_id_subject()) + ", " + subject.get_name() + ", " + subject.get_teacher() + "\n")

    def add_subject(self, subject):
        """
        adds a subject to the dictionary
        :param subject: Subject
        :raises: RepoError - if the subject already exists throw a RepoError exception with the error message
                             "subject already exists!\n"
        """
        self.__read_all_from_file()
        SubjectsRepo.add_subject(self, subject)
        self.__write_all_to_file()

    def delete_subject_by_id(self, id_subject):
        """
        deletes a subject from the dictionary
        :param id_subject: int
        :raises: RepoError - if the subject with the given id does not exist throw a RepoError exception with
                             the error message "subject does not exist!\n"
        """
        self.__read_all_from_file()
        SubjectsRepo.delete_subject_by_id(self, id_subject)
        self.__write_all_to_file()

    def search_subject_by_id(self, id_subject):
        """
        search for a subject from the dictionary
        :param id_subject: int
        :return: Subject - the subject with the given id
        :raises: RepoError - if the subject with the given id does not exist throw a RepoError exception with
                             the error message "subject does not exist!\n"
        """
        self.__read_all_from_file()
        return SubjectsRepo.search_subject_by_id(self, id_subject)

    def update_subject(self, updated_subject):
        """
        update a subject from the dictionary
        :param updated_subject: Subject
        :raises: RepoError - if the subject with the given id does not exist throw a RepoError exception with
                             the error message "subject does not exist!\n"
        """
        self.__read_all_from_file()
        SubjectsRepo.update_subject(self, updated_subject)
        self.__write_all_to_file()

    def get_all(self):
        """
        returns a list with all the subjects from the dictionary
        :return: list - list of subjects
        """
        self.__read_all_from_file()
        return SubjectsRepo.get_all(self)

    def delete_all(self):
        """
        deletes all subjects from the dictionary
        """
        SubjectsRepo.delete_all(self)
        self.__write_all_to_file()

    def __len__(self):
        """
        the number of subjects in the dictionary
        :return: int - the number of subjects in the dictionary
        """
        self.__read_all_from_file()
        return SubjectsRepo.__len__(self)
