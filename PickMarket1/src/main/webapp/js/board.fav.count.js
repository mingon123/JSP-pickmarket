$(function(){
    /*============================
     * 좋아요 수 조회 (숫자만 표시)
     *============================*/
    function selectFav(){
        var boardNum = $('#output_fcount').attr('data-num'); // 게시물 번호 가져오기
        if (!boardNum) {
            console.error("게시물 번호가 없습니다.");
            return;
        }

        $.ajax({
            url: 'getFavCountAction.do',
            type: 'post',
            data: { board_num: boardNum }, // board_num 값 전달
            dataType: 'json',
            success: function(param){
                if (param.count !== undefined) {
                    $('#output_fcount').text(param.count); // 좋아요 수 출력
                } else {
                    alert('좋아요 수를 불러올 수 없습니다.');
                }
            },
            error: function(){
                alert('좋아요 수를 불러오는 중 오류 발생');
            }
        });
    }

    // 페이지 로딩 시 자동 호출
    selectFav();
});
