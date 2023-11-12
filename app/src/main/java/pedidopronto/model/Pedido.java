package pedidopronto.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private boolean status;
    private double total;
    private MetodoPagamento metodoPagamento;

    List<Produto> listaProdutos = new ArrayList<>();

    public Pedido(int id, boolean status){
        this.id = id;
        this.status = status;
    }

    public Pedido(int id, boolean status, double total, MetodoPagamento metodoPagamento) {
        this.id = id;
        this.status = status;
        this.total = total;
        this.metodoPagamento = metodoPagamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public List<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public void addProduto(Produto produto) {
        listaProdutos.add(produto);
    }
}
