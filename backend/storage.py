import json
from pathlib import Path


BASE_DIR = Path(__file__).resolve().parent.parent


FILE_PATH = BASE_DIR / "students.json"


def load_data():

    if not FILE_PATH.exists():
        return []


    try:
        with FILE_PATH.open(
            "r",
            encoding="utf-8"
        ) as file:
            data = json.load(file)

    except (json.JSONDecodeError, OSError):
        return []


    if not isinstance(data, list):
        return []


    return data


def save_data(data):
    with FILE_PATH.open(
        "w",
        encoding="utf-8"
    ) as file:

        json.dump(
            data,
            file,
            ensure_ascii=False,
            indent=4
        )