package kr.or.ddit.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import kr.or.ddit.member.MemberMain;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.util.DBUtil2;
import kr.or.ddit.util.DBUtil3;

public class MemberDaoImpl implements IMemberDao {
	
	// Log4j를 이용한 로그남기기 위한 로거 생성
	private static final Logger sqlLogger = Logger.getLogger("log4jexam.sql.Query");
	private static final Logger paramLogger = Logger.getLogger("log4jexam.sql.Parameter");
	private static final Logger resultLogger = Logger.getLogger(MemberDaoImpl.class);
	
	private static MemberDaoImpl dao; 
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private MemberDaoImpl() {
		
	}
	
	public static MemberDaoImpl getInstance() {
		if(dao == null) {
			dao = new MemberDaoImpl();
		}
		
		return dao;
	}
	
	/**
	 * 자원반납용 메서드
	 */
	private void disConnect() {
		//사용했던 자원 반납
		if(rs != null) try {rs.close();} catch(SQLException e2) {}
		if(pstmt != null) try {pstmt.close();} catch(SQLException e2) {}
		if(stmt != null) try {stmt.close();} catch(SQLException e2) {}
		if(conn != null) try {conn.close();} catch(SQLException e2) {}
	}
	
	@Override
	public int insertMember(MemberVO mv) {
		int cnt = 0;
		
		try {
			conn = DBUtil2.getConnection();
			
			String sql = " insert into mymember (mem_id, mem_name, mem_tel, mem_addr) values (, ?, ?, ?) ";
			
			sqlLogger.warn("쿼리(DBBUG) : " + sql);
			sqlLogger.warn("쿼리(WARN) : " + sql);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, mv.getMem_id());
			pstmt.setString(2, mv.getMem_name());
			pstmt.setString(3, mv.getMem_tel());
			pstmt.setString(4, mv.getMem_addr());
			
			paramLogger.warn("파라미터 : (" + mv.getMem_id() + ", " + mv.getMem_name() + "," + mv.getMem_tel() + ", " + mv.getMem_addr() + ")");
			
			cnt = pstmt.executeUpdate();
			
			resultLogger.fatal("결과 : " + cnt);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect(); // 자원 반납
		}
		return cnt;
	}

	@Override
	public boolean getMember(String memId) {
		boolean chk = false; 
		
		try {
			conn = DBUtil2.getConnection();
			String sql = " select count(*) cnt from mymember " + " where mem_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memId);
			
			rs = pstmt.executeQuery();
			
			int cnt = 0;
			if(rs.next()) {
				cnt = rs.getInt("cnt");
			}
			
			if(cnt > 0) {
				chk = true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			chk = false;
		}finally {
			disConnect();
		}
		return chk;
	}

	@Override
	public List<MemberVO> getAllMemberList() {
		
		List<MemberVO> memList = new ArrayList<MemberVO>();
		
		try {
			conn = DBUtil3.getConnection();
			
			String sql = " select * from mymember ";
			
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				MemberVO mv = new MemberVO();
				
				mv.setMem_id(rs.getString("mem_id"));
				mv.setMem_name(rs.getString("mem_name"));
				mv.setMem_tel(rs.getString("mem_tel"));
				mv.setMem_addr(rs.getString("mem_addr"));

				memList.add(mv);
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect();
		}
		return memList;
	}

	@Override
	public int updateMember(MemberVO mv) {
		int cnt = 0;
		
		try {
			conn = DBUtil2.getConnection();
			
			String sql = " Update mymember set mem_name = ?, mem_tel = ?, mem_addr = ? where mem_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, mv.getMem_name());
			pstmt.setString(2, mv.getMem_tel());
			pstmt.setString(3, mv.getMem_addr());
			pstmt.setString(4, mv.getMem_id());
			
			cnt = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect(); // 자원반납
		}
		return cnt;
	}

	@Override
	public int deleteMembeR(String memId) {
		int cnt = 0;
		
		try {
			conn = DBUtil2.getConnection();
			String sql = " delete from mymember where mem_id = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memId);
			
			cnt = pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disConnect(); //자원반납
		}
		return cnt;
	}

	@Override
	public List<MemberVO> getSearchMember(MemberVO mv) {
		
		List<MemberVO> memList = new ArrayList<>();
		
		try {
			conn = DBUtil3.getConnection();
			
			String sql = " select * from mymember where 1=1 ";
			
			//동적 쿼리 (다이나믹 쿼리)
			if(mv.getMem_id() != null && !mv.getMem_id().equals("")){
				sql += " and mem_id = ?";
			}
			if(mv.getMem_name() != null && !mv.getMem_name().equals("")){
				sql += " and mem_name = ?";
			}
			if(mv.getMem_tel() != null && !mv.getMem_tel().equals("")){
				sql += " and mem_tel = ?";
			}
			if(mv.getMem_addr() != null && !mv.getMem_addr().equals("")){
				sql += " and mem_addr like '%' || ? || '%' ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			int index = 1;
			if(mv.getMem_id() != null && !mv.getMem_id().equals("")){
				pstmt.setString(index++, mv.getMem_id());
			}
			if(mv.getMem_name() != null && !mv.getMem_name().equals("")){
				pstmt.setString(index++, mv.getMem_name());
			}
			if(mv.getMem_tel() != null && !mv.getMem_tel().equals("")){
				pstmt.setString(index++, mv.getMem_tel());
			}
			if(mv.getMem_addr() != null && !mv.getMem_addr().equals("")){
				pstmt.setString(index++, mv.getMem_addr());
			}
			
			rs = pstmt.executeQuery(); 
			
			while(rs.next()) {
				MemberVO memVO = new MemberVO();
				
				memVO.setMem_id(rs.getString("mem_id"));
				memVO.setMem_name(rs.getString("mem_name"));
				memVO.setMem_tel(rs.getString("mem_tel"));
				memVO.setMem_addr(rs.getString("mem_addr"));
				
				memList.add(memVO);
			}
		}catch(SQLException e){
			memList = null;
			e.printStackTrace();
		}finally {
			disConnect(); // 자원반납
		}
		
		return memList;
	}

}
