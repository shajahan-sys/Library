<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Readers</title>
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
       <c:if test="${readers.size() > 0}">
            <div align="left">
            List of Readers<br>
             <table border="1" cellpadding="5">
               <tr>
                   <th>Surname</th>
                   <th>Name</th>
                   <th>Borrowed books</th>
               </tr>
            <form action="readers" method="post">
               <c:forEach var="reader" items="${readers}">
                   <tr>
                       <td><c:out value="${reader.getSurname()}" /></td>
                       <td><c:out value="${reader.getName()}" /></td>
                       <c:choose>
                             <c:when test="${not empty reader.getLendings()}">
                                <td>${reader.getLendings().size()}</td>
                             </c:when>
                       <c:otherwise>
                        <td>-</td>
                      </c:otherwise>
                       </c:choose>
                        <td><input type="radio" name ="selected" value="${reader.getId()}" required/></td>
                   </tr>
                </c:forEach>
            </table>
            <INPUT TYPE="submit" name="button" VALUE="edit"/>
            <INPUT TYPE="submit" name="button" VALUE="return"/>
            <INPUT TYPE="submit" name="button" VALUE="lend"/>
            <INPUT TYPE="submit" name="button" VALUE="delete"/>
        </form> </c:if>
               <c:if test="${readers.size() == 0}">
        Add your readers in order to display them.
        </c:if>
         <form action="readers" method="post">
             <input type="submit" name="button" value="add new">
             <input type="submit" name="button" value="menu">
          </form><br>
             <form action="logout" method="post">
               <input type="submit" name="button" value="logout">
              </form><br>
        </div>
    </body>
</html