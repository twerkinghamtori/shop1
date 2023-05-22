package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;
import logic.User;

@Repository //@Component + dao(model)기능
public class ItemDao {
	private NamedParameterJdbcTemplate template;
	private Map<String, Object> param = new HashMap<String, Object>();
	//조회된 컬럼명과 Item 클래스의 property가 같은 값을 Item 객체로 생성
	private RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);

	@Autowired
	public void setDataSource(DataSource dataSource) { //dataSource 객체 spring-db.xml에서 생성함. 이건 왜 context.getBean()으로 안가져와도됨?
		//NamedParameterJdbcTemplate : spring framework의 jdbc 템플릿
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public List<Item> list() {
		return template.query("select * from item order by id", param, mapper);
	}
	public Item getItem(Integer id) {
		param.clear();
		param.put("id", id);
		Item item = template.queryForObject("select * from item where id=:id", param, mapper);
		return item;
	}
	public int maxId() {
		return template.queryForObject("select ifnull(max(id), 0) from item", param, Integer.class); //Integer.class : select 결과 자료형
	}
	public void insert(Item item) {
		//:id => item 객체의 프로퍼티로 설정
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		String sql = "insert into item (id, name, price, description, pictureUrl) values (:id, :name, :price, :description, :pictureUrl)";
		template.update(sql, param);
	}

	public void update(Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		String sql = "update item set name=:name, price=:price, description=:description, pictureUrl=:pictureUrl where id=:id";
		template.update(sql, param);
	}

	public void delete(Integer id) {
		param.clear();
		param.put("id", id);
		String sql = "delete from item where id=:id";
		template.update(sql, param);
	}
}
