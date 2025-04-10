package mytld.mycompany.myapp;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("membership_DAO")
public class membership_DAO implements membership_mapper {

	@Resource(name="sqltemp")
	public SqlSessionTemplate st;

	@Override
	public String id_row(String mid) {
		String result = null;
		try {
			//id는 대문자 소문자를 구분한다.
			result = st.selectOne("id_row",mid);
		}catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}
	
}
