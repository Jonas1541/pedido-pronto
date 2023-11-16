package pedidopronto.model;

public class CategoriaProduto {
    private int id;
    private String categoria;

    public CategoriaProduto(int id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public CategoriaProduto(String categoria) {
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
