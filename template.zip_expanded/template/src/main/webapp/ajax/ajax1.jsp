<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AJAX (GET) - 문자열, Array를 이용하여 데이터 전송</title>
</head>
<body>
<!-- 
1. ajax => form 태그로 전송하지 않습니다. <form></form> (X)
2. FormData 함수를 통하여 전송은 합니다.
3. ajax 브라우져의 URL이 변경 되지 않습니다.
4. Back-end 무조건 결과을 Front-end에게 전송해주어야 함
 -->
<input type="button" value="전송" onclick="ajax_gopage()">
</body>
<!-- 
ajax GET 통신 (선택된 상품만 Back-end 전송)
1. 같은 이름으로 문자열로 보내면 될까요?  => ./ajax1.do?product=1,2,3,4,5
2. 키를 이용하여 배열로 보내면 될까요?  => product=['1','2','4']
 -->
<script src="./ajax1.js?v=2"></script>
</html>