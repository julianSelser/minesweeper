import client from '/site/js/client.js'

$("#login").submit(e => {
    e.preventDefault()

    let username = $("#login .username").val()
    let password = $("#login .password").val()

    client
        .login(username, password)
        .then(success => {
            $("#login").hide()
            $("#game").show()
        })
        .catch(error => {
            $("#login .errortxt").text(error)
            $("#login .errortxt").show()
        })
})

$("#signup").submit(e => {
    e.preventDefault()

    let username = $("#signup .username").val()
    let password = $("#signup .password").val()
    let confirm = $("#signup .confirm").val()

    if(confirm != password) {
        $("#signup .errortxt").text("Passwords do not match!")
        $("#signup .errortxt").show()

        return false
    }

    client
        .signup(username, password)
        .then(success => {
            $("#signup").hide()
            $("#login").show()
            $("#login .errortxt").hide()
        })
        .catch(error => {
            $("#signup .errortxt").text(error)
            $("#signup .errortxt").show()
        })
})

$("#tosignup").click(e => {
    $("#login").hide()
    $("#signup").show()
})