$(function() {
  // 사진 선택하면 미리보기 띄우기
  $('#upload-photo').on('change', function(event) {
    console.log('파일 선택 감지됨');
    const file = event.target.files[0];
    if (file) {
      console.log('파일 이름:', file.name);
      const reader = new FileReader();
      reader.onload = function(e) {
        console.log('파일 읽기 완료');
        $('#preview-image').attr('src', e.target.result);
        $('#photo-preview').show();
      }
      reader.readAsDataURL(file);
    } else {
      console.log('파일 없음');
      $('#photo-preview').hide();
    }
  });


  // X 버튼 누르면 미리보기 삭제
  $(document).on('click', '#remove-preview', function() {
    $('#preview-image').attr('src', ''); // 사진 비움
    $('#photo-preview').hide(); // 미리보기 통째로 숨김
    $('#upload-photo').val(''); // 파일 input도 초기화
  });
});
