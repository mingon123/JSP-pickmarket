package kr.report.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.util.FileUtil;

public class WriteReportAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		long suspect_num = Long.parseLong(request.getParameter("suspect_num"));
		
		ReportDAO dao = ReportDAO.getInstance();
		ReportVO report = new ReportVO();
		report.setReport_title(request.getParameter("report_title"));
		report.setReport_content(request.getParameter("report_content"));
		report.setReport_img(FileUtil.uploadFile(request, "report_img"));
		report.setSuspect_num(suspect_num);
		report.setReporter_num(user_num);
		
		dao.insertReport(report);
		
		request.setAttribute("notice_msg", "신고 완료");
		request.setAttribute("notice_url", request.getContextPath()+"/report/reportList.do");
		
		return "common/alert_view.jsp";
	}
}
