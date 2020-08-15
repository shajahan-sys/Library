<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage = "true" %>
<html>
    <head>
        <title>Error</title>
    </head>
    <body bgcolor = 'red'>
        <h2>Error occurred, sorry :(</h2>
        <%= exception.getMessage() %>
    </body>
</html>