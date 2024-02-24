from Errors.repo_error import RepoError


class StudentsRepo:
    """
        class created with the responsibility of managing the set of students (i.e. it offers a persistent storage for
        objects of class Student)
    """

    def __init__(self):
        """
        the students will be stored in a dictionary
        """
        self._students = {}

    def add_student(self, student):
        """
        adds a student to the dictionary
        :param student: Student
        :raises: RepoError - if the student already exists throw a RepoError exception with the error message
                             "student already exists!\n"
        """
        if student.get_id_student() in self._students:
            raise RepoError("student already exists!\n")
        self._students[student.get_id_student()] = student

    def delete_student_by_id(self, id_student):
        """
        deletes a student from the dictionary
        :param id_student: int
        :raises: RepoError - if the student with the given id does not exist throw a RepoError exception with
                             the error message "student does not exist!\n"
        """
        if id_student not in self._students:
            raise RepoError("student does not exist!\n")
        del self._students[id_student]

    def search_student_by_id(self, id_student):
        """
        search for a student from the dictionary
        :param id_student: int
        :return: Student - the student with the given id
        :raises: RepoError - if the student with the given id does not exist throw a RepoError exception with
                             the error message "student does not exist!\n"
        """
        if id_student not in self._students:
            raise RepoError("student does not exist!\n")
        return self._students[id_student]

    def update_student(self, updated_student):
        """
        update a student from the dictionary
        :param updated_student: Student
        :raises: RepoError - if the student with the given id does not exist throw a RepoError exception with
                             the error message "student does not exist!\n"
        """
        if updated_student.get_id_student() not in self._students:
            raise RepoError("student does not exist!\n")
        self._students[updated_student.get_id_student()] = updated_student

    def get_all(self):
        """
        returns a list with all the students from the dictionary
        :return: list - list of students
        """
        return [s for s in self._students.values()]

    def delete_all(self):
        """
        deletes all students from the dictionary
        """
        self._students = {}

    def __len__(self):
        """
        the number of students in the dictionary
        :return: int - the number of students in the dictionary
        """
        return len(self._students)
