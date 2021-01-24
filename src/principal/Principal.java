package principal;


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
import view.TelaPrincipal;

public class Principal {

    public static void main(String[] args) {
        TelaPrincipal tela = new TelaPrincipal();
        tela.setVisible(true);
        tela.setLocationRelativeTo(null);
        
        System.out.println("Desenvolvido por Guilherme Rodrigues de Melo");
    }
}
