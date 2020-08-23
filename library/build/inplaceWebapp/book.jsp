<%@ page import="org.library.features.book.Book" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                           <td><c:out value="${book.getManagement().getReturnDate()}"/></td>
                              <td><input type="radio" name ="selected" value="${book.getId()}" required/></td>
                       </tr>
                   </c:forEach>
               </table>
                <INPUT TYPE="submit" name="button" VALUE="edit"/>
                <INPUT TYPE="submit" name="button" VALUE="manage"/>
                <INPUT TYPE="submit" name="button" VALUE="delete"/>
                </form>
             </div>
                  <div align="center">
                   <form action="logout" method="post">
                          <input type="submit" name="button" value="logout">
                          </form><br>
             <form name="myForm" action="books"  onsubmit="return validateForm()" method="post">
             <%if(request.getAttribute("edit") != null){%> Edit book <%} else {%> Add new Book <%}%><br>
             Book Title : <input type="text" name="editTitle" value=<%if(request.getAttribute("edit") != null){%> "${edit.getTitle()}"<%}%>>
             Author : <select name="author1" id="1">
             <option value="no author">select author</option>
               <c:forEach var="author" items="${authors}" >
                  <option value="${author.getId()}" <%
                  if(request.getAttribute("edit") != null){
                   Book book = (Book) request.getAttribute("edit");
                            if(book.getAuthor().getSurname().equals("${author.getSurname()}")){
                  %> selected ="selected" <%}}%> >${author.getSurname()}
                  </option>
                </c:forEach>
              </select><br>
              Publication Year : <input type="text" name="year" value=<%if(request.getAttribute("edit") != null){%> "${edit.getPublicationYear()}"<%}%>><br>
              <input type="submit" name="button" value="save"/>
             </form>
             <form action="books" method="get">
              <input type="submit" name="button" value="cancel"/>
              </form>
           </div>
            <script>
                     function validateForm() {
                        var e = document.getElementById("1");
                        var strUser = e.options[e.selectedIndex].value;
                         var x = document.forms["myForm"]["title"].value;
                         var y = document.forms["myForm"]["year"].value;
                         if (x == "" || y == "" || strUser == "no author") {
                           alert("Fill in all fields!");
                           return false;
                         }
                       }
               </script>
       </body>
   </html