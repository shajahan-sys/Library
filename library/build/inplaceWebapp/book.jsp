<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Books</title>
    </head>
    <body>
    <div align="center">
            <table border="1" cellpadding="5">
                <caption><h2>List of users</h2></caption>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                </tr>
                <c:forEach var="book" items="${books}">
                    <tr>
                        <td><c:out value="${book.getTitle()}" /></td>
                        <td><c:out value="${book.getAuthor().getSurname()}" /></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</html