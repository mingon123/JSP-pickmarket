package kr.info.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import kr.controller.Action;
import kr.info.dao.NoticeDAO;
import kr.info.vo.NoticeVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;
import kr.util.StringUtil;

public class AdminNoticeUpdateAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}
		
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}

        // 입력값
        long noti_num = Long.parseLong(request.getParameter("noti_num"));
        NoticeDAO dao = NoticeDAO.getInstance();

        NoticeVO notice = new NoticeVO();
        notice.setNoti_num(noti_num);
        notice.setNoti_title(StringUtil.parseQuot(StringUtil.useNoHtml(request.getParameter("noti_title"))));
		notice.setNoti_content(StringUtil.parseQuot(StringUtil.useBrNoHtml(request.getParameter("noti_content"))));

        dao.updateNoticeByAdmin(notice);
        
        request.setAttribute("notice_msg", "수정이 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/info/adminNoticeDetail.do?noti_num="+ noti_num);

		return "common/alert_view.jsp";

    }
}
