// Khi nội dung file html đã được hiển thị trên browser thì sẽ kích hoạt
$(document).ready(function () {

    // Đăng ký sự kiện click cho thẻ tag được chỉ định bên HTML
    $("#btn-sign-in").click(function () {
        // .val() : Lấy giá trị của thẻ input được chỉ định
        var email = $("#email").val()
        var password = $("#password").val()

        // Xuất giá trị ra trên tab console trên trình duyệt
        console.log("email : ", email, " password : ", password);

        //ajax : Dùng để call ngầm API mà không cần trình duyệt
        //axios, fetch
        //data : chỉ có khi tham số truyền ngầm
        $.ajax({
            url: "http://localhost:8080/index/login",
            method: "post",
            data: {
                email: email,
                password: password
            }
        }).done(function (data) {
            if (data && data.statusCode == 200) {
                localStorage.setItem("token", data.data)
                if (data.message == "[ROLE_ADMIN]" || data.message == "[ROLE_STAFF]") {
                window.location = "../staff/view";
                }else if(data.message == "[ROLE_NULL]" ){
                    window.alert("Your Account Has Been Disable");
                    window.location = "logout";
                }else{
                    window.location = "home";
                }

            }

            console.log("server tra ve ", data)

        }).fail(function (data) {
            alert("Email or Password are wrong!");
        })

    })

    //Xử lý đăng ký

    $("#btn-sign-up").click(function () {

        var fullname = $("#fullName").val()
        var username = $("#userName").val()
        var password = $("#password").val()
        var repassword = $("#passwordConfirm").val()
        var email = $("#email").val()
        var result = (password === repassword);


        console.log("username : ", username, " password : ", password, " repassword : ", repassword, "email : ", email, " fullname : ", fullname);
        if (result) {
            $.ajax({
                url: "http://localhost:8080/index/sign-up-add",
                method: "post",
                contentType: "application/json",
                data: JSON.stringify({
                    fullName: fullname,
                    userName: username,
                    password: password,
                    email: email
                })
            }).done(function (data) {
                console.log("server tra ve ", data)
            })
        }else{
        }

    })

    $("#btn-check").click(function () {


        var email = $("#email").val()
        console.log( "email : ", email,);
        $.ajax({
            url: "http://localhost:8080/index/send",
            method: "post",
            data: {
                email: email
            }
        }).done(function (data) {
            console.log("server tra ve ", data)
            alert("dã gửi otp vè địa chỉ mail của bạn");
            window.location = "home";
        }).fail(function (data) {
            alert(" email chưa đăng ký tài khoản");
        })


    })


})