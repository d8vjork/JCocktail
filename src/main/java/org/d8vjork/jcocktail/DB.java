package org.d8vjork.jcocktail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTree;

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

	public void insertData(JTree tree) {
		/*try {
			this.conn.nativeSQL(tree);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
