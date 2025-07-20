package kr.searchword.action;

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.searchword.dao.SearchWordDAO;
import kr.searchword.vo.SearchWordVO;
import kr.util.StringUtil;

public class SearchWordListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, Object> mapAjax = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			mapAjax.put("result", "logout");

		}else { 
			int keyfield = Integer.parseInt(request.getParameter("keyfield"));
			SearchWordDAO searchwordDAO = SearchWordDAO.getInstance();
			List<SearchWordVO> list = searchwordDAO.getListSearchWord(user_num, keyfield);
			if(list == null) list = Collections.emptyList();

			mapAjax.put("result", "success");
			mapAjax.put("list", list);
		}
		//JSON 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}
}	
