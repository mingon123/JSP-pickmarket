package kr.info.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.controller.Action;
import kr.info.dao.NoticeDAO;
import kr.info.vo.NoticeVO;
import kr.util.StringUtil;

public class NoticeDetailAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		//비회원도 조회 가능		
		long noti_num = Long.parseLong(request.getParameter("noti_num"));
		
		NoticeDAO dao = NoticeDAO.getInstance();
		//조회수 증가
		dao.updateNoticeCount(noti_num);
		
		NoticeVO notice = dao.getNoticeDetailByAdmin(noti_num);
		
		//제목에 HTML을 허용하지 않음
		notice.setNoti_title(StringUtil.useNoHtml(notice.getNoti_title()));
		
		request.setAttribute("notice", notice);
		
		return "info/noticeDetail.jsp";
	}

} //class
