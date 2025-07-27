document.addEventListener("DOMContentLoaded", () => {
    const game = document.getElementById("game");
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
            let cell = document.createElement("div");
            cell.className = 'cell';
            let front = document.createElement('div');
            front.className = 'front';
            let back = document.createElement('div');
            back.className = 'back';
            back.textContent = numbers[i];

            cell.appendChild(front);
            cell.appendChild(back);
            cell.addEventListener('click', () => handleCellClick(cell, back));
            game.appendChild(cell);
        }
    }

    function handleCellClick(cell, back) {
        if (cell.classList.contains('flipped') || selectedCells.length === 2) {
            return;
        }

        cell.classList.add('flipped');
        selectedCells.push({ cell, back });

        if (selectedCells.length === 2) {
            if (selectedCells[0].back.textContent === selectedCells[1].back.textContent) {
                matchesFound += 2;
                selectedCells = [];
                if (matchesFound === size * size) {
                    alert('Congratulations!');
                }
            } else {
                setTimeout(() => {
                    selectedCells.forEach(({ cell }) => cell.classList.remove('flipped'));
                    selectedCells = [];
                }, 1000);
            }
        }
    }

    setupGrid();
})