package kr.product.action;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.location.dao.LocationDAO;
import kr.location.vo.LocationVO;
import kr.product.dao.CategoryDAO;
import kr.product.dao.ProductDAO;
import kr.product.vo.CategoryVO;
import kr.product.vo.ProductVO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.PagingUtil;

public class UserProductListAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//비회원도 목록 확인 가능
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null) pageNum = "1";

        String keyfield = request.getParameter("keyfield");
        String keyword = request.getParameter("keyword");

        HttpSession session = request.getSession();
        Long user_num = (Long) session.getAttribute("user_num");

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
		if (user_num != null) {
			UserDAO userDAO = UserDAO.getInstance();
			UserVO user = userDAO.getUser(user_num);
			request.setAttribute("user", user);
			if (user.getLocationVO() != null) {
				user_region_cd = user.getLocationVO().getRegion_cd();
			}
		}
		
		if ("ALL".equalsIgnoreCase(region_nm)) {
		    region_cd = null;
		} else if ((region_cd == null || region_cd.isEmpty()) && user_num != null) {
		    region_cd = user_region_cd;
		}
		
		// 전체 또는 지역에 따라 숨김상품/카테고리/차단유저 필터링 포함
		String regionName = null;
		if (region_cd == null || "ALL".equals(region_cd)) {
			regionName = "전체지역";
		} else {
			regionName = locationDAO.findRegionNmByCode(region_cd);
		}
		
        // 지역 필터링 처리
        int count = 0;
        List<ProductVO> list = null;
        PagingUtil page;
        ProductDAO dao = ProductDAO.getInstance();
        
        if (user_num != null) {
            // 로그인 사용자용: 차단유저 + 숨김상품 + 숨김카테고리 포함 필터링
            count = dao.getProductCountByRegionBlockHide(region_cd, keyfield, keyword, user_num);
            page = new PagingUtil(keyfield, keyword, Integer.parseInt(pageNum), count, 12, 10, "userProductList.do", "&region_nm=" + region_nm);
            if (count > 0) {
                list = dao.getListProductByRegionBlockHide(page.getStartRow(), page.getEndRow(), region_cd, keyfield, keyword, user_num);
            }
        } else {
            // 비로그인 사용자: 숨김상품 + 숨김카테고리 필터링
            count = dao.getProductCountByRegionBlockHide(region_cd, keyfield, keyword, 0L);
            page = new PagingUtil(keyfield, keyword, Integer.parseInt(pageNum), count, 12, 10, "userProductList.do", "&region_nm=" + region_nm);
            if (count > 0) {
                list = dao.getListProductByRegionBlockHide(page.getStartRow(), page.getEndRow(), region_cd, keyfield, keyword, 0L);
            }
        }
        
        if (list != null) { 
            for (ProductVO p : list) {
                String code = p.getRegion_cd();
                String name = locationDAO.findRegionNmByCode(code);
                String[] sub_name = name.split(" ");
                p.setRegion_nm(sub_name[sub_name.length - 1]);
            }
        } else {
            list = new ArrayList<>();
        }

		request.setAttribute("regionName", regionName);
		request.setAttribute("locationList", locationDAO.getAllLocations());

		request.setAttribute("count", count);
        request.setAttribute("list", list);
        request.setAttribute("page", page.getPage());
        
		//JSP 경로 반환
		return "product/user_productList.jsp";
	}
} //class
