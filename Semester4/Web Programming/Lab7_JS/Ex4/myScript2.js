document.addEventListener('DOMContentLoaded', function() {
    const table = document.getElementById('data-table');
    const headerCells = table.querySelectorAll('tr th');

    headerCells.forEach((header, idx) => {
        header.addEventListener('click', function() {
            const rows = Array.from(table.querySelectorAll('tr'));
            const isAscending = header.classList.toggle('asc', !header.classList.contains('asc'));
            let columns = [];
            rows.forEach(row => {
                columns.push(Array.from(row.querySelectorAll('td')));
            });
            columns = columns[0].map((_, colIndex) => columns.map(row => row[colIndex]));
            columns.sort((a, b) => {
                const valA = a[idx].textContent;
                const valB = b[idx].textContent;
                return valA.localeCompare(valB, undefined, {numeric: true}) * (isAscending ? 1 : -1);
            });
            columns.forEach((col, colIndex) => {
                col.forEach((cell, rowIndex) => {
                    rows[rowIndex].appendChild(cell);
                });
            });
        });
    });
});
