package kr.user.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.http.*;

import kr.controller.Action;
import kr.util.StringUtil;

public class SendEmailAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String code = UUID.randomUUID().toString().substring(0, 6);

        HttpSession session = request.getSession();
        session.setAttribute("authCode", code);
        session.setAttribute("authCodeIssuedTime", System.currentTimeMillis());

        String senderEmail = "pickmarket.ovo@gmail.com";
        String senderPass = "rrasdrchmfnrvgex";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        Session mailSession = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPass);
            }
        });

        Message message = new MimeMessage(mailSession);
        
        try {
            // 보낸 사람 이름 설정
            message.setFrom(new InternetAddress(senderEmail, "P!ck Market"));
        } catch (Exception e) {
            message.setFrom(new InternetAddress(senderEmail)); // fallback
        }

        // 회신 주소 설정 (답장시)
        message.setReplyTo(InternetAddress.parse("pickmarket@pickmarket.com"));
        
        String html =
        	    "<!DOCTYPE html>" +
        	    "<html>" +
        	    "<head>" +
        	    "  <meta charset='UTF-8'>" +
        	    "  <title>P!ck Market 이메일 인증</title>" +
        	    "</head>" +
        	    "<body style='margin:0;padding:0;background-color:#111;font-family:Arial,sans-serif;color:#fff;'>" +
        	    "  <div style='max-width:600px;margin:0 auto;padding:30px;background-color:#000;border-radius:10px;'>" +
        	    "    <div style='text-align:center;margin-bottom:30px;'>" +
        	    "      <img src='https://i.imgur.com/QgGOSsU.png' alt='P!ck Market' style='max-width:180px;'>" +
        	    "    </div>" +
        	    "    <h2 style='color:#fff;text-align:center;'>이메일 인증코드 안내</h2>" +
        	    "    <p style='font-size:16px;line-height:1.6;text-align:center;color:#ffffff;'>" +
        	    "      <strong>P!ck Market</strong>을 이용해 주셔서 감사합니다.<br>" +
        	    "      아래 인증코드를 입력해 주세요." +
        	    "    </p>" +
        	    "    <div style='margin:30px 0;padding:15px;text-align:center;background-color:#222;border:1px solid #444;border-radius:8px;'>" +
        	    "      <span style='font-size:24px;font-weight:bold;color:#ff3366;'>" + code + "</span>" +
        	    "    </div>" +
        	    "    <p style='font-size:14px;color:#aaa;text-align:center;'>" +
        	    "      본 인증코드는 발급 시점부터 5분간 유효합니다.<br>" +
        	    "      이 메일은 발신 전용이며 회신되지 않습니다." +
        	    "    </p>" +
        	    "    <div style='margin-top:40px;text-align:center;'>" +
        	    "      <small style='color:#666;'>© 2025 P!ck Market</small>" +
        	    "    </div>" +
        	    "  </div>" +
        	    "</body>" +
        	    "</html>";


        
        
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("P!ck Market 인증코드입니다");
        message.setContent(html, "text/html; charset=UTF-8");
        
        Map<String,String> mapAjax = new HashMap<String,String>();

        try {
            Transport.send(message);
            mapAjax.put("result", "success");
            //발급시간
            mapAjax.put("issuedTime", String.valueOf(System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔 로그 확인용
            mapAjax.put("result", "fail");
        }

        return StringUtil.parseJSON(request, mapAjax); 
    }
}
