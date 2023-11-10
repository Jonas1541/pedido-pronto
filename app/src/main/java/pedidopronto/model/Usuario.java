package pedidopronto.model;

public class Usuario {
    private int id;
    private String nomeUsuario;
    private String senha;

    private TipoUsuario tipoUsuario;

    public Usuario(int id, String nomeUsuario, String senha, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
