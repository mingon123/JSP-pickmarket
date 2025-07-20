package kr.review.vo;

public class MannerRateVO {
	private long manner_num;
	private long rated_num; // 평가받는 회원
	private long rater_num; // 평가하는 회원
	private int[] selectedOps; // op* 배열
    private String mannerOp; // 목록 출력에 사용
    private int count;       // op 개수
	
    // 매너 항목 번호에 따른 텍스트 반환
    public String getMannerOpText(String mannerOp) {
        switch (mannerOp) {
            case "op1": return "친절하고 매너가 좋아요.";
            case "op2": return "시간 약속을 잘 지켜요.";
            case "op3": return "응답이 빨라요.";
            case "op4": return "물품 상태가 설명한 것과 같아요.";
            case "op5": return "나눔을 해주셨어요.";
            case "op6": return "불친절해요.";
            case "op7": return "시간 약속을 안 지켜요.";
            case "op8": return "채팅 메시지를 읽고도 답이 없어요.";
            case "op9": return "약속 장소에 나타나지 않았어요.";
            case "op10": return "약속 후 직전 취소했어요.";
            default: return "기타";
        }
    }
    
	public String getMannerOp() {
		return mannerOp;
	}
	public void setMannerOp(String mannerOp) {
		this.mannerOp = mannerOp;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int[] getSelectedOps() {
		return selectedOps;
	}
	public void setSelectedOps(int[] selectedOps) {
		this.selectedOps = selectedOps;
	}
	public long getManner_num() {
		return manner_num;
	}
	public void setManner_num(long manner_num) {
		this.manner_num = manner_num;
	}
	public long getRated_num() {
		return rated_num;
	}
	public void setRated_num(long rated_num) {
		this.rated_num = rated_num;
	}
	public long getRater_num() {
		return rater_num;
	}
	public void setRater_num(long rater_num) {
		this.rater_num = rater_num;
	}
}
