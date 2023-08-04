package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import domain.Plan;
import util.DBManager;

public class PlanDAO {
	DBManager dbManager;
	
	public PlanDAO(DBManager dbManager) {
		this.dbManager=dbManager;
	}
	
	//일정 등록하기
	public int insert(Plan plan) {
		Connection con=null;
		PreparedStatement pstmt=null;
		int result=0;
		
		con=dbManager.connect();
		String sql="insert into plan(plan_idx, yy, mm, dd, team_idx, icon_idx)";
		sql+=" values(seq_plan.nextval, ?,?,?,?,?)";
		
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, plan.getYy());
			pstmt.setInt(2, plan.getMm());
			pstmt.setInt(3, plan.getDd());
			pstmt.setInt(4, plan.getTeam().getTeam_idx());
			pstmt.setInt(5, plan.getIcon().getIcon_idx());
			
			result=pstmt.executeUpdate(); //DML 실행
			
			if(result>0) { //plan에 레코드 입력이 성공했다면.. todo에 work의 내용을 insert
				sql="insert into todo(todo_idx, plan_idx, work) values(seq_todo.nextval,?,?)";
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(con, pstmt);
		}
		return result;
	}
	
}








