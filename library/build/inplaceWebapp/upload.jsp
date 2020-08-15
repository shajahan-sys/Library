<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Upload Page</title>
    </head>
    <body>
        <form action="upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" multiple><br>
        <input type="submit">
        </form>
    </body>
</html>