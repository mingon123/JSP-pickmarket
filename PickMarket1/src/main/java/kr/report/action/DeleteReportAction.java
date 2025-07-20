package kr.report.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;

public class DeleteReportAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		long report_num = Long.parseLong(request.getParameter("report_num"));
		ReportDAO dao = ReportDAO.getInstance();
		ReportVO db_report = dao.getReport(report_num);
		if(user_num != db_report.getReporter_num()) {
			return "common/accessDenied.jsp";
		}
		
		dao.deleteReport(report_num);
		
		request.setAttribute("notice_msg", "신고 삭제 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/report/reportList.do");
		
		return "common/alert_view.jsp";
	}
}
