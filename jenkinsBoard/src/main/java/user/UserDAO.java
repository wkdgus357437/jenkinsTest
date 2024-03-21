package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	private Connection conn; // 데이터 베이스 접근
	private PreparedStatement pstmt; // 
	private ResultSet rs; // 정보 담을 곳
	
	//mysql 실직적으로 접속하는 정보
	public UserDAO() {
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
	
	//로그인 했을 때
	public int login(String userID,String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID= ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; //로그인 성공
				}else {
					return 0; //비밀번호 불일치
				}
				
			}
			return -1; //아이디가 없음
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; //데이터베이스 오류
	}
	
	//회원가입
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	

}
