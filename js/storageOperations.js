const API_URL = "/api/requests";


async function getStudents(
    nameFilter = "",
    groupFilter = "",
    dormFilter = ""
) {
    const params = new URLSearchParams();

    if (nameFilter) {
        params.set("fullName", nameFilter);
    }

    if (groupFilter) {
        params.set("group", groupFilter);
    }

    if (dormFilter) {
        params.set("dormitory", dormFilter);
    }

    const filtersCount = [
        nameFilter,
        groupFilter,
        dormFilter
    ].filter(Boolean).length;


    const query = params.toString();

    const url = query
        ? `${API_URL}?${query}`
        : API_URL;


    const method = filtersCount >= 2
        ? "QUERY"
        : "GET";


    const response = await fetch(url, {
        method: method
    });


    const result = await response.json();


    if (!response.ok) {
        throw new Error(
            result.error ||
            `Ошибка HTTP: ${response.status}`
        );
    }


    return result;
}


async function getStudentById(studentID) {
    const response = await fetch(
        `${API_URL}/${encodeURIComponent(studentID)}`
    );

    const result = await response.json();

    if (!response.ok) {
        throw new Error(
            result.error ||
            `Ошибка HTTP: ${response.status}`
        );
    }

    return result;
}


async function addStudent(student) {
    const response = await fetch(API_URL, {
        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(student)
    });

    const result = await response.json();

    if (!response.ok) {
        throw new Error(
            result.error ||
            `Ошибка HTTP: ${response.status}`
        );
    }

    return result;
}


async function updateStudent(studentID, student) {
    const response = await fetch(
        `${API_URL}/${encodeURIComponent(studentID)}`,
        {
            method: "PATCH",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify(student)
        }
    );

    const result = await response.json();

    if (!response.ok) {
        throw new Error(
            result.error ||
            `Ошибка HTTP: ${response.status}`
        );
    }

    return result;
}


async function deleteStudent(studentID) {
    const response = await fetch(
        `${API_URL}/${encodeURIComponent(studentID)}`,
        {
            method: "DELETE"
        }
    );


    if (!response.ok) {
        let result = {};

        try {
            result = await response.json();
        } catch {
            // У ответа может не быть тела
        }

        throw new Error(
            result.error ||
            `Ошибка HTTP: ${response.status}`
        );
    }


    return true;
}


export {
    getStudents,
    getStudentById,
    addStudent,
    updateStudent,
    deleteStudent
};