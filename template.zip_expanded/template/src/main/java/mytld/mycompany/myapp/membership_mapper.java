package mytld.mycompany.myapp;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface membership_mapper {
  
	public String id_row(String mid);
	
}
