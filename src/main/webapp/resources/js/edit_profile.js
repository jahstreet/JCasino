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
var formBirthdate = document.getElementsByName("change-birthdateForm")[0],
    bDate = formBirthdate.elements.birthdate,
    formPass = document.getElementsByName("change-password")[0],
    pwdNew1 = formPass.elements.password,
    pwdNew2 = formPass.elements.password_again;

bDate.addEventListener("change", checkBirthdate, false);
pwdNew1.addEventListener("change", checkPassword, false);
pwdNew2.addEventListener("change", checkPassword, false);

function checkBirthdate() {
    var birthdateValue = new Date(bDate.value),
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
        bDate.setCustomValidity(NOT_ENOUGH_AGE);
    } else {
        bDate.setCustomValidity('');
    }
}

function checkPassword() {
    if (pwdNew1.value != pwdNew2.value) {
        pwdNew1.setCustomValidity(PASSWORD_MISMATCH);
    } else {
        pwdNew1.setCustomValidity('');
    }
}

function validateBirthdate() {
    checkBirthdate();
    if (!formBirthdate.checkValidity()) {
        bDate.focus();
        return false;
    }
}

function validatePass() {
    checkPassword();
    if (!formPass.checkValidity()) {
        pwdNew1.focus();
        return false;
    }
}

function changeProfileItem(event) {
    var button = event.currentTarget,
        form = button.nextElementSibling,
        span = button.previousElementSibling;
    button.style.display = "none";
    span.style.display = "none";
    if (form.name != "change-password") {
        form.style.display = "inline-block";
    } else {
        form.style.display = "block";
    }
    form.elements[1].focus();
}