<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Login Page</title>
    </head>
    <body>
      <form action="login" method="post">
            Enter username : <input type="text" name="username"><br>
            Enter password : <input type="password" name="password"><br>
            <INPUT TYPE="radio" name="command" value="0" checked="true"/>Login
            <INPUT TYPE="radio" NAME="command" VALUE="1"/>Create new account<br>
            <INPUT TYPE="submit" VALUE="submit" />
            </form>

    </body>
</html>