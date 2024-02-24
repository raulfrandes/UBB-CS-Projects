from Errors.repo_error import RepoError
from Errors.valid_error import ValidError


class UI:

    def __init__(self, students_service, subjects_service, grades_service):
        """
        initializes the UI class
        :param students_service: StudentsService
        :param subjects_service: SubjectsService
        :param grades_service: GradesService
        """
        self.__students_service = students_service
        self.__subjects_service = subjects_service
        self.__grades_service = grades_service

    def __print_menu(self):
        print("Menu:")
        print("    0. Print the students and the subjects.")
        print("    1. Add student.")
        print("    2. Add subject.")
        print("    3. Delete student.")
        print("    4. Delete subject.")
        print("    5. Update student.")
        print("    6. Update subject.")
        print("    7. Search for a student by id.")
        print("    8. Search for a subject by id.")
        print("    9. Generate a random student.")
        print("    10. Add grade.")
        print("    11. Delete grade.")
        print("    12. Print grades.")
        print("    13. The list of students and their grades from a subject sorted alphabetically by their names.")
        print("    14. The list of students and their grades from a subject sorted in descending order" +
              " by their grades.")
        print("    15. First 20% of the students with the highest grades average.")
        print("    16. Print menu.")
        print("    17. Exit.")

    def __print_students(self):
        students = self.__students_service.get_all()
        if len(students) == 0:
            print("No students added!")
            return
        for student in students:
            print(student)

    def __print_subjects(self):
        subjects = self.__subjects_service.get_all()
        if len(subjects) == 0:
            print("No subjects added!")
            return
        for subject in subjects:
            print(subject)

    def __add_student_ui(self):
        id_student = int(input("Student's id: "))
        name = input("Student's name: ")
        self.__students_service.add_student(id_student, name)
        print("Student added successfully!")

    def __add_subject_ui(self):
        id_subject = int(input("Subject's id: "))
        name = input("Subject's name: ")
        teacher = input("Subject's teacher name: ")
        self.__subjects_service.add_subject(id_subject, name, teacher)
        print("Subject added successfully!")

    def __delete_student_by_id_ui(self):
        if self.__students_service.get_number_of_students() == 0:
            print("No students to delete!")
            return
        id_student = int(input("Student's id: "))
        self.__grades_service.delete_student_by_id(id_student)
        print("Student deleted successfully!")

    def __delete_subject_by_id_ui(self):
        if self.__subjects_service.get_number_of_subjects() == 0:
            print("No subjects to delete!")
            return
        id_subject = int(input("Subject's id: "))
        self.__grades_service.delete_subject_by_id(id_subject)
        print("Subject deleted successfully!")

    def __update_student_ui(self):
        if self.__students_service.get_number_of_students() == 0:
            print("No students to update!")
            return
        id_student = int(input("Student's id: "))
        new_name = input("New name: ")
        self.__students_service.update_student(id_student, new_name)
        print("Student updated successfully!")

    def __update_subject_ui(self):
        if self.__subjects_service.get_number_of_subjects() == 0:
            print("No subjects to update!")
            return
        id_subject = int(input("Subject's id: "))
        new_name = input("New name: ")
        new_teacher = input("New teacher: ")
        self.__subjects_service.update_subject(id_subject, new_name, new_teacher)
        print("Subject updated successfully!")

    def __search_student_by_id_ui(self):
        if self.__students_service.get_number_of_students() == 0:
            print("No students to search for!")
            return
        id_student = int(input("Student's id: "))
        print(self.__students_service.search_student_by_id(id_student))

    def __search_subject_by_id_ui(self):
        if self.__subjects_service.get_number_of_subjects() == 0:
            print("No subjects to search for!")
            return
        id_subject = int(input("Subject's id: "))
        print(self.__subjects_service.search_subject_by_id(id_subject))

    def __get_random_student_ui(self):
        print(self.__students_service.get_random_student())

    def __print_grades(self):
        grades = self.__grades_service.get_all()
        if len(grades) == 0:
            print("No grades added!")
            return
        for grade in grades:
            print(grade)

    def __add_grade_ui(self):
        id_grade = int(input("Grade's id: "))
        id_student = int(input("Student's id: "))
        id_subject = int(input("Subject's id: "))
        value = float(input("Grade's value: "))
        self.__grades_service.add_grade(id_grade, id_student, id_subject, value)
        print("Grade added successfully!")

    def __delete_grade_by_id(self):
        if self.__grades_service.get_number_of_grades() == 0:
            print("No grades to delete!")
            return
        id_grade = int(input("Grade's id: "))
        self.__grades_service.delete_grade_by_id(id_grade)
        print("Grade deleted successfully!")

    def __write_all_to_file(self, statistics):
        with open("Presentation/statistics.txt", "w") as f:
            for e in statistics:
                f.write(str(e) + "\n")

    def __sort_students_by_name_ui(self):
        if self.__subjects_service.get_number_of_subjects() == 0:
            print("No subjects to filter from!")
            return
        id_subject = int(input("Subject's id: "))
        students = self.__grades_service.sort_students_by_name(id_subject)
        self.__write_all_to_file(students)
        print("The statistics file has been updated successfully!")

    def __sort_students_by_grades_ui(self):
        if self.__subjects_service.get_number_of_subjects() == 0:
            print("No subjects to filter from!")
            return
        id_subject = int(input("Subject's id: "))
        students = self.__grades_service.sort_students_by_grades(id_subject)
        self.__write_all_to_file(students)
        print("The statistics file has been updated successfully!")

    def __get_first_20percents_of_students_with_highest_grades_ui(self):
        students = self.__grades_service.get_first_20percents_of_students_with_highest_grades()
        self.__write_all_to_file(students)
        print("The statistics file has been updated successfully!")

    def run(self):
        self.__print_menu()
        while True:
            option = input("Choose an option: ")
            try:
                if option == '0':
                    print("---- list of students ----")
                    self.__print_students()
                    print("---- list of subjects ----")
                    self.__print_subjects()
                elif option == '1':
                    self.__add_student_ui()
                elif option == '2':
                    self.__add_subject_ui()
                elif option == '3':
                    self.__delete_student_by_id_ui()
                elif option == '4':
                    self.__delete_subject_by_id_ui()
                elif option == '5':
                    self.__update_student_ui()
                elif option == '6':
                    self.__update_subject_ui()
                elif option == '7':
                    self.__search_student_by_id_ui()
                elif option == '8':
                    self.__search_subject_by_id_ui()
                elif option == '9':
                    self.__get_random_student_ui()
                elif option == '10':
                    self.__add_grade_ui()
                elif option == '11':
                    self.__delete_grade_by_id()
                elif option == '12':
                    print("---- list of grades ----")
                    self.__print_grades()
                elif option == '13':
                    self.__sort_students_by_name_ui()
                elif option == '14':
                    self.__sort_students_by_grades_ui()
                elif option == '15':
                    self.__get_first_20percents_of_students_with_highest_grades_ui()
                elif option == '16':
                    self.__print_menu()
                elif option == '17':
                    return
                else:
                    print("invalid option!")
            except RepoError as e:
                print(e)
            except ValueError as e:
                print(e)
            except ValidError as e:
                print(e)
