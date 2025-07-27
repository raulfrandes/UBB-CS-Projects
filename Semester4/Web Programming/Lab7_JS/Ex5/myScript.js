document.addEventListener('DOMContentLoaded', function() {
    const items = document.querySelectorAll('#imageList li');
    let currentIndex = 0;

    function showItem(index) {
        items.forEach(item => item.classList.remove('visible'));
        items[index].classList.add('visible');
    }

    function nextItem() {
        currentIndex = (currentIndex + 1) % items.length;
        showItem(currentIndex);
    }

    function previousItem() {
        currentIndex = (currentIndex - 1 + items.length) % items.length;
        showItem(currentIndex);
    }

    document.getElementById('next').addEventListener('click', nextItem);
    document.getElementById('previous').addEventListener('click', previousItem);

    setInterval(nextItem, 5000);
});
