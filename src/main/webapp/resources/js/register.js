var PASSWORD_MISMATCH,
    NOT_ENOUGH_AGE;

if (locale == "en_US") {
    PASSWORD_MISMATCH = "Passwords doesn't match";
    NOT_ENOUGH_AGE = "You are less then 18 years old";
} else {
    PASSWORD_MISMATCH = "Пароли не совпадают";
    NOT_ENOUGH_AGE = "Вам нет 18 лет";
}

var form = document.getElementsByName("registerForm")[0],
    pwd1 = form.elements.password,
    pwd2 = form.elements.password_again,
    bdate = form.elements.birthdate;

pwd1.addEventListener("change", checkPassword, false);
pwd2.addEventListener("change", checkPassword, false);
bdate.addEventListener("change", checkBdate, false);

function checkPassword() {
    if (pwd1.value != pwd2.value) {
        pwd1.setCustomValidity(PASSWORD_MISMATCH);
    } else {
        pwd1.setCustomValidity('');
    }
}

function checkBdate() {
    var bdateValue = new Date(bdate.value),
        year = bdateValue.getFullYear(),
        month = bdateValue.getMonth(),
        day = bdateValue.getDate(),
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
        bdate.setCustomValidity(NOT_ENOUGH_AGE);
    } else {
        bdate.setCustomValidity('');
    }
}

function validateRegister() {
    checkPassword();
    if (!form.checkValidity()) {
        pwd1.focus();
        return false;
    }
    checkBdate();
    if (!form.checkValidity()) {
        bdate.focus();
        return false;
    }
}