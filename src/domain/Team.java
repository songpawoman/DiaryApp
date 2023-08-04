package domain;

//같은 그룹의 팀을 표현 DTO 
//데이터베이스의 Team 테이블의 레코드 1건을 담기 위한 객체 
//만일 회원 10명을 담으려며,이 DTO의 인스턴스 또한 new 10회
public class Team {
	private int team_idx;
	private String id;
	private String pass;
	private String name;
	private String photo;
	
	public int getTeam_idx() {
		return team_idx;
	}
	public void setTeam_idx(int team_idx) {
		this.team_idx = team_idx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}
