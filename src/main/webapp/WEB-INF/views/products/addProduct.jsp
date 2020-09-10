<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add product</title>
</head>
<body>
<h1>Hello! Please provide product parameters</h1>
<h1 style="color:red">${message}</h1>
<form method="post" action="${pageContext.request.contextPath}/products/add">
    Product name: <input type="text" name="name">
    Product price: <input type="text" name="price">

    <button type="submit">Add</button>
</form>
<a href="${pageContext.request.contextPath}/">Go to main</a>
</body>
</html>
