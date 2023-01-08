package com.example.payment.controller;

import com.example.payment.util.DataEncrypt;
import com.example.payment.util.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.example.payment.util.OrderUtil.getyyyyMMddHHmmss;
import static com.example.payment.util.PayUtil.connectToServer;
import static com.example.payment.util.PayUtil.jsonStringToHashMap;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        /*
         *****************************************
         * <결제요청 파라미터>
         * 결제시 Form 에 보내는 결제요청 필수 파라미터
         *****************************************
         */
        /* 1. 주문번호 생성 */
        int seq = 20000;
        final String orderNumber = OrderUtil.makeOrderNumber(String.valueOf(seq));

        /* 2, 데이터 생성 및 암호화 */
        Map<String, Object> payData = new HashMap<>();

        String payMethod = "CARD";                      // 결제수단
        String merchantKey = "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg=="; //상점 키
        String merchantID = "nicepay00m";                   // 상점 id
        String price 	  = "100"; 						    // 결제상품금액
        String buyerName  = "테스트"; 						// 구매자명
        String buyerTel   = "01000000000"; 				    // 구매자연락처
        String buyerEmail = "happy@day.co.kr"; 			    // 구매자메일주소
        String moid 	  = orderNumber; 			        // 상품주문번호
        String returnURL  = "http://localhost:8080/callback"; //결과페이지(절대경로)

        /*
         * <해쉬암호화> (수정금지) -  SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법
         */
        DataEncrypt sha256Enc 	= new DataEncrypt();
        String ediDate 			= getyyyyMMddHHmmss(); //요청시간
        String signData         = sha256Enc.encrypt(ediDate + merchantID + price + merchantKey); //위변조 검증 데이터

        /* 3. 결제 요청 데이터 페이지에 내려주기*/
        model.addAttribute("payMethod", payMethod);
        model.addAttribute("merchantKey", merchantKey);
        model.addAttribute("merchantID", merchantID);
        model.addAttribute("price", price);
        model.addAttribute("buyerName", buyerName);
        model.addAttribute("buyerTel", buyerTel);
        model.addAttribute("buyerEmail", buyerEmail);
        model.addAttribute("moid", moid);
        model.addAttribute("returnURL", returnURL);
        model.addAttribute("ediDate", ediDate);
        model.addAttribute("signData", signData);

        return "index";
    }

    @PostMapping("/callBack")
    public String payCancelResult(HttpServletRequest request, Model model) throws Exception {
        request.setCharacterEncoding("utf-8");
        /*
         ****************************************************************************************
         * <인증 결과 파라미터>
         ****************************************************************************************
         */
        String authResultCode 	= request.getParameter("AuthResultCode"); 	// 인증결과 : 0000(성공)
        String authResultMsg 	= request.getParameter("AuthResultMsg"); 	    // 인증결과 메시지
        String nextAppURL 		= request.getParameter("NextAppURL"); 		// 승인 요청 URL
        String txTid 			= request.getParameter("TxTid"); 			    // 거래 ID
        String authToken 		= request.getParameter("AuthToken"); 		    // 인증 TOKEN
        String payMethod 		= request.getParameter("PayMethod"); 		    // 결제수단
        String mid 				= request.getParameter("MID"); 				// 상점 아이디
        String moid 			= request.getParameter("Moid"); 			    // 상점 주문번호
        String amt 				= request.getParameter("Amt"); 				// 결제 금액
        String reqReserved 		= request.getParameter("ReqReserved"); 		// 상점 예약필드
        String netCancelURL 	= request.getParameter("NetCancelURL"); 	    // 망취소 요청 URL
        // String authSignature = (String)request.getParameter("Signature");	    // Nicepay에서 내려준 응답값의 무결성 검증 Data
        log.info("### AuthResultCode {}, AuthResultMsg {} ###", authResultCode, authResultMsg);
        /*
         ****************************************************************************************
         * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한 요소를 방지하기 위해 연동 시 사용하시기 바라며
         * 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
         ****************************************************************************************
         */

        DataEncrypt sha256Enc 	= new DataEncrypt();
        String merchantKey 		= "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg=="; // 상점키

        // 인증 응답 Signature = hex(sha256(AuthToken + MID + Amt + MerchantKey)
        // String authComparisonSignature = sha256Enc.encrypt(authToken + mid + amt + merchantKey);


        /*
         ****************************************************************************************
         * <승인 결과 파라미터 정의>
         * 샘플페이지에서는 승인 결과 파라미터 중 일부만 예시되어 있으며,
         * 추가적으로 사용하실 파라미터는 연동메뉴얼을 참고하세요.
         ****************************************************************************************
         */
        String ResultCode 	= ""; String ResultMsg 	= ""; String PayMethod 	= "";
        String GoodsName 	= ""; String Amt 		= ""; String TID 		= "";
        // String Signature = ""; String paySignature = "";

        /* <인증 결과 성공시 승인 진행> */
        String resultJsonStr = "";
        if(authResultCode.equals("0000")){

            /* <해쉬암호화> SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.*/
            String ediDate			= getyyyyMMddHHmmss();
            String signData 		= sha256Enc.encrypt(authToken + mid + amt + ediDate + merchantKey);

            /* <승인 요청>승인에 필요한 데이터 생성 후 server to server 통신을 통해 승인 처리 합니다. */
            StringBuffer requestData = new StringBuffer();
            requestData.append("TID=").append(txTid).append("&");
            requestData.append("AuthToken=").append(authToken).append("&");
            requestData.append("MID=").append(mid).append("&");
            requestData.append("Amt=").append(amt).append("&");
            requestData.append("EdiDate=").append(ediDate).append("&");
            requestData.append("CharSet=").append("utf-8").append("&");
            requestData.append("SignData=").append(signData);

            resultJsonStr = connectToServer(requestData.toString(), nextAppURL);

            HashMap resultData = new HashMap();
            boolean paySuccess = false;
            if("9999".equals(resultJsonStr)){
                /* <망취소 요청> 승인 통신중에 Exception 발생시 망취소 처리를 권고합니다.*/
                StringBuffer netCancelData = new StringBuffer();
                requestData.append("&").append("NetCancel=").append("1");
                String cancelResultJsonStr = connectToServer(requestData.toString(), netCancelURL);

                HashMap cancelResultData = jsonStringToHashMap(cancelResultJsonStr);
                ResultCode = (String)cancelResultData.get("ResultCode");
                ResultMsg = (String)cancelResultData.get("ResultMsg");
                /*Signature = (String)cancelResultData.get("Signature");
                String CancelAmt = (String)cancelResultData.get("CancelAmt");
                paySignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);*/
            }else{
                resultData = jsonStringToHashMap(resultJsonStr);
                ResultCode 	= (String)resultData.get("ResultCode");	// 결과코드 (정상 결과코드:3001)
                ResultMsg 	= (String)resultData.get("ResultMsg");	// 결과메시지
                PayMethod 	= (String)resultData.get("PayMethod");	// 결제수단
                GoodsName   = (String)resultData.get("GoodsName");	// 상품명
                Amt       	= (String)resultData.get("Amt");		// 결제 금액
                TID       	= (String)resultData.get("TID");		// 거래번호
                // Signature : Nicepay에서 내려준 응답값의 무결성 검증 Data
                // 가맹점에서 무결성을 검증하는 로직을 구현하여야 합니다.
                /*Signature = (String)resultData.get("Signature");
                paySignature = sha256Enc.encrypt(TID + mid + Amt + merchantKey);*/

                /* <결제 성공 여부 확인> */
                if(PayMethod != null){
                    if(PayMethod.equals("CARD")){
                        if(ResultCode.equals("3001")) paySuccess = true; // 신용카드(정상 결과코드:3001)
                    }else if(PayMethod.equals("BANK")){
                        if(ResultCode.equals("4000")) paySuccess = true; // 계좌이체(정상 결과코드:4000)
                    }else if(PayMethod.equals("CELLPHONE")){
                        if(ResultCode.equals("A000")) paySuccess = true; // 휴대폰(정상 결과코드:A000)
                    }else if(PayMethod.equals("VBANK")){
                        if(ResultCode.equals("4100")) paySuccess = true; // 가상계좌(정상 결과코드:4100)
                    }else if(PayMethod.equals("SSG_BANK")){
                        if(ResultCode.equals("0000")) paySuccess = true; // SSG은행계좌(정상 결과코드:0000)
                    }else if(PayMethod.equals("CMS_BANK")){
                        if(ResultCode.equals("0000")) paySuccess = true; // 계좌간편결제(정상 결과코드:0000)
                    }
                }
                log.info("### paySuccess {} ###", paySuccess);
            }
        }else/*if(authSignature.equals(authComparisonSignature))*/{
            ResultCode 	= authResultCode;
            ResultMsg 	= authResultMsg;
        }/*else{
	System.out.println("인증 응답 Signature : " + authSignature);
	System.out.println("인증 생성 Signature : " + authComparisonSignature);
    */
        log.info("### 결제결과 {} ###", ResultMsg);
        model.addAttribute("code", ResultCode);
        model.addAttribute("ResultMsg", ResultMsg);
        model.addAttribute("PayMethod", payMethod);
        model.addAttribute("Amt", Amt);
        model.addAttribute("TID", TID);
        return "/return";
    }

    @PostMapping("/cancelCallBack")
    public String payResult(HttpServletRequest request, Model model) throws Exception {
        request.setCharacterEncoding("utf-8");
        /* <취소요청 파라미터> */
        String tid = (String) request.getParameter("TID");    // 거래 ID
        String cancelAmt = (String) request.getParameter("CancelAmt");    // 취소금액
        String partialCancelCode = (String) request.getParameter("PartialCancelCode");    // 부분취소여부
        String mid = "nicepay00m";    // 상점 ID
        String moid = "nicepay_api_3.0_test";    // 주문번호
        String cancelMsg = "고객요청";    // 취소사유

        /* <해쉬암호화> SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다. */
        DataEncrypt sha256Enc = new DataEncrypt();
        String merchantKey = "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg=="; // 상점키
        String ediDate = getyyyyMMddHHmmss();
        String signData = sha256Enc.encrypt(mid + cancelAmt + ediDate + merchantKey);

        /* <취소 요청> 취소에 필요한 데이터 생성 후 server to server 통신을 통해 취소 처리 합니다.
         취소 사유(CancelMsg) 와 같이 한글 텍스트가 필요한 파라미터는 euc-kr encoding 처리가 필요합니다. */
        StringBuffer requestData = new StringBuffer();
        requestData.append("TID=").append(tid).append("&");
        requestData.append("MID=").append(mid).append("&");
        requestData.append("Moid=").append(moid).append("&");
        requestData.append("CancelAmt=").append(cancelAmt).append("&");
        requestData.append("CancelMsg=").append(URLEncoder.encode(cancelMsg, "euc-kr")).append("&");
        requestData.append("PartialCancelCode=").append(partialCancelCode).append("&");
        requestData.append("EdiDate=").append(ediDate).append("&");
        requestData.append("CharSet=").append("utf-8").append("&");
        requestData.append("SignData=").append(signData);
        String resultJsonStr = connectToServer(requestData.toString(), "https://webapi.nicepay.co.kr/webapi/cancel_process.jsp");

        /* <취소 결과 파라미터 정의> */
        String ResultCode 	= ""; String ResultMsg 	= ""; String CancelAmt 	= "";
        String CancelDate 	= ""; String CancelTime = ""; String TID 		= ""; String Signature = "";

        /* Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한 요소를 방지하기 위해 연동 시 사용하시기 바라며
         * 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
         */
//String Signature = ""; String cancelSignature = "";

        if("9999".equals(resultJsonStr)){
            ResultCode 	= "9999";
            ResultMsg	= "통신실패";
        }else{
            HashMap resultData = jsonStringToHashMap(resultJsonStr);
            ResultCode 	= (String)resultData.get("ResultCode");	// 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)
            ResultMsg 	= (String)resultData.get("ResultMsg");	// 결과메시지
            CancelAmt 	= (String)resultData.get("CancelAmt");	// 취소금액
            CancelDate 	= (String)resultData.get("CancelDate");	// 취소일
            CancelTime 	= (String)resultData.get("CancelTime");	// 취소시간
            TID 		= (String)resultData.get("TID");		// 거래아이디 TID
            //Signature       	= (String)resultData.get("Signature");
            //cancelSignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);
        }
        log.info("### 취소결과 {} ###", ResultMsg);
        model.addAttribute("ResultMsg", ResultMsg);
        model.addAttribute("CancelAmt", CancelAmt);
        model.addAttribute("CancelDate", CancelDate);
        model.addAttribute("CancelTime", CancelTime);

        return "/cancelReturn";
    }
}
