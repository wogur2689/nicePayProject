<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>NICEPAY PAY RESULT(UTF-8)</title>
    <meta charset="utf-8">
</head>
<body>
<table>
    <tr>
        <th>결과 메세지</th>
        <td>${ResultMsg}</td>
    </tr>
    <tr>
        <th>상품명</th>
        <td>테스트</td>
    </tr>
    <tr>
        <th>결제 금액</th>
        <td>${Amt}</td>
    </tr>
</table>
<p>*테스트 아이디인경우 당일 오후 11시 30분에 취소됩니다.</p>
</body>
</html>