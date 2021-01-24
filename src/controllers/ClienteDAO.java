package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.Cliente;

public class ClienteDAO {
    
    static Connection connection = ConexaoBancoDados.getConexao();
    
    public static String create(Cliente novoCliente) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "insert into cliente (cpf, nome, endereco, email,"
                + "telefone) values (?,?,?,?,?)";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setString(1, novoCliente.getCpf());
            state.setString(2, novoCliente.getNome());
            state.setString(3, novoCliente.getEndereco());
            state.setString(4, novoCliente.getEmail());
            state.setString(5, novoCliente.getTelefone());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Cliente adicionado com Sucesso!");
        }
        return erro;
    }

    public static ArrayList<Cliente> read() {
        ArrayList<Cliente> clienteList = new ArrayList();
        PreparedStatement state;
        ResultSet res;
        String msgSQL;
        msgSQL = "Select * from cliente";
        
        try {
            state = connection.prepareStatement(msgSQL);
            res = state.executeQuery();
            clienteList = resultSetToArrayListCliente(res);
            res.close();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
        }

        return clienteList;
    }

    private static ArrayList<Cliente> resultSetToArrayListCliente(ResultSet res) {
        ArrayList<Cliente> clientList = new ArrayList();
        try {
            while (res.next()) {
                Cliente newClient = new Cliente();
                newClient.setCpf(res.getString("cpf"));
                newClient.setEndereco(res.getString("endereco"));
                newClient.setNome(res.getString("nome"));
                newClient.setEmail(res.getString("email"));
                newClient.setTelefone(res.getString("telefone"));
                clientList.add(newClient);
            }
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
        }

        return clientList;
    }

    public static String update(Cliente cliente) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "update cliente set endereco=?, nome=?, telefone=?, "
                + "email=? where cpf=?";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setString(1, cliente.getEndereco());
            state.setString(2, cliente.getNome());
            state.setString(3, cliente.getTelefone());
            state.setString(4, cliente.getEmail());
            state.setString(5, cliente.getCpf());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("\n Cliente atualizado com sucesso!");
        }
        return erro;
    }

    public static String delete(Cliente cliente) {
        String erro = null;
        PreparedStatement state;
        String msgSQL = "delete from cliente where cpf=?";
        try {
            state = connection.prepareStatement(msgSQL);
            state.setString(1, cliente.getCpf());
            state.execute();
            state.close();
        } catch (SQLException e) {
            System.out.println("Erro Encontrado: " + e.toString());
            erro = e.toString();
        }
        if (erro == null) {
            System.out.println("Cliente Removido com sucesso!");
        }
        return erro;
    }
    
}
