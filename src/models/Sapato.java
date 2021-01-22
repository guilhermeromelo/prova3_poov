package models;

public class Sapato {
    private int idSapato;
    private String linkImagem;
    private String modelo;

    public Sapato() {
        this.idSapato = -1;
        this.linkImagem = "";
        this.modelo = "";
    }

    public Sapato(int idSapato, String linkImagem, String modelo) {
        this.idSapato = idSapato;
        this.linkImagem = linkImagem;
        this.modelo = modelo;
    }
    
    public int getIdSapato() {
        return idSapato;
    }

    public void setIdSapato(int idSapato) {
        this.idSapato = idSapato;
    }

    public String getLinkImagem() {
        return linkImagem;
    }

    public void setLinkImagem(String linkImagem) {
        this.linkImagem = linkImagem;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    @Override
    public String toString() {
        return "Sapato{" + "idSapato=" + idSapato + ", linkImagem=" + linkImagem + ", modelo=" + modelo + '}';
    }
}
