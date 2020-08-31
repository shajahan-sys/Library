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
            %>
              <form name="myForm" action="add-edit-book" method="post">
                         <% boolean isEdited = false;
                         if(request.getAttribute("edit") != null){isEdited = true;}%>
                         <%if(isEdited){%> Edit book <%} else {%> Add new Book <%}%><br>
                         Book Title : <input type="text" name="editTitle" value=<%if(isEdited){%> "${edit.getTitle()}"<%}%>><br>
                         Author : <select name="author1" id="1">
                         <option value="no author" >select author</option>
                           <c:forEach var="author" items="${authors}" >
                              <option value="${author.getId()}"
                              <%if(isEdited){%>
                              ${author.getSurname() == edit.getAuthor().getSurname() ? 'selected="selected"' : ''}<%}%>>
                             ${author.getSurname()}
                              </option>
                            </c:forEach>
                          </select><br>
                          Publication Year : <input type="text" name="year" value=<%if(isEdited){%> "${edit.getPublicationYear()}"<%}%>><br>
                          <input type="submit" name="button" onclick="return validateForm()" value="save"/>
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