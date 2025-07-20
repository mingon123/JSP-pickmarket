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
import kr.product.vo.ProductFavVO;
import kr.product.vo.ProductImgVO;
import kr.product.vo.ProductVO;
import kr.user.dao.UserDAO;
import kr.util.FileUtil;
import kr.util.LocationUtil;

public class ProductModifyAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//관리자&사용자 공통 (TODO: 공통 사용여부 재검토요망)
		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		
		if (user_num == null) { //로그인 되지 않은 경우
			return "redirect:/user/loginForm.do";
		} // if
		
		//로그인한 경우
		long product_num = Long.parseLong(request.getParameter("product_num"));
		
		//DB에 저장된 정보 읽기
		ProductDAO dao = ProductDAO.getInstance();
		ProductVO db_product = dao.getProduct(product_num);
		
		//전송된 정보 저장
		ProductVO product = new ProductVO();
		product.setProduct_num(product_num);
		product.setTitle(request.getParameter("title"));
		product.setCategory_num(Integer.parseInt(request.getParameter("category_num")));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setContent(request.getParameter("content"));
		product.setState(Integer.parseInt(request.getParameter("state")));
		product.setIp(request.getRemoteAddr());		
		double x_loc = Double.parseDouble(request.getParameter("x_loc"));
		double y_loc = Double.parseDouble(request.getParameter("y_loc"));
		product.setX_loc(x_loc);
		product.setY_loc(y_loc);
		
		LocationDAO locationDAO = LocationDAO.getInstance();
        String regionName = LocationUtil.getRegionName(x_loc, y_loc);
        product.setRegion_nm(regionName);
        String regionCd = locationDAO.findRegionCdByName(regionName);
        product.setRegion_cd(regionCd);

		
		//사진 수정 처리
        //사진 추가
        List<ProductImgVO> newImgList = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals("filename") && part.getSize() > 0) {
                String filename = FileUtil.uploadFile(request, part);
                ProductImgVO img = new ProductImgVO();
                img.setFilename(filename);
                newImgList.add(img);
            } //if
        } //foreach
        if (!newImgList.isEmpty()) {
            dao.insertProductImages(product_num, newImgList);
        } //if

        //기존 사진 삭제
        String[] deleteImageIds = request.getParameterValues("deleteImageIds");
        boolean thumbnailDeleted = false;

        if (deleteImageIds != null) {
            for (String filename : deleteImageIds) {
                FileUtil.removeFile(request, filename);
                dao.deleteProductImage(product_num, filename);

                if (filename.equals(db_product.getThumbnail_img())) {
                    thumbnailDeleted = true;
                } //if
            } //foreach
        } //if
        
        // 썸네일 이미지 설정
        setThumbnail(product, dao, product_num, newImgList, thumbnailDeleted, db_product.getThumbnail_img());
				
		//중고상품 정보 수정
		dao.updateProduct(product);
		
		//자바스크립트로 처리할 예정
		request.setAttribute("notice_msg", "정상적으로 수정되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/product/userProductDetail.do?product_num="+product_num);
		
		/*찜 알림 전송*/
		NotificationDAO notifiDAO = NotificationDAO.getInstance();
		List<Long> userList = dao.getUsersNumByProductFav(product_num);
		String type = "favorite";
		// 가격 변동 시
		if(db_product.getPrice() != product.getPrice() && db_product.getState() != product.getState()) {
			String message = "'"+product.getTitle()+"' 상품의 가격/상태 변경되었어요!";
			
			for (Long users_num : userList) {
				if(!users_num.equals(user_num)) {
					notifiDAO.insertNotification(users_num, message, type, product_num, null);
				}
			}
			
		}else {
			if(db_product.getPrice() != product.getPrice()) {
				String message = "'"+product.getTitle()+"' 상품의 가격이 변경되었어요!";
				
				for (Long users_num : userList) {
					if(!users_num.equals(user_num)) {
						notifiDAO.insertNotification(users_num, message, type, product_num, null);
					}
				}
			}else if(db_product.getState() != product.getState()) {// 판매 상태 변경 시
				String message = "'"+product.getTitle()+"' 상품의 상태가 변경되었어요!";
				
				for (Long users_num : userList) {
					if(!users_num.equals(user_num)) {
						notifiDAO.insertNotification(users_num, message, type, product_num, null);
					}
				}
			}
		}
		/*찜 알림 끝*/
		
		
		/*키워드 알림 전송*/
		UserDAO userDAO = UserDAO.getInstance();
		List<Map<String, String>> keywordList = userDAO.getAllKeywordsWithUsers();
		
		String title = product.getTitle();
		Set<Long> sentUsers = new HashSet<>();
		
		for (Map<String, String> entry : keywordList) {
		    Long userNum = Long.parseLong(entry.get("user_num"));
		    String keyword = entry.get("k_word");

		    if (title.contains(keyword) && !sentUsers.contains(userNum) && !userNum.equals(user_num)) {
		        String message = "[" + keyword + "] 키워드에 해당하는 상품 '" + title + "'가 변경되었어요!";
		        notifiDAO.insertNotification(userNum, message, "keyword", product_num, null);
		        sentUsers.add(userNum); // 중복 방지
		    }
		}
		/*키워드 알림 끝*/
		
		return "common/alert_view.jsp";
	}
	
    // 썸네일 이미지 자동 설정 메서드
    private void setThumbnail(ProductVO product, ProductDAO dao, long product_num,
                                      List<ProductImgVO> newImgList, boolean thumbnailDeleted, String oldThumbnail) throws Exception {
        // 썸네일 삭제된 경우
        if (thumbnailDeleted) {
            if (!newImgList.isEmpty()) {
                product.setThumbnail_img(newImgList.get(0).getFilename());
                return;
            } //if
            List<ProductImgVO> remainingImgs = dao.getProductImages(product_num);
            if (!remainingImgs.isEmpty()) {
                product.setThumbnail_img(remainingImgs.get(0).getFilename());
            } else {
                product.setThumbnail_img(null);
            } //if-else
        } //if

        // 썸네일 이미지가 설정되지 않은 경우, 새로 추가한 이미지나 기존 이미지에서 첫 번째 이미지로 설정
        if (product.getThumbnail_img() == null) {
            List<ProductImgVO> imgs = dao.getProductImages(product_num);
            if (!imgs.isEmpty()) {
                product.setThumbnail_img(imgs.get(0).getFilename());
            } //if
        } //if
    } //setThumbnail

} //class



