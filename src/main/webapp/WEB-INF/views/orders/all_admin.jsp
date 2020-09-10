<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>All orders</title>
</head>
<body>
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
            <td>
                <a href="${pageContext.request.contextPath}/orders/delete?id=${order.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/">Go to main</a>
</body>
</html>
