<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title><c:choose>
          <c:when test="${edit != null}">
             Edit Reader
           </c:when>
          <c:otherwise>
          Add new Reader
           </c:otherwise>
       </c:choose></title>
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
             <form name="myForm" action="add-edit-reader" method="post">
                <c:choose>
                  <c:when test="${edit != null}">
                     Edit Reader<br>
                      Reader name : <input type="text" name="name" value="${edit.getName()}"><br>
                      Reader surname : <input type="text" name="surname" value="${edit.getSurname()}"><br>
                  </c:when>
                 <c:otherwise>
                  Add new Reader<br>
                   Reader name : <input type="text" name="name"><br>
                   Reader surname : <input type="text" name="surname"><br>
                </c:otherwise>
                 </c:choose>
                 <input type="submit" name="button" onclick="return validateForm()" value="save"/>
                  <input type="submit" name="button" value="cancel"/>
               </form>
             <script>
              function validateForm() {
              var x = document.forms["myForm"]["name"].value;
              var y = document.forms["myForm"]["surname"].value;
                  if (x == "" || y == "") {
                    alert("Fill in all fields!");
                    return false;
                  }
                  if(x.length > 45 || y.length > 45){
                  alert("Too long name or surname! Use up to 45 characters for each.");
                  return false;
                 }
                }
            </script>
       </body>
  </html