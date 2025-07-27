document.addEventListener('DOMContentLoaded', function() {
    const tables = document.querySelectorAll('table');

    tables.forEach(table => {
        const headers = table.querySelectorAll('th');
        headers.forEach(header => {
            header.addEventListener('click', function() {
                const column = Array.from(headers).indexOf(header);
                const order = header.dataset.order = -(header.dataset.order || -1);
                const rows = Array.from(table.querySelector('tbody').rows);

                rows.sort((a, b) => {
                    const aText = a.cells[column.textContent];
                    const bText = b.cells[column].textContent;

                    return aText === bText ? 0 : (aText > bText ? 1 : -1) * order;
                });

                rows.forEach(row => table.querySelector('tbody').appendChild(row));
            });
        });
    });
});