<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All products</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<table border="1">
    <tr>
        <th>Name</th>
        <th>Price</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>
                <c:out value="${product.name}"/>
            </td>
            <td>
                <c:out value="${product.price}"/>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/shopping-carts/products/add?id=${product.id}">Buy</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a class="btn btn-dark" href="${pageContext.request.contextPath}/" role="button">Go to main</a>
</body>
</html>
