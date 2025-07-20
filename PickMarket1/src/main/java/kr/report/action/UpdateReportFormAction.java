package kr.report.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.util.StringUtil;

public class UpdateReportFormAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		long report_num = Long.parseLong(request.getParameter("report_num"));
		ReportDAO dao = ReportDAO.getInstance();
		ReportVO report = dao.getReport(report_num);
		if(user_num != report.getReporter_num()) {
			return "common/accessDenied.jsp";
		}
		
		long suspect_num = report.getSuspect_num();
		report.setReport_title(StringUtil.parseQuot(report.getReport_title()));
		String suspectNickname = dao.getUserNickname(report.getSuspect_num());
		report.setNickname(suspectNickname);
		
		request.setAttribute("report", report);
		request.setAttribute("suspect_num", suspect_num); 
		
		return "report/updateReportForm.jsp";
	}
}
