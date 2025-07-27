document.addEventListener("DOMContentLoaded", () => {
    const game = document.getElementById("game");
    const size = 4;
    let images = [
        'img1.png', 'img2.png', 'img3.png', 'img4.png', 'img5.png', 'img6.png', 'img7.png', 'img8.png',
        'img1.png', 'img2.png', 'img3.png', 'img4.png', 'img5.png', 'img6.png', 'img7.png', 'img8.png'
    ]
    images = shuffle(images);
    let selectedCells = [];
    let matchesFound = 0;

    function shuffle(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]]
        }
        return array;
    }

    function setupGrid() {
        for (let i = 0; i < size * size; i++) {
            let cell = document.createElement("div");
            cell.className = 'cell';
            let front = document.createElement('div');
            front.className = 'front';
            let back = document.createElement('div');
            back.className = 'back';
            let img = document.createElement('img');
            img.src = images[i];
            img.style.width = '100%';
            img.style.height = '100%';
            back.appendChild(img);

            cell.appendChild(front);
            cell.appendChild(back);
            cell.addEventListener('click', () => handleCellClick(cell));
            game.appendChild(cell);
        }
    }

    function handleCellClick(cell) {
        if (cell.classList.contains('flipped') || selectedCells.length === 2) {
            return;
        }

        cell.classList.add('flipped');
        selectedCells.push(cell);

        if (selectedCells.length === 2) {
            if (selectedCells[0].children[1].children[0].src === selectedCells[1].children[1].children[0].src) {
                matchesFound += 2;
                selectedCells = [];
                if (matchesFound === size * size) {
                    alert('Congratulations!');
                }
            } else {
                setTimeout(() => {
                    selectedCells.forEach(c => c.classList.remove('flipped'));
                    selectedCells = [];
                }, 1000);
            }
        }
    }

    setupGrid();
})