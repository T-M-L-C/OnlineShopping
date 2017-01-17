<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>ModifyUser</title>
	</head>

	<body>
		<center>
			<h1>修改</h1><hr>
			<form action="${path}/action/user/editServlet" method="post">
				<input type="hidden" name="id" value="${user.id}">
			<table>
				<tr>
					<td>用户名：</td>
					<td><input type="text" name="username" value="${user.username}"></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input type="password" name="password" value="${user.password}"></td>
				</tr>
				<tr>
					<td>性别：</td>
					<td>
						male<input type="radio" name="sex" value="1" <c:if test="${user.sex==1}">checked</c:if>>&nbsp;&nbsp;
						female<input type="radio" name="sex" value="0" <c:if test="${user.sex==0}">checked</c:if>>
					</td>
				</tr>
				<tr align="center">
					<td colspan="2">
						<input type="submit" value="修改">
						<input type="reset" value="取消">
					</td>
				</tr>
			</table>
			</form>
		</center>
	</body>
</html>
