<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>UserList</title>
		<style>
			table {
				border-collapse:collapse;
				width: 80%;
			}
			td {
				border: 1px solid black;
				height: 30px;
			}
			.tr_0 {
				background-color: #BF6FE3;
			}
			.tr_1 {
				background-color: #D3EA93;
			}
		</style>
	</head>

	<body>
		<center>
			<h1>userlist</h1><hr>
			<table>
				<tr>
					<td>id</td>
					<td>username</td>
					<td>password</td>
					<td>sex</td>
					<td>operate</td>
				</tr>
				<c:forEach var="u" items="${users}" varStatus="ind">
				<tr class="tr_${ind.index%2}">
					<td>${ind.index+1 }</td>
					<td>${u.username }</td>
					<td>${u.password }</td>
					<td>
						<c:if test="${u.sex==0}">female</c:if>
						<c:if test="${u.sex==1}">male</c:if>
					</td>
					<td>
						<a href="${path}/action/user/edit?id=${u.id}">edit</a> &nbsp;&nbsp;
						<a href="${path}/action/user/delete?id=${u.id}">delete</a>
					</td>
				</tr>	
				</c:forEach>
				<tr>
					<td colspan="6">
						<a href="${path}/action/user/list?page=1">首页</a>
						<a href="${path}/action/user/list?page=${page+1}">下一页</a>
						<a href="${path}/action/user/list?page=${page-1}">上一页</a>
						<a href="${path}/action/user/list?page=${pageTotal}">尾页</a>
					</td>
				</tr>
			</table>
		</center>
	</body>
</html>
