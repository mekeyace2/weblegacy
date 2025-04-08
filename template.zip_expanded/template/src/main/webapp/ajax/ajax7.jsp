<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ajax - Jquery로 전송(POST) - FormData</title>
<script src="./jquery.js"></script>
<script>
$(function(){
	$("#btn").click(function(){
		var $formdata = new FormData();
		$formdata.append("fdata","a1");
		$formdata.append("fdata","a2");
		$formdata.append("fdata","a3");
		$formdata.append("fdata","a4");
		$formdata.append("fdata","a5");
		
		$.ajax({
			url : "./ajax8.do",	
			cache: false,
			type : "POST",
			dataType : "HTML",	
			contentType : false,	//파싱되는 사항을 방지
			processData : false,	//String으로 변환하여 전송
			data : $formdata,	//formdata 로 전송
			async : true,
			success:function($result){
				console.log($result)	
			},
			error:function(){
				
			}
		});
		
	});
});
</script>
</head>
<body>
<input type="button" value="전송" id="btn">
</body>
</html>