package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import models.Venda;

public class VendaDAO {

    static Connection connection = ConexaoBancoDados.getConexao();

    public static String create(Venda novaVenda) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "insert into venda (fk_cpf, dataVenda, fk_idSapato, valor) values (?,?,?,?)";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setString(1, novaVenda.getFk_cpf());
            state.setTimestamp(2, Timestamp.valueOf(novaVenda.getDataVenda()));
            state.setInt(3, novaVenda.getFk_idSapato());
            state.setDouble(4, novaVenda.getValor());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Venda adicionada com Sucesso!");
        }
        return erro;
    }

    public static ArrayList<Venda> read() {
        ArrayList<Venda> vendaList = new ArrayList();
        PreparedStatement state;
        ResultSet res;
        String msgSQL;
        msgSQL = "Select * from venda";

        try {
            state = connection.prepareStatement(msgSQL);
            res = state.executeQuery();
            vendaList = resultSetToArrayListVenda(res);
            res.close();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
        }

        return vendaList;
    }

    private static ArrayList<Venda> resultSetToArrayListVenda(ResultSet res) {
        ArrayList<Venda> vendaList = new ArrayList();
        try {
            while (res.next()) {
                Venda newVenda = new Venda();
                newVenda.setIdVenda(res.getInt("idVenda"));
                newVenda.setFk_idSapato(res.getInt("fk_idSapato"));
                newVenda.setFk_cpf(res.getString("fk_cpf"));
                Timestamp timestampFromDataBase = res.getTimestamp("dataVenda");
                newVenda.setDataVenda(timestampFromDataBase.toLocalDateTime());
                newVenda.setValor(res.getDouble("valor"));
                vendaList.add(newVenda);
            }
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
        }

        return vendaList;
    }
    
    public static String update(Venda venda) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "update venda set fk_idSapato=?, fk_cpf=?, dataVenda=?, "
                + "valor=? where idVenda=?";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setInt(1, venda.getFk_idSapato());
            state.setString(2, venda.getFk_cpf());
            state.setTimestamp(3, Timestamp.valueOf(venda.getDataVenda()));
            state.setDouble(4, venda.getValor());
            state.setInt(5, venda.getIdVenda());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Venda atualizada com sucesso!");
        }
        return erro;
    }

    public static String delete(Venda venda) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "delete from venda where idVenda=?";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setInt(1, venda.getIdVenda());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Venda Removida com sucesso!");
        }
        return erro;
    }
     
}
