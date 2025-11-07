package com.nttdata.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static final String URL = "jdbc:postgresql://localhost:5432/NCP?currentSchema=ncp";
	//private static final String URL = "jdbc:postgresql://localhost:5432/NCP?currentSchema=public";
	private static final String USER = "postgres";
	private static final String PASSWORD = "password";

	static {
		try {
			Class.forName("org.postgresql.Driver"); // carica il driver
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
