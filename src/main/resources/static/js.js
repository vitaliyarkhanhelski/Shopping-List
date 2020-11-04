function over1() {
    document.getElementById("b1").style.backgroundColor = "#e74444"
}

function out1() {
    document.getElementById("b1").style.backgroundColor = "white"
}

function over11() {
    document.getElementById("b2").style.backgroundColor = "#e74444"
}

function out11() {
    document.getElementById("b2").style.backgroundColor = "#fafafa"
}

function over2(el) {
    document.getElementById(el).style.backgroundColor = "#e74444"
}

function out2(el) {
    document.getElementById(el).style.backgroundColor = "white"
}

function over3(el) {
    document.getElementById(el).style.backgroundColor = "#264bba"
}

// function reload() {
//     $("#form").submit();
// }

function confirm_delete() {
    return confirm('Are you sure? The item will be completed and deleted');
}
