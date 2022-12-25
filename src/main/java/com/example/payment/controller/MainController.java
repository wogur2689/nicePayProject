package com.example.payment.controller;

import com.example.payment.util.DataEncrypt;
import com.example.payment.util.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static kr.co.nicevan.pg.common.TimeUtils.getyyyyMMddHHmmss;

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
        String price 	  = "1004"; 						// 결제상품금액
        String buyerName  = "테스트"; 						// 구매자명
        String buyerTel   = "01000000000"; 				    // 구매자연락처
        String buyerEmail = "happy@day.co.kr"; 			    // 구매자메일주소
        String moid 	  = orderNumber; 			        // 상품주문번호
        String returnURL  = "http://localhost:8080/return.jsp"; //결과페이지(절대경로)

        /*
         * <해쉬암호화> (수정금지) -  SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법
         */
        DataEncrypt sha256Enc 	= new DataEncrypt();
        String ediDate 			= getyyyyMMddHHmmss(); //요청시간
        String signData         = sha256Enc.encrypt(ediDate + merchantID + price + merchantKey); //위변조 검증 데이터

        /* 3. 결제 요청 데이터 페이지에 내려주기*/
        payData.put("payMethod", payMethod);
        payData.put("merchantKey", merchantKey);
        payData.put("merchantID", merchantID);
        payData.put("price", price);
        payData.put("buyerName", buyerName);
        payData.put("buyerTel", buyerTel);
        payData.put("buyerEmail", buyerEmail);
        payData.put("moid", moid);
        payData.put("returnURL", returnURL);
        payData.put("ediDate", ediDate);
        payData.put("signData", signData);

        model.addAttribute("payData", payData);
        return "index";
    }
}
