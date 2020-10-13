<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" ; charset="UTF-8">
    <title>Return Book</title>
</head>
<body>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0");
if(session.getAttribute("userLogin")==null){
response.sendRedirect("login.jsp");
}%>
<form action="return-book" method="get">
    <% boolean isSelected = false;
    if(request.getAttribute("selectedReader") != null)
    {isSelected = true;}%>
    Reader : <select name="selReader">
    <option value="no reader">select reader</option>
    <c:forEach var="reader" items="${activeReaders}">
        <option value="${reader.getId()}">
            <%if(isSelected){%>
            ${reader.getId() == selectedReader.getId() ? 'selected="selected"' : ''}<%}%>
            ${reader.getSurname()} ${reader.getName()}
        </option>
    </c:forEach>
</select>
    <input type="submit" name="button" value="submit">
</form>
<%if(isSelected){%>
<div align="left">
    <form action="return-book" method="post">
        <table border="1" cellpadding="5">
            <tr>
                <th>Title</th>
                <th>Return Date</th>
                <th>Returned</th>
            </tr>
            <c:forEach var="lending" items="${lendings}">
                <tr>
                    <c:choose>
                        <c:when test="${lendings.size() == 1}">
                            <td>
                                <c:out value="${lendings.iterator().next().getBook().getTitle()}"/>
                            </td>
                            <td>
                                <c:out value="${lendings.iterator().next().getReturnDate()}"/>
                            </td>
                            <td><input type="radio" name="selected" value="${lendings.iterator().next().getId()}"
                                       required/></td>
                        </c:when>
                        <c:otherwise>
                            <td>
                                <c:out value="${lending.getBook().getTitle()}"/>
                            </td>
                            <td>
                                <c:out value="${lending.getReturnDate()}"/>
                            </td>
                            <td><input type="radio" name="selected" value="${lending.getId()}" required/></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
        </table>
        <INPUT TYPE="submit" name="button" VALUE="return">
    </form>
    <%} else{%>
    Select reader that wants to return book
    <%}%>
    <form action="return-book" method="post">
        <INPUT TYPE="submit" name="button" VALUE="cancel">
    </form>
</div>
</body>
</html>