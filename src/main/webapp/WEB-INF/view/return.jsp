<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%@ page import="org.apache.commons.codec.binary.Hex" %>
<%@ page import="org.apache.tomcat.util.json.JSONParser" %>
<!DOCTYPE html>
<html>
<head>
    <title>NICEPAY PAY RESULT(UTF-8)</title>
    <meta charset="utf-8">
</head>
<body>
<table>
    <%if("9999".equals(resultJsonStr)){%>
    <tr>
        <th>승인 통신 실패로 인한 망취소 처리 진행 결과</th>
        <td>[<%=ResultCode%>]<%=ResultMsg%></td>
    </tr>
    <%}else{%>
    <tr>
        <th>결과 내용</th>
        <td>[<%=ResultCode%>]<%=ResultMsg%></td>
    </tr>
    <tr>
        <th>결제수단</th>
        <td><%=PayMethod%></td>
    </tr>
    <tr>
        <th>상품명</th>
        <td><%=GoodsName%></td>
    </tr>
    <tr>
        <th>결제 금액</th>
        <td><%=Amt%></td>
    </tr>
    <tr>
        <th>거래 번호</th>
        <td><%=TID%></td>
    </tr>
    <!-- <%if(Signature.equals(paySignature)){%>
		<tr>
			<th>Signature</th>
			<td><%=Signature%></td>
		</tr>
		<%}else{%>
		<tr>
			<th>승인 Signature</th>
			<td><%=Signature%></td>
		</tr>
		<tr>
			<th>생성 Signature</th>
			<td><%=paySignature%></td>
		</tr> -->
    <%}%>
</table>
<p>*테스트 아이디인경우 당일 오후 11시 30분에 취소됩니다.</p>
</body>
</html>