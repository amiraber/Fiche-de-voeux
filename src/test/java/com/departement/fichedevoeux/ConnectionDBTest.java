package com.departement.fichedevoeux;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDBTest {
	public static void main(String[] args) {
		String url = "jdbc:sqlite:fichedb.db";
		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				System.out.println("Connexion a la base reussie !");
			}else {
				System.out.println("Echec de connexion a la base");
			}
		} catch (SQLException e) {
			System.out.println("Erreur de connexion : " + e.getMessage());
		}
	}
}
