<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8" >
        <title>
         <c:choose>
          <c:when test="${edit != null}">
             Edit Book
           </c:when>
          <c:otherwise>
          Add new Book
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
            }
            %>
              <form name="myForm" action="add-edit-book" method="post">
                <c:choose>
                        <c:when test="${edit != null}">
                          Edit Book<br>
                            Book Title : <input type="text" name="title" value="${edit.getTitle()}"><br>
                           Author : <select name="author1" id="1">
                           <option value="no author" >select author</option>
                             <c:forEach var="author" items="${authorList}" >
                                <option value="${author.getId()}"
                                ${author.getSurname() == edit.getAuthor().getSurname() ? 'selected="selected"' : ''}>
                               ${author.getSurname()}
                                </option>
                              </c:forEach>
                            </select><br>
                            Publication Year : <input type="text" name="year" value="${edit.getPublicationYear()}"><br>
                          </c:when>
                       <c:otherwise>
                        Add new Book<br>
                        Book Title : <input type="text" name="title"><br>
                           Author : <select name="author1" id="1">
                           <option value="no author" >select author</option>
                             <c:forEach var="author" items="${authorList}" >
                                <option value="${author.getId()}">
                               ${author.getSurname()}
                                </option>
                              </c:forEach>
                            </select><br>
                            Publication Year : <input type="text" name="year"><br>
                        </c:otherwise>
                   </c:choose>
                   <input type="submit" name="button" onclick="return validateForm()" value="save"/>
                   <input type="submit" name="button" value="cancel"/>
              </form>
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
                   if(x.length > 45 || y.length > 4){
                    alert("Too long title or year! Use up to 45 characters for title and 4 for year.");
                    return false;
                    }
                  }
             </script>
       </body>
   </html