package dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.User;

@Repository
public class UserDao {
	private NamedParameterJdbcTemplate template;
	private Map<String, Object> param = new HashMap<String, Object>();
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public void userinsert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount (userid, password, username, phoneno, postcode, address, email, birthday) values (:userid, :password, :username, :phoneno, :postcode, :address, :email, :birthday)";
		template.update(sql, param);
	}

	public User selectUserOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.queryForObject("select * from useraccount where userid=:userid", param, mapper);
	}

	public void userUpdate(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "update useraccount set username=:username, phoneno=:phoneno, postcode=:postcode, address=:address, email=:email, birthday=:birthday  where userid=:userid";
		template.update(sql, param);
	}

	public void userDelete(String userid) {
		param.clear();
		param.put("userid", userid);
		String sql = "delete from useraccount where userid=:userid";
		template.update(sql, param);
	}

	public void changePass(String chgpass, String userid) {
		param.clear();
		param.put("password", chgpass);
		param.put("userid", userid);
		String sql = "update useraccount set password=:password where userid=:userid";
		template.update(sql, param);
	}
}
