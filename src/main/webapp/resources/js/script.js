// FOOTER-GO_TO_TOP-BUTTON
let mybutton = document.getElementById("btn-back-to-top");

// When the user clicks on the button, scroll to the top of the document
mybutton.addEventListener("click", backToTop);

function backToTop() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}

//Alert when Admin delete one Food
function deleteSuccess(){
    $(document).ajaxStop(function() {
        setInterval(function() {
            location.reload();
        }, 3000);
    });
}

function deleteFailure(){
    alert("Xóa không thành công");
}

