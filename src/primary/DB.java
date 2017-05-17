package primary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DB {
	
	private Connection conn;
	
	DB() {
		String url = "jdbc:sqlite:app.db";
        
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        this.conn = conn;
	}
	
	//public void insertData
	
}
