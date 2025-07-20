package kr.review.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.review.dao.MannerRateDAO;
import kr.review.vo.MannerRateVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;

public class MannerListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if (user_num == null) {
			return "redirect:/user/loginForm.do";
		}
		
		Long rated_num = Long.parseLong(request.getParameter("user_num"));
		
		MannerRateDAO dao = MannerRateDAO.getInstance();
		List<MannerRateVO> list = dao.getListManner(1,10,rated_num);

        for (MannerRateVO manner : list) {
            String mannerOpText = manner.getMannerOpText(manner.getMannerOp());
            manner.setMannerOp(mannerOpText);
        }
        
        int count = dao.getMannerCountByRatedNum(rated_num);


        UserDAO userDao = UserDAO.getInstance();
        // 닉네임 가져오기
        UserVO rated_user = userDao.getUser(rated_num);
        String nickname = rated_user.getNickname();        
        // 현재 로그인한 계정
        UserVO login_user = userDao.getUser(user_num);
        
        request.setAttribute("user", login_user);
        request.setAttribute("list", list);
        request.setAttribute("count", count);
        request.setAttribute("rated_num", rated_num);
        request.setAttribute("nickname", nickname);
        
		return "review/mannerList.jsp";
	}
}
