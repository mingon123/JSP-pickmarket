package kr.report.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.util.PagingUtil;

public class ReportListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {
			return "redirect:/user/loginForm.do";
		}

		String target_num = request.getParameter("user_num");
		Long userNum = (target_num != null) ? Long.parseLong(target_num) : user_num;
		
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null) pageNum = "1";
		
		String keyfield = request.getParameter("keyfield");
		String keyword = request.getParameter("keyword");
		
		ReportDAO dao = ReportDAO.getInstance();
		int count = dao.getReportCountByUser(user_num);
		
		PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,20,10,"reportList.do");
		
		List<ReportVO> list = null;
		if(count > 0) {
			list = dao.getListReport(page.getStartRow(), page.getEndRow(), keyfield, keyword, user_num);
		}
		
		request.setAttribute("count", count);
		request.setAttribute("list", list);
		request.setAttribute("page", page.getPage());
		request.setAttribute("startRow", page.getStartRow());
		request.setAttribute("userNum", userNum);
		
		return "report/reportList.jsp";
	}
}
