$('div.holder').jPages({
    containerID: "itemContainer",
    perPage: 6,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "←",
    next: "→",
    direction: "random",
    callback: function (pages) {
        if (pages.count < 2) {
            $('div.holder')[0].style.display = "none";
        }
    }
});