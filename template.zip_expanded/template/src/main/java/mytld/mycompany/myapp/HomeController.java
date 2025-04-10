package mytld.mycompany.myapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;

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
	
	
	@Autowired
	BasicDataSource dbinfo;
	
	@Resource(name="membershop_DAO")
	membershop_DAO dao;
	
	
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
