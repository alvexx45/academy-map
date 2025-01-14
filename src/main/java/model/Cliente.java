package model;

public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private boolean ativo;

    public Cliente() {}

    public Cliente(int id, String nome, String email, String cpf, String senha, boolean ativo) {
        setId(id);
        setNome(nome);
        setEmail(email);
        setCpf(cpf);
        setSenha(senha);
        setAtivo(ativo);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public String getSenha() { return senha; }
    public boolean getAtivo() { return ativo; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}