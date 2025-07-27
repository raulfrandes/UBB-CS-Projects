function validate() {
    let errorMessage = "";

    // name validation
    let name = document.getElementById("name");

    let regex = /^[A-Za-z\s]+$/;
    if (name.value.trim() === "") { 
        errorMessage += "Please enter a name\n";
        name.style.borderColor = "red";
    }
    else if (!regex.test(name.value)) {
        errorMessage += "Invalid name\n" 
    }
    else {
        name.style.borderColor = "green";
    }

    // date validation
    let date = document.getElementById("date");

    let dateType = new Date(date.value);
    let currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0);

    if (!date.value) {
        errorMessage += "Please enter a date\n";
        date.style.borderColor = "red"
    }
    else if (dateType > currentDate) {
        errorMessage += "Invalid birth\n";
        date.style.borderColor = "red"
    }
    else {
        date.style.borderColor = "green"
    }

    // age validation
    let age = document.getElementById("age");

    if (isNaN(age.value) || age.value < 1 || age.value > 99) {
        errorMessage += "Invalid age\n";
        age.style.borderColor = "red"
    }
    else {
        age.style.borderColor = "green"
    }

    // email validation
    let email = document.getElementById("email");

    regex = /^[^@]+@[^@]+\.[^@.]+$/;
    if (email.value.trim() === "") {
        errorMessage += "Please enter an email\n";
        email.style.borderColor = "red"
    }
    else if (!regex.test(email.value)) {
        errorMessage += "Invalid email\n"
        email.style.borderColor = "red"
    }

    if (errorMessage === "") {
        alert("Form submited");
    }
    else {
        alert(errorMessage);
    }
}

function findAge() {
    let date = new Date(document.getElementById("date").value);
    let currentDate = new Date();

    let age = currentDate.getFullYear() - date.getFullYear();
    let months = currentDate.getMonth() - date.getMonth();

    if (months < 0 || (months === 0 && currentDate.getDate() < date.getDate())) {
        age--;
    }

    document.getElementById("age").value = age;
}