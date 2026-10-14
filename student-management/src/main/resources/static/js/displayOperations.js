function renderTable(
    table = document.querySelector("tbody"),
    students = [],
    isAdmin = false
) {
    table.innerHTML = "";

    for (let i = 0; i < students.length; i++) {
        const actions = isAdmin
            ? `<button class="btn-edit">Изменить</button>
               <button class="btn-delete">Удалить</button>`
            : `<span class="read-only">только чтение</span>`;

        const template = `
            <tr data-id="${students[i].id}">
                <td>
                    <a href="/html/infoPage.html?id=${students[i].id}">
                        ${students[i].fullName}
                    </a>
                </td>
                <td>${students[i].group}</td>
                <td>${students[i].isu}</td>
                <td>${actions}</td>
            </tr>
        `;

        table.innerHTML += template;
    }
}

export { renderTable };