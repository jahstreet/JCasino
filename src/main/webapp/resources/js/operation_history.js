$("div.holder").jPages({
    containerID: "itemContainer",
    perPage: 10,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "←",
    next: "→"
});

$("div.transactionHolder").jPages({
    containerID: "transactionItemContainer",
    perPage: 10,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "←",
    next: "→"
});

$("div.streakHolder").jPages({
    containerID: "streakItemContainer",
    perPage: 5,
    startPage: 1,
    startRange: 1,
    midRange: 5,
    endRange: 1,
    previous: "↑",
    next: "↓"
});