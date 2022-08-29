function validateForm() {
    let z = document.forms["myForm"]["name"].value;
    if (z === "" || z == null || z.length < 3) {
        alert("Podaj imię! Imię musi mieć co najmniej 3 litery!");
        return false;
    }
    let y = document.forms["myForm"]["familyName"].value;
    if (y === "" || y == null || y.length < 3) {
        alert("Podaj Nazwisko! Nazwisko musi mieć co najmniej 3 litery!");
        return false;
    }
    const s = document.forms["myForm"]["dead"].value;
    if (s === "" || s == null) {
        alert("Podaj datę śmierci!");
        return false;
    }
}

function update() {
    const select = document.getElementById('relatives');
    const option = select.options[select.selectedIndex];
    document.getElementById('value').value = option.value;
}
update();

function reload() {
    window.location.reload(true);
}

const fixHeight = document.querySelector('#check2');
const adjustableHeight = document.querySelector('#check');

fixHeight.addEventListener('change', adjustableHeightCheck);
adjustableHeight.addEventListener('change', adjustableHeightCheck);

function adjustableHeightCheck() {
    if (document.getElementById("check").checked) {
        document.getElementById("max-height").style.display = "block";
    } else {
        document.getElementById("max-height").style.display = "none";
    }
}

function myFunction() {
    var x = document.getElementById("myDIV");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}


