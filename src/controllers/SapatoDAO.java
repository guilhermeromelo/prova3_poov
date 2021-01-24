package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Sapato;

public class SapatoDAO {
        
    static Connection connection = ConexaoBancoDados.getConexao();
    
    public static String create(Sapato novoSapato) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "insert into sapato (modelo, linkImagem) values (?,?)";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setString(1, novoSapato.getModelo());
            state.setString(2, novoSapato.getLinkImagem());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Sapato adicionado com Sucesso!");
        }
        return erro;
    }

    public static ArrayList<Sapato> read() {
        ArrayList<Sapato> sapatoList = new ArrayList();
        PreparedStatement state;
        ResultSet res;
        String msgSQL;
        msgSQL = "Select * from sapato";
        
        try {
            state = connection.prepareStatement(msgSQL);
            res = state.executeQuery();
            sapatoList = resultSetToArrayListSapato(res);
            res.close();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
        }

        return sapatoList;
    }

    private static ArrayList<Sapato> resultSetToArrayListSapato(ResultSet res) {
        ArrayList<Sapato> sapatoList = new ArrayList();
        try {
            while (res.next()) {
                Sapato newSapato = new Sapato();
                newSapato.setIdSapato(res.getInt("idSapato"));
                newSapato.setLinkImagem(res.getString("linkImagem"));
                newSapato.setModelo(res.getString("modelo"));
                sapatoList.add(newSapato);
            }
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
        }

        return sapatoList;
    }

    public static String update(Sapato sapato) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "update sapato set modelo=?, linkImagem=? where idSapato=?";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setString(1, sapato.getModelo());
            state.setString(2, sapato.getLinkImagem());
            state.setInt(3, sapato.getIdSapato());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Sapato atualizado com sucesso!");
        }
        return erro;
    }

    public static String delete(Sapato sapato) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "delete from sapato where idSapato=?";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setInt(1, sapato.getIdSapato());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Sapato Removido com sucesso!");
        }
        return erro;
    }
}
