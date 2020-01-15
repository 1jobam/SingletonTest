package board;

import java.util.List;
import java.util.Scanner;

import board.service.BoardService;
import board.service.BoardServiceImpl;
import board.vo.BoardVO;

public class BoardMain {
	private BoardService boSer;
	
	public BoardMain() {
		boSer = BoardServiceImpl.getInstance();
	}
	
	private Scanner sc = new Scanner(System.in);
	
	
	public static void main(String[] args) {
		new BoardMain().start();
	}
	
	public void start() {
		String menu;
		
		do {
			System.out.println("======================================");
			System.out.println("게시판 관리 시스템에 오신것을 환영합니다.");
			System.out.println("--------------------------------------");
			System.out.println("1). 게시글 작성");
			System.out.println("2). 게시글 수정");
			System.out.println("3). 게시글 삭제");
			System.out.println("4). 게시글 검색");
			System.out.println("5). 게시글 전체 조회");
			System.out.println("6). 시스템 종료");
			System.out.println("======================================");
			System.out.print("메뉴를 선택하여 주세요 > ");
			menu = sc.nextLine();
			
			switch(menu) {
				case "1" :
					boardInsert();
					break;
				case "2" :
					boardUpdate();
					break;
				case "3" :
					boardDelete();
					break;
				case "4" :
					boardcheck();
					break;
				case "5" :
					boardAllSelect();
					break;
				case "6" :
					System.exit(0);
					break;
				default :
					System.out.println("잘못된 입력입니다.");
					break;
			}
			
		}while(!menu.equals("6"));
	}

	private void boardcheck() {
		System.out.println("======================================");
		System.out.println("게시글 검색을 진행하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("검색하시고 싶으신 게시글의 번호를 입력하여주세요 > ");
		int num = Integer.parseInt(sc.next());
		
		BoardVO bv = new BoardVO();
		bv.setBoardNo(num);
		
		System.out.println(" 게시글\t게시글 제목\t게시글 작성자\t게시글 작성날짜\t게시글 내용");
		
		List<BoardVO> bolist = boSer.getSearchBoard(bv);
		
		sc.nextLine(); //버퍼 비우기
		
		if(bolist.size() == 0) {
			System.out.println("조회할 게시글이 없습니다.");
			return;
		}else {
			for(BoardVO bv2 : bolist) {
				System.out.println(bv2.getBoardNo()+"\t"+bv2.getBoardTitle()+"\t"+bv2.getBoardWriter()+"\t"+bv2.getBoardDate()+"\t"+bv2.getBoardContent());
			}
		}
		System.out.println("======================================");
		System.out.println("출력 완료...");
		
	}

	private void boardUpdate() {
		System.out.println("======================================");
		System.out.println("게시글 수정을 진행하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("수정하실 게시글의 번호를 입력하여주세요 > ");
		int board_no = Integer.parseInt(sc.next());
		
		System.out.println("입력하신 게시글의 번호 : " + board_no + "입니다.");
		System.out.print("수정하실 게시글의 제목을 입력하여주세요 > ");
		String boardTitle = sc.next();
		System.out.print("수정하실 게시글의 작성자 이름을 입력하여주세요 > ");
		String boardwriter = sc.next();
		System.out.print("수정하실 게시글의 내용을 입력하여주세요 > ");
		String boardcontent = sc.next();
		
		BoardVO bv = new BoardVO();
		
		bv.setBoardNo(board_no);
		bv.setBoardTitle(boardTitle);
		bv.setBoardWriter(boardwriter);
		bv.setBoardContent(boardcontent);
		
		int cnt = boSer.updateBoard(bv);
		
		sc.nextLine(); //버퍼 비우기
		
		if(cnt > 0) {
			System.out.println("게시글 수정 성공...");
		}else {
			System.out.println("게시글 수정 실패!!!");
		}
		
		
	}

	private void boardDelete() {
		System.out.println("======================================");
		System.out.println("게시글 삭제를 진행 하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("삭제하실 게시글 번호를 입력하여주세요 > ");
		int board_no = Integer.parseInt(sc.next());
		
		BoardVO bv = new BoardVO();
		bv.setBoardNo(board_no);
		
		int cnt = boSer.deleteBoard(bv);
		
		sc.nextLine(); //버퍼 비우기
		
		if(cnt > 0) {
			System.out.println(cnt + " 번의 게시글 삭제 성공...");
		}else {
			System.out.println(cnt + " 번의 게시글 삭제 실패!!!");
		}
		
	}
	

	private void boardInsert() {
		System.out.println("======================================");
		System.out.println("게시글 등록을 진행 하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("등록하실 게시글 제목을 입력하여주세요 > ");
		String boardtitle = sc.next();
		
		System.out.print("등록하실 게시글의 작성자를 입력하여주세요 > ");
		String boardwriter = sc.next();
		
		System.out.print("등록하실 게시글의 내용을 입력하여주세요 > ");
		String boardcontent = sc.next();
		
		BoardVO bv = new BoardVO();
		bv.setBoardTitle(boardtitle);
		bv.setBoardWriter(boardwriter);
		bv.setBoardContent(boardcontent);
		
		int cnt = boSer.insertBoard(bv);
		
		sc.nextLine(); //버퍼 비우기
		
		if(cnt > 0){
			System.out.println("게시글 등록 성공...");
		}else {
			System.out.println("게시글 등록 실패!!!");
		}
		
		
	}

	private void boardAllSelect() {
		System.out.println();
		System.out.println("======================================");
		System.out.println("등록된 게시글을 전체 조회 하겠습니다.");
		System.out.println("--------------------------------------");
		
		List<BoardVO> bolist = boSer.getAllBoard();
		
		if(bolist.size() == 0) {
			System.out.println("출력할 게시글이 없습니다.");
			return;
		}else {
			for(BoardVO bov : bolist) {
				System.out.println("게시글 번호 : " + bov.getBoardNo());
				System.out.println("게시글 제목 : " + bov.getBoardTitle());
				System.out.println("게시글 작성자 : " + bov.getBoardWriter());
				System.out.println("게시글 작성날짜 : " + bov.getBoardDate());
				System.out.println("게시글 내용 : " + bov.getBoardContent());
				System.out.println("------------------------------------");
			}
		}
		System.out.println("======================================");
		System.out.println("출력 작업 완료");
		
	}


}
