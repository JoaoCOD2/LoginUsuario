package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mod_conexao {

    public static Connection conector() {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/sistema";
        String user = "root";
        String password = "";

        try {

            Class.forName(driver);

            Connection conexao = DriverManager.getConnection(
                    url,
                    user,
                    password
            );

            System.out.println("CONEXÃO REALIZADA COM SUCESSO!");

            return conexao;

        } catch (ClassNotFoundException e) {

            System.out.println("ERRO: DRIVER DO MYSQL NÃO ENCONTRADO!");
            return null;

        } catch (SQLException e) {

            System.out.println("ERRO NA CONEXÃO: " + e.getMessage());
            return null;
        }
    }
}