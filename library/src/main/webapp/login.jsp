<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Login Page</title>
    </head>
    <body>
<form name="myForm" action="login" onsubmit="return validateForm()" method="post" required>
            Enter username : <input type="text" name="username"><br>
            Enter password : <input type="password" name="password"><br>
            <INPUT TYPE="radio" name="command" value="login" checked="true"/>Login
            <INPUT TYPE="radio" NAME="command" VALUE="create"/>Create new account<br>
            <INPUT TYPE="submit" VALUE="submit" />
            </form>
            <script>
            function validateForm() {
              var x = document.forms["myForm"]["username"].value;
              var y = document.forms["myForm"]["password"].value;
              if (x == "" || y == "") {
                alert("Both fields must be filled out");
                return false;
              }
            }
    </script>
    </body>
</html>