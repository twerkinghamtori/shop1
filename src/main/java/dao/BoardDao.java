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

import logic.Board;

@Repository
public class BoardDao {
	private NamedParameterJdbcTemplate template;
	private Map<String, Object> param = new HashMap<String, Object>();
	private RowMapper<Board> mapper = new BeanPropertyRowMapper<Board>(Board.class);
	
	private String select = "select num, writer, pass, title, content, file1 fileurl, regdate, readcnt, grp, grplevel, grpstep, boardid from board";
	
	@Autowired
	public void setDataSource(DataSource dataSource) { 
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public void insert(Board board) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(board);
		String sql = "insert into board values (:num, :writer, :pass, :title, :content, :fileurl, :boardid, now(), 0, :grp, :grplevel, :grpstep)";
		template.update(sql, param);
	}

	public int maxnum() {
		return template.queryForObject("select ifnull(max(num), 0) from board", param, Integer.class);
	}

	public int count(String boardid, String searchType, String searchContent) {
		param.clear();
		param.put("boardid", boardid);
		param.put("searchType", searchType);
		param.put("searchContent", "%" + searchContent + "%");
		String sql = "select count(*) from board where boardid=:boardid ";
		if(!searchType.equals("") && !searchContent.equals("")) {
			sql += " and ";
			sql += searchType + " like ";
			sql += " :searchContent";
		}
		return template.queryForObject(sql, param, Integer.class);
	}

	public List<Board> list(Integer pageNum, int limit, String boardid, String searchType, String searchContent) {
		param.clear();		
		param.put("s", (pageNum-1)*limit);
		param.put("e", limit);
		param.put("b", boardid);
		param.put("searchContent", "%" + searchContent + "%");
		
		String sql = select;
		sql += " where boardid=:b ";
		if(!searchType.equals("") && !searchContent.equals("")) {
			sql += " and ";
			sql += searchType + " like ";
			sql += " :searchContent";
		}
		sql += " order by grp desc, grpstep asc limit :s, :e";
		return template.query(sql, param, mapper);
	}

	public Board selectOne(int num) {
		String sql = select;
		sql += " where num="+num;
		return template.queryForObject(sql, param, mapper);
	}

	public void addReadCnt(int num) {
		param.clear();
		param.put("num", num);
		String sql = "update board set readcnt=readcnt+1 where num=:num";
		template.update(sql, param);		
	}

	public void insertReply(Board b) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(b);
		String sql = "insert into board values (:num, :writer, :pass, :title, :content, :fileurl, :boardid, now(), 0, :grp, :grplevel, :grpstep)";
		template.update(sql, param);
	}

	public void grpStepAdd(Board b) {
		param.clear();
		param.put("grp", b.getGrp());
		param.put("grpstep", b.getGrpstep());
		String sql = "update board set grpstep=grpstep+1 where grp=:grp and grpstep>:grpstep";
		template.update(sql, param);
	}

}
