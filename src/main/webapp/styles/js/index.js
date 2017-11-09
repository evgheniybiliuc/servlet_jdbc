document.getElementById("register_submit_button").addEventListener("click", function (event) {
    event.preventDefault();

    var xhttp = new XMLHttpRequest();
    var form = document.getElementById("reg_form");
    var alertDiv = document.getElementById("reg_form_err");
    var formData = new FormData(form);
    alertDiv.style.display = "none";
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                console.log("Register response");
            } else {

                alertDiv.innerHTML = "<b>Oopss...</b>Something get wrong please retry.";
                alertDiv.style.display = "block";
            }

        }
    };

    xhttp.open("POST", "/register", true);
    xhttp.send(formData);
});

document.getElementById("auth_submit_button").addEventListener("click", function (event) {
    event.preventDefault();
    console.log("form has been send");
    var xhttp = new XMLHttpRequest();
    var form = document.getElementById("auth_form");
    var alertDiv = document.getElementById("auth_form_err");
    var formData = new FormData(form);
    alertDiv.style.display = "none";
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                window.location.href = "/home";
                console.log("redirect to home");
            } else {
                alertDiv.innerHTML = "<b>Oopss...</b>Invalid user or password.";
                alertDiv.style.display = "block";
            }

        }
    };

    xhttp.open("POST", "/auth", true);
    xhttp.send(formData);
});


