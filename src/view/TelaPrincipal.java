/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controllers.ClienteDAO;
import controllers.SapatoDAO;
import controllers.VendaDAO;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Cliente;
import models.Sapato;
import models.Venda;

/**
 *
 * @author Guilherme
 */
public class TelaPrincipal extends javax.swing.JFrame {

    boolean isClienteUpdate = false;
    boolean isSapatoUpdate = false;
    boolean isVendaUpdate = false;

    /**
     * Creates new form TelaPrincipal
     */
    public TelaPrincipal() {
        initComponents();

        clientComboBoxBuilder();
        sapatoComboBoxBuilder();

        //INICIAR MASCARAS
        try {
            jff_cliente_telefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)########*")));
            jff_cliente_cpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
            jff_venda_data.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/##")));
            jff_venda_hora.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            System.out.print("Erro Encontrado: " + ex.toString());
        }

        //INICIAR TABLEAS
        vendaTableBuilder(jtb_vendas, VendaDAO.read());
        clientTableBuilder(jtb_clientes, ClienteDAO.read());
        sapatoTableBuilder(jtb_sapatos, SapatoDAO.read());

        //INICIAR ALGUNS TEXTFILEDS
        jtf_sapatos_id.setText("Gerado pelo Sistema");
        jtf_vendas_idVenda.setText("Gerado pelo Sistema");
        jtf_vendas_idVenda.setText("Gerado pelo Sistema");
        jtf_vendas_cpfCliente.setText("Selecione Acima o Cliente");

    }

    String dateBuilder(LocalDateTime localDate) {
        String dia = "" + localDate.getDayOfMonth();
        if (dia.length() == 1) {
            dia = "0" + dia;
        }
        String mes = "" + localDate.getMonthValue();
        if (mes.length() == 1) {
            mes = "0" + mes;
        }
        String ano = "" + localDate.getYear();
        ano = ano.substring(2);
        return dia + "/" + mes + "/" + ano;
    }

    String hourBuilder(LocalDateTime localDate) {
        String hora = "" + localDate.getHour();
        if (hora.length() == 1) {
            hora = "0" + hora;
        }
        String min = "" + localDate.getMinute();
        if (min.length() == 1) {
            min = "0" + min;
        }
        return (hora + ":" + min);
    }

    void clientComboBoxBuilder() {
        ArrayList<Cliente> clientList = ClienteDAO.read();
        jcb_vendas_cliente.removeAllItems();
        jcb_vendas_cliente.addItem("Selecionar...");
        jcb_vendas_cliente.setSelectedIndex(0);
        clientList.forEach(c -> {
            jcb_vendas_cliente.addItem(c.getNome());
        });
    }

    void sapatoComboBoxBuilder() {
        ArrayList<Sapato> sapatoList = SapatoDAO.read();
        jcb_vendas_sapatos.removeAllItems();
        jcb_vendas_sapatos.addItem("Selecionar...");
        jcb_vendas_sapatos.setSelectedIndex(0);
        sapatoList.forEach(s -> {
            jcb_vendas_sapatos.addItem(s.getIdSapato() + "- " + s.getModelo());
        });
    }

    void clientTableBuilder(JTable jtable, ArrayList<Cliente> clienteList) {
        DefaultTableModel tableRows;
        tableRows = new DefaultTableModel(new String[]{"Nº", "CPF", "Nome", "Telefone",
            "Endereço", "Email"}, 0);
        for (int i = 0; i < clienteList.size(); i++) {
            Cliente c = clienteList.get(i);
            tableRows.addRow(new Object[]{(i + 1), c.getCpf(), c.getNome(), c.getTelefone(),
                c.getEndereco(), c.getEmail()});
        }
        jtable.setModel(tableRows);
    }

    void sapatoTableBuilder(JTable jtable, ArrayList<Sapato> sapatoList) {
        DefaultTableModel tableRows;
        tableRows = new DefaultTableModel(new String[]{"Nº", "ID", "Modelo", "Link Imagem"}, 0);
        for (int i = 0; i < sapatoList.size(); i++) {
            Sapato s = sapatoList.get(i);
            tableRows.addRow(new Object[]{(i + 1), s.getIdSapato(), s.getModelo(), s.getLinkImagem()});
        }
        jtable.setModel(tableRows);
    }

    void vendaTableBuilder(JTable jtable, ArrayList<Venda> vendaList) {
        DefaultTableModel tableRows;
        tableRows = new DefaultTableModel(new String[]{"Nº", "ID", "Cliente", "CPF Cliente", "Sapato",
            "Data", "Hora", "Valor"}, 0);
        ArrayList<Cliente> clienteList = ClienteDAO.read();
        ArrayList<Sapato> sapatoList = SapatoDAO.read();
        for (int i = 0; i < vendaList.size(); i++) {
            Venda v = vendaList.get(i);
            String nomeCliente = "";
            String sapato = "";
            for (int j = 0; j < clienteList.size(); j++) {
                if (clienteList.get(j).getCpf().equals(v.getFk_cpf())) {
                    nomeCliente = clienteList.get(j).getNome();
                }
            }
            for (int k = 0; k < sapatoList.size(); k++) {
                if (sapatoList.get(k).getIdSapato() == v.getFk_idSapato()) {
                    sapato = sapatoList.get(k).getIdSapato() + "- " + sapatoList.get(k).getModelo();
                }
            }
            tableRows.addRow(new Object[]{(i + 1), v.getIdVenda(), nomeCliente,
                v.getFk_cpf(), sapato, dateBuilder(v.getDataVenda()), hourBuilder(v.getDataVenda()), "R$ " + v.getValor()});
        }
        jtable.setModel(tableRows);
    }

    boolean newClientValidation() {
        boolean valido = true;
        String erro = "";
        if (jff_cliente_cpf.getText().equals("   .   .   -  ") || jff_cliente_cpf.getText().contains(" ")) {
            erro = erro + "\nCpf do Cliente Inválido";
            valido = false;
        }
        if (jff_cliente_telefone.getText().equals("(  )         ")) {
            erro = erro + "\nTelefone do Cliente Inválido";
            valido = false;
        }
        if (jtf_cliente_nome.getText().isEmpty()) {
            erro = erro + "\nNome do Cliente Não Preenchido";
            valido = false;
        }
        if (jtf_cliente_email.getText().isEmpty()) {
            erro = erro + "\nEmail do Cliente Não Preenchido";
            valido = false;
        }
        if (jtf_cliente_endereco.getText().isEmpty()) {
            erro = erro + "\nEndereço do Cliente Não Preenchido";
            valido = false;
        }
        //SHOW ERROR MESSAGE
        if (erro != "") {
            JOptionPane.showMessageDialog(null, "Erro(s) Encontrados: " + erro,
                    "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
        }
        return valido;
    }

    boolean newSapatoValidation() {
        boolean valido = true;
        String erro = "";
        if (jtf_sapatos_modelo.getText().isEmpty()) {
            erro = erro + "\nModelo do Sapato Não Preenchido";
            valido = false;
        }
        if (jtf_sapatos_linkImagem.getText().isEmpty()) {
            erro = erro + "\nLink da Imagem Não Preenchido";
            valido = false;
        }
        //SHOW ERROR MESSAGE
        if (erro != "") {
            JOptionPane.showMessageDialog(null, "Erro(s) Encontrados: " + erro,
                    "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
        }
        return valido;
    }

    boolean dateVerification(String date) {
        //CRIAÇÃO DAS VARIAVEIS
        boolean dataValida = true;
        int dia = 29, mes = 12, ano = 2020;
        if (date.equals("  /  /  ") || date.contains(" ")) {
            dataValida = false;
        }
        if (dataValida == true) {
            StringTokenizer dataTokenizer = new StringTokenizer(date, "/");
            dia = Integer.parseInt(dataTokenizer.nextToken());
            mes = Integer.parseInt(dataTokenizer.nextToken());
            if (mes > 12 || mes < 1) {
                dataValida = false;
            }
            //MES COM 31 DIAS
            if (mes == 1 || mes == 3 || mes == 5 || mes == 7 || mes == 8 || mes == 10 || mes == 12) {
                if (dia > 31 || dia < 1) {
                    dataValida = false;
                }
            } else if (mes == 2) {
                //FEVEREIRO COM 29 DIAS
                if (ano % 4 == 0) {
                    if (dia > 29 || dia < 1) {
                        dataValida = false;
                    }
                } else {
                    //FEVEREIRO COM 28 DIAS
                    if (dia > 28 || dia < 1) {
                        dataValida = false;
                    }
                }
            } else {
                //MES COM 30 DIAS
                if (dia > 30 || dia < 1) {
                    dataValida = false;
                }
            }
        }
        return dataValida;
    }

    boolean timeVerification(String time) {
        boolean valido = true;
        if (time.equals("  :  ") || time.contains(" ")) {
            valido = false;
        }
        if (valido == true) {
            StringTokenizer token = new StringTokenizer(time, ":");
            int hora = Integer.parseInt(token.nextToken());
            int min = Integer.parseInt(token.nextToken());

            if (hora < 0 || hora > 23) {
                valido = false;
            }
            if (min < 0 || min > 59) {
                valido = false;
            }
        }
        return valido;
    }

    boolean newVendaValidation() {
        boolean valido = true;
        String erro = "";
        if (jtf_vendas_cpfCliente.getText().equals("") || jtf_vendas_cpfCliente.getText().equals("Selecione Acima o Cliente")) {
            erro = erro + "\nCliente Não Selecionado";
            valido = false;
        }
        if (jcb_vendas_sapatos.getSelectedIndex() == 0) {
            erro = erro + "\nSapato Não Selecionado";
            valido = false;
        }
        if (dateVerification(jff_venda_data.getText()) == false) {
            valido = false;
            erro = erro + "\nData Inválida";
        }
        if (timeVerification(jff_venda_hora.getText()) == false) {
            valido = false;
            erro = erro + "\nHora Inválida";
        }
        try {
            double d = Double.parseDouble(jtf_vendas_valor.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            valido = false;
            erro = erro + "\nValor Inválido";
        }
        if (jtf_vendas_valor.equals("")) {
            valido = false;
            erro = erro + "\nHora Inválida";
        }
        //SHOW ERROR MESSAGE
        if (erro != "") {
            JOptionPane.showMessageDialog(null, "Erro(s) Encontrados: " + erro,
                    "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
        }
        return valido;
    }

    void limparCamposCliente() {
        jlb_cadastroCliente.setText("Cadastrar Novo Cliente");
        jtf_cliente_email.setText("");
        jtf_cliente_endereco.setText("");
        jtf_cliente_nome.setText("");
        jff_cliente_cpf.setText("");
        jff_cliente_telefone.setText("");
        isClienteUpdate = false;
        jff_cliente_cpf.setEditable(true);
    }

    void limparCamposSapato() {
        jlb_cadastrar_sapato.setText("Cadastrar Novo Sapato");
        jtf_sapatos_id.setText("Gerado pelo Sistema");
        jtf_sapatos_modelo.setText("");
        jtf_sapatos_linkImagem.setText("");
        isSapatoUpdate = false;
    }

    void limparCamposVenda() {
        jlb_cadastrar_venda.setText("Cadastrar Nova Venda");
        jtf_vendas_idVenda.setText("Gerado pelo Sistema");
        jtf_vendas_cpfCliente.setText("Selecione Acima o Cliente");
        jtf_vendas_valor.setText("");
        jff_venda_data.setText("");
        jff_venda_hora.setText("");
        jcb_vendas_cliente.setSelectedIndex(0);
        jcb_vendas_sapatos.setSelectedIndex(0);
        isVendaUpdate = false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtb_vendas = new javax.swing.JTable();
        jcb_vendas_cliente = new javax.swing.JComboBox<>();
        jtf_vendas_cpfCliente = new javax.swing.JTextField();
        jcb_vendas_sapatos = new javax.swing.JComboBox<>();
        jtf_vendas_idVenda = new javax.swing.JTextField();
        jtf_vendas_valor = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jb_vendas_getDateTime = new javax.swing.JButton();
        jlb_cadastrar_venda = new javax.swing.JLabel();
        jb_vendas_inserir = new javax.swing.JButton();
        jb_vendas_cancelar = new javax.swing.JButton();
        jb_vendas_remover = new javax.swing.JButton();
        jb_vendas_editar = new javax.swing.JButton();
        jff_venda_data = new javax.swing.JFormattedTextField();
        jff_venda_hora = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtb_sapatos = new javax.swing.JTable();
        jtf_sapatos_modelo = new javax.swing.JTextField();
        jtf_sapatos_id = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtf_sapatos_linkImagem = new javax.swing.JTextField();
        jb_sapatos_inserir = new javax.swing.JButton();
        jb_sapatos_cancelar = new javax.swing.JButton();
        jlb_cadastrar_sapato = new javax.swing.JLabel();
        jb_sapatos_remover = new javax.swing.JButton();
        jb_sapatos_editar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtb_clientes = new javax.swing.JTable();
        jff_cliente_cpf = new javax.swing.JFormattedTextField();
        jtf_cliente_nome = new javax.swing.JTextField();
        jtf_cliente_endereco = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jff_cliente_telefone = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jtf_cliente_email = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jb_cliente_inserir = new javax.swing.JButton();
        jb_cliente_cancelarInsercao = new javax.swing.JButton();
        jb_cliente_editarCliente = new javax.swing.JButton();
        jb_cliente_removerCliente = new javax.swing.JButton();
        jlb_cadastroCliente = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Loja de Sapatos");

        jtb_vendas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtb_vendas);

        jcb_vendas_cliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcb_vendas_cliente.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcb_vendas_clienteItemStateChanged(evt);
            }
        });

        jtf_vendas_cpfCliente.setEditable(false);

        jcb_vendas_sapatos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jtf_vendas_idVenda.setEditable(false);

        jtf_vendas_valor.setToolTipText("");
        jtf_vendas_valor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtf_vendas_valorKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Sapato:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Valor:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("ID Venda:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Cliente:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("CPF:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Data:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText("Hora:");

        jb_vendas_getDateTime.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jb_vendas_getDateTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/baseline_schedule_black_18dp.png"))); // NOI18N
        jb_vendas_getDateTime.setMargin(new java.awt.Insets(1, 14, 1, 14));
        jb_vendas_getDateTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_vendas_getDateTimeActionPerformed(evt);
            }
        });

        jlb_cadastrar_venda.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlb_cadastrar_venda.setText("Cadastrar Nova Venda");

        jb_vendas_inserir.setText("Salvar");
        jb_vendas_inserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_vendas_inserirActionPerformed(evt);
            }
        });

        jb_vendas_cancelar.setText("Cancelar");
        jb_vendas_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_vendas_cancelarActionPerformed(evt);
            }
        });

        jb_vendas_remover.setText("Remover Venda");
        jb_vendas_remover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_vendas_removerActionPerformed(evt);
            }
        });

        jb_vendas_editar.setText("Editar Venda");
        jb_vendas_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_vendas_editarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1227, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(184, 184, 184)
                                .addComponent(jb_vendas_remover, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(jb_vendas_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 290, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jtf_vendas_idVenda)
                            .addComponent(jcb_vendas_cliente, 0, 192, Short.MAX_VALUE)
                            .addComponent(jtf_vendas_cpfCliente))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(jLabel12))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel14))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcb_vendas_sapatos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jff_venda_data, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jff_venda_hora, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jb_vendas_getDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jtf_vendas_valor)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jb_vendas_cancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jb_vendas_inserir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jlb_cadastrar_venda)
                .addGap(153, 153, 153))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jlb_cadastrar_venda)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtf_vendas_idVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcb_vendas_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtf_vendas_cpfCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtf_vendas_valor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jb_vendas_editar)
                            .addComponent(jb_vendas_remover)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jb_vendas_inserir)
                        .addComponent(jb_vendas_cancelar))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcb_vendas_sapatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19)
                                        .addComponent(jff_venda_data, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jff_venda_hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jb_vendas_getDateTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(72, 72, 72)))))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Vendas", jPanel1);

        jtb_sapatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jtb_sapatos);

        jtf_sapatos_id.setEditable(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("ID:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Modelo:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Link Imagem:");

        jb_sapatos_inserir.setText("Salvar");
        jb_sapatos_inserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_sapatos_inserirActionPerformed(evt);
            }
        });

        jb_sapatos_cancelar.setText("Cancelar");
        jb_sapatos_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_sapatos_cancelarActionPerformed(evt);
            }
        });

        jlb_cadastrar_sapato.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlb_cadastrar_sapato.setText("Cadastrar novo Sapato");

        jb_sapatos_remover.setText("Remover Sapato");
        jb_sapatos_remover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_sapatos_removerActionPerformed(evt);
            }
        });

        jb_sapatos_editar.setText("Editar Sapato");
        jb_sapatos_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_sapatos_editarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1227, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jtf_sapatos_linkImagem))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jtf_sapatos_id, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jtf_sapatos_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(187, 187, 187)
                        .addComponent(jb_sapatos_remover)
                        .addGap(53, 53, 53)
                        .addComponent(jb_sapatos_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jb_sapatos_cancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jb_sapatos_inserir, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jlb_cadastrar_sapato)
                .addGap(152, 152, 152))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jlb_cadastrar_sapato)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtf_sapatos_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtf_sapatos_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtf_sapatos_linkImagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_sapatos_inserir)
                    .addComponent(jb_sapatos_cancelar)
                    .addComponent(jb_sapatos_remover)
                    .addComponent(jb_sapatos_editar))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Sapatos", jPanel2);

        jtb_clientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jtb_clientes);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("CPF:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Nome:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Endereço:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Telefone:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Email:");

        jb_cliente_inserir.setText("Salvar");
        jb_cliente_inserir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cliente_inserirActionPerformed(evt);
            }
        });

        jb_cliente_cancelarInsercao.setText("Cancelar");
        jb_cliente_cancelarInsercao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cliente_cancelarInsercaoActionPerformed(evt);
            }
        });

        jb_cliente_editarCliente.setText("Editar Cliente");
        jb_cliente_editarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cliente_editarClienteActionPerformed(evt);
            }
        });

        jb_cliente_removerCliente.setText("Remover Cliente");
        jb_cliente_removerCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_cliente_removerClienteActionPerformed(evt);
            }
        });

        jlb_cadastroCliente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlb_cadastroCliente.setText("Cadastrar Novo Cliente");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(jb_cliente_removerCliente)
                        .addGap(48, 48, 48)
                        .addComponent(jb_cliente_editarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jb_cliente_cancelarInsercao)
                                .addGap(18, 18, 18)
                                .addComponent(jb_cliente_inserir, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(39, 39, 39)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtf_cliente_email)
                                    .addComponent(jtf_cliente_endereco)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jff_cliente_cpf, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                            .addComponent(jtf_cliente_nome))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jff_cliente_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jlb_cadastroCliente)
                .addGap(178, 178, 178))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jlb_cadastroCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jff_cliente_cpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jff_cliente_telefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtf_cliente_nome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtf_cliente_endereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtf_cliente_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_cliente_inserir)
                    .addComponent(jb_cliente_cancelarInsercao)
                    .addComponent(jb_cliente_editarCliente)
                    .addComponent(jb_cliente_removerCliente))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Clientes", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(531, 531, 531))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //INICIO PAGINA CLIENTES ----------------------------------------------------------
    private void jb_cliente_inserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cliente_inserirActionPerformed
        if (newClientValidation() == true) {
            Cliente novoCliente = new Cliente();
            novoCliente.setCpf(jff_cliente_cpf.getText());
            novoCliente.setNome(jtf_cliente_nome.getText());
            novoCliente.setEmail(jtf_cliente_email.getText());
            novoCliente.setEndereco(jtf_cliente_endereco.getText());
            novoCliente.setTelefone(jff_cliente_telefone.getText());
            String erro = null;
            if (isClienteUpdate == false) {
                erro = ClienteDAO.create(novoCliente);
            } else {
                erro = ClienteDAO.update(novoCliente);
            }
            isClienteUpdate = false;
            JOptionPane.showMessageDialog(null, (erro == null)
                    ? "Dados do Cliente salvos com sucesso!"
                    : "Erro Encontado: \n" + erro, "Resultado da operação",
                    (erro == null) ? JOptionPane.INFORMATION_MESSAGE
                            : JOptionPane.ERROR_MESSAGE);
            if (erro == null) {
                limparCamposCliente();
                clientComboBoxBuilder();
            }
            clientTableBuilder(jtb_clientes, ClienteDAO.read());
        }
    }//GEN-LAST:event_jb_cliente_inserirActionPerformed

    private void jb_cliente_cancelarInsercaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cliente_cancelarInsercaoActionPerformed
        limparCamposCliente();
    }//GEN-LAST:event_jb_cliente_cancelarInsercaoActionPerformed

    private void jb_cliente_editarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cliente_editarClienteActionPerformed
        //PERGUNTAR QUAL OBJETO A MODIFICAR
        String cpfUpdate = JOptionPane.showInputDialog("Por favor digite o CPF do Cliente para modificar: ");
        //PROCURAR QUAL OBJETO A SER MODIFICADO
        if (cpfUpdate != null) {
            ArrayList<Cliente> clientsList = ClienteDAO.read();
            Cliente clientModify = new Cliente();
            boolean achou = false;
            for (int i = 0; i < clientsList.size() && achou == false; i++) {
                clientModify = clientsList.get(i);
                if (cpfUpdate.replace(".", "").replace("-", "").equals(clientModify.getCpf().replace(".", "").replace("-", ""))) {
                    achou = true;
                }
            }
            //PASSAR OS DADOS DO OBJETO PARA TELA DE UPDADE OU MOSTRAR ERRO DE NÃO ENCONTRADO
            if (achou == true) {
                jtf_cliente_nome.setText(clientModify.getNome());
                jff_cliente_cpf.setText(clientModify.getCpf());
                jff_cliente_telefone.setText(clientModify.getTelefone());
                jtf_cliente_endereco.setText(clientModify.getEndereco());
                jtf_cliente_email.setText(clientModify.getEmail());
                jff_cliente_cpf.setEditable(false);

                jlb_cadastroCliente.setText("Alterar Dados Cliente");
                isClienteUpdate = true;
            } else {
                JOptionPane.showMessageDialog(null, "CPF não Encontrado", "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jb_cliente_editarClienteActionPerformed

    private void jb_cliente_removerClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_cliente_removerClienteActionPerformed
        String cpfDelete = JOptionPane.showInputDialog("Por favor digite o CPF do Cliente para remover: ");
        if (cpfDelete != null) {
            ArrayList<Cliente> clientsList = ClienteDAO.read();
            Cliente clientDelete = new Cliente();
            boolean achou = false;
            for (int i = 0; i < clientsList.size() && achou == false; i++) {
                clientDelete = clientsList.get(i);
                if (cpfDelete.replace(".", "").replace("-", "").equals(clientDelete.getCpf().replace(".", "").replace("-", ""))) {
                    achou = true;
                }
            }
            //FAZER OPERAÇÃO E PEGAR E MOSTRAR O RESULTADO OU ERROS
            if (achou == true) {
                int delete = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir "
                        + "o Cliente:\nNome: " + clientDelete.getNome() + ", CPF: " + clientDelete.getCpf()
                        + "\nATENÇÃO: ISSO IRÁ EXCLUIR TODOS AS\nVENDAS DO CLIENTE!!!",
                        "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

                if (delete == 0) {
                    String erro = ClienteDAO.delete(clientDelete);
                    JOptionPane.showMessageDialog(null, (erro == null)
                            ? "Cliente Removido com Sucesso!"
                            : "Erro Encontado: \n" + erro, "Resultado da operação",
                            (erro == null) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Operação Cancelada!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "CPF não Encontrado", "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
            }
            //ATUALIZAR TABLEAS
            vendaTableBuilder(jtb_vendas, VendaDAO.read());
            clientTableBuilder(jtb_clientes, ClienteDAO.read());
            clientComboBoxBuilder();
        }
    }//GEN-LAST:event_jb_cliente_removerClienteActionPerformed
    //FIM PAGINA CLIENTES ----------------------------------------------------------

    //INICIO PAGINA SAPATOS ----------------------------------------------------------
    private void jb_sapatos_inserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_sapatos_inserirActionPerformed
        if (newSapatoValidation() == true) {
            Sapato novoSapato = new Sapato();
            if (isSapatoUpdate == true) {
                novoSapato.setIdSapato(Integer.parseInt(jtf_sapatos_id.getText()));
            }
            novoSapato.setModelo(jtf_sapatos_modelo.getText());
            novoSapato.setLinkImagem(jtf_sapatos_linkImagem.getText());
            String erro = null;
            if (isSapatoUpdate == false) {
                erro = SapatoDAO.create(novoSapato);
            } else {
                erro = SapatoDAO.update(novoSapato);
            }
            isSapatoUpdate = false;
            JOptionPane.showMessageDialog(null, (erro == null)
                    ? "Dados do Sapato salvos com sucesso!"
                    : "Erro Encontado: \n" + erro, "Resultado da operação",
                    (erro == null) ? JOptionPane.INFORMATION_MESSAGE
                            : JOptionPane.ERROR_MESSAGE);
            if (erro == null) {
                limparCamposSapato();
                sapatoComboBoxBuilder();
                sapatoTableBuilder(jtb_sapatos, SapatoDAO.read());
            }
        }
    }//GEN-LAST:event_jb_sapatos_inserirActionPerformed

    private void jb_sapatos_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_sapatos_cancelarActionPerformed
        limparCamposSapato();
    }//GEN-LAST:event_jb_sapatos_cancelarActionPerformed

    private void jb_sapatos_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_sapatos_editarActionPerformed
        //PERGUNTAR QUAL OBJETO A MODIFICAR
        String idSapatoUpdate = JOptionPane.showInputDialog("Por favor digite o ID do Sapato para modificar: ");
        //PROCURAR QUAL OBJETO A SER MODIFICADO
        if (idSapatoUpdate != null) {
            ArrayList<Sapato> sapatoList = SapatoDAO.read();
            Sapato sapatoModify = new Sapato();
            boolean achou = false;
            for (int i = 0; i < sapatoList.size() && achou == false; i++) {
                sapatoModify = sapatoList.get(i);
                if (idSapatoUpdate.equals("" + sapatoModify.getIdSapato())) {
                    achou = true;
                }
            }
            //PASSAR OS DADOS DO OBJETO PARA TELA DE UPDADE OU MOSTRAR ERRO DE NÃO ENCONTRADO
            if (achou == true) {
                jtf_sapatos_id.setText("" + sapatoModify.getIdSapato());
                jtf_sapatos_linkImagem.setText(sapatoModify.getLinkImagem());
                jtf_sapatos_modelo.setText(sapatoModify.getModelo());

                jlb_cadastrar_sapato.setText("Alterar Dados Sapato");
                isSapatoUpdate = true;
            } else {
                JOptionPane.showMessageDialog(null, "ID não Encontrado", "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jb_sapatos_editarActionPerformed

    private void jb_sapatos_removerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_sapatos_removerActionPerformed
        String idDelete = JOptionPane.showInputDialog("Por favor digite o ID do Sapato para remover: ");
        if (idDelete != null) {
            ArrayList<Sapato> sapatoList = SapatoDAO.read();
            Sapato sapatoDelete = new Sapato();
            boolean achou = false;
            for (int i = 0; i < sapatoList.size() && achou == false; i++) {
                sapatoDelete = sapatoList.get(i);
                if (idDelete.equals("" + sapatoDelete.getIdSapato())) {
                    achou = true;
                }
            }
            //FAZER OPERAÇÃO E PEGAR E MOSTRAR O RESULTADO OU ERROS
            if (achou == true) {
                int delete = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir "
                        + "o Sapato:\n" + sapatoDelete.getIdSapato() + "- " + sapatoDelete.getModelo()
                        + "\nATENÇÃO: ISSO IRÁ EXCLUIR TODOS AS\nVENDAS COM O SAPATO!!!",
                        "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

                if (delete == 0) {
                    String erro = SapatoDAO.delete(sapatoDelete);
                    JOptionPane.showMessageDialog(null, (erro == null)
                            ? "Sapato Removido com Sucesso!"
                            : "Erro Encontado: \n" + erro, "Resultado da operação",
                            (erro == null) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Operação Cancelada!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ID não Encontrado", "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
            }
            //ATUALIZAR TABLEAS
            vendaTableBuilder(jtb_vendas, VendaDAO.read());
            sapatoComboBoxBuilder();
            sapatoTableBuilder(jtb_sapatos, SapatoDAO.read());
        }
    }//GEN-LAST:event_jb_sapatos_removerActionPerformed
    //FIM PAGINA SAPATOS ----------------------------------------------------------

    //INICIO PAGINA VENDAS ----------------------------------------------------------
    private void jcb_vendas_clienteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcb_vendas_clienteItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String newChoice = jcb_vendas_cliente.getSelectedItem().toString();
            if (!newChoice.equals("Selecionar...")) {
                boolean achou = false;
                ArrayList<Cliente> clientList = ClienteDAO.read();
                Cliente selectedCliente = null;
                for (int i = 0; i < clientList.size() && achou == false; i++) {
                    selectedCliente = clientList.get(i);
                    if (selectedCliente.getNome().equals(newChoice)) {
                        achou = true;
                    }
                }
                if (achou == true) {
                    jtf_vendas_cpfCliente.setText(selectedCliente.getCpf());
                }
            } else {
                jtf_vendas_cpfCliente.setText("Selecione Acima o Cliente");
            }
        }
    }//GEN-LAST:event_jcb_vendas_clienteItemStateChanged

    private void jb_vendas_getDateTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_vendas_getDateTimeActionPerformed
        jff_venda_data.setText(dateBuilder(LocalDateTime.now()));
        jff_venda_hora.setText(hourBuilder(LocalDateTime.now()));
    }//GEN-LAST:event_jb_vendas_getDateTimeActionPerformed

    private void jb_vendas_inserirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_vendas_inserirActionPerformed
        if (newVendaValidation() == true) {
            Venda novaVenda = new Venda();
            if (isVendaUpdate == true) {
                novaVenda.setIdVenda(Integer.parseInt(jtf_vendas_idVenda.getText()));
            }
            novaVenda.setFk_cpf(jtf_vendas_cpfCliente.getText());
            StringTokenizer idSapato = new StringTokenizer(jcb_vendas_sapatos.getSelectedItem().toString(), "-");
            novaVenda.setFk_idSapato(Integer.parseInt(idSapato.nextToken()));
            novaVenda.setValor(Double.parseDouble(jtf_vendas_valor.getText().replace(",", ".")));

            //RECUPERAR DATA E HORA E GRAVAR
            StringTokenizer st = new StringTokenizer(jff_venda_data.getText(), "/");
            int dia = Integer.parseInt(st.nextToken());
            int mes = Integer.parseInt(st.nextToken());
            int ano = Integer.parseInt(st.nextToken()) + 2000;
            StringTokenizer st2 = new StringTokenizer(jff_venda_hora.getText(), ":");
            int hora = Integer.parseInt(st2.nextToken());
            int min = Integer.parseInt(st2.nextToken());
            novaVenda.setDataVenda(LocalDateTime.of(ano, mes, dia, hora, min));
            String erro = null;
            if (isVendaUpdate == false) {
                erro = VendaDAO.create(novaVenda);
            } else {
                erro = VendaDAO.update(novaVenda);
            }
            isVendaUpdate = false;
            JOptionPane.showMessageDialog(null, (erro == null)
                    ? "Dados do Sapato salvos com sucesso!"
                    : "Erro Encontado: \n" + erro, "Resultado da operação",
                    (erro == null) ? JOptionPane.INFORMATION_MESSAGE
                            : JOptionPane.ERROR_MESSAGE);
            if (erro == null) {
                limparCamposVenda();
                vendaTableBuilder(jtb_vendas, VendaDAO.read());
            }
        }
    }//GEN-LAST:event_jb_vendas_inserirActionPerformed

    private void jb_vendas_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_vendas_cancelarActionPerformed
        // TODO add your handling code here:
        limparCamposVenda();
    }//GEN-LAST:event_jb_vendas_cancelarActionPerformed

    private void jb_vendas_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_vendas_editarActionPerformed
        //PERGUNTAR QUAL OBJETO A MODIFICAR
        String idVendaUpdate = JOptionPane.showInputDialog("Por favor digite o ID da Venda para modificar: ");
        //PROCURAR QUAL OBJETO A SER MODIFICADO
        if (idVendaUpdate != null) {
            ArrayList<Venda> vendaList = VendaDAO.read();
            Venda vendaModify = new Venda();
            boolean achou = false;
            for (int i = 0; i < vendaList.size() && achou == false; i++) {
                vendaModify = vendaList.get(i);
                if (idVendaUpdate.equals("" + vendaModify.getIdVenda())) {
                    achou = true;
                }
            }
            //PASSAR OS DADOS DO OBJETO PARA TELA DE UPDADE OU MOSTRAR ERRO DE NÃO ENCONTRADO
            if (achou == true) {
                jtf_vendas_idVenda.setText("" + vendaModify.getIdVenda());
                jtf_vendas_cpfCliente.setText(vendaModify.getFk_cpf());
                String nome = "";
                ArrayList<Cliente> clienteList = ClienteDAO.read();
                for (int i = 0; i < clienteList.size(); i++) {
                    if (clienteList.get(i).getCpf().equals(vendaModify.getFk_cpf())) {
                        nome = clienteList.get(i).getNome();
                    }
                }
                System.out.println(nome);
                jcb_vendas_cliente.setSelectedItem(nome);
                
                ArrayList<Sapato> sapatoList = SapatoDAO.read();
                for(int i=0;i<sapatoList.size();i++){
                    Sapato auxSapato = sapatoList.get(i);
                    if(auxSapato.getIdSapato() == vendaModify.getFk_idSapato()){
                        jcb_vendas_sapatos.setSelectedItem(auxSapato.getIdSapato()+"- "+auxSapato.getModelo());
                    }
                }
                
                
                jff_venda_data.setText(dateBuilder(vendaModify.getDataVenda()));
                jff_venda_hora.setText(hourBuilder(vendaModify.getDataVenda()));
                jtf_vendas_valor.setText(""+vendaModify.getValor());
                jlb_cadastrar_venda.setText("Alterar Dados Venda");
                isVendaUpdate = true;
            } else {
                JOptionPane.showMessageDialog(null, "ID não Encontrado", "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jb_vendas_editarActionPerformed

    private void jb_vendas_removerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_vendas_removerActionPerformed
        // TODO add your handling code here:
        String idDelete = JOptionPane.showInputDialog("Por favor digite o ID da Venda para modificar: ");
        if (idDelete != null) {
            ArrayList<Venda> vendaList = VendaDAO.read();
            Venda vendaDelete = new Venda();
            boolean achou = false;
            for (int i = 0; i < vendaList.size() && achou == false; i++) {
                vendaDelete = vendaList.get(i);
                if (idDelete.equals("" + vendaDelete.getIdVenda())) {
                    achou = true;
                }
            }
            String modeloSapato = "";
            ArrayList<Sapato> sapatoList = SapatoDAO.read();
            for(int i=0;i<sapatoList.size();i++){
                if(sapatoList.get(i).getIdSapato() == vendaDelete.getFk_idSapato()){
                    modeloSapato = sapatoList.get(i).getModelo();
                }
            }
            String nomeCliente = "";
            ArrayList<Cliente> clienteList = ClienteDAO.read();
            for(int i=0;i<clienteList.size();i++){
                if(clienteList.get(i).getCpf().equals(vendaDelete.getFk_cpf())){
                    nomeCliente = clienteList.get(i).getNome();
                }
            }
            
            //FAZER OPERAÇÃO E PEGAR E MOSTRAR O RESULTADO OU ERROS
            if (achou == true) {
                int delete = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir "
                        + "\na Venda do Sapato: " + vendaDelete.getFk_idSapato()+ "- " + modeloSapato
                        +"\ndo Cliente: " + nomeCliente + ",\nCPF: "+vendaDelete.getFk_cpf() ,
                        "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

                if (delete == 0) {
                    String erro = VendaDAO.delete(vendaDelete);
                    JOptionPane.showMessageDialog(null, (erro == null)
                            ? "Sapato Removido com Sucesso!"
                            : "Erro Encontado: \n" + erro, "Resultado da operação",
                            (erro == null) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Operação Cancelada!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ID não Encontrado", "Erro ao Realizar Operação", JOptionPane.ERROR_MESSAGE);
            }
            //ATUALIZAR TABLEAS
            vendaTableBuilder(jtb_vendas, VendaDAO.read());
        }
    }//GEN-LAST:event_jb_vendas_removerActionPerformed

    private void jtf_vendas_valorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtf_vendas_valorKeyTyped
        String caracteres = "0987654321.,";
        String aux = ",.";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        } else {
            if (aux.contains(evt.getKeyChar() + "")) {
                if (jtf_vendas_valor.getText().contains(".") || jtf_vendas_valor.getText().contains(",")) {
                    evt.consume();
                }
            }
        }
    }//GEN-LAST:event_jtf_vendas_valorKeyTyped
    //FIM PAGINA VENDAS ----------------------------------------------------------
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jb_cliente_cancelarInsercao;
    private javax.swing.JButton jb_cliente_editarCliente;
    private javax.swing.JButton jb_cliente_inserir;
    private javax.swing.JButton jb_cliente_removerCliente;
    private javax.swing.JButton jb_sapatos_cancelar;
    private javax.swing.JButton jb_sapatos_editar;
    private javax.swing.JButton jb_sapatos_inserir;
    private javax.swing.JButton jb_sapatos_remover;
    private javax.swing.JButton jb_vendas_cancelar;
    private javax.swing.JButton jb_vendas_editar;
    private javax.swing.JButton jb_vendas_getDateTime;
    private javax.swing.JButton jb_vendas_inserir;
    private javax.swing.JButton jb_vendas_remover;
    private javax.swing.JComboBox<String> jcb_vendas_cliente;
    private javax.swing.JComboBox<String> jcb_vendas_sapatos;
    private javax.swing.JFormattedTextField jff_cliente_cpf;
    private javax.swing.JFormattedTextField jff_cliente_telefone;
    private javax.swing.JFormattedTextField jff_venda_data;
    private javax.swing.JFormattedTextField jff_venda_hora;
    private javax.swing.JLabel jlb_cadastrar_sapato;
    private javax.swing.JLabel jlb_cadastrar_venda;
    private javax.swing.JLabel jlb_cadastroCliente;
    private javax.swing.JTable jtb_clientes;
    private javax.swing.JTable jtb_sapatos;
    private javax.swing.JTable jtb_vendas;
    private javax.swing.JTextField jtf_cliente_email;
    private javax.swing.JTextField jtf_cliente_endereco;
    private javax.swing.JTextField jtf_cliente_nome;
    private javax.swing.JTextField jtf_sapatos_id;
    private javax.swing.JTextField jtf_sapatos_linkImagem;
    private javax.swing.JTextField jtf_sapatos_modelo;
    private javax.swing.JTextField jtf_vendas_cpfCliente;
    private javax.swing.JTextField jtf_vendas_idVenda;
    private javax.swing.JTextField jtf_vendas_valor;
    // End of variables declaration//GEN-END:variables
}
