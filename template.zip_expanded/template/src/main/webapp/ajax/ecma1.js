document.querySelector("#btn").addEventListener("click",function(){
	//new box().abc("hong");	//클래스 호출 및 메소드 호출
	new box2().abc("kim");
	new box2().bbb();
});

var msg = "테스트!!!";

class box{	//클래스
	abc(data){	//메소드
		this.msg = data + "데이터 확인";
		console.log(this.msg); 
	}	
}

class box2 extends box{
	bbb(){
		console.log("상속 받은 클래스 bbb 메소드");
	}	
}


