package kr.product.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kr.controller.Action;
import kr.location.dao.LocationDAO;
import kr.notification.dao.NotificationDAO;
import kr.product.dao.ProductDAO;
import kr.product.vo.ProductImgVO;
import kr.product.vo.ProductVO;
import kr.user.dao.UserDAO;
import kr.util.FileUtil;
import kr.util.LocationUtil;

public class ProductWriteAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		//관리자&사용자 공통		
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if
		
		//로그인한 경우
		ProductVO product = new ProductVO();
		product.setTitle(request.getParameter("title"));
		product.setContent(request.getParameter("content"));
		product.setUser_num(user_num); //작성자 회원번호
		product.setIp(request.getRemoteAddr());
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		//카테고리명 선택하면 카테고리 번호로 저장됨.
		product.setCategory_num(Integer.parseInt(request.getParameter("category_num")));

		double x_loc = Double.parseDouble(request.getParameter("x_loc"));
		double y_loc = Double.parseDouble(request.getParameter("y_loc"));
		product.setX_loc(x_loc);
		product.setY_loc(y_loc);
		
		LocationDAO locationDAO = LocationDAO.getInstance();
        String regionName = LocationUtil.getRegionName(x_loc, y_loc);
        product.setRegion_nm(regionName);
        String regionCd = locationDAO.findRegionCdByName(regionName);
        product.setRegion_cd(regionCd);
		
		
		//상품 이미지 정보 저장
		List<ProductImgVO> productImgList = new ArrayList<ProductImgVO>();
		
		//getParts()를 사용하여 모든 파일 Part를 처리
		for (Part part : request.getParts()) {
			 //"filename" 이름을 가진 파일만 처리
            if (part.getName().equals("filename") && part.getSize() > 0) {
                //파일 업로드 처리
                String filename = FileUtil.uploadFile(request, part);
                
                //ProductImgVO 객체 생성 후 파일명 설정
                ProductImgVO productImg = new ProductImgVO();
                productImg.setFilename(filename);

                //리스트에 추가
                productImgList.add(productImg);
            }
		} // foreach
		
		//상품 및 이미지 정보 DB에 저장
		ProductDAO dao = ProductDAO.getInstance();
		long product_num = dao.insertProduct(product, productImgList);
		
		UserDAO userDAO = UserDAO.getInstance();
		userDAO.modifyTemperatureUser(user_num, 2);
		
		//Refresh 정보를 응답 헤더에 추가
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		String url = null;
		if (user_auth == 9) { //관리자인 경우 관리자 목록으로
			url = request.getContextPath() + "/product/adminProductList.do";
		} else { //사용자는 사용자 목록으로
			url = request.getContextPath() + "/product/userProductList.do";
		}
									//2초 뒤에 자동으로 redirect
		response.addHeader("Refresh", "2;url="+url);
		request.setAttribute("result_title", "상품 등록 완료");
		request.setAttribute("result_msg", "성공적으로 등록되었습니다.");
		request.setAttribute("result_url", url);
		
		/*키워드 알림 전송*/
		
		List<Map<String, String>> keywordList = userDAO.getAllKeywordsWithUsers();
		NotificationDAO notifiDAO = NotificationDAO.getInstance();
		
		String title = product.getTitle();
		Set<Long> sentUsers = new HashSet<>();
		
		for (Map<String, String> entry : keywordList) {
		    Long userNum = Long.parseLong(entry.get("user_num"));
		    String keyword = entry.get("k_word");

		    if (title.contains(keyword) && !sentUsers.contains(userNum) && !userNum.equals(user_num)) {
		        String message = "[" + keyword + "] 키워드에 해당하는 상품 '" + title + "'이 등록되었어요!";
		        notifiDAO.insertNotification(userNum, message, "keyword", product_num, null);
		        sentUsers.add(userNum); // 중복 방지
		    }
		}
		/*키워드 알림 끝*/
		
		return "common/result_view.jsp";
	}

} //class
