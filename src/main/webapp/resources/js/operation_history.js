$('div.holder').jPages({
    containerID: "itemContainer",
    perPage: 10,
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

$('div.transactionHolder').jPages({
    containerID: "transactionItemContainer",
    perPage: 10,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "←",
    next: "→",
    callback: function (pages) {
        if (pages.count < 2) {
            $('div.transactionHolder')[0].style.display = "none";
        }
    }
});

$('div.streakHolder').jPages({
    containerID: "streakItemContainer",
    perPage: 5,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "↑",
    next: "↓",
    callback: function (pages) {
        if (pages.count < 2) {
            var holderArray = $('div.streakHolder');
            holderArray[0].style.display = "none";
            holderArray[1].style.display = "none";
        }
    }
});