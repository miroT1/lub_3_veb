import os

from flask import (
    Flask,
    jsonify,
    request,
    send_from_directory
)

from .routes import api_bp


app = Flask(__name__)



app.register_blueprint(api_bp)


BASE_DIR = os.path.dirname(
    os.path.dirname(
        os.path.abspath(__file__)
    )
)


@app.route("/")
def index_page():
    return send_from_directory(
        os.path.join(BASE_DIR, "html"),
        "index.html"
    )


@app.route("/html/<path:filename>")
def serve_html(filename):
    return send_from_directory(
        os.path.join(BASE_DIR, "html"),
        filename
    )


@app.route("/css/<path:filename>")
def serve_css(filename):
    return send_from_directory(
        os.path.join(BASE_DIR, "css"),
        filename
    )


@app.route("/js/<path:filename>")
def serve_js(filename):
    return send_from_directory(
        os.path.join(BASE_DIR, "js"),
        filename
    )



def api_error(message, status_code):
    return jsonify({
        "error": message
    }), status_code


@app.errorhandler(400)
def handle_bad_request(error):
    if request.path.startswith("/api/"):
        return api_error(
            "Bad request",
            400
        )

    return error


@app.errorhandler(404)
def handle_not_found(error):
    if request.path.startswith("/api/"):
        return api_error(
            "Resource not found",
            404
        )

    return error


@app.errorhandler(405)
def handle_method_not_allowed(error):
    if request.path.startswith("/api/"):
        return api_error(
            "Method not allowed",
            405
        )

    return error


@app.errorhandler(500)
def handle_internal_error(error):
    if request.path.startswith("/api/"):
        return api_error(
            "Internal server error",
            500
        )

    return error



if __name__ == "__main__":
    app.run(
        debug=True,
        port=5000
    )