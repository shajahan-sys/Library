<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
                boolean isReaderSelected = false;
                if(request.getAttribute("selReader") != null)
                {isReaderSelected = true;}
                boolean isBookSelected = false;
                if(request.getAttribute("selBook") != null){
                isBookSelected = true;
                }
                %>
            <div align="left">
                           <form name="myForm" action="lending" method="post">
                              Book : <select name="book" id="1">
                                                    <option value="no book" >select book</option>
                                                      <c:forEach var="book" items="${books}" >
                                                         <option value="${book.getId()}"
                                                         <%if(isBookSelected){%>
                                                         ${book.getId() == selBook.getId() ? 'selected="selected"' : ''}<%}%>>
                                                        ${book.getTitle()}
                                                         </option>
                                                       </c:forEach>
                                                     </select><br>
                               Reader : <select name="reader" id="2">
                                     <option value="no reader" >select reader</option>
                                       <c:forEach var="reader" items="${readers}" >
                                          <option value="${reader.getId()}"
                                          <%if(isReaderSelected){%>
                                          ${reader.getId() == selReader.getId() ? 'selected="selected"' : ''}<%}%>>
                                         ${reader.getSurname()} ${reader.getName()}
                                          </option>
                                        </c:forEach>
                                      </select><br>
                                Return Date : <input type="text" name="date"><br>
                          <INPUT TYPE="submit" name="button" VALUE="cancel"/>
                          <INPUT TYPE="submit" name="button"  onclick="return validateForm()" VALUE="lend"/>
                      </form>
                            </div>
                     <script>
                    function validateForm() {
                       var e = document.getElementById("1");
                       var f = document.getElementById("2");
                       var selBook = e.options[e.selectedIndex].value;
                       var selReader = f.options[f.selectedIndex].value;
                        var x = document.forms["myForm"]["date"].value;
                        if (x == "" || selReader == "select reader" || selBook == "select book") {
                          alert("Fill in all fields!");
                          return false;
                        }
                      }
                  </script>
         </body>
</html