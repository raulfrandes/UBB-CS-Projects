$(document).ready(function() {
    const $items = $('#imageList li');
    let currentIndex = 0;
    let intervalId;

    function showItem(index) {
        $items.removeClass('visible');
        $items.eq(index).addClass('visible');
    }

    function nextItem() {
        currentIndex = (currentIndex + 1) % $items.length;
        showItem(currentIndex);
        resetInterval();
    }

    function previousItem() {
        currentIndex = (currentIndex - 1 + $items.length) % $items.length;
        showItem(currentIndex);
        resetInterval();
    }

    $('#next').on('click', function() {
        nextItem();
    });

    $('#previous').on('click', function() {
        previousItem();
    });

    function resetInterval() {
        clearInterval(intervalId);
        intervalId = setInterval(nextItem, 5000);
    }

    resetInterval();
});
