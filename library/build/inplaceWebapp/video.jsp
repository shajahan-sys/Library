<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Login Page</title>
    </head>
    <body>
        <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate")
            if(session.getAttribute("uname")==null){
            response.sendRedirect("login.jsp");
            }
            %>
            <iframe width="560" height="315" src="https://www.youtube.com/embed/OuBUUkQfBYM" frameborder="0"
            allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
       Video
    </body>
</html>