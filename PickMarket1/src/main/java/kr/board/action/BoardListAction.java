/*package kr.board.action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.util.PagingUtil;

public class BoardListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String pageNum=request.getParameter("pageNum");
		if(pageNum==null) pageNum="1";

		String keyfield = request.getParameter("keyfield");
		String keyword= request.getParameter("keyword");

		BoardDAO dao = BoardDAO.getInstance();
		int count = dao.getBoardCount(keyfield, keyword);

		PagingUtil page = new PagingUtil(keyfield, keyword, Integer.parseInt(pageNum),count,20,10,"BoardList.do");

		List<BoardVO> board=null;
		if(count > 0) {
			board = dao.getListBoardd(page.getStartRow(), page.getEndRow(), keyfield, keyword);
		}
		request.setAttribute("count", count);
		request.setAttribute("board", board);
		request.setAttribute("page", page.getPage());

		//jsp 경로
		return "board/boardList.jsp";
	}
	
}*/
package kr.board.action;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.board.dao.BoardDAO;
import kr.board.vo.BoardVO;
import kr.controller.Action;
import kr.location.dao.LocationDAO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.PagingUtil;

public class BoardListAction implements Action{

@Override
public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpSession session = request.getSession();
	Long user_num = (Long) session.getAttribute("user_num");
	if (user_num == null) {
		return "redirect:/user/loginForm.do"; // 로그인 안된 경우 로그인 페이지로 리다이렉트
	}

    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) pageNum = "1";

    String keyfield = request.getParameter("keyfield");
    String keyword = request.getParameter("keyword");

    // 지역명 -> 지역코드 찾기
	LocationDAO locationDAO = LocationDAO.getInstance();
	String region_cd = request.getParameter("region_cd");
	String region_nm = request.getParameter("region_nm");
	
	if ("ALL".equalsIgnoreCase(region_nm)) {
	    region_cd = null;
	} else if ((region_cd == null || region_cd.isEmpty()) && region_nm != null && !region_nm.isEmpty()) {
	    region_cd = locationDAO.findRegionCdByName(region_nm);
	}

	String user_region_cd = null;
	UserDAO userDAO = UserDAO.getInstance();
	UserVO user = userDAO.getUser(user_num);
	request.setAttribute("user", user);
	if (user.getLocationVO() != null) {
		user_region_cd = user.getLocationVO().getRegion_cd();
	}

	if ("ALL".equalsIgnoreCase(region_nm)) {
	    region_cd = null;
	} else if ((region_cd == null || region_cd.isEmpty()) && user_num != null) {
	    region_cd = user_region_cd;
	}
	
	String regionName = null;
	if (region_cd == null || "ALL".equals(region_cd)) {
		regionName = "전체지역";
	} else {
		regionName = locationDAO.findRegionNmByCode(region_cd);
	}
	
    BoardDAO dao = BoardDAO.getInstance();
    int count = dao.getBoardCountByRegionBlock(region_cd, keyfield, keyword, user_num);

    PagingUtil page = new PagingUtil(keyfield, keyword, Integer.parseInt(pageNum), count, 10, 10, "boardList.do", "&region_nm=" + region_nm);

    List<BoardVO> board = null;
    if (count > 0) {
        board = dao.getListBoardByRegionBlock(page.getStartRow(), page.getEndRow(), region_cd, keyfield, keyword, user_num);
    }

    if (board != null) { 
        for (BoardVO b : board) {
            String name = locationDAO.findRegionNmByCode(b.getRegion_cd());
            String[] sub_name = name.split(" ");
            b.setRegion_nm(sub_name[sub_name.length - 1]);
        }
    } else {
    	board = new ArrayList<>();
    }

	request.setAttribute("regionName", regionName);
	request.setAttribute("locationList", locationDAO.getAllLocations());
    
    request.setAttribute("count", count);
    request.setAttribute("board", board);
    request.setAttribute("page", page.getPage());

    return "board/boardList.jsp";
}
}


