package pedidopronto.controller;

import java.util.List;

import pedidopronto.model.Produto;
import pedidopronto.repository.ProdutoRepository;

public class ProdutoController {
    private ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public void createProduto(Produto produto) {
        produtoRepository.create(produto);
    }

    public List<Produto> readProduto() {
        return produtoRepository.read();
    }

    public void updateProduto(Produto produto) {
        produtoRepository.update(produto);
    }

    public void deleteProduto(int id) {
        produtoRepository.delete(id);
    }

    public Produto findById(int id){
        return produtoRepository.findById(id);
    }
}
