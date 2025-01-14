package model;

public class Academia {
    private int idAcademia;
    private String nomeAcademia;
    private String localizacao;
    private boolean destaque;
    private String email;
    private String senha;
    private String cnpj;
    private float precoAula;
    private String fotoPerfil;

    public Academia() {}

    public Academia(int idAcademia, String nomeAcademia, String localizacao, boolean destaque, String email, String senha, String cnpj, float precoAula, String fotoPerfil) {
        setIdAcademia(idAcademia);
        setNomeAcademia(nomeAcademia);
        setLocalizacao(localizacao);
        setDestaque(destaque);
        setEmail(email);
        setSenha(senha);
        setCnpj(cnpj);
        setPrecoAula(precoAula);
        setFotoPerfil(fotoPerfil);
    }

    public int getIdAcademia() { return idAcademia; }
    public String getNomeAcademia() { return nomeAcademia; }
    public String getLocalizacao() { return localizacao; }
    public boolean getDestaque() { return destaque; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getCnpj() { return cnpj; }
    public float getPrecoAula() { return precoAula; }
    public String getFotoPerfil() { return fotoPerfil; }

    public int setIdAcademia(int idAcademia) { return this.idAcademia = idAcademia; }
    public void setNomeAcademia(String nomeAcademia) { this.nomeAcademia = nomeAcademia; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public void setDestaque(boolean destaque) { this.destaque = destaque; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public void setPrecoAula(float precoAula) { this.precoAula = precoAula; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}