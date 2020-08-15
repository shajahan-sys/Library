<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>secure Page</title>
    </head>
    <body>

        <%
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Expires", "0");
        if(session.getAttribute("uname")==null){
        response.sendRedirect("login.jsp");
        }
        %>
        Hell yeah<br>
        welcome ${uname} <br>
        <a href="video.jsp">Videos here</a> <br>
        <form action="logout" method="post">
        <input type="submit" value="logout">
        </form>

    </body>
</html>