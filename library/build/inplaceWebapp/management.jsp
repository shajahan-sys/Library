<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="org.library.features.management.Management" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List" %>

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Management Page</title>
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
            <div align="left">
            List of Readers<br>
             <table border="1" cellpadding="5">
                              <tr>
                                  <th>Surname</th>
                                  <th>Name</th>
                                  <th>Borrowed books</th>
                              </tr>
                           <form action="management" method="get">
                              <c:forEach var="reader" items="${readers}">
                                  <tr>
                                      <td><c:out value="${reader.getSurname()}" /></td>
                                      <td><c:out value="${reader.getName()}" /></td>
                                      <c:choose>
                                            <c:when test="${not empty  reader.getManagements()}">
                                                  <td>${reader.getManagements().size()}</td>
                                            </c:when>
                                            <c:otherwise>
                                                  <td>-</td>
                                            </c:otherwise>
                                      </c:choose>
                                       <td><input type="radio" name ="selected" value="${reader.getId()}" required/></td>
                                  </tr>
                              </c:forEach>
                          </table>
                          <INPUT TYPE="submit" name="button" VALUE="details"/>
                          <INPUT TYPE="submit" name="button" VALUE="lend"/>
                      </form>
                      </div>
                      <%if("${r}" != null){%>
                        <div align="center">
                                   <table border="1" cellpadding="5">
                                   Books borrowed by <%if("${reader1}" != null){%>${reader1.getName()} ${reader1.getSurname()}<%}%>
                                                    <tr>
                                                        <th>Title</th>
                                                        <th>Return Date</th>
                                                        <th>Returned</th>
                                                    </tr>
                                                 <form action="management" method="post">
                                                    <c:forEach var="reader" items="${r}">
                                                        <tr>
                                                          <c:choose>
                                                                <c:when test="${r.size() == 1}">
                                                                <td><c:out value="${r.iterator().next().getBook().getTitle()}" /></td>
                                                                 <td><c:out value="${r.iterator().next().getReturnDate()}" /></td>
                                                             <td><input type="radio" name ="selected" value="${r.iterator().next().getBook().getId()}" required/></td>
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                   <td><c:out value="${reader.getBook().getTitle()}" /></td>
                                                                   <td><c:out value="${reader.getReturnDate()}" /></td>
                                                                   <td><input type="radio" name ="selected" value="${reader.getBook().getId()}" required/></td>
                                                                  </c:otherwise>
                                                                </c:choose>
                                                             </tr>
                                                    </c:forEach>
                                                </table>
                                                <INPUT TYPE="submit" name="button" VALUE="cancel"/>
                                                <INPUT TYPE="submit" name="button" VALUE="returned"/>
                                            </form>
                                            </div> <%}%>
    </body>
</html