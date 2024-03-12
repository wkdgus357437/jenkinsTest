package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {


	private Connection conn; // 데이터 베이스 접근
	private ResultSet rs; // 정보 담을 곳
	
	
	public BbsDAO() {
		try {
			//어느 프로젝트에 연결할 것인지
			String dbURL ="jdbc:mysql://localhost:3306/BoardStudy";
			
			//MySQL의 id,password 정보
			String dbID= "root";
			String dbPassword="jjhyun6582";
			
			//연결
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL,dbID,dbPassword);
					
		} catch (Exception e) {
			//오류가 무엇인지 띄우기
			e.printStackTrace();
		}
	}
	
	//현재 날짜(시간)
	public String getDate() {
		String SQL="select now()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; //데이터베이스 오류를 빈 문자열로 하여금 알게 하기
	}
	
	
	public int getNext() {
		String SQL="select bbsID from bbs order by bbsID desc";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1)+1;
			}
			return 1; //첫번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	//글쓰기 처리
	 public int write(String bbsTitle, String userID, String bbsContent) {
		 String SQL="INSERT INTO BBS VALUES(?,?,?,?,?,?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL); 
				pstmt.setInt(1, getNext());
				pstmt.setString(2, bbsTitle);
				pstmt.setString(3, userID);
				pstmt.setString(4, getDate());
				pstmt.setString(5, bbsContent);
				pstmt.setInt(6, 1);
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
	 }
	 
	 public ArrayList<Bbs> getList(int pageNumber){
		 String SQL="SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		 ArrayList<Bbs> list = new ArrayList<Bbs>();
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL); 
				pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					Bbs bbs = new Bbs();
					
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					
					list.add(bbs);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list; //데이터베이스 오류
		 
	 }
	 
	 //page 처리
	 public boolean nextPage(int pageNumber) {
		 String SQL="SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
	
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL); 
				pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					return true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
	 }
	 
	 //글 불러오기(view)
	 public Bbs getBbs(int bbsID) {
		 String SQL="SELECT * FROM BBS WHERE bbsID = ?";
			
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL); 
				pstmt.setInt(1,bbsID);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					Bbs bbs = new Bbs();
					
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					
					return bbs;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	 }
	 
	 public int update(int bbsID,String bbsTitle, String bbsContent) {
		 String SQL="UPDATE BBS SET bbsTitle = ? , bbsContent = ? WHERE bbsID = ? ";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL); 
				pstmt.setString(1, bbsTitle);
				pstmt.setString(2, bbsContent);
				pstmt.setInt(3, bbsID);
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
	 }
	 
	 //글을 삭제 해도 정보가 남아 있을 수 있게 1과 0으로 구분, 1은 게시된 글, 0은 삭제된 글로 표현하여 DB에 저장
	 public int delete(int bbsID) {
		 String SQL="UPDATE BBS SET bbsAvailable = 0 WHERE bbsID =? ";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL); 
				pstmt.setInt(1, bbsID);
				
				return pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; //데이터베이스 오류
	 }
	
}
