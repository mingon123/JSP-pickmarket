package kr.report.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.report.dao.ReportDAO;

public class WriteReportFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
        long suspect_num = Long.parseLong(request.getParameter("suspect_num"));

        ReportDAO dao = ReportDAO.getInstance();
        String nickname = dao.getUserNickname(suspect_num);
        if (nickname == null) {
            request.setAttribute("notice_msg", "신고 대상 사용자를 찾을 수 없습니다.");
            request.setAttribute("notice_url", request.getContextPath() + "/main/main.do");
            return "common/alert_view.jsp";
        }

        request.setAttribute("nickname", nickname);
        request.setAttribute("suspect_num", suspect_num);

		return "report/writeReportForm.jsp";
	}
}
