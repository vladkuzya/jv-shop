<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
<h1 style="color:red">${errorMsg}</h1>
<%@include file="/WEB-INF/views/header.jsp" %>
<form method="post" action="${pageContext.request.contextPath}/users/login">
    Your login: <input type="text" name="login">
    Your password: <input type="password" name="psw">
    <button type="submit">Login</button>
</form>
<a class="btn btn-dark" href="${pageContext.request.contextPath}/" role="button">Go to main</a>
</body>
</html>
