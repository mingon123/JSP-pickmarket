package kr.product.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.product.dao.CategoryDAO;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;
import kr.product.vo.ProductVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.LocationUtil;
import kr.util.PagingUtil;

public class MyProductListAction implements Action{
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num==null) {
			return "redirect:/user/loginForm.do";
		}
		
		String target_num = request.getParameter("user_num");
		Long userNum = (target_num != null) ? Long.parseLong(target_num) : user_num;
		
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) pageNum = "1";

        String keyfield = request.getParameter("keyfield");
        String keyword = request.getParameter("keyword");

        ProductDAO dao = ProductDAO.getInstance();
        // 내 상품 갯수 조회
        int count = dao.getMyProductCount(keyfield, keyword, userNum);
        PagingUtil page = new PagingUtil(keyfield,keyword,Integer.parseInt(pageNum),count,10,10,"myProductList.do","&user_num=" + userNum);

        // 전체 상품 목록 조회
        List<ProductVO> tempList = dao.getMyListProduct(page.getStartRow(), page.getEndRow(), keyfield, keyword, userNum);
        List<ProductVO> list;
		for (ProductVO product : tempList) {
			String regionName = LocationUtil.getRegionName(product.getX_loc(), product.getY_loc());
			String[] size = regionName.split(" ");
	        product.setRegion_nm(regionName.split(" ")[size.length-1]);
	    }
		
        // 로그인한 사용자와 조회 대상이 다르면 숨김 카테고리 필터 적용
        if (!user_num.equals(userNum)) {
            CategoryDAO categoryDAO = CategoryDAO.getInstance();
            List<CategoryVO> categoryList = categoryDAO.getCategoryStatus(1); // 숨김 아님만
            Set<Long> yesCategory = new HashSet<>();
            for (CategoryVO c : categoryList) {
                yesCategory.add(c.getCategory_num());
            }

            // 필터링된 리스트 생성
            list = new ArrayList<>();
            for (ProductVO p : tempList) {
                if (yesCategory.contains(p.getCategory_num())) { // 숨겨진 카테고리는 제외
                    list.add(p); 
                }
            }
        } else {
            // 본인일 경우 전부 출력
            list = tempList;
        }
        
		UserDAO userDao = UserDAO.getInstance();
		int productCount = dao.getMyProductCount(keyfield, keyword, userNum);
		UserVO target_user = userDao.getUser(userNum);
		String nickname = target_user.getNickname();
		UserVO login_user = userDao.getUser(user_num);
        
        request.setAttribute("count", count);
        request.setAttribute("list", list);
        request.setAttribute("page", page.getPage());
        request.setAttribute("productCount", productCount);
        request.setAttribute("nickname", nickname);
        request.setAttribute("userNum", userNum); // 해당페이지 회원번호
        request.setAttribute("user", login_user); // 현재 접속한 계정
        
        return "product/myProductList.jsp";
	}
	
}
