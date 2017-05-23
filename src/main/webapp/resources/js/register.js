var PASSWORD_MISMATCH,
    NOT_ENOUGH_AGE;

if (locale == "en_US") {
    PASSWORD_MISMATCH = "Passwords doesn't match";
    NOT_ENOUGH_AGE = "You are less then 18 years old";
} else {
    PASSWORD_MISMATCH = "Пароли не совпадают";
    NOT_ENOUGH_AGE = "Вам нет 18 лет";
}

//noinspection JSUnresolvedVariable
var form = document.getElementsByName("registerForm")[0],
    pwd1 = form.elements.password,
    pwd2 = form.elements.password_again,
    birthdateForm = form.elements.birthdate;

pwd1.addEventListener("change", checkPassword, false);
pwd2.addEventListener("change", checkPassword, false);
birthdateForm.addEventListener("change", checkBirthdate, false);

function checkPassword() {
    if (pwd1.value != pwd2.value) {
        pwd1.setCustomValidity(PASSWORD_MISMATCH);
    } else {
        pwd1.setCustomValidity('');
    }
}

function checkBirthdate() {
    var birthdateValue = new Date(birthdateForm.value),
        year = birthdateValue.getFullYear(),
        month = birthdateValue.getMonth(),
        day = birthdateValue.getDate(),
        curDate = new Date(),
        curYear = curDate.getFullYear(),
        curMonth = curDate.getMonth(),
        curDay = curDate.getDate(),
        delYear = curYear - year,
        delMonth = curMonth - month,
        delDay = curDay - day;
    if (!(delYear > 18
        || delYear == 18 && delMonth > 0
        || delYear == 18 && delMonth == 0 && delDay >= 0)) {
        birthdateForm.setCustomValidity(NOT_ENOUGH_AGE);
    } else {
        birthdateForm.setCustomValidity('');
    }
}

function validateRegister() {
    checkPassword();
    if (!form.checkValidity()) {
        pwd1.focus();
        return false;
    }
    checkBirthdate();
    if (!form.checkValidity()) {
        birthdateForm.focus();
        return false;
    }
}