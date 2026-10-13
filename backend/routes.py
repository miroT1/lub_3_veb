from flask import Blueprint, jsonify, request

from . import service


api_bp = Blueprint(
    "api",
    __name__,
    url_prefix="/api"
)


def error_response(message, status_code):
    return jsonify({
        "error": message
    }), status_code


@api_bp.get("/requests")
def get_requests():
    full_name = request.args.get("fullName")
    group = request.args.get("group")
    dormitory = request.args.get("dormitory")

    students = service.get_all_requests(
        full_name=full_name,
        group=group,
        dormitory=dormitory
    )

    return jsonify(students), 200

@api_bp.route("/requests", methods=["QUERY"])
def query_requests():
    full_name = request.args.get("fullName")
    group = request.args.get("group")
    dormitory = request.args.get("dormitory")

    students = service.get_all_requests(
        full_name=full_name,
        group=group,
        dormitory=dormitory
    )

    return jsonify(students), 200

@api_bp.get("/requests/<student_id>")
def get_request(student_id):
    student = service.get_request_by_id(student_id)

    if student is None:
        return error_response(
            "Student not found",
            404
        )

    return jsonify(student), 200


@api_bp.route("/requests", methods=["POST"])
def post_request():
    data = request.get_json(silent=True)

    if data is None:
        return error_response(
            "Request body must contain valid JSON",
            400
        )

    result, error, code = service.create_request(data)

    if error is not None:
        return error_response(
            error,
            code
        )

    return jsonify(result), code

@api_bp.route("/requests/<student_id>", methods=["PATCH"])
def patch_request(student_id):
    data = request.get_json(silent=True)

    if data is None:
        return error_response(
            "Request body must contain valid JSON",
            400
        )

    result, error, code = service.update_request(
        student_id,
        data
    )

    if error is not None:
        return error_response(
            error,
            code
        )

    return jsonify(result), code


@api_bp.route("/requests/<student_id>", methods=["DELETE"])
def delete_request(student_id):
    success = service.delete_request(student_id)

    if not success:
        return error_response(
            "Student not found",
            404
        )

    return "", 204

@api_bp.route("/requests/<student_id>", methods=["QUERY"])
def query_request(student_id):
    student = service.get_request_by_id(student_id)

    if student is None:
        return error_response(
            "Student not found",
            404
        )

    return jsonify(student), 200