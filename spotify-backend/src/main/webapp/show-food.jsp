<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
food items coming soon
<c:forEach var="itemki" items = "${foods}">
<p>&nbsp; wypisac</p>
${itemki}

</c:forEach>
</body>
</html>