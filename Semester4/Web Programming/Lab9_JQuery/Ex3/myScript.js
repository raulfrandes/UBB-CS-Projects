$(document).ready(function() {
    const game = $('#game');
    const size = 4;
    let numbers = generateCells(size * size);
    let selectedCells = [];
    let matchesFound = 0;

    function generateCells(count) {
        let array = [];
        for (let i = 1; i <= count / 2; i++) {
            array.push(i, i);
        }
        return array.sort(() => Math.random() - 0.5);
    }

    function setupGrid() {
        for (let i = 0; i < size * size; i++) {
            let cell = $('<div>').addClass('cell');
            let front = $('<div>').addClass('front');
            let back = $('<div>').addClass('back').text(numbers[i]);

            cell.append(front);
            cell.append(back);
            cell.click(function() {
                handleCellClick(cell, back);
            });
            game.append(cell);
        }
    }

    function handleCellClick(cell, back) {
        if (cell.hasClass('flipped') || selectedCells.length === 2) {
            return;
        }

        cell.addClass('flipped');
        selectedCells.push({ cell: cell, back: back });

        if (selectedCells.length === 2) {
            if (selectedCells[0].back.text() === selectedCells[1].back.text()) {
                matchesFound += 2;
                selectedCells = [];
                if (matchesFound === size * size) {
                    alert('Congratulations!');
                }
            }
            else {
                setTimeout(function() {
                    selectedCells.forEach(function(item) {
                        item.cell.removeClass('flipped');
                    });
                    selectedCells = [];
                }, 1000);
            }
        }
    }

    setupGrid();
});