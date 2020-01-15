package board.service;

import java.util.List;

import board.dao.BoardDao;
import board.dao.BoardDaoImpl;
import board.vo.BoardVO;

public class BoardServiceImpl implements BoardService {

	private BoardDao bodao;
	
	private static BoardServiceImpl service;
	
	private BoardServiceImpl() {
		bodao = BoardDaoImpl.getInstance();
	}
	
	public static BoardServiceImpl getInstance() {
		if(service == null) {
			service = new BoardServiceImpl();
		}
		return service;
	}
	
	@Override
	public int insertBoard(BoardVO bv) {
		return bodao.insertBoard(bv);
	}

	@Override
	public int updateBoard(BoardVO bv) {
		return bodao.updateBoard(bv);
	}

	@Override
	public int deleteBoard(BoardVO bv) {
		return bodao.deleteBoard(bv);
	}

	@Override
	public List<BoardVO> getAllBoard() {
		return bodao.getAllBoard();
	}

	@Override
	public List<BoardVO> getSearchBoard(BoardVO bv) {
		return bodao.getSearchBoard(bv);
	}

}
