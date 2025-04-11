package mytld.mycompany.myapp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbcp.BasicDataSource;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;



@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	PrintWriter pw = null;
	
	@Autowired
	BasicDataSource dbinfo;
	
	@Resource(name="membership_DAO")
	membership_DAO dao;
	
	@Resource(name = "md5")
	md5 md5;
		
	HttpSession session = null;
	
	
	//API Patch로 개인정보 값 수정
	@PatchMapping("/ajax/myinfo_modify.do/{key}")
	public String myinfo_modify(ServletResponse res,
			@PathVariable("key")String key,
			@RequestBody String datainfo ) {
		try {
			this.pw = res.getWriter();
			if(key.equals("mykey")) {
				JSONObject jo = new JSONObject(datainfo); //=>Map
				
				Map<String, String> userdata = new HashMap<String, String>();
				
				//System.out.println(jo.keySet());
				for(String k : jo.keySet()) {
					if(!jo.get(k).equals("")) {
						if(k.equals("MPASS")) {	//암호화
							userdata.put(k, this.md5.md5_pass(jo.get(k).toString()));
						}
						else {
							userdata.put(k, jo.get(k).toString());
						}
					}
				}
				this.logger.info(userdata.toString());
				int result = this.dao.id_update(userdata);
				if(result > 0) {				
					this.pw.write("ok");
				}
				else {
					this.pw.write("no");
				}
			}
			else {
				this.pw.write("key_error");	
			}
		}catch (Exception e) {
			System.out.println(e);
			this.pw.write("error");
		}finally {
			this.pw.close();
		}
			
		return null;
	}
	
	
	//로그아웃
	@GetMapping("/ajax/logout.do")
	public String logout(HttpServletRequest req, Model m) {
		this.session = req.getSession();
		this.session.invalidate();
		
		m.addAttribute("msg","alert('정상적으로 로그아웃 되었습니다.'); location.href='../kakao2.jsp';");
		
		return "joinok";
	}
	
	//로그인 사용자 정보 출력하는 페이지
	@GetMapping("/ajax/myinfo.do")
	public String myinfo(@SessionAttribute("MID")String MID,Model m) {
		
		List<membership_DTO> mydata = this.dao.id_info(MID, "");
		m.addAttribute("MID",MID);
		m.addAttribute("mydata",mydata);
		
		return "/myinfo";
	}
	
	
	//회원가입
	@PostMapping("/joinok.do")
	public String joinok(@ModelAttribute membership_DTO dto, Model m) {
		String pw = dto.getMPASS();
		//사용자가 입력한 값을 MD5로 암호화 한 후 DTO에서 해당 메소드 setter로 이관 
		dto.setMPASS(this.md5.md5_pass(pw));
		try {
			int result = this.dao.join_insert(dto);
			if(result > 0) {
			m.addAttribute("msg","alert('정상적으로 회원가입이 완료 되었습니다.'); "
					+ "location.href='./kakao2.jsp';");
			}
			else {
				m.addAttribute("msg","alert('회원가입이 완료되지 않았습니다.'); "
						+ "history.go(-1);");
			}
		}catch (Exception e) {
			this.logger.info(e.toString());
		}	
		
		return null;
	}
	
	
	
	@PostMapping("/login_idck.do")
	public String login_idck(@RequestParam (name = "id")String id,
			ServletResponse res){
          
         try {
        	 //toUpperCase() : 소문자->대문자로 변경해준다.
        	 //toLowerCase() : 대문자->소문자로 변경해준다.
        	 this.pw= res.getWriter();
 
        	 String result = this.dao.id_row(id.toLowerCase());
        	 if(result == null || result.equals("0")) {
        		 this.pw.write("ok");
        	 }else {
        		 this.pw.write("no");
        	 }
        	 
        	 
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
	/*
	  ServletRequest, ServletResponse => session 불가능  
	  HttpServletRequest, HttpServletResponse  => session 사용가능
	 */
	@PostMapping("/ajax/web_loginok.do")
	public String web_loginok(
			@RequestParam(name="code") String code,
			@RequestParam(name="mid", required = false) String mid,
			@RequestParam(name="mpass", required = false) String mpass,
			@RequestParam(name="kakao_id", required = false) String kakao_id,
			@RequestParam(name="kakao_nicknm", required = false) String kakao_nicknm,
			Model m, HttpServletRequest req) throws Exception {
		
		this.session = req.getSession();
				
		if(code.equals("1")) {	//일반로그인 처리
			String pw = this.md5.md5_pass(mpass);	//사용자가 입력한 값을 암호화 하여 회신
			
			List<membership_DTO> all = this.dao.id_info(mid, pw);
			String msg = null;
			if(all.size() > 0) {
				//해당 로그인시 아이디를 session으로 등록함
				this.session.setAttribute("MID", all.get(0).getMID());
				msg = "alert('로그인 하였습니다.'); location.href='./myinfo.do';";
			}else {
				msg = "alert('아이디 및 패스워드를 확인하세요'); history.go(-1);";
			}
			m.addAttribute("msg",msg);
			
		}
		else if(code.equals("2")) {	//카카오 로그인 처리
			List<membership_DTO> all = this.dao.id_info(kakao_id, "");
			String msg = null;
			if(all.size() > 0) {
				//해당 로그인시 아이디를 session으로 등록함
				this.session.setAttribute("MID", all.get(0).getMID());
				msg = "alert('로그인 하셨습니다.'); location.href='./myinfo.do';";
			}else {
				//sessionStorage를 이용하여 간편회원가입을 등록시킬려고 함 단, 닉네임일 경우 특수문자
				//를 사용할 수 있으므로 Storage 생성시 ''로 변수값을 적용하여 처리해야 함
				msg = "alert('카카오 사용자로 로그인시 간편회원가입이 필요합니다.');"
						+ "sessionStorage.setItem('mid','"+ kakao_id +"');"
						+ "sessionStorage.setItem('mnick','"+ kakao_nicknm +"');"
						+ "location.href='../join.jsp';";
			}
			m.addAttribute("msg",msg);
		}
		return "joinok";
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
