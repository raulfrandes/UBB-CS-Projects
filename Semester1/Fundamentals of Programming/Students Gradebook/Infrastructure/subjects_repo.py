from Errors.repo_error import RepoError


class SubjectsRepo:
    """
        class created with the responsibility of managing the set of subjects (i.e. it offers a persistent storage for
        objects of class Subject)
    """

    def __init__(self):
        """
        the subjects will be stored in a dictionary
        """
        self._subjects = {}

    def add_subject(self, subject):
        """
        adds a subject to the dictionary
        :param subject: Subject
        :raises: RepoError - if the subject already exists throw a RepoError exception with the error message
                             "subject already exists!\n"
        """
        if subject.get_id_subject() in self._subjects:
            raise RepoError("subject already exists!\n")
        self._subjects[subject.get_id_subject()] = subject

    def delete_subject_by_id(self, id_subject):
        """
        deletes a subject from the dictionary
        :param id_subject: int
        :raises: RepoError - if the subject with the given id does not exist throw a RepoError exception with
                             the error message "subject does not exist!\n"
        """
        if id_subject not in self._subjects:
            raise RepoError("subject does not exist!\n")
        del self._subjects[id_subject]

    def search_subject_by_id(self, id_subject):
        """
        search for a subject from the dictionary
        :param id_subject: int
        :return: Subject - the subject with the given id
        :raises: RepoError - if the subject with the given id does not exist throw a RepoError exception with
                             the error message "subject does not exist!\n"
        """
        if id_subject not in self._subjects:
            raise RepoError("subject does not exist!\n")
        return self._subjects[id_subject]

    def update_subject(self, updated_subject):
        """
        update a subject from the dictionary
        :param updated_subject: Subject
        :raises: RepoError - if the subject with the given id does not exist throw a RepoError exception with
                             the error message "subject does not exist!\n"
        """
        if updated_subject.get_id_subject() not in self._subjects:
            raise RepoError("subject does not exist!\n")
        self._subjects[updated_subject.get_id_subject()] = updated_subject

    def get_all(self):
        """
        returns a list with all the subjects from the dictionary
        :return: list - list of subjects
        """
        return [s for s in self._subjects.values()]

    def delete_all(self):
        """
        deletes all subjects from the dictionary
        """
        self._subjects = {}

    def __len__(self):
        """
        the number of subjects in the dictionary
        :return: int - the number of subjects in the dictionary
        """
        return len(self._subjects)
