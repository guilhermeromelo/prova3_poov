package models;

import java.time.LocalDate;

public class Venda {
    private int idVenda;
    private String fk_cpf;
    private int fk_idSapato;
    private LocalDate data;
    private double valor;

    public Venda(int idVenda, String fk_cpf, int fk_idSapato, LocalDate data, double valor) {
        this.idVenda = idVenda;
        this.fk_cpf = fk_cpf;
        this.fk_idSapato = fk_idSapato;
        this.data = data;
        this.valor = valor;
    }
    
    public Venda() {
        this.idVenda = -1;
        this.fk_cpf = "";
        this.fk_idSapato = -1;
        this.data = null;
        this.valor = 0;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public String getFk_cpf() {
        return fk_cpf;
    }

    public void setFk_cpf(String fk_cpf) {
        this.fk_cpf = fk_cpf;
    }

    public int getFk_idSapato() {
        return fk_idSapato;
    }

    public void setFk_idSapato(int fk_idSapato) {
        this.fk_idSapato = fk_idSapato;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Venda{" + "idVenda=" + idVenda + ", fk_cpf=" + fk_cpf + ", fk_idSapato=" + fk_idSapato + ", data=" + data + ", valor=" + valor + '}';
    }
}
