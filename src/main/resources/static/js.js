function over1() {
    document.getElementById("b1").style.backgroundColor = "#e74444"
}

function out1() {
    document.getElementById("b1").style.backgroundColor = "#a9a9a9"
}

function over11() {
    document.getElementById("b2").style.backgroundColor = "#e74444"
}

function out11() {
    document.getElementById("b2").style.backgroundColor = "#a9a9a9"
}

function over33() {
    document.getElementById("b3").style.backgroundColor = "#e74444"
}

function out33() {
    document.getElementById("b3").style.backgroundColor = "#a9a9a9"
}

function over44() {
    document.getElementById("b4").style.backgroundColor = "#e74444"
}

function out44() {
    document.getElementById("b4").style.backgroundColor = "#a9a9a9"
}

function over2(el) {
    document.getElementById(el).style.backgroundColor = "#e74444"
}

function out2(el) {
    document.getElementById(el).style.backgroundColor = "#a9a9a9"
}

function over3(el) {
    document.getElementById(el).style.backgroundColor = "#264bba"
}

function confirm_delete() {
    return confirm('Are you sure? The item will be completed and deleted');
}

function imageValidation() {
    const imageSize = Math.round(document.getElementById('check').files.item(0).size / 1024);
    const imageType = document.getElementById('check').files.item(0).type;

    const name = document.getElementById('check').files.item(0).name
    const res = name.split(".");

    if (res.length > 2) {
        document.getElementById('check').value = null;
        alert('Wrong fie name. Can\'t be more than 1 dot')
    } else if (imageSize >= 5120) {
        alert("Size of file is " + Math.round(imageSize / 1024 * 100) / 100 + " MB. Please select a file less than 5mb");
        document.getElementById('check').value = null;
    }
    else if (imageType !== 'image/jpeg' && imageType !== 'image/jpg' && imageType !== 'image/png' && imageType !== 'image/gif') {
        alert('Your file is \'' + imageType + '\'. Ony images are allowed')
        document.getElementById('check').value = null;
    }
}

