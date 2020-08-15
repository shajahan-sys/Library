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
            <form method="get" action="insert.jsp" enctype=text/plain>
            <INPUT TYPE="radio" name="command" value="0"/>Run<br>
            <INPUT TYPE="radio" NAME="command" VALUE="1"/>Walk
            <INPUT TYPE="submit" VALUE="submit" />
            </form>
            <input type="submit" value="login">
            </form>
    </body>
</html>