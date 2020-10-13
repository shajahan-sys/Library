<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
    <title>Menu</title>
</head>
<body>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0");
if(session.getAttribute("userLogin")==null){
response.sendRedirect("login");
}%>
Welcome ${user}!
<br>
<form action="menu" method="post">
    <INPUT TYPE="submit" NAME="button" VALUE="books">
    <INPUT TYPE="submit" NAME="button" VALUE="readers">
    <INPUT TYPE="submit" NAME="button" VALUE="authors">
    <INPUT TYPE="submit" NAME="button" VALUE="lend book">
    <INPUT TYPE="submit" NAME="button" VALUE="return book">
</form>
<form action="logout" method="post"><br>
    <input type="submit" name="button" value="logout">
</form>
</body>
</html>