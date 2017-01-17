<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Login</title>
		
	</head>

	<body>
		<center>
			<h1>UserLogin</h1><hr>
			<!-- 文件上传, type="file", method="post" enctype="multipart/form-data" -->
			<form action="${path}/action/user/login" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td>用户名：</td>
					<td><input type="text" name="username"></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input type="password" name="password"></td>
				</tr>
				<!-- <tr>
					<td>附件：</td>
					<td><input type="file" name="attache"></td>
				</tr> -->
				<tr align="center">
					<td colspan="2">
						<input type="submit" value="确认">
						<input type="reset" value="取消">
						<a href="${path}/register.jsp">register</a>
					</td>
				</tr>
			</table>
			</form>
		</center>
	</body>
</html>

		<c:if test="${!empty msg}">
		alert('${msg}');
		</c:if>
	
