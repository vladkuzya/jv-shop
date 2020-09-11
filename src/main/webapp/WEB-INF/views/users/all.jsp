<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All users</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<h1>All users page</h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>NAME</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>
                <c:out value="${user.id}"/>
            </td>
            <td>
                <c:out value="${user.login}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/users/delete?id=${user.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a class="btn btn-dark" href="${pageContext.request.contextPath}/" role="button">Go to main</a>
</body>
</html>
