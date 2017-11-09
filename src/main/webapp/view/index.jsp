<link rel="stylesheet" href="/styles/login_form.css">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<style>
    body {
        background: url('http://www.wallpaperup.com/uploads/wallpapers/2017/05/06/1089109/5ec08a98f5cd10e2af8329308185e23d.jpg') no-repeat center center fixed;
        -webkit-background-size: cover;
        -moz-background-size: cover;
        -o-background-size: cover;
        background-size: cover;
    }
</style>
<div class="jumbotron" style="margin-top: 20px">
    <div class="container" style="text-align: center">
        <h1>Welcome</h1>
        <p>We are happy to announce that our online <i>NOTES</i> application is online </p>
        <a href="#" class="btn btn-lg btn-success" data-toggle="modal" data-target="#login-modal">Login</a>
        <a href="#" class="btn btn-lg btn-warning" data-toggle="modal" data-target="#register-modal">Register</a>
    </div>
</div>

<div class="modal fade" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     style="display: none;">
    <div class="modal-dialog">
        <div class="loginmodal-container">
            <h1>Login to Your Account</h1><br>
            <div class="alert alert-danger" style="display: none" id="auth_form_err"></div>
            <form method="post" id="auth_form" action="#">
                <input type="text" required name="user" placeholder="Username">
                <input type="password" required name="password" placeholder="Password">
                <input type="submit" name="login" id="auth_submit_button" class="login loginmodal-submit" value="Login">
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="register-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="loginmodal-container">
            <h1>Register Your Account</h1><br>
            <div class="alert alert-danger" style="display: none" id="reg_form_err"></div>

            <form method="post" id="reg_form" action="#">
                <input type="text" name="username" placeholder="*Username">
                <input type="text" name="email" placeholder="*Email">
                <input type="password" name="password" placeholder="*Password">
                <input type="submit" name="login" id="register_submit_button" class="login loginmodal-submit"
                       value="Login">
            </form>

            <div class="login-help">
                <b>* - mandatory field</b>
            </div>
        </div>
    </div>
</div>
<script src="/styles/js/index.js"></script>