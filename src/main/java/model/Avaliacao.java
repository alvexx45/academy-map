package model;

public class Avaliacao {
    private int idAvaliacao;
    private int nota;
    private String comentario;
    private Academia academia;

    public Avaliacao() {}

    public Avaliacao(int idAvaliacao, int nota, String comentario, Academia academia) {
        setIdAvaliacao(idAvaliacao);
        setNota(nota);
        setComentario(comentario);
        setAcademia(academia);
    }

    public int getIdAvaliacao() { return idAvaliacao; }
    public int getNota() { return nota; }
    public String getComentario() { return comentario; }
    public Academia getAcademia() { return academia; }

    public void setIdAvaliacao(int idAvaliacao) { this.idAvaliacao = idAvaliacao; }
    public void setNota(int nota) { this.nota = nota; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setAcademia(Academia academia) { this.academia = academia; }
}
