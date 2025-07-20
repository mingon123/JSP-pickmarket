package kr.searchword.action;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.searchword.dao.SearchWordDAO;
import kr.util.StringUtil;

public class WriteSearchWordAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, String> mapAjax = new HashMap<String,String>();
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if(user_num==null) {
			mapAjax.put("result", "logout");
		}else {
			String word = StringUtil.parseQuot(StringUtil.useNoHtml(request.getParameter("word")));
			int keyfield = Integer.parseInt(request.getParameter("keyfield"));
			SearchWordDAO searchwordDAO = SearchWordDAO.getInstance();

			int count = searchwordDAO.getSearchWordCount(user_num,keyfield);
			boolean haveWord = searchwordDAO.checkHaveSearchWord(user_num, word, keyfield);
			
			if (haveWord) { //이미 있는 경우 update
				searchwordDAO.updateSearchWord(user_num, word, keyfield);
			} else { //없는 경우 최대 개수보다 많으면 삭제
				if (count > 4) {
					long s_num = searchwordDAO.getOldSearchWord(user_num, keyfield);
					searchwordDAO.deleteSearchWord(s_num);
				}
				searchwordDAO.insertSearchWord(user_num, word, keyfield);
			}

			mapAjax.put("result", "success");	
		}

		//JSON에 문자열로 변환
		return StringUtil.parseJSON(request, mapAjax);
	}
}	
