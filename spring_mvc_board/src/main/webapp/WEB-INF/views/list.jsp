<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table width ="500" border="1">
		<tr>
			<td>번호</td>
			<td>이름</td>
			<td>제목</td>
			<td>날짜</td>
			<td>히트</td>
		</tr>
<!-- 		list : 모델 객체에서 보낸 이름 -->
<!-- 테스트 데이터 : insert into mvc_board VALUES (1,'a','aa','aaa',sysdate,0); -->
		<c:forEach items="${list}" var="dto">
			<tr>
				<td>${dto.bid}</td>
				<td>${dto.bname}</td>
<%-- 				<td>${dto.btitle}</td> --%>
<!-- 				content_view :컨트롤러단 호출 -->
<!-- 					글 번호로 게시글 조회해서 화면 보기 -->
				<td>
					<a href="content_view?bid=${dto.bid}">${dto.btitle}</a>
				</td>
				<td>${dto.bdate}</td>
				<td>${dto.bhit}</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="5">
				<a href="write_view">글작성</a>
			</td>
		</tr>
	</table>
</body>
</html>