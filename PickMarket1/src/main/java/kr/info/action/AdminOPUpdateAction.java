package kr.info.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import kr.controller.Action;
import kr.info.dao.NoticeDAO;
import kr.info.dao.OperationalPolicyDAO;
import kr.info.vo.NoticeVO;
import kr.info.vo.OperationalPolicyVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;
import kr.util.StringUtil;

public class AdminOPUpdateAction implements Action {

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
        long pol_num = Long.parseLong(request.getParameter("pol_num"));
        OperationalPolicyDAO dao = OperationalPolicyDAO.getInstance();

        OperationalPolicyVO op = new OperationalPolicyVO();
        op.setPol_num(pol_num);
        op.setPol_title(StringUtil.parseQuot(StringUtil.useNoHtml(request.getParameter("pol_title"))));
		op.setPol_content(StringUtil.parseQuot(StringUtil.useBrNoHtml(request.getParameter("pol_content"))));

        dao.updateOPByAdmin(op);
        
        request.setAttribute("notice_msg", "수정이 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/info/adminOPDetail.do?pol_num="+ pol_num);

		return "common/alert_view.jsp";

    }
}
