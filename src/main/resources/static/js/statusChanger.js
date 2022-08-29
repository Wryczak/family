function show(buttonId,divId,partnerId,val,a,b,d) {
    let hideDiv2 = document.getElementById(buttonId);
    let setStatus2 = false;
    let c = document.getElementById(divId);
    const div1 = document.getElementById(a);
    const div2 = document.getElementById(b);
    const div3 = document.getElementById(d);
    hideDiv2.onclick = function () {
        document.getElementById(partnerId).value = val;
        setStatus2 = !setStatus2;
        if (setStatus2 === true) {
            c.style.display = "block";
            div1.style.display = "none";
            div2.style.display = "none";
            div3.style.display = "none";
        } else {
            c.style.display = "none";
            div1.style.display = "block";
            div2.style.display = "block";
            div3.style.display = "block";
        }
        return false;
    }
}
function selectOnlyThis(id,name){
    const myCheckbox = document.getElementsByName(name);
    Array.prototype.forEach.call(myCheckbox,function(el){
        el.checked = false;
    });
    id.checked = true;
}
function enable(id,div) {
    document.getElementById(div).style.display = "block";
    document.getElementById(id).disabled = false;
}
function disable(id,div) {
    document.getElementById(div).style.display = "none";
    document.getElementById(id).disabled = true;
    document.getElementById(id).value = '';
}

function setTime(birth,dead) {
    const start = document.getElementById(birth);
    const end = document.getElementById(dead);

    start.addEventListener('change', function () {
        if (start.value)
            end.min = start.value;
    }, false);
    end.addEventListener('change', function () {
        if (end.value)
            start.max = end.value;
    }, false);
}
