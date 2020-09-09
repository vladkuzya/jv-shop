<%--
  Created by IntelliJ IDEA.
  User: Влад
  Date: 09.09.2020
  Time: 14:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add product</title>
</head>
<body>
<h1>Hello! Please provide product parameters</h1>
<form method="post" action="${pageContext.request.contextPath}/products/add">
    Product name: <input type="text" name="name">
    Product price: <input type="text" name="price">

    <button type="submit">Add</button>
</form>
</body>
</html>
