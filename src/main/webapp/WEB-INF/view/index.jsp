<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="org.apache.commons.codec.binary.Hex" %>-
<!DOCTYPE html>
<html>
<head>
    <title>NICEPAY PAY REQUEST(UTF-8)</title>
    <meta charset="utf-8">
    <style>
        html,body {height: 100%;}
        form {overflow: hidden;}
    </style>
    <!-- 아래 js는 PC 결제창 전용 js입니다.(모바일 결제창 사용시 필요 없음) -->
    <script src="https://web.nicepay.co.kr/v3/webstd/js/nicepay-3.0.js" type="text/javascript"></script>
    <script type="text/javascript">
        //결제창 최초 요청시 실행됩니다.
        function nicepayStart(){
            if(checkPlatform(window.navigator.userAgent) == "mobile"){//모바일 결제창 진입
                document.payForm.action = "https://web.nicepay.co.kr/v3/v3Payment.jsp";
                document.payForm.acceptCharset="euc-kr";
                document.payForm.submit();
            }else{//PC 결제창 진입
                goPay(document.payForm);
            }
        }

        //[PC 결제창 전용]결제 최종 요청시 실행됩니다. <<'nicepaySubmit()' 이름 수정 불가능>>
        function nicepaySubmit(){
            document.payForm.submit();
        }

        //[PC 결제창 전용]결제창 종료 함수 <<'nicepayClose()' 이름 수정 불가능>>
        function nicepayClose(){
            alert("결제가 취소 되었습니다");
        }

        //pc, mobile 구분(가이드를 위한 샘플 함수입니다.)
        function checkPlatform(ua) {
            if(ua === undefined) {
                ua = window.navigator.userAgent;
            }

            ua = ua.toLowerCase();
            var platform = {};
            var matched = {};
            var userPlatform = "pc";
            var platform_match = /(ipad)/.exec(ua) || /(ipod)/.exec(ua)
                || /(windows phone)/.exec(ua) || /(iphone)/.exec(ua)
                || /(kindle)/.exec(ua) || /(silk)/.exec(ua) || /(android)/.exec(ua)
                || /(win)/.exec(ua) || /(mac)/.exec(ua) || /(linux)/.exec(ua)
                || /(cros)/.exec(ua) || /(playbook)/.exec(ua)
                || /(bb)/.exec(ua) || /(blackberry)/.exec(ua)
                || [];

            matched.platform = platform_match[0] || "";

            if(matched.platform) {
                platform[matched.platform] = true;
            }

            if(platform.android || platform.bb || platform.blackberry
                || platform.ipad || platform.iphone
                || platform.ipod || platform.kindle
                || platform.playbook || platform.silk
                || platform["windows phone"]) {
                userPlatform = "mobile";
            }

            if(platform.cros || platform.mac || platform.linux || platform.win) {
                userPlatform = "pc";
            }

            return userPlatform;
        }
    </script>
</head>
<body>
<form name="payForm" method="post" action="payResult_utf.jsp" accept-charset="euc-kr">
    <table>
        <tr>
            <th><span>결제 수단</span></th>
            <td><input type="text" name="PayMethod" value=""></td>
        </tr>
        <tr>
            <th><span>결제 상품명</span></th>
            <td><input type="text" name="GoodsName" value="<%=goodsName%>"></td>
        </tr>
        <tr>
            <th><span>결제 상품금액</span></th>
            <td><input type="text" name="Amt" value="<%=price%>"></td>
        </tr>
        <tr>
            <th><span>상점 아이디</span></th>
            <td><input type="text" name="MID" value="<%=merchantID%>"></td>
        </tr>
        <tr>
            <th><span>상품 주문번호</span></th>
            <td><input type="text" name="Moid" value="<%=moid%>"></td>
        </tr>
        <tr>
            <th><span>구매자명</span></th>
            <td><input type="text" name="BuyerName" value="<%=buyerName%>"></td>
        </tr>
        <tr>
            <th>구매자명 이메일</th>
            <td><input type="text" name="BuyerEmail" value="<%=buyerEmail%>"></td>
        </tr>
        <tr>
            <th><span>구매자 연락처</span></th>
            <td><input type="text" name="BuyerTel" value="<%=buyerTel%>"></td>
        </tr>
        <tr>
            <th><span>인증완료 결과처리 URL<!-- (모바일 결제창 전용)PC 결제창 사용시 필요 없음 --></span></th>
            <td><input type="text" name="ReturnURL" value="<%=returnURL%>"></td>
        </tr>
        <tr>
            <th>가상계좌입금만료일(YYYYMMDD)</th>
            <td><input type="text" name="VbankExpDate" value=""></td>
        </tr>

        <!-- 옵션 -->
        <input type="hidden" name="GoodsCl" value="1"/>						<!-- 상품구분(실물(1),컨텐츠(0)) -->
        <input type="hidden" name="TransType" value="0"/>					<!-- 일반(0)/에스크로(1) -->
        <input type="hidden" name="CharSet" value="utf-8"/>					<!-- 응답 파라미터 인코딩 방식 -->
        <input type="hidden" name="ReqReserved" value=""/>					<!-- 상점 예약필드 -->

        <!-- 변경 불가능 -->
        <input type="hidden" name="EdiDate" value="<%=ediDate%>"/>			<!-- 전문 생성일시 -->
        <input type="hidden" name="SignData" value="<%=signData%>"/>	<!-- 해쉬값 -->
    </table>
    <a href="#" class="btn_blue" onClick="nicepayStart();">요 청</a>
</form>
</body>
</html>