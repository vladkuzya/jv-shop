<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<%@include file="/WEB-INF/views/header.jsp" %>
<h1>Hello! Please provide your user details</h1>
<h1 style="color:red">${message}</h1>
<form method="post" action="${pageContext.request.contextPath}/users/registration">
    Your login: <input type="text" name="login">
    Your password: <input type="password" name="psw">
    Please, repeat password: <input type="password" name="psw-repeat">

    <button type="submit">GO!</button>
</form>
<a class="btn btn-dark" href="${pageContext.request.contextPath}/" role="button">Go to main</a>
</body>
</html>
