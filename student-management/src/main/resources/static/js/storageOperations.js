const API_URL = "/api/students";

function getToken() {
    return localStorage.getItem("accessToken");
}

function authHeaders() {
    const token = getToken();
    return token ? { "Authorization": `Bearer ${token}` } : {};
}

async function getStudents(nameFilter = "", groupFilter = "", dormFilter = "") {
    const params = new URLSearchParams();
    if (nameFilter) params.set("fullName", nameFilter);
    if (groupFilter) params.set("group", groupFilter);
    if (dormFilter) params.set("dormNumber", dormFilter);

    const query = params.toString();
    const url = query ? `${API_URL}?${query}` : API_URL;

    const response = await fetch(url, {
        method: "GET",
        headers: { ...authHeaders() }
    });

    if (!response.ok) {
        const result = await response.json().catch(() => ({}));
        throw new Error(result.message || `Ошибка HTTP: ${response.status}`);
    }
    const page = await response.json();
    return page.content || [];
}

async function getStudentById(studentID) {
    const response = await fetch(`${API_URL}/${studentID}`, {
        method: "GET",
        headers: { ...authHeaders() }
    });
    if (!response.ok) {
        const result = await response.json().catch(() => ({}));
        throw new Error(result.message || `Ошибка HTTP: ${response.status}`);
    }
    return await response.json();
}

async function addStudent(student) {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            ...authHeaders()
        },
        body: JSON.stringify(student)
    });
    if (!response.ok) {
        const result = await response.json().catch(() => ({}));
        throw new Error(result.message || `Ошибка HTTP: ${response.status}`);
    }
    return await response.json();
}

async function updateStudent(studentID, student) {
    const response = await fetch(`${API_URL}/${studentID}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            ...authHeaders()
        },
        body: JSON.stringify(student)
    });
    if (!response.ok) {
        const result = await response.json().catch(() => ({}));
        throw new Error(result.message || `Ошибка HTTP: ${response.status}`);
    }
    return await response.json();
}

async function deleteStudent(studentID) {
    const response = await fetch(`${API_URL}/${studentID}`, {
        method: "DELETE",
        headers: { ...authHeaders() }
    });
    if (!response.ok) {
        const result = await response.json().catch(() => ({}));
        throw new Error(result.message || `Ошибка HTTP: ${response.status}`);
    }
    return true;
}

export { getStudents, getStudentById, addStudent, updateStudent, deleteStudent };