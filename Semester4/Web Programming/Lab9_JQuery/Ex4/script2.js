$(document).ready(function() {
    const table = $('#data-table');
    const headerCells = table.find('th');

    headerCells.each(function(index) {
        $(this).click(function() {
            const rows = table.find('tr').toArray();
            const isAscending = $(this).toggleClass('asc').hasClass('asc');
            let columns = [];
            rows.forEach(row => {
                columns.push($(row).find('td').toArray());
            });
            columns = columns[0].map((_, colIndex) => columns.map(row => row[colIndex]));
            columns.sort((a, b) => {
                const valA = $(a[index]).text();
                const valB = $(b[index]).text();
                return valA.localeCompare(valB, undefined, {numeric: true}) * (isAscending ? 1 : -1);
            });
            columns.forEach((col, colIndex) => {
                col.forEach((cell, rowIndex) => {
                    $(rows[rowIndex]).append(cell);    
                });
            });
        });
    });
});