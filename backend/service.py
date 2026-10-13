import re
import uuid
from datetime import date

from . import storage


REQUIRED_FIELDS = (
    "fullName",
    "group",
    "ISU",
)


def validate_student(data):
    if not isinstance(data, dict):
        return "Request body must be a JSON object"


    for field in REQUIRED_FIELDS:
        if field not in data:
            return f"Missing required field: {field}"



    full_name = data["fullName"]

    if not isinstance(full_name, str):
        return "fullName must be a string"

    full_name = full_name.strip()
    names = full_name.split()

    if len(names) < 2:
        return "fullName must contain at least two words"

    if any(len(word) < 2 for word in names):
        return "Each word in fullName must contain at least two characters"



    group = data["group"]

    if not isinstance(group, str):
        return "group must be a string"

    if not re.fullmatch(r"[A-Za-z]\d{4}", group):
        return "group must have format like P3223"



    isu = data["ISU"]

    if not isinstance(isu, str):
        return "ISU must be a string"

    if not re.fullmatch(r"\d{6}", isu):
        return "ISU must contain exactly 6 digits"



    dorm_number = data.get("dormNumber")

    if dorm_number is not None:
        if isinstance(dorm_number, bool) or not isinstance(dorm_number, int):
            return "dormNumber must be an integer"

        if dorm_number < 1:
            return "dormNumber must be greater than 0"



    room = data.get("room")

    if room is not None:
        if isinstance(room, bool) or not isinstance(room, int):
            return "room must be an integer"

        if room < 1:
            return "room must be greater than 0"



    date_arrived = data.get("dateArrived")

    if date_arrived not in (None, ""):
        if not isinstance(date_arrived, str):
            return "dateArrived must be a string"

        try:
            date.fromisoformat(date_arrived)
        except ValueError:
            return "dateArrived must have format YYYY-MM-DD"



    is_foreign = data.get("isForeign", False)

    if not isinstance(is_foreign, bool):
        return "isForeign must be boolean"



    notes = data.get("notes", "")

    if not isinstance(notes, str):
        return "notes must be a string"


    return None



def get_all_requests(full_name=None, group=None, dormitory=None):
    students = storage.load_data()

    if full_name:
        search_words = full_name.strip().casefold().split()

        students = [
            student
            for student in students
            if all(
                word in student.get("fullName", "").casefold()
                for word in search_words
            )
        ]

    if group:
        target_group = group.strip().casefold()

        students = [
            student
            for student in students
            if student.get("group", "").strip().casefold()
            == target_group
        ]

    if dormitory:
        students = [
            student
            for student in students
            if str(student.get("dormNumber")) == str(dormitory)
        ]

    return students



def get_request_by_id(student_id):
    students = storage.load_data()

    for student in students:
        if student.get("ID") == student_id:
            return student

    return None



def create_request(data):


    error = validate_student(data)

    if error is not None:
        return None, error, 422


    students = storage.load_data()




    if any(student.get("ISU") == data["ISU"] for student in students):
        return None, "Student with this ISU already exists", 409


    student = {
        "ID": str(uuid.uuid4()),
        "fullName": data["fullName"].strip(),
        "group": data["group"].strip(),
        "ISU": data["ISU"],
        "dormNumber": data.get("dormNumber"),
        "room": data.get("room"),
        "dateArrived": data.get("dateArrived"),
        "isForeign": data.get("isForeign", False),
        "notes": data.get("notes", "")
    }


    names = student["fullName"].split()

    student["fullName"] = " ".join(
        word.capitalize()
        for word in names
    )



    student["group"] = (
        student["group"][0].upper()
        + student["group"][1:]
    )



    if student["dormNumber"] is None:
        student["room"] = None
        student["dateArrived"] = None



    if student["dateArrived"] in (None, ""):
        student["dateArrived"] = None
        student["room"] = None


    students.append(student)

    storage.save_data(students)

    return student, None, 201



def update_request(student_id, data):

    if not isinstance(data, dict):
        return None, "Request body must be a JSON object", 400


    students = storage.load_data()



    student_index = -1

    for index, student in enumerate(students):
        if student.get("ID") == student_id:
            student_index = index
            break


    if student_index == -1:
        return None, "Student not found", 404


    current_student = students[student_index]


    if "ID" in data and data["ID"] != student_id:
        return None, "ID cannot be changed", 400


    updated_student = current_student.copy()

    data = data.copy()
    data.pop("ID", None)

    updated_student.update(data)



    error = validate_student(updated_student)

    if error is not None:
        return None, error, 422



    if any(
        student.get("ISU") == updated_student["ISU"]
        and student.get("ID") != student_id
        for student in students
    ):
        return None, "Student with this ISU already exists", 409


    updated_student["fullName"] = " ".join(
        word.capitalize()
        for word in updated_student["fullName"].strip().split()
    )



    updated_student["group"] = (
        updated_student["group"][0].upper()
        + updated_student["group"][1:]
    )



    if updated_student["dormNumber"] is None:
        updated_student["room"] = None
        updated_student["dateArrived"] = None

    if updated_student["dateArrived"] in (None, ""):
        updated_student["dateArrived"] = None
        updated_student["room"] = None


    students[student_index] = updated_student

    storage.save_data(students)

    return updated_student, None, 200



def delete_request(student_id):
    students = storage.load_data()



    student_index = -1

    for index, student in enumerate(students):
        if student.get("ID") == student_id:
            student_index = index
            break


    if student_index == -1:
        return False



    students.pop(student_index)

    storage.save_data(students)

    return True