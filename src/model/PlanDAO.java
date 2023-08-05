package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Icon;
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
		ResultSet rs=null; //insert 에 의해 발생되는 pk값을 가져오기 위해...
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
			
			//insert가 완료되면  plan DTO안의 plan_idx값을, 방금들어간 시퀀스값으로 대입!!!
			
			//현재 나에 의해 즉 내가 발생시킨 최신 시퀀스만을 얻어오기
			sql="select seq_plan.currval as plan_idx from dual";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			//rs가 보유한 최신 시퀀스값을 꺼내기 
			if(rs.next()) { //존재한다면..
				//plan_idx가 0이었던 상태에서 드뎌, 시퀀스값으로 대체되는 시점...
				plan.setPlan_idx(rs.getInt("plan_idx"));
			};
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(con, pstmt, rs);
		}
		return result;
	}
	
	//달력의 날짜와 일차하는 일정가져오기 
	public List selectAll(Plan plan) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List list=new ArrayList();
		TodoDAO todoDAO=new TodoDAO(dbManager);
		
		con=dbManager.connect();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select p.plan_idx as plan_idx, yy, mm, dd, filename, work");
		sb.append(" from icon c, plan p, todo t");
		sb.append(" where ");
		sb.append(" c.icon_idx=p.icon_idx and");
		sb.append(" p.plan_idx = t.plan_idx and team_idx=?");
		sb.append(" and yy=? and mm=?");
		
		System.out.println(sb.toString());
		
		try {
			pstmt=con.prepareStatement(sb.toString());
			pstmt.setInt(1, plan.getTeam().getTeam_idx());
			pstmt.setInt(2, plan.getYy());
			pstmt.setInt(3, plan.getMm());
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				Plan dto = new Plan();
				dto.setPlan_idx(rs.getInt("plan_idx"));
				dto.setYy(rs.getInt("yy"));
				dto.setMm(rs.getInt("mm"));
				dto.setDd(rs.getInt("dd"));
				
				Icon icon = new Icon();
				icon.setFilename(rs.getString("filename"));
				dto.setIcon(icon); //아이콘 대입 
				
				//여러건이 존재할 수 잇는 work 가져오기 
				List todoList=todoDAO.selectAllByPlanIdx(dto.getPlan_idx());
				dto.setTodoList(todoList); //하나의 plan에 소속된 여러개의 todo 
				
				//완성된 Plan을 list에 담기 
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbManager.release(con, pstmt, rs);
		}
		
		return list;		
	}
}








