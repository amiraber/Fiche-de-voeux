package com.departement.fichedevoeux;
// chaque membre doit telecharger postgresql + creer la bdd "fichedb" + modifier son mdp
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDBTest {
	public static void main(String[] args) {
		String url = "jdbc:postgresql://localhost:5432/fichedb";
		String user = "postgres";
		String password = "amira";  //mettez votre mot de passe hnaya + dans applicaton.properties
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
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
