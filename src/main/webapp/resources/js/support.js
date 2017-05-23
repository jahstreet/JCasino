$('div.holder').jPages({
    containerID: "itemContainer",
    perPage: 7,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "←",
    next: "→",
    callback: function (pages) {
        if (pages.count < 2) {
            $('div.holder')[0].style.display = "none";
        }
    }
});