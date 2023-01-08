<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>취소요청 결과</title>
    <meta charset="utf-8">
</head>
<body>
<table>
    <tr>
        <th>취소 결과 내용</th>
        <td>${ResultMsg}</td>
    </tr>
    <tr>
        <th>취소 금액</th>
        <td>${CancelAmt}</td>
    </tr>
    <tr>
        <th>취소일</th>
        <td>${CancelDate}</td>
    </tr>
    <tr>
        <th>취소시간</th>
        <td>${CancelTime}</td>
    </tr>
</table>
<a href="/">메인페이지로</a>
</body>
</html>