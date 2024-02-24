from Errors.repo_error import RepoError


class GradesRepo:
    """
        class created with the responsibility of managing the set of grades (i.e. it offers a persistent storage for
        objects of class Grade)
    """

    def __init__(self):
        """
        the grades will be stored in a dictionary
        """
        self._grades = {}

    def add_grade(self, grade):
        """
        adds a grade to the dictionary
        :param grade: Grade
        :raises: RepoError - if the grade already exists throw a RepoError exception with the error message
                             "grade already exists!\n"
        """
        if grade.get_id_grade() in self._grades:
            raise RepoError("grade already exists!\n")
        self._grades[grade.get_id_grade()] = grade

    def delete_grade_by_id(self, id_grade):
        """
        deletes a grade from the dictionary
        :param id_grade: int
        :raises: RepoError - if the grade with the given id does not exist throw a RepoError exception with
                             the error message "grade does not exist!\n"
        """
        if id_grade not in self._grades:
            raise RepoError("grade does not exist!\n")
        del self._grades[id_grade]

    def search_grade_by_id(self, id_grade):
        """
        search for a grade from the dictionary
        :param id_grade: int
        :return: Grade - the grade with the given id
        :raises: RepoError - if the grade with the given id does not exist throw a RepoError exception with
                             the error message "grade does not exist!\n"
        """
        if id_grade not in self._grades:
            raise RepoError("grade does not exist!\n")
        return self._grades[id_grade]

    def update_grade(self, updated_grade):
        """
        update a grade from the dictionary
        :param updated_grade: Grade
        :raises: RepoError - if the grade with the given id does not exist throw a RepoError exception with
                             the error message "grade does not exist!\n"
        """
        if updated_grade.get_id_grade() not in self._grades:
            raise RepoError("grade does not exist!\n")
        self._grades[updated_grade.get_id_grade()] = updated_grade

    def get_all(self):
        """
        returns a list with all the grades from the dictionary
        :return: list - list of grades
        """
        return [g for g in self._grades.values()]

    def delete_all(self):
        """
        deletes all grades from the dictionary
        """
        self._grades = {}

    def __len__(self):
        """
        the number of grades in the dictionary
        :return: int - the number of grades in the dictionary
        """
        return len(self._grades)
