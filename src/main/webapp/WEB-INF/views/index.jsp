<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>iStore</h1>
<ul>
    <li><a href="${pageContext.request.contextPath}/users/registration">Check in</a></li>
    <li><a href="${pageContext.request.contextPath}/users/all">All users</a></li>
    <li><a href="${pageContext.request.contextPath}/products/add">Add new product</a></li>
    <li><a href="${pageContext.request.contextPath}/products/all_user">All products</a></li>
    <li><a href="${pageContext.request.contextPath}/products/all_admin">All products(Admin)</a></li>
    <li><a href="${pageContext.request.contextPath}/shopping-carts/show">My shopping cart</a></li>
    <li><a href="${pageContext.request.contextPath}/orders/all_user">My orders</a></li>
    <li><a href="${pageContext.request.contextPath}/orders/all_admin">All orders(admin)</a></li>
</ul>
</body>
</html>
