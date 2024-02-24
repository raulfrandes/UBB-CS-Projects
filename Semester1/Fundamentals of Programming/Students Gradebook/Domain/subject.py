class Subject:

    def __init__(self, id_subject, name, teacher):
        """
        create a new object of type Subject
        :param id_subject: int
        :param name: string
        :param teacher: string
        """
        self.__id_subject = id_subject
        self.__name = name
        self.__teacher = teacher

    def get_id_subject(self):
        """
        return the id of the subject
        :return: int - the id of the subject
        """
        return self.__id_subject

    def get_name(self):
        """
        return the name of the subject
        :return: string - the name of the subject
        """
        return self.__name

    def get_teacher(self):
        """
        return the teacher of the subject
        :return: string - the teacher of the subject
        """
        return self.__teacher

    def set_name(self, new_name):
        """
        update the name of the subject
        :param new_name: string
        """
        self.__name = new_name

    def set_teacher(self, new_teacher):
        """
        update the teacher of the subject
        :param new_teacher: string
        """
        self.__teacher = new_teacher

    def __eq__(self, other):
        """
        verify if two subject objects are equal
        :param other: Subject
        :return: bool - True if both objects are equal or False otherwise
        """
        return self.__id_subject == other.__id_subject

    def __str__(self):
        """
        return string representation of the subject
        :return: string - subject information
        """
        return f"ID: {self.__id_subject}, Name: {self.__name}, Teacher: {self.__teacher}"
