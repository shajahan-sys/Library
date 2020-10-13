<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" ; charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${edit != null}">
                Edit Author
            </c:when>
            <c:otherwise>
                Add new Author
            </c:otherwise>
        </c:choose>
    </title>
</head>
<body>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Expires", "0");
if(session.getAttribute("userLogin")==null){
response.sendRedirect("login.jsp");
} %>
<form name="myForm" action="add-edit-author" method="post">
    <c:choose>
        <c:when test="${edit != null}">
            Edit Author<br>
            Author surname : <input type="text" name="surname" value="${edit.getSurname()}"><br>
        </c:when>
        <c:otherwise>
            Add new Author<br>
            Author surname : <input type="text" name="surname"><br>
        </c:otherwise>
    </c:choose>
    <input type="submit" name="button" onclick="return validateForm()" value="save"/>
    <input type="submit" name="button" value="cancel"/>
</form>
<script>
       function validateForm() {
           var y = document.forms["myForm"]["surname"].value;
           if (y == "") {
             alert("Fill in the field!");
             return false;
           }
            if(y.length > 45){
            alert("Too long surname! Use up to 45 characters.");
            return false;
           }
         }
</script>
</body>
</html>