jQuery('<div class="quantity-nav"><div class="quantity-button quantity-up">+</div><div class="quantity-button quantity-down">-</div></div>').insertAfter('.quantity input');
jQuery('.quantity').each(function() {
    var spinner = jQuery(this),
        input = spinner.find('input[type="number"]'),
        btnUp = spinner.find('.quantity-up'),
        btnDown = spinner.find('.quantity-down'),
        min = input.attr('min'),
        max = input.attr('max'),
        step = input.attr('step');

    btnUp.click(function() {
        var oldValue = parseFloat(input.val());
        var newVal;
        if (oldValue >= max) {
            newVal = min;
        } else {
            newVal = oldValue + Number(step);
        }
        spinner.find("input").val(newVal);
        spinner.find("input").trigger("change");
    });

    btnDown.click(function() {
        var oldValue = parseFloat(input.val());
        var newVal;
        if (oldValue <= min) {
            newVal = max;
        } else {
            newVal = oldValue - Number(step);
        }
        spinner.find("input").val(newVal);
        spinner.find("input").trigger("change");
    });

});