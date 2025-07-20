$(function(){
    //찜 상태 조회
    $('.heart-icon img').each(function() {
        const $img = $(this);
        const productNum = $img.attr('data-num');
        $.ajax({
            url: 'getFav.do',
            type: 'post',
            data: {product_num: productNum},
            dataType: 'json',
            success: function(param) {
                if (param.status === 'yesFav') {
                    $img.attr('src', '../images/heart.png');
                } else {
                    $img.attr('src', '../images/unheart.png');
                }
            },
            error: function() {
                alert('네트워크 오류 발생');
            }
        });
    });

    //찜 등록/삭제 처리
    $('.heart-icon img').click(function() {
        const $img = $(this);
        const productNum = $img.attr('data-num');
        $.ajax({
            url: 'writeFav.do',
            type: 'post',
            data: {product_num: productNum},
            dataType: 'json',
            success: function(param) {
                if (param.result === 'logout') {
                    alert('로그인 후 이용해주세요!');
                } else if (param.result === 'success') {
                    if (param.status === 'yesFav') {
                        $img.attr('src', '../images/heart.png');
                    } else {
                        $img.attr('src', '../images/unheart.png');
                    }
                } else {
                    alert('찜 등록/삭제 오류 발생');
                }
            },
            error: function() {
                alert('네트워크 오류 발생');
            }
        });
    });
});