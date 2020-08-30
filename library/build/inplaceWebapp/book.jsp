<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>Books</title>
    </head>
    <body>
     <%
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.setHeader("Expires", "0");
            if(session.getAttribute("userLogin")==null){
            response.sendRedirect("login.jsp");
            }
            %>  <div align="left">
                         <form action="books" method="get">
                         Book Title : <input type="text" name ="searchTitle">
                            Author : <select name="author2">
                                       <option value="no author">select author</option>
                                         <c:forEach var="author" items="${authors}" >
                                            <option value="${author.getId()}" >${author.getSurname()}
                                            </option>
                                          </c:forEach>
                                     </select>
                                  <input type ="submit" name="button" value="search"><br>
                              </form>
               <table border="1" cellpadding="5">
                   <tr>
                       <th>Title</th>
                       <th>Author</th>
                       <th>Publication Year</th>
                       <th>Return date</th>
                   </tr>
                <form action="books" method="post">
                   <c:forEach var="book" items="${books}">
                       <tr>
                           <td><c:out value="${book.getTitle()}" /></td>
                           <td><c:out value="${book.getAuthor().getSurname()}" /></td>
                           <td><c:out value="${book.getPublicationYear()}"/></td>
                           <td><c:out value="${book.getLending().getReturnDate()}"/></td>
                              <td><input type="radio" name ="selected" value="${book.getId()}" required/></td>
                       </tr>
                   </c:forEach>
               </table>
                <INPUT TYPE="submit" name="button" VALUE="edit"/>
                <INPUT TYPE="submit" name="button" VALUE="lend"/>
                <INPUT TYPE="submit" name="button" VALUE="return"/>
                <INPUT TYPE="submit" name="button" VALUE="delete"/>
                </form>
                <form action="books" method="post">
                 <input type="submit" name="button" value="add new">
                 </form><br>
             </div>
                  <div align="left">
                   <form action="logout" method="post">
                          <input type="submit" name="button" value="logout">
                          </form><br>
       </body>
   </html