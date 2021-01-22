package models;

public class Cliente {
    private String cpf;
    private String nome;
    private String endereço;
    private String email;
    private String telefone;

    public Cliente() {
        this.cpf = "";
        this.nome = "";
        this.endereço = "";
        this.email = "";
        this.telefone = "";
    }
    
    public Cliente(String cpf, String nome, String endereço, String email, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.endereço = endereço;
        this.email = email;
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Cliente{" + "cpf=" + cpf + ", nome=" + nome + ", endere\u00e7o=" + endereço + ", email=" + email + ", telefone=" + telefone + '}';
    }
}
