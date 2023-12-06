package com.office.library.admin.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminMemberDao {

	@Autowired // WEB-INF/spring/jdbc-context.xml 에jdbcTemplate 빈 생성
	JdbcTemplate jdbcTemplate;

	@Autowired // WEB-INF/spring/security-context.xml - 데이터를 암호화하고 복호화하는 모듈
	PasswordEncoder passwordEncoder;

	public boolean isAdminMember(String a_m_id) { // 중복체크를 하기 위해 관리자가 입력한 아이디(a_m_id)를 매개변수로 받는다.
		System.out.println("[AdminMemberDao] isAdminMember()..");

		String sql = "SELECT COUNT (*) FROM tbl_admin_member WHERE a_m_id=?"; // 쿼리문

		int result = jdbcTemplate.queryForObject(sql, Integer.class, a_m_id); // jdbcTemplate의 queryForObject를 이용해 SQL문을
																				// 실행하고 결과를 result에 받는다.
		// (SQL문,쿼리 실행 후 반환되는 데이터 타입,관리자 입력 아이디)
		if (result > 0)
			return true; // 아이디가 있는 경우
		else
			return false; // 아이디가 없는 경우
	}

	public int insertAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] insertAdminAccount()..");

		List<String> args = new ArrayList<>();

		String sql = "INSERT INTO tbl_admin_member(";
		if (adminMemberVo.getA_m_id().equals("super admin")) { // 아이디가 super admin(최고관리자)이면
			sql += "a_m_approval, "; // 관리자 승인을
			args.add("1"); // 1로 설정한다. 테이블생성시 최고관리자를 제외한 나머지는 0으로 정의.
		}

		sql += "a_m_id, ";
		args.add(adminMemberVo.getA_m_id());

		sql += "a_m_pw, ";
//			args.add(adminMemberVo.getA_m_pw());
		args.add(passwordEncoder.encode(adminMemberVo.getA_m_pw())); // encode()를 이용해 데이터르 ㄹ암호화할 수 있다.

		sql += "a_m_name, ";
		args.add(adminMemberVo.getA_m_name());

		sql += "a_m_gender, ";
		args.add(adminMemberVo.getA_m_gender());

		sql += "a_m_part, ";
		args.add(adminMemberVo.getA_m_part());

		sql += "a_m_position, ";
		args.add(adminMemberVo.getA_m_position());

		sql += "a_m_mail, ";
		args.add(adminMemberVo.getA_m_mail());

		sql += "a_m_phone, ";
		args.add(adminMemberVo.getA_m_phone());

		sql += "a_m_reg_date, a_m_mod_date) ";

		if (adminMemberVo.getA_m_id().equals("super admin")) // 아이디가 super admin이면 a_m_approval을 포함해 ?가 9개
			sql += "VALUES(?,?,?,?,?,?,?,?,?,NOW(),NOW())";
		else // 나머지는 8개
			sql += "VALUES(?,?,?,?,?,?,?,?,NOW(),NOW())";

		int result = -1;

		try {
			result = jdbcTemplate.update(sql, args.toArray()); // update(SQL문,statement값)를 통해 SQL문 실행.
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 로그인 인증을 처리하기 위해서 관리자가 입력한 정보(아이디,비밀번호)와 일치하는 관리자가 데이터베이스에 있는지 확인.
	// 서비스에서 넘겨준 adminMemberVo를 이용해서 아이디와 비밀번호가 일치하는 관리자를 데이터베이스에서 조회하고, 결과를
	// AdminMemberVo타입으로 서비스에 반환.
	public AdminMemberVo selectAdmin(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] selectAdmin()..");

		// 관리자가 입력한 아이디가 일치하고 관리자승인이 완료(1)된 회원을 조회한다.
		// 비밀번호를 비교하지 않는 이유는 비밀번호가 암호화되어 있기때문에. 비밀번호는 뒤에 복호화하여 비교.(matches)
		String sql = "SELECT * FROM tbl_admin_member WHERE a_m_id=? AND a_m_approval > 0";

		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();

		try {

			/*
			 * 3개의 파라미터를 받습니다. 첫번째는 쿼리문
			 * 두번째는 RowMapper인터페이스를 구현한 익명 클래스(데이터베이스에서 row(행)를 어딘가에 매핑하는 역할. row를 VO객체에 매핑)
			 * 세번째는 관리자가 입력한 아이디로 서비스에서 전달받은 adminMemberVO의 getter(getA_m_id())를 이용
			*/
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {	
				
			/*
			 * RowMapper를 구현한 익명 클래스는 RowMapper의 추상메서드를 구현해야하는 의무가 있는데, 바로 mapRow()입니다.
			 * mapRow()는 ResultSet과 행의 개수를 파라미터로 받습니다.
			 * rs에는 데이터베이스에서 조회된 데이터셋이 저장되어 있으며, rowNum에는 데이터셋의 현재 행 번호가 저장되어 있습니다.
			 */
			
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			//따라서 mapRow()에서 조회된 데이터를 Java데이터 형식으로 변경하는 코드를 작성해야하는데, 이떄 사용되는 데이터형식의 객체가 AdminMemberVo이다.
					AdminMemberVo adminMemberVo = new AdminMemberVo();
			//rs에 있는 데이터를 AdminMemberVo객체의 setter를 이용해서 저장, 모든데이터의 저장(set)이 끝나면 AdminMemberVo 객체를 반환하고 
			//List 타입으로 저장된 후 adminMemberVos에 할당되니다.	
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			}, adminMemberVo.getA_m_id());
			
			//관리자가 입력한 비밀번호와 RowMapper에 의해서 VO에 매핑된 비밀번호를 비교하는 코드
			//암호화된 문자열을 복호화해서 관리자가 입력한 비밀번호와 비교하여 결과를 boolean으로 반환하는 메서드는 PassWordEncoder의 matches()입니다. 
			/*	Mathces(CharSequence rawPassword, String encodedPassword)
			 * 			 암호화되지 않은 비밀번호(관리자입력), 암호화된 비밀번호(데이터베이스)
			 */
			if(!passwordEncoder.matches(adminMemberVo.getA_m_pw(), adminMemberVos.get(0).getA_m_pw()))
				adminMemberVos.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos.size() > 0 ?adminMemberVos.get(0):null;	//길이가 0보다크다면 관리자정보 조회 성공. 인덱스0에 해당하는 adminMemberVo를 서비스에 반환.
	}

	public List<AdminMemberVo> selectAdmins() {
		System.out.println("[AdminMemberDao] selectAdmins()..");
		
		String sql = "SELECT * FROM tbl_admin_member";		//모든 관리자를 조회(select)하고
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();	//rowMapper를 이용해 리스트에 저장	
		
		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {	
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
						
					return adminMemberVo;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos;
	}

	public int updateAdminAccount(int a_m_no) {
		System.out.println("[AdminMemberDao] updateAdminAccount()..");
		
		String sql = "UPDATE tbl_admin_member SET a_m_approval=1 WHERE a_m_no=?";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql,a_m_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateAdminAccount(AdminMemberVo adminMemberVo) {
		System.out.println("[AdminMemberDao] updateAdminAccount()..");
		
		String sql = "UPDATE tbl_admin_member SET a_m_name=?, a_m_gender=?,a_m_part=?,a_m_position=?,"
					+ "a_m_mail=?,a_m_phone=?,a_m_mod_date=NOW() WHERE a_m_no=?";
		
		int result = -1;
		try {
			result = jdbcTemplate.update(sql,adminMemberVo.getA_m_name(),adminMemberVo.getA_m_gender(),
					adminMemberVo.getA_m_part(),adminMemberVo.getA_m_position(),
					adminMemberVo.getA_m_mail(),adminMemberVo.getA_m_phone(),adminMemberVo.getA_m_no());			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//관리자 정보를 조회하는 메서드(오버로드)
	public AdminMemberVo selectAdmin(int a_m_no) {
		System.out.println("[AdminMemberDao] selectAdmin()..");
		
		String sql = "SELECT * FROM tbl_admin_member WHERE a_m_no=?";
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();

		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {	
				
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_approval(rs.getInt("a_m_approval"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			}, a_m_no);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos.size() > 0 ?adminMemberVos.get(0):null;
	}

	public AdminMemberVo selectAdmin(String a_m_id, String a_m_name, String a_m_mail) {
		System.out.println("[AdminMemberDao] selectAdmin()..");
		
		String sql = "SELECT * FROM tbl_admin_member "
				+ "WHERE a_m_id=? AND a_m_name=? AND a_m_mail=?";
		
		List<AdminMemberVo> adminMemberVos = new ArrayList<AdminMemberVo>();
		
		try {
			adminMemberVos = jdbcTemplate.query(sql, new RowMapper<AdminMemberVo>() {	
				
				@Override
				public AdminMemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
					AdminMemberVo adminMemberVo = new AdminMemberVo();
					adminMemberVo.setA_m_no(rs.getInt("a_m_no"));
					adminMemberVo.setA_m_id(rs.getString("a_m_id"));
					adminMemberVo.setA_m_pw(rs.getString("a_m_pw"));
					adminMemberVo.setA_m_name(rs.getString("a_m_name"));
					adminMemberVo.setA_m_gender(rs.getString("a_m_gender"));
					adminMemberVo.setA_m_part(rs.getString("a_m_part"));
					adminMemberVo.setA_m_position(rs.getString("a_m_position"));
					adminMemberVo.setA_m_mail(rs.getString("a_m_mail"));
					adminMemberVo.setA_m_phone(rs.getString("a_m_phone"));
					adminMemberVo.setA_m_reg_date(rs.getString("a_m_reg_date"));
					adminMemberVo.setA_m_mod_date(rs.getString("a_m_mod_date"));
					
					return adminMemberVo;
				}
			}, a_m_id,a_m_name,a_m_mail);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminMemberVos.size() > 0 ?adminMemberVos.get(0):null;
	}

	public int updatePassword(String a_m_id, String newPassword) {
		System.out.println("[AdminMemberDao] updatePasword()..");
		
		String sql = "UPDATE tbl_admin_member SET a_m_pw=?, a_m_mod_date=NOW() WHERE a_m_id=?";
		
		int result = -1;
		
		try {
			result = jdbcTemplate.update(sql,passwordEncoder.encode(newPassword),a_m_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
