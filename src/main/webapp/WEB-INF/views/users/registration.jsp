<%--
  Created by IntelliJ IDEA.
  User: Влад
  Date: 09.09.2020
  Time: 12:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Hello! Please provide your user details</h1>
<h1 style="color:red">${message}</h1>
<form method="post" action="${pageContext.request.contextPath}/users/registration">
    Your login: <input type="text" name="login">
    Your password: <input type="password" name="psw">
    Please, repeat password: <input type="password" name="psw-repeat">

    <button type="submit">GO!</button>
</form>
<a href="${pageContext.request.contextPath}/">Go to main</a>
</body>
</html>
