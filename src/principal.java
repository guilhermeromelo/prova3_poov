
import controllers.ClienteDAO;
import controllers.ConexaoBancoDados;
import controllers.SapatoDAO;
import controllers.VendaDAO;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import models.Cliente;
import models.Sapato;
import models.Venda;

public class principal {

    public static void main(String[] args) {
/*
        Sapato sapato = new Sapato();
        sapato.setIdSapato(1);
        sapato.setLinkImagem("yyyyyyy");
        sapato.setModelo("chinelo");
        SapatoDAO.create(sapato);
    
        Cliente novoCliente = new Cliente();
        novoCliente.setCpf("111.553.506-75");
        novoCliente.setEmail("22222@gmail");
        novoCliente.setEndereco("Rua do 22222");
        novoCliente.setNome("Gui22222");
        novoCliente.setTelefone("2222222");
        ClienteDAO.create(novoCliente);
 */  
        ArrayList<Venda> vendas = VendaDAO.read();
        System.out.println(vendas.get(0).toString());

        Venda venda = new Venda();
        venda.setFk_cpf("111.553.506-75");
        venda.setIdVenda(1);
        venda.setDataVenda(LocalDateTime.now());
        venda.setValor(111.50);
        venda.setFk_idSapato(3);
        VendaDAO.delete(venda);
    }
}
