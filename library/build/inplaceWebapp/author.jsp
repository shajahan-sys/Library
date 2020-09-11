<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Authors</title>
    </head>
    <body>
     <%
         response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
         response.setHeader("Pragma", "no-cache");
         response.setHeader("Expires", "0");
           if(session.getAttribute("userLogin")==null){
                response.sendRedirect("login.jsp");
              }%>
            <div align="left">
               <table border="1" cellpadding="5">
                   <tr>
                       <th>Surname</th>
                       <th>Books</th>
                   </tr>
               <form action="authors" method="post">
                  <c:forEach var="author" items="${authorList}">
                      <tr>
                          <td><c:out value="${author.getSurname()}"/></td>
                            <c:choose>
                              <c:when test="${not empty author.getBooks()}">
                               <td>${author.getBooks().size()}</td>
                                </c:when>
                                <c:otherwise>
                                <td>-</td>
                                </c:otherwise>
                              </c:choose>
                          <td><input type="radio" name ="selected" value="${author.getId()}" required/></td>
                      </tr>
                  </c:forEach>
              </table>
               <INPUT TYPE="submit" name="button" VALUE="edit"/>
               <INPUT TYPE="submit" name="button" VALUE="delete"/>
              </form>
              <form action="authors" method="post">
                <input type="submit" name="button" value="add new">
                <input type="submit" name="button" value="menu">
              </form><br>
              <form action="logout" method="post">
                  <input type="submit" name="button" value="logout">
             </form><br>
        </div>
    </body>
</html