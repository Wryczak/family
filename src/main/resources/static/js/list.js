$("#birthplace").select2({
    theme: "bootstrap4",
    ajax: {
        url: '/list',
        dataType: 'json',
        delay: 250,
        processResults: function (response) {
            return {
                results: response
            };
        },
        cache: true
    }
});

window.addEventListener("load", function () {
    document.getElementById("check").addEventListener("change", function () {
        document.getElementById("text").disabled = !this.checked;
        // document.getElementById("check2").disabled = this.checked;
    });
});