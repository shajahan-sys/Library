<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage = "error.jsp" %>
 <%@ page import = "java.sql.*"%>
<%@ taglib prefix="c"
  uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Hello Page</title>
    </head>
    <body>
        <h2>Hello, maan</h2>
        <%="5+5" %>
        <%=5+5 %>

        <%
        String url = "jdbc:mysql://localhost:3306/library";
        String username = "root";
        String password = "root";
        String sql = "SELECT * FROM READERS;";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, username, password);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        String id = rs.getString(1);

        %>
        <%= id %>
        <c:out value="${s}" />
         <c:out value="${r}" />

        <c:forEach items="${r}" var ="reader">
        ${reader} <br>
        </c:forEach>
    </body>

    <%@ include file = "header.jsp" %>
</html>