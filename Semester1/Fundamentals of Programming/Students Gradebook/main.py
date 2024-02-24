import unittest

from Infrastructure.subjects_file_repo import SubjectsRepo, SubjectsFileRepo
from Infrastructure.grades_file_repo import GradesRepo, GradesFileRepo
from Infrastructure.students_file_repo import StudentsRepo, StudentsFileRepo
from Validation.student_validator import StudentValidator
from Validation.subject_validator import SubjectValidator
from Validation.grade_validator import GradeValidator
from Infrastructure.students_repo import StudentsRepo
from Infrastructure.subjects_repo import SubjectsRepo
from Infrastructure.grades_repo import GradesRepo
from Business.students_service import StudentsService
from Business.subjects_service import SubjectsService
from Business.grades_service import GradesService
from Presentation.ui import UI

if __name__ == '__main__':
    students_file_repo = StudentsFileRepo("students.txt")
    student_validator = StudentValidator()
    students_service = StudentsService(student_validator, students_file_repo)

    subjects_file_repo = SubjectsFileRepo("subjects.txt")
    subject_validator = SubjectValidator()
    subjects_service = SubjectsService(subject_validator, subjects_file_repo)

    grades_file_repo = GradesFileRepo("grades.txt")
    grade_validator = GradeValidator()
    grades_service = GradesService(grade_validator, grades_file_repo, students_file_repo, subjects_file_repo,
                                   student_validator, subject_validator)

    ui = UI(students_service, subjects_service, grades_service)

    ui.run()
