$(document).ready(function() {
    const headers = $('th');
    headers.each(function() {
        $(this).click(function() {
            const column = headers.index(this);
            let order = $(this).data('order') || 'asc';
            order = order === 'asc' ? 'desc' : 'asc';
            $(this).data('order', order);
            const rows = $('tbody').find('tr');

            rows.sort(function(a, b) {
                const aText = $(a).find('td').eq(column).text();
                const bText = $(b).find('td').eq(column).text();

                let comparison = aText.localeCompare(bText);
                return order === 'asc' ? comparison : -comparison;
            });

            rows.each(function() {
                $('tbody').append(this);
            });
        });
    });
});