package kr.user.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.StringUtil;

public class GetMaskedPasswordAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        // 세션에서 인증 여부 확인
        Boolean verified = (Boolean) session.getAttribute("emailVerified");
        String id = request.getParameter("id");

        Map<String, String> mapAjax = new HashMap<>();

        if (verified == null || !verified) {
            mapAjax.put("result", "unauthorized");
        } else {
            UserDAO dao = UserDAO.getInstance();
            UserVO user = dao.checkMember(id);

            if (user != null) {
                String passwd = user.getPasswd();
                // 앞 3글자만 보여주고 나머지 * 처리
                String masked = passwd.length() <= 3 ? "*".repeat(passwd.length()) 
                                  : passwd.substring(0, 3) + "*".repeat(passwd.length() - 3);
                mapAjax.put("result", "success");
                mapAjax.put("maskedPasswd", masked);
            } else {
                mapAjax.put("result", "not_found");
            }
        }

        return StringUtil.parseJSON(request, mapAjax);
    }
}
