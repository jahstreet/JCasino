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

$(function () {
    var range = $('#slider-range-withdrawal'),
        input = $('#month-withdrawal');
    range.slider({
        range: true,
        min: 0,
        max: 1000,
        values: [50, 300],
        slide: function (event, ui) {
            input.val(ui.values[0] + " ÷ " + ui.values[1]);
        }
    });
    input.val(range.slider("values", 0) +
        " ÷ " + range.slider("values", 1));
});

$(function () {
    var range = $('#slider-range-balance'),
        input = $('#balance');
    range.slider({
        range: true,
        min: 0,
        max: 20000,
        values: [1000, 10000],
        slide: function (event, ui) {
            input.val(ui.values[0] + " ÷ " + ui.values[1]);
        }
    });
    input.val(range.slider("values", 0) +
        " ÷ " + range.slider("values", 1));
});