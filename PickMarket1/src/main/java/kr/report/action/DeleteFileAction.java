package kr.report.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.report.dao.ReportDAO;
import kr.report.vo.ReportVO;
import kr.util.FileUtil;
import kr.util.StringUtil;

public class DeleteFileAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,String> mapAjax = new HashMap<String, String>();
		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) { // 로그인이 되지 않은 경우
			mapAjax.put("result", "logout");			
		}else { // 로그인이 된 경우
			long report_num = Long.parseLong(request.getParameter("report_num"));
			ReportDAO dao = ReportDAO.getInstance();
			ReportVO db_report = dao.getReport(report_num);
			// 로그인한 회원번호와 작성자 회원번호 일치 여부 체크
			if(user_num!=db_report.getReporter_num()) {
				mapAjax.put("result", "wrongAccess");
			}else {
				dao.deleteImage(report_num);
				// 파일 삭제
				FileUtil.removeFile(request, db_report.getReport_img());
				
				mapAjax.put("result", "success");
			}
		}
		// JSON 데이터로 변환		
		return StringUtil.parseJSON(request, mapAjax);
	}
}
