package kr.user.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.controller.Action;
import kr.location.dao.LocationDAO;
import kr.user.dao.UserDAO;
import kr.user.vo.UserVO;
import kr.util.FileUtil;

public class AdminUserUpdateAction implements Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Long user_num = (Long)session.getAttribute("user_num");
		if(user_num == null) {//로그인이 되지 않은 경우
			return "redirect:/user/loginForm.do";
		}

		UserDAO dao = UserDAO.getInstance();
		Integer user_auth = (Integer)session.getAttribute("user_auth");
		if(user_auth != 9) {//관리자로 로그인하지 않은 경우
			return "common/accessDenied.jsp";
		}

		// 입력값
		long target_num = Long.parseLong(request.getParameter("user_num"));
		String name = request.getParameter("name");
		String nickname = request.getParameter("nickname");
		String phone = request.getParameter("phone");
		String passwd = request.getParameter("passwd");
		String passwd_check = request.getParameter("passwd_check");
		String region = request.getParameter("region");
		String authStr = request.getParameter("auth");
		int auth = authStr != null ? Integer.parseInt(authStr) : 2;
		int report_count = Integer.parseInt(request.getParameter("report_count"));
		int score = Integer.parseInt((request.getParameter("score")));


		// 닉네임 중복 검사 1이어야 통과
		String nicknameChecked = request.getParameter("nicknameChecked");
		if (!"1".equals(nicknameChecked)) {
			request.setAttribute("result_msg", "닉네임 중복 확인을 먼저 해주세요.");
			request.setAttribute("result_url", "user/adminUserUpdateForm.do?user_num="+target_num);
			return "common/result_view.jsp";
		}

		// 비밀번호 확인
		if (passwd != null && !passwd.trim().isEmpty() && !passwd.equals(passwd_check) && "1".equals(nicknameChecked)) {
			request.setAttribute("result_msg", "비밀번호 확인이 일치하지 않습니다.");
			request.setAttribute("result_url", "user/adminUserUpdateForm.do?user_num="+target_num);
			return "common/result_view.jsp";
		}

		// region 유효성 검사 추가
		LocationDAO locationDAO = LocationDAO.getInstance();
		if ((region == null || !locationDAO.isValidRegionCode(region)) && "1".equals(nicknameChecked) && passwd != null && !passwd.trim().isEmpty() && passwd.equals(passwd_check)) {
			request.setAttribute("result_msg", "정확한 지역명을 입력해주세요.");
			request.setAttribute("result_url", "user/adminUserUpdateForm.do?user_num=" + target_num);
			return "common/result_view.jsp";
		}

		if(auth == 0) {
			dao.deleteUser(target_num);
		}else {

			UserVO db_user = dao.getDetailMemberByAdmin(target_num);

			UserVO update_user = new UserVO();
			update_user.setUser_num(target_num);
			update_user.setName(name);
			update_user.setNickname(nickname);
			update_user.setPhone(phone.replaceAll("[-.\\s]", ""));
			update_user.setRegion_cd(region);
			update_user.setAuth(auth);
			update_user.setPasswd(passwd != null && !passwd.trim().isEmpty() ? passwd : db_user.getPasswd());
			update_user.setReport_count(report_count);
			update_user.setScore(score);

			// 프로필 사진 업로드
			String filename = FileUtil.uploadFile(request, "photo");
			if (filename != null && !filename.isEmpty()) {
				// 새 사진이 올라온 경우
				update_user.setPhoto(filename);

				// 기존 사진 삭제
				if (db_user.getPhoto() != null) {
					FileUtil.removeFile(request, db_user.getPhoto());
				}
			} else {
				// 사진 수정 안함
				update_user.setPhoto(db_user.getPhoto());
			}

			// 온도처리 - 신고횟수 증가 시, 감소 시 처리
			int originalReportCount = db_user.getReport_count();
			int diff = report_count - originalReportCount; // 차이 계산


			dao.updateUserByAdmin(update_user);

			if (diff != 0) {
				dao.modifyTemperatureUser(target_num, -20 * diff);
			}

			int reportCount = dao.checkReportCount(update_user.getUser_num());
			if(reportCount >= 3) {
				dao.deleteUserByReport(update_user.getUser_num());
			}else {
				dao.revertDeleteUserByReport(update_user.getUser_num());
			}

		}
		request.setAttribute("notice_msg", "수정이 완료되었습니다.");
		request.setAttribute("notice_url", request.getContextPath()+"/user/adminUserDetail.do?user_num="+ target_num);

		return "common/alert_view.jsp";

	}
}
