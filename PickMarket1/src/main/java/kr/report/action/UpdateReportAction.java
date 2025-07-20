package kr.report.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.util.FileUtil;

public class UpdateReportAction implements Action{
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
		
		ReportVO report = new ReportVO();
		report.setReport_num(report_num);
		report.setReport_title(request.getParameter("report_title"));
		report.setReport_content(request.getParameter("report_content"));
		
		// 이미지 변경(수정하면 수정한대로, 수정안하면 기존파일)
		String newImg = FileUtil.uploadFile(request, "report_img");
		if (newImg == null || newImg.isEmpty()) {
		    newImg = db_report.getReport_img();
		} else {
		    FileUtil.removeFile(request, db_report.getReport_img());
		}
		report.setReport_img(newImg);

		dao.updateReport(report);

		if(report.getReport_img()!=null && !report.getReport_img().equals("")) {
			FileUtil.removeFile(request, db_report.getReport_img());
		}
		
		return "redirect:/report/reportDetail.do?report_num="+report_num;
	}
}
