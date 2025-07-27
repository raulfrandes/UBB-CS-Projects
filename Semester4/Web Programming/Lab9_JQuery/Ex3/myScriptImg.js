$(document).ready(function() {
    const game = $('#game');
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
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }

    function setupGrid() {
        for (let i = 0; i < size * size; i++) {
            let cell = $('<div>').addClass('cell');
            let front = $('<div>').addClass('front');
            let back = $('<div>').addClass('back');
            let img = $('<img>').attr('src', images[i]).css({
                'width': '100%',
                'height': '100%'
            });
            back.append(img);

            cell.append(front);
            cell.append(back);
            cell.click(function() {
               handleCellClick(cell); 
            });
            game.append(cell);
        }
    }

    function handleCellClick(cell) {
        if (cell.hasClass('flipped') || selectedCells.length === 2) {
            return;
        }

        cell.addClass('flipped')
        selectedCells.push(cell);

        if (selectedCells.length === 2) {
            if (selectedCells[0].find('.back img').attr('src') === selectedCells[1].find('.back img').attr('src')) {
                matchesFound += 2;
                selectedCells = [];
                if (matchesFound === size * size) {
                    alert('Congratulations!');
                }
            }
            else {
                setTimeout(() => {
                    selectedCells.forEach(function(item) {
                        item.removeClass('flipped');
                    });
                    selectedCells = [];
                }, 1000);
            }
        }
    }

    setupGrid();
});