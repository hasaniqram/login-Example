<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<a href="${pageContext.request.contextPath}/employeeTask"> EmployeeTask </a> ||
	<a href="${pageContext.request.contextPath}/managerTask"> ManagerTask </a> ||
	<a href="${pageContext.request.contextPath}/userInfo"> User Info </a>||
	<a href="${pageContext.request.contextPath}/login"> Login </a> ||
	<a href="${pageContext.request.contextPath}/logout"> Logout </a> &nbsp;
	<span style="color: red">[ ${loginedUser.userName} ]</span>
</body>
</html>