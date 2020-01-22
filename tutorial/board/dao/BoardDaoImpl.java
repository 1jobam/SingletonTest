package board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import board.vo.BoardVO;
import kr.or.ddit.member.dao.MemberDaoImpl;
import kr.or.ddit.util.DBUtil2;
import kr.or.ddit.util.DBUtil3;

public class BoardDaoImpl implements BoardDao{
	
	private static final Logger sqlLogger = Logger.getLogger("log4jexam.sql.Query");
	private static final Logger paramLogger = Logger.getLogger("log4jexam.sql.Parameter");
	private static final Logger resultLogger = Logger.getLogger(BoardDaoImpl.class);
	
	private static BoardDaoImpl dao;
	
	private Connection conn;
	private Statement stat;
	private PreparedStatement psta;
	private ResultSet re;
	
	private BoardDaoImpl() {
		
	}
	
	public static BoardDaoImpl getInstance() {
		if(dao == null) {
			dao = new BoardDaoImpl();
		}
		return dao;
	}
	
	private void disconnect(){
		//자원 반납용
		if(re != null) try {re.close();} catch(SQLException e2) {}
		if(psta != null) try {psta.close();} catch(SQLException e2) {}
		if(stat != null) try {stat.close();} catch(SQLException e2) {}
		if(conn != null) try {conn.close();} catch(SQLException e2) {}

	}
	
	@Override
	public int insertBoard(BoardVO bv) {
		int cnt = 0;
		try {
			conn = DBUtil3.getConnection();
			
			String sql = " insert into jdbc_board (board_no, board_title, board_writer, board_date, board_content) values (board_seq.nextVal, ?, ?, sysdate, ?) a";
			
			sqlLogger.debug("쿼리(DBBUG) : " + sql);
			sqlLogger.warn("쿼리(WARN) : " + sql);
			
			psta = conn.prepareStatement(sql);
			
			psta.setString(1, bv.getBoardTitle());
			psta.setString(2, bv.getBoardWriter());
			psta.setString(3, bv.getBoardContent());
			
			paramLogger.debug("파라미터 : (" + bv.getBoardTitle() + ", " + bv.getBoardWriter() + "," + bv.getBoardContent() + ")");
			
			cnt = psta.executeUpdate();
			
			resultLogger.fatal("결과 : " + cnt);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		return cnt;
	}
	
	@Override
	public int updateBoard(BoardVO bv) {
		int chk = 0;
		
		try {
			conn = DBUtil3.getConnection();
			
			String sql = " update jdbc_board set board_title = ?, board_writer = ?, board_content = ? where board_no = ? ";
			
			psta = conn.prepareStatement(sql);
			
			psta.setString(1, bv.getBoardTitle());
			psta.setString(2, bv.getBoardWriter());
			psta.setString(3, bv.getBoardContent());
			psta.setInt(4, bv.getBoardNo());
			
			chk = psta.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		
		return chk;
	}
	@Override
	public int deleteBoard(BoardVO bv) {
		int chk = 0;
		try {
			conn = DBUtil3.getConnection();
			
			String sql = " delete from jdbc_board where board_no = ? ";
			
			psta = conn.prepareStatement(sql);
			
			psta.setInt(1, bv.getBoardNo());
			
			chk = psta.executeUpdate();
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		return chk;
	}
	@Override
	public List<BoardVO> getAllBoard() {
		
		List<BoardVO> bolist = new ArrayList<BoardVO>();
		
		try {
			conn = DBUtil2.getConnection();
			
			stat = conn.createStatement();
			
			String sql = " select * from jdbc_board ";
			
			re = stat.executeQuery(sql);
			
			while(re.next()) {
				BoardVO bv = new BoardVO();
				bv.setBoardNo(re.getInt("board_no"));
				bv.setBoardTitle(re.getString("board_title"));
				bv.setBoardWriter(re.getString("board_writer"));
				bv.setBoardDate(re.getDate("board_date"));
				bv.setBoardContent(re.getString("board_content"));
				
				bolist.add(bv);
			}
			
			}catch(SQLException e) {
				e.printStackTrace();
			}finally {
				disconnect();
			}
		return bolist;
	}
	@Override
	public List<BoardVO> getSearchBoard(BoardVO bv) {
		List<BoardVO> bolist = new ArrayList<BoardVO>();
		
		try {
			
			conn = DBUtil2.getConnection();
			
			String sql = " select * from jdbc_board where board_no = ?";
			
			psta = conn.prepareStatement(sql);
			
			psta.setInt(1, bv.getBoardNo());
			
			re = psta.executeQuery();
			
			while(re.next()) {
				BoardVO bv2 = new BoardVO();
				
				bv2.setBoardNo(re.getInt("board_no"));
				bv2.setBoardTitle(re.getString("board_title"));
				bv2.setBoardWriter(re.getString("board_writer"));
				bv2.setBoardDate(re.getDate("board_date"));
				bv2.setBoardContent(re.getString("board_content"));
				
				bolist.add(bv2);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		
		return bolist;
	}

}
