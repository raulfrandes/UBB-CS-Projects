$(document).ready(function(){
    $('button').click(function() {
        let errorMessage = "";

        // name validation
        let name = $('#name')

        let regex = /^[A-Za-z\s]+$/;
        if (name.val().trim() === "") {
            errorMessage += 'Please enter a name\n';
            name.css('borderColor', 'red');
        }
        else if (!regex.test(name.val())) {
            errorMessage += 'Invalid name\n';
        }
        else {
            name.css('borderColor', 'green');
        }

        // date validation
        let date = $('#birth');

        let dateType = new Date(date.val());
        let currentDate = new Date();
        currentDate.setHours(0, 0, 0, 0);

        if (!date.val()) {
            errorMessage += 'Please enter a date\n';
            date.css('border-color', 'red');
        }
        else if (dateType > currentDate) {
            errorMessage += 'Invalid birth\n';
            date.css('border-color', 'red');
        }
        else {
            date.css('border-color', 'green');
        }

        // age validation
        let age = $('#age');

        if (isNaN(age.val()) || age.val() < 1 || age.val() > 99) {
            errorMessage += 'Invalid age\n';
            age.css('border-color', 'red');
        }
        else {
            age.css('border-color', 'green');
        }

        // email validation
        let email = $('#email');

        regex = /^[^@]+@[^@]+\.[^@.]+$/;
        if (email.val().trim() === "") {
            errorMessage += 'Please enter an email\n';
            email.css('border-color', 'red');
        }
        else if (!regex.test(email.val())) {
            errorMessage += 'Invalid email\n';
            email.css('border-color', 'red');
        }
        else {
            email.css('border-color', 'green');
        }

        if (errorMessage === "") {
            alert('Form submitted');
        }
        else {
            alert(errorMessage);
        }
    });

    $('#birth').change(function() {
        let date = new Date($('#birth').val());
        let currentDate = new Date();

        let age = currentDate.getFullYear() - date.getFullYear();
        let months = currentDate.getMonth() - date.getMonth();

        if (months < 0 || (months === 0 && currentDate.getDate() < date.getDate())) {
            age--;
        }

        $('#age').val(age);
    });
});