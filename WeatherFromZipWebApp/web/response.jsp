<%-- 
    Document   : response
    Created on : Feb 23, 2016, 3:24:03 PM
    Author     : Harshit
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.lang.String"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Weather Report</title>
        <link href="<c:url value="style.css" />" rel="stylesheet">
    </head>
    <body class="dark-matter">
        <h1>Weather Report</h1>

        <h3>The following information is obtained by invoking Google's Geocoding API. (Restful)</h3>
        <%
            String[] sArray = (String[]) request.getAttribute("geo");
        %>
        <table>
            <tr>
                <td>City: </td>
                <td><%out.print(sArray[0]);%> </td>
            </tr>
            <tr>
                <td>Country: </td>
                <td><%out.print(sArray[1]);%> </td>
            </tr>
            <tr>
                <td>Latitude: </td>
                <td><%out.print(sArray[2]);%> </td>
            </tr>
            <tr>
                <td>Longitude: </td>
                <td><%out.print(sArray[3]);%> </td>
            </tr>
        </table>

        <h3>The following details are obtained by invoking Global Weather Web Service (SOAP based WSDL)</h3>
        <%
            ArrayList<String> list = (ArrayList<String>) request.getAttribute("xmllist");
        %>
        <table>
            <tr>
                <td>Location: </td>
                <td><%out.print(list.get(0));%> </td>
            </tr>
            <tr>
                <td>Time: </td>
                <td><%out.print(list.get(1));%> </td>
            </tr>
            <tr>
                <td>Sky Conditions: </td>
                <td><%out.print(list.get(2));%> </td>
            </tr>
            <tr>
                <td>Temperature: </td>
                <td><%out.print(list.get(3));%> </td>
            </tr>
            <tr>
                <td>Relative Humidity:  </td>
                <td><%out.print(list.get(4));%> </td>
            </tr>
        </table>

        <h3>The following details are obtained by invoking Sunset-sunrise Web Service (Restful)</h3>
        <%
            ArrayList<String> jlist = (ArrayList<String>) request.getAttribute("jsonlist");
            System.out.print("jsp inside" + jlist.size());
        %>
        <table>
            <tr>
                <td colspan="2">
                    All times are in UTC format.
                </td>
            </tr>
            <tr>
                <td>Sunrise Time:  </td>
                <td><%out.print(jlist.get(0));%> </td>
            </tr>
            <tr>
                <td>Sunset Time: </td>
                <td><%out.print(jlist.get(1));%> </td>
            </tr>
            <tr>
                <td>Day Length: </td>
                <td><%out.print(jlist.get(2));%> </td>
            </tr>
            <tr>
                <td>Civil Twilight Begin: </td>
                <td><%out.print(jlist.get(3));%> </td>
            </tr>
            <tr>
                <td>Nautical Twilight Begin:  </td>
                <td><%out.print(jlist.get(4));%> </td>
            </tr>
            <tr>
                <td>Astronomical Twilight Begin: </td>
                <td><%out.print(jlist.get(4));%> </td>
            </tr>
        </table>

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
