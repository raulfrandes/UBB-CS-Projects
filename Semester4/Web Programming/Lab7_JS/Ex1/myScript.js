function moveItem(id){
    let lists = document.getElementsByTagName("select");
    let item = document.getElementById(id);

    if (item.parentNode.id === "list1"){
        item.parentNode.removeChild(item);
        lists[1].appendChild(item);
    }
    else {
        item.parentNode.removeChild(item);
        lists[0].appendChild(item);
    }
}