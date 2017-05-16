function changeNewsItem(event) {
    var button = event.currentTarget,
        form = button.nextElementSibling,
        content = button.previousElementSibling;
    button.style.display = "none";
    content.style.display = "none";
    form.style.display = "block";
    form.elements[1].focus();
}

$("div.holder").jPages({
    containerID: "itemContainer",
    perPage: 5,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "←",
    next: "→"
});