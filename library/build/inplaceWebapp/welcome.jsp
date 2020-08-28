<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Login Page</title>
    </head>
    <body>
 <%
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
        if(session.getAttribute("userLogin")==null){
        response.sendRedirect("login.jsp");
        }
        %>
    Welcome ${userLogin.getUserName()}!
     <br>
      <form action="welcome" method="post">
           <INPUT TYPE="submit" NAME = "button" VALUE="Books" />
           <INPUT TYPE="submit" NAME = "button" VALUE="Readers" />
           <INPUT TYPE="submit" NAME = "button" VALUE="Authors" />
           <INPUT TYPE="submit" NAME = "button" VALUE="Manage lending" />
            </form>
            <form action="logout" method="post"><br>
                   <input type="submit" name="button" value="logout">
            </form>
    </body>
</html>