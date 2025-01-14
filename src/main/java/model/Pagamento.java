package model;

public class Pagamento {
    private int idPagamento;
    private Academia academia;
    private Cliente cliente;
    private int quantidadeAulas;
    private double precoFinal;

    public Pagamento() {}

    public Pagamento(int idPagamento, Academia academia, Cliente cliente, int quantidadeAulas, double precoFinal) {
        setIdPagamento(idPagamento);
        setAcademia(academia);
        setCliente(cliente);
        setQuantidadeAulas(quantidadeAulas);
        setPrecoFinal(precoFinal);
    }

    public int getIdPagamento() { return idPagamento; }
    public Academia getAcademia() { return academia; }
    public Cliente getCliente() { return cliente; }
    public int getQuantidadeAulas() { return quantidadeAulas; }
    public double getPrecoFinal() { return precoFinal; }

    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setQuantidadeAulas(int quantidadeAulas) { this.quantidadeAulas = quantidadeAulas; }
    public void setPrecoFinal(double precoFinal) { this.precoFinal = precoFinal; }
}
