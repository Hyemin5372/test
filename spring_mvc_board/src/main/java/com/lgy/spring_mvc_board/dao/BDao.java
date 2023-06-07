package com.lgy.spring_mvc_board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.lgy.spring_mvc_board.dto.BDto;

//DB SQL 처리
public class BDao {
	DataSource dataSource;
	
	public BDao() {
//		dbcp 방식으로 oracle 연결
		try {
			Context context=new InitialContext();
//			context.xml 에 설정(server.xml 과 달리 한번 설정으로 모든 프로젝트에서 사용가능)
//			dataSource : 조회,저장,수정,삭제 쿼리에 모두 사용 가능
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//게시판 목록 조회
	public ArrayList<BDto> list(){
//		dtos : 게시글들을 담는 객체
		ArrayList<BDto> dtos=new ArrayList<BDto>();
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		try {
			conn = dataSource.getConnection();
			String sql="select bid, bname, btitle, bcontent, bdate, bhit from mvc_board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int bid = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				
//				하나의 게시글 객체
				BDto dto = new BDto(bid, bname, btitle, bcontent, bdate, bhit);
//				게시글을 추가(dtos : 여러 게시글이 될수 있음)
				dtos.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dtos;
	}
//	시퀀스
	public void write(String bname, String btitle, String bcontent) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn = dataSource.getConnection();
//			SEQ가 시퀀스 : count 대신 사용 
//			ex) 1, 2, 3의 PK가 있는데 2번쨰 PK를 삭제 할 경우 count를 사용하면 1, 3의 PK이가 남고 
//			시퀀스를 사용 하면 1, 2가 됨
			String sql="insert into mvc_board(bid, bname, btitle, bcontent, bhit) "
					+ "VALUES(MVC_BOARD_SEQ.NEXTVAL,?,?,?,0)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, bname);
			pstmt.setString(2, btitle);
			pstmt.setString(3, bcontent);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		
		}
	}
//	게시글 하나를 리턴하기 위해서BDto 사용(strID :글번호)
	public BDto contentView(String strID) {
//	게시글 조회수 올리기
		upHit(strID);
		
		ArrayList<BDto> dtos=new ArrayList<BDto>();
		
		BDto dto = null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		try {
			conn = dataSource.getConnection();
			String sql="select bid, bname, btitle, bcontent, bdate, bhit "
						+"from mvc_board where bid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(strID));
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int bid = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				
//				하나의 게시글 객체
				dto = new BDto(bid, bname, btitle, bcontent, bdate, bhit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
//	게시글 조회수 올리기
	private void upHit(String bid) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn = dataSource.getConnection();
			String sql="update mvc_board set bhit = bhit+1 where bid=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(bid));
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		
		}
		
	}
	public void modify(String bid, String bname, String btitle, String bcontent) {

		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn = dataSource.getConnection();
			String sql="update mvc_board set bname = ?, btitle = ?, bcontent = ? where bid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bname);
			pstmt.setString(2, btitle);
			pstmt.setString(3, bcontent);
			pstmt.setInt(4, Integer.parseInt(bid));
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		
		}
		
	}
	//게시글 삭제
	public void delete(String strID) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn = dataSource.getConnection();
			String sql="delete from mvc_board where bid=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, Integer.parseInt(strID));
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		
		}
		
	}
}
