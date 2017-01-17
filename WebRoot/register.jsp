<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>UserRegister</title>
	</head>

	<body>
		<center>
			<h1>UserRegister</h1><hr>
			<form action="${path}/action/user/register" method="post">
			<table>
				<tr>
					<td>用户名：</td>
					<td><input type="text" name="username"></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input type="password" name="password"></td>
				</tr>
				<tr>
					<td>确认密码：</td>
					<td><input type="password" name="passwd2"></td>
				</tr>
				<tr>
					 <td>sex：</td>
					<td>
						male<input type="radio" name="sex" value="1" checked="checked">&nbsp;&nbsp;
						female<input type="radio" name="sex" value="0">
					</td> 
				</tr>
				<tr align="center">
					<td colspan="2">
						<input type="submit" value="保存">
						<input type="reset" value="取消">
					</td>
				</tr>
			</table>
			</form>
		</center>
	</body>
</html>
