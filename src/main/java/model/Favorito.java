package model;

public class Favorito {
    private int idFavorito;
    private Academia academia;
    private Cliente cliente;

    public Favorito() {}

    public Favorito(int idFavorito, Academia academia, Cliente cliente) {
        setIdFavorito(idFavorito);
        setAcademia(academia);
        setCliente(cliente);
    }

    public int getIdFavorito() { return idFavorito; }
    public Academia getAcademia() { return academia; }
    public Cliente getCliente() { return cliente; }

    public void setIdFavorito(int idFavorito) { this.idFavorito = idFavorito; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}