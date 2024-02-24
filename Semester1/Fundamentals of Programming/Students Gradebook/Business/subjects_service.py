from Domain.subject import Subject


class SubjectsService:
    """
        Responsible for implementing operations related to subjects
        Coordinates the operations necessary to perform the actions triggered by the user
    """

    def __init__(self, subject_validator, subjects_repo):
        """
        Initializes the SubjectsService class
        :param subject_validator: SubjectValidator
        :param subjects_repo: SubjectsRepo
        """
        self.__subject_validator = subject_validator
        self.__subjects_repo = subjects_repo

    def add_subject(self, id_subject, name, teacher):
        """
        adds a subject
        :param id_subject: int
        :param name: string
        :param teacher: string
        :raises: ValidError - if the subject has invalid information
                 RepoError - if the subject already exists in the repository
        """
        subject = Subject(id_subject, name, teacher)
        self.__subject_validator.validate(subject)
        self.__subjects_repo.add_subject(subject)

    def search_subject_by_id(self, id_subject):
        """
        searches for a subject by id
        :param id_subject: int
        :return: subject - the subject with the given id
        :raises: RepoError - if the subject does not exist in the repository
        """
        return self.__subjects_repo.search_subject_by_id(id_subject)

    def update_subject(self, id_subject, new_name, new_teacher):
        """
        updates a subject
        :param id_subject: int
        :param new_name: string
        :param new_teacher: string
        :raises: ValidError - if the subject has invalid information
                 RepoError - if the subject does not exist in the repository
        """
        subject = Subject(id_subject, new_name, new_teacher)
        self.__subject_validator.validate(subject)
        self.__subjects_repo.update_subject(subject)

    def get_all(self):
        """
        returns a list of all subjects
        :return: list - list of subjects
        """
        return self.__subjects_repo.get_all()

    def get_number_of_subjects(self):
        """
        returns the number of students in the repository
        :return: int - the number of students
        """
        return self.__subjects_repo.__len__()
