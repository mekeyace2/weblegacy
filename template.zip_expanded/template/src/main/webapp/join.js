export class member_ck{
	
	//아이디 체크
	ajax_idcheck(){
		let id = frm.MID.value.replaceAll(" ","");
		if(id==""){
			alert("아이디를 입력하셔야 합니다.");
		}else{
			fetch("./login_idck.do",{
			method : "POST",
			headers : {"content-type":"application/x-www-form-urlencoded"},
			body : "id=" + frm.MID.value
			}).then(function(aa){
				return aa.text();
			}).then(function(bb){
				console.log(bb);
			}).catch(function(error){
				console.log(error);	
			});
			
					
		}
	}
	
	//회원가입 
	join_okpage(){
		
	}	
	
}