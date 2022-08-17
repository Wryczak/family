function show_1() {
    let setStatus = false;
    let hideDiv = document.getElementById("myStatus");
    let a = document.getElementById("myDIV");
    hideDiv.onclick = function () {
        document.getElementById('father').value = '1';
        setStatus = !setStatus;
        if (setStatus === true) {
            a.style.display = "block";
        } else {
            a.style.display = "none";
        }
        return false;
    }
}
function show_2() {
    let hideDiv1 = document.getElementById("myStatus1");
    let setStatus1 = false;
    let b = document.getElementById("myDIV1");
    hideDiv1.onclick = function () {
        document.getElementById('mother').value = '2';
        setStatus1 = !setStatus1;
        if (setStatus1 === true) {
            b.style.display = "block";
        } else {
            b.style.display = "none";
        }
        return false;
    }
}

function show_3() {
    let hideDiv2 = document.getElementById("myStatus2");
    let setStatus2 = true;
    let c = document.getElementById("myDIV2");
    hideDiv2.onclick = function () {
        document.getElementById('husband').value = '5';
        setStatus2 = !setStatus2;
        if (setStatus2 === true) {
            c.style.display = "block";
        } else {
            c.style.display = "none";
        }
        return false;
    }
}

    function show_4() {
        let hideDiv3 = document.getElementById("myStatus3");
        let setStatus3 = false;
        let d = document.getElementById("myDIV3");
        hideDiv3.onclick = function () {
            document.getElementById('wife').value = '5';
            setStatus3 = !setStatus3;
            if (setStatus3 === true) {
                d.style.display = "block";
            } else {
                d.style.display = "none";
            }
            return false;
        }
    }

    function show_5() {
        let hideDiv4 = document.getElementById("myStatus4");
        let setStatus4 = false;
        let e = document.getElementById("myDIV4");
        hideDiv4.onclick = function () {
            setStatus4 = !setStatus4;
            if (setStatus4 === true) {
                e.style.display = "block";
            } else {
                e.style.display = "none";
            }
            return false;
        }
    }






