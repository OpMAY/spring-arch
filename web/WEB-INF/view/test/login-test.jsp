<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
  <!-- Required meta tags -->
  <meta charset="utf-8">
  <meta name="viewport"
        content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
        integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
        crossorigin="anonymous">
  <!-- Base -->
  <link rel="stylesheet"
        href="../../../resources/css/base/reset.css">
  <link rel="stylesheet"
        href="../../../resources/css/base/common.css">

  <!-- Theme -->
  <link rel="stylesheet"
        href="../../../resources/css/theme/theme.css">

  <!-- Layout -->
  <link rel="stylesheet"
        href="../../../resources/css/layout/layout.css">

  <!-- Component -->
  <link rel="stylesheet"
        href="../../../resources/css/component/button.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/textarea.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/media.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/list.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/dropdown.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/badge.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/breadcrumb.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/pagination.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/toast.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/card.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/carousel.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/modal.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/scrollspy.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/tab.css">
  <link rel="stylesheet"
        href="../../../resources/css/component/components.css">
  <!-- Plugin -->

  <!-- Module -->
  <link rel="stylesheet"
        href="../../../resources/css/module/modals.css">

  <!-- Meta -->

  <title>Architecture Test</title>
</head>
<body>
<div class="container">
  <div class="row">
    <div class="col-3">
      <h1>Kakao Login</h1>
      <button id="kakao"
              type="button" onclick="KakaoLoginButton()"
              class="btn btn-primary"
              value="kakao">Kakao Login Button
      </button>
      <div id="kakaoBlank">

      </div>
    </div>
    <div class="col-3">
      <h1>Google Login</h1>
      <button id="google"
              type="button"
              class="btn btn-primary">Google Login Button
      </button>
    </div>
    <div class="col-3">
      <h1>Naver Login</h1>
      <button id="naver"
              type="button"
              class="btn btn-primary">Naver Login Button
      </button>
    </div>
  </div>
</div>
<!-- Optional JavaScript; choose one of the two! -->

<!-- Option 1: jQuery and Bootstrap Bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous"></script>
<!-- Jquery 3.6.0 -->
<script src="https://code.jquery.com/jquery-3.6.0.js"
        integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk="
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-Fy6S3B9q64WdZWQUiU+q4/2Lc9npb8tCaSX9FK7E8HnRr0Jz8D6OP9dO5Vg3Q9ct"
        crossorigin="anonymous"></script>

<!-- Option 2: Separate Popper and Bootstrap JS -->
<!--
<script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js" integrity="sha384-9/reFTGAW83EW2RDu2S0VKaIzap3H66lZH81PoYlFhbGU+6BZp6G7niu735Sk7lN" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.min.js" integrity="sha384-+sLIOodYLS7CIrQpBjl+C7nPvqq+FbNUBDunl/OZv93DB7Ln/533i8e/mZXLi/P+" crossorigin="anonymous"></script>
-->
<script src="../../../resources/js/utility.js"></script>
<script src="../../../resources/js/common.js"></script>
<script src="../../../resources/js/app.js"></script>
<script src="../../../resources/js/api.js"></script>
<!-- Plugin JS-->
<!-- Module JS -->
<!-- Module JS 는 특정 페이지가 아니라 다양한 페이지에서 작동하는 부분으로 Event 및 Element 생성 및 다양한 화면에 진입했을 때
     공통적으로 로직을 수행하는 Javascript를 Module JS라고 한다.
     Selector (선택자)의 Length 및 Empty 여부를 예외처리로 해줘야한다.
     (선택자가 없으면 또는 선택자의 Length가 0이면 에러가 터질 수 있기 때문에) -->
<script src="../../../resources/js/module/modal.js"></script>
<script src="../../../resources/js/module/inspection.js"></script>
<script src="../../../resources/js/module/validation.js"></script>
<script src="../../../resources/js/module/api/login/login.js"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
<script>
    /**
     * Static JS
     * Static JS는 특정 페이지 에서만 작동하는 부분으로 Event 및 Element 생성 및 화면에 진입했을 때의
     * 해당 화면만의 특정 로직을 수행하는 Javascript를 Static JS라고 한다.
     * */




    $(document).ready(function () {
        console.log('Static JS is ready');

        new APILogin({
            type: LOGIN_TYPE.KAKAO,
            button: '#kakao',
            redirect_url: '/oauth/callback',
            get_key_url: '/sns/key',
            initialize: (options) => {
                console.log('initialize', options);
            }
        });
        // new APILogin({
        //     type: LOGIN_TYPE.GOOGLE,
        //     button: '#google',
        //     redirect_url: '/auth/oauth/callback',
        //     get_key_url: '/web/resources/assets/datas/getKey.json',
        //     initialize: (options) => {
        //         console.log('initialize', options);
        //     }
        // });
        new APILogin({
            type: LOGIN_TYPE.NAVER,
            button: '#naver',
            redirect_url: '/oauth/callback',
            get_key_url: '',
            initialize: (options) => {
                console.log('initialize', options);
            }
        });
    });



</script>
</body>
</html>
