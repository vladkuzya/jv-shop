<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My orders</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Products</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>
                <c:out value="${order.id}"/>
            </td>
            <td>
                <c:out value="${order.products}"/>
            </td>
        </tr>
    </c:forEach>
</table>
<a class="btn btn-dark" href="${pageContext.request.contextPath}/" role="button">Go to main</a>
</body>
</html>
