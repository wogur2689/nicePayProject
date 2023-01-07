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
<form name="cancelForm" method="post" target="_self" action="/cancelCallBack">
    <table>
        <tr>
            <th>원거래 ID</th>
            <td><input type="text" name="TID" value="${TID}" /></td>
        </tr>
        <tr>
            <th>취소 금액</th>
            <td><input type="text" name="CancelAmt" value="${Amt}" /></td>
        </tr>
        <tr>
            <th>부분취소 여부</th>
            <td>
                <input type="radio" name="PartialCancelCode" value="0" checked="checked"/> 전체취소
                <input type="radio" name="PartialCancelCode" value="1"/> 부분취소
            </td>
        </tr>
    </table>
    <a href="#" onClick="reqCancel();">요 청</a>
</form>
</body>
<script type="text/javascript">
    function reqCancel(){
        document.cancelForm.submit();
    }
</script>
</html>