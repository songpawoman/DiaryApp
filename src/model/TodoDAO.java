package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Plan;
import domain.Todo;
import util.DBManager;

public class TodoDAO {
	DBManager dbManager;
	
	public TodoDAO(DBManager dbManager) {
		this.dbManager=dbManager;
	}
	
	
	public int insert(Todo todo) {
		Connection con=null;
		PreparedStatement pstmt=null;
		int result=0;
		
		con=dbManager.connect();
		
		StringBuilder sb=new StringBuilder();
		sb.append("insert into todo(todo_idx, plan_idx, work)");
		sb.append(" values(seq_todo.nextval, ?, ?)");
		
		try {
			pstmt=con.prepareStatement(sb.toString());
			pstmt.setInt(1, todo.getPlan().getPlan_idx()); //어떤일정에 대한 할일인데?
			pstmt.setString(2, todo.getWork());
			
			result=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(con, pstmt);
		}
		return result;
	}
	
	//넘겨받은 fkey 조건으로 레코드 가져오기 
	public List selectAllByPlanIdx(int plan_idx) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List list=new ArrayList();
		
		con=dbManager.connect();
		
		String sql="select * from todo where plan_idx=?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, plan_idx);
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				Todo todo = new Todo();
				todo.setTodo_idx(rs.getInt("todo_idx"));
				
				Plan plan = new Plan();
				plan.setPlan_idx(rs.getInt("plan_idx"));
				todo.setPlan(plan);
				todo.setWork(rs.getString("work"));
				
				list.add(todo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(con, pstmt, rs);
		}
		return list;	
	}
}







