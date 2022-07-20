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
    const x = document.forms["myForm"]["dt"].value;
    if (x === "" || x == null) {
        alert("Podaj datę urodzin!");
        return false;
    }
}

function reload() {
    window.location.reload(true);
}
