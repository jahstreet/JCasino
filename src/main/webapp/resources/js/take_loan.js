//noinspection JSUnresolvedVariable
var form = document.takeLoan,
    input = form.elements.amount,
    INVALID_AMOUNT;
if (locale == "en_US") {
    INVALID_AMOUNT = "You can't take loan more than " + maxLoan;
} else {
    INVALID_AMOUNT = "Вы не можете взять кредит на сумму более " + maxLoan;
}
input.addEventListener("change", checkAmount, false);

function validateLoan() {
    checkAmount();
    if (!form.checkValidity()) {
        input.focus();
        return false;
    }
    return true;
}

function checkAmount() {
    var amount = input.value;
    if (amount > maxLoan) {
        input.setCustomValidity(INVALID_AMOUNT);
    } else {
        input.setCustomValidity('');
    }
}