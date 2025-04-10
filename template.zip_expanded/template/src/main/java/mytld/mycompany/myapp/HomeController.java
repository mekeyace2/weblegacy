package mytld.mycompany.myapp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	PrintWriter pw = null;
	
	@Autowired
	BasicDataSource dbinfo;
	
	@Resource(name="membership_DAO")
	membership_DAO dao;
	
	@PostMapping("/login_idck.do")
	public String login_idck(@RequestParam (name = "id")String id,
			ServletResponse res){
          
         try {
        	 //toUpperCase() : 소문자->대문자로 변경해준다.
        	 //toLowerCase() : 대문자->소문자로 변경해준다.
        	 this.pw= res.getWriter();
        	 String a = id.toLowerCase();
        	 this.logger.info(a);
        	 String result = this.dao.id_row(id);
        	 this.logger.info(result);
		} catch (Exception e) {
			this.logger.info(e.toString());
	  }finally {
		  this.pw.close();		  
	  }
		
		this.logger.info(id);
		return null;
	}
	
	
	//@Post => 일반로그인, Kakao api, @Get => Kakao Script 
	//일반로그인 + Kakao Script Loing => @RequestMapping
	//일반로그인 + Kakao API => @PostMapping
	@PostMapping("/ajax/web_loginok.do")
	public String web_loginok(
			@RequestParam(name="code") String code,
			@RequestParam(name="mid", required = false) String mid,
			@RequestParam(name="mpass", required = false) String mpass,
			@RequestParam(name="kakao_id", required = false) String kakao_id,
			@RequestParam(name="kakao_nicknm", required = false) String kakao_nicknm
			) {
		if(code.equals("1")) {	//일반로그인 처리
			this.logger.info(mid);			
		}
		else if(code.equals("2")) {	//카카오 로그인 처리
			this.logger.info(kakao_id);			
		}
		
		
		return null;
	}

	
	
	
	
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public String home() {
		try {
			Connection con = this.dbinfo.getConnection();
			String sql = "select count(*) as ctn from MEMBER";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			this.logger.info(rs.getString("ctn"));
			
			this.logger.info(con.toString());
			this.logger.info("테스트 진행중!!");
			
			rs.close();
			ps.close();
			con.close();
			
		}catch (Exception e) {
			this.logger.error(e.toString());
			this.logger.debug("오류발생");
		}
		return "/WEB-INF/views/home";
	}
	
}
