package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBancoDados {
        public static Connection getConexao(){
        boolean hasError = false;
        Connection databaseConnection = null;
        try {
            //Class.forName("org.postgresql.Driver");
            databaseConnection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/prova3_poov", "poov", "password");
        } catch (SQLException e) {
            System.out.println("\nErro encontrado: "+ e.toString());
            hasError = true;
        }
            //System.out.println((hasError == false) ? "Conexão efetuada com sucesso!" : "Não foi Possível efetuar uma conexão ao banco...");
        return databaseConnection;
    }
}