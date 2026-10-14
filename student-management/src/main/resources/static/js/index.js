import {
    getStudents,
    deleteStudent
} from "./storageOperations.js";

import {
    renderTable
} from "./displayOperations.js";

// ==== Проверка авторизации ====
if (!localStorage.getItem("accessToken")) {
    window.location.href = "/html/login.html";
}

// Роль берём из localStorage (её сохраняет login.html)
const userRole = localStorage.getItem("role");
const isAdmin = userRole === "ADMIN";

// Скрыть кнопку "Добавить студента" для не-админов
const addButton = document.querySelector("#show-add-form-bin");
if (!isAdmin && addButton) {
    addButton.style.display = "none";
}

if (addButton) {
    addButton.addEventListener("click", () => {
        location.href = "/html/studentFormPage.html";
    });
}

// ==== Фильтры ====
const filterForm = document.querySelector("#filter-form");
const groupFilter = document.querySelector("#group-filter");
const dormitoryFilter = document.querySelector("#dormitory-filter");
const resetFilterButton = document.querySelector("#reset-filter-button");
const nameFilter = document.querySelector("#name-filter");

async function loadStudents() {
    try {
        const students = await getStudents(
            nameFilter.value.trim(),
            groupFilter.value.trim(),
            dormitoryFilter.value.trim()
        );

        renderTable(undefined, students, isAdmin);
    } catch (error) {
        console.error("Ошибка при загрузке студентов:", error);

        // Если токен истёк или невалиден — вернуть на логин
        if (error.message && (error.message.includes("401") || error.message.includes("403"))) {
            localStorage.clear();
            window.location.href = "/html/login.html";
        }
    }
}

loadStudents();

filterForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    await loadStudents();
});

resetFilterButton.addEventListener("click", async () => {
    nameFilter.value = "";
    groupFilter.value = "";
    dormitoryFilter.value = "";

    await loadStudents();
});

// ==== Действия в таблице ====
const table = document.querySelector("#students-tbody");

table.addEventListener("click", async (event) => {
    const row = event.target.closest("tr");
    if (!row) return;

    const studentID = row.dataset.id;

    if (event.target.classList.contains("btn-delete")) {
        try {
            await deleteStudent(studentID);
            await loadStudents();
        } catch (error) {
            console.error("Ошибка при удалении студента:", error);
            alert(error.message);
        }
    }

    if (event.target.classList.contains("btn-edit")) {
        location.href = `/html/studentFormPage.html?id=${studentID}`;
    }
});