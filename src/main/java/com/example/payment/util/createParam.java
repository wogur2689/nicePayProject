package com.example.payment.util;

import static com.example.payment.util.OrderUtil.getyyyyMMddHHmmss;

public class createParam {
    /*
     *******************************************************
     * <결제요청 파라미터>
     * 결제시 Form 에 보내는 결제요청 파라미터입니다.
     * 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며,
     * 추가 가능한 옵션 파라미터는 연동메뉴얼을 참고하세요.
     *******************************************************
     */
    String merchantKey 		= "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg=="; // 상점키
    String merchantID 		= "nicepay00m"; 				// 상점아이디
    String goodsName 		= "나이스페이"; 					// 결제상품명
    String price 			= "1004"; 						// 결제상품금액
    String buyerName 		= "나이스"; 						// 구매자명
    String buyerTel 		= "01000000000"; 				// 구매자연락처
    String buyerEmail 		= "happy@day.co.kr"; 			// 구매자메일주소
    String moid 			= "mnoid1234567890"; 			// 상품주문번호
    String returnURL 		= "http://localhost:8080/return.jsp"; // 결과페이지(절대경로) - 모바일 결제창 전용

    /*
     *******************************************************
     * <해쉬암호화> (수정하지 마세요)
     * SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
     *******************************************************
     */
    DataEncrypt sha256Enc 	= new DataEncrypt();
    String ediDate 			= getyyyyMMddHHmmss();
    String signData 		= sha256Enc.encrypt(ediDate + merchantID + price + merchantKey);
}
