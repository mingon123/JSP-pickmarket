package kr.user.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.util.StringUtil;

public class VerifyCodeAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String inputCode = request.getParameter("code");
        HttpSession session = request.getSession();

        String sessionCode = (String) session.getAttribute("authCode");
        Long issuedTime = (Long) session.getAttribute("authCodeIssuedTime");

        Map<String, String> mapAjax = new HashMap<>();

        // 유효시간 체크 (5분 = 300000ms)
        if (issuedTime == null || System.currentTimeMillis() - issuedTime > 5 * 60 * 1000) {
            mapAjax.put("result", "expired");
        } else if (sessionCode != null && inputCode != null && sessionCode.equals(inputCode.trim())) {
            session.setAttribute("emailVerified", true);
            session.removeAttribute("authCode");
            session.removeAttribute("authCodeIssuedTime");
            mapAjax.put("result", "verified");
        } else {
            mapAjax.put("result", "not_verified");
        }

        return StringUtil.parseJSON(request, mapAjax);
    }
}
