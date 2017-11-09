// Create a "close" button and append it to each list item
var myNodelist = document.getElementsByTagName("LI");
var i;
for (i = 0; i < myNodelist.length; i++) {
    var span = document.createElement("SPAN");
    var txt = document.createTextNode("\u00D7");
    span.className = "close";
    span.appendChild(txt);
    myNodelist[i].appendChild(span);
}

// Click on a close button to hide the current list item
var close = document.getElementsByClassName("close");
var i;
for (i = 0; i < close.length; i++) {
    close[i].onclick = function () {
        var div = this.parentElement;
        div.style.display = "none";
        var datavalue = div.getAttribute("data-value");
        var form = new FormData();
        form.append("_d", datavalue);
        form.append("_a", "delete");
        sendAction(form);
    }
}

// Add a "checked" symbol when clicking on a list item
var list = document.querySelector('ul');
list.addEventListener('click', function (ev) {
    if (ev.target.tagName === 'LI') {
        ev.target.classList.toggle('checked');
        var elem = ev.srcElement;
        var datavalue = elem.getAttribute("data-value");

        var form = new FormData();
        form.append("_d", datavalue);
        form.append("_a", "done");
        sendAction(form);
    }
}, false);

// Create a new list item when clicking on the "Add" button
function newElement() {
    var li = document.createElement("li");
    var inputValue = document.getElementById("myInput").value;
    var t = document.createTextNode(inputValue);
    li.appendChild(t);
    if (inputValue === '') {
        alert("You must write something!");
    } else {
        var form = new FormData();
        form.append("title", inputValue);
        form.append("_a", "create");
        sendAction(form, li);
        document.getElementById("myUL").appendChild(li);
    }
    document.getElementById("myInput").value = "";

    var span = document.createElement("SPAN");
    var txt = document.createTextNode("\u00D7");
    span.className = "close";
    span.appendChild(txt);
    li.appendChild(span);

    for (i = 0; i < close.length; i++) {
        close[i].onclick = function () {
            var div = this.parentElement;
            div.style.display = "none";
        }
    }
}


function sendAction(data, element) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var parsedResp = JSON.parse(this.response);

                element.setAttribute("data-value", parsedResp.id);
            } else {
                alertDiv.innerHTML = "<b>Oopss...</b>Invalid user or password.";
                alertDiv.style.display = "block";
            }

        }
    };
    xhttp.open("POST", "/home", true);
    xhttp.send(data);

}

