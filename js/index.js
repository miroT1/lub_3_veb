import {
    getStudents,
    deleteStudent
} from "./storageOperations.js";

import {
    renderTable
} from "./displayOperations.js";



const addButton = document.querySelector(
    "#show-add-form-bin"
);

addButton.addEventListener("click", () => {
    location.href = "/html/studentFormPage.html";
});




const filterForm = document.querySelector(
    "#filter-form"
);

const groupFilter = document.querySelector(
    "#group-filter"
);

const dormitoryFilter = document.querySelector(
    "#dormitory-filter"
);

const resetFilterButton = document.querySelector(
    "#reset-filter-button"
);




const nameFilter = document.querySelector("#name-filter");

async function loadStudents() {
    try {
        const students = await getStudents(
            nameFilter.value.trim(),
            groupFilter.value.trim(),
            dormitoryFilter.value.trim()
        );

        renderTable(undefined, students);
    } catch (error) {
        console.error("Ошибка при загрузке студентов:", error);
    }
}



loadStudents();



filterForm.addEventListener(
    "submit",
    async (event) => {
        event.preventDefault();

        await loadStudents();
    }
);



resetFilterButton.addEventListener(
    "click",
    async () => {
        nameFilter.value = "";
        groupFilter.value = "";
        dormitoryFilter.value = "";

        await loadStudents();
    }
);


const table = document.querySelector(
    "#students-tbody"
);

table.addEventListener(
    "click",
    async (event) => {
        const row = event.target.closest("tr");

        if (!row) {
            return;
        }

        const studentID = row.dataset.id;



        if (
            event.target.classList.contains(
                "btn-delete"
            )
        ) {
            try {
                await deleteStudent(studentID);

                await loadStudents();

            } catch (error) {
                console.error(
                    "Ошибка при удалении студента:",
                    error
                );
            }
        }



        if (
            event.target.classList.contains(
                "btn-edit"
            )
        ) {
            location.href =
                `/html/studentFormPage.html?id=${studentID}`;
        }
    }
);