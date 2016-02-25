<%-- 
    Document   : error
    Created on : Feb 23, 2016, 12:26:51 PM
    Author     : Harshit
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error Page</title>
        <link href="<c:url value="style.css" />" rel="stylesheet">
    </head>
    <body class="dark-matter">
        <h2>Please try again with a valid 5-digit zip code.</h2>
        <%
            String str = (String) request.getAttribute("error");
        %>
        <h3><% out.println(str);%></h3>
        <form id="main" method="post" name="main" action="" onsubmit="redirect(this);">
            <input type="submit" name="submit" value="Go back to Homepage"/> 
        </form>
        <script>
            function redirect(elem) {
                elem.setAttribute("action", "index.jsp");
                elem.submit();
            }
        </script>
    </body>
</html>
