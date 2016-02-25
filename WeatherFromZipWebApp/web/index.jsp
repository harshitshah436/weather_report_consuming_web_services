<%-- 
    Document   : index
    Created on : Feb 23, 2016, 12:10:36 PM
    Author     : Harshit
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Weather Report Application</title>
        <link href="<c:url value="style.css" />" rel="stylesheet">
    </head>
    <body>
        <h3>Welcome to Weather application.</h3>
        <form action="WeatherFromZipServlet" method="POST" class="dark-matter">
            Enter a valid 5-digit Zip Code: <input type="text" name="zipcode">
            <input type="submit" value="Submit" />
        </form>
    </body>
</html>
