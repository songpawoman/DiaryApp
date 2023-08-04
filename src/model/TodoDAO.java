package model;

import util.DBManager;

public class TodoDAO {
	DBManager dbManager;
	
	public TodoDAO(DBManager dbManager) {
		this.dbManager=dbManager;
	}
	
}
