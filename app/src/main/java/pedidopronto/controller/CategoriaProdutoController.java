package pedidopronto.controller;

import java.util.List;

import pedidopronto.model.CategoriaProduto;
import pedidopronto.repository.CategoriaProdutoRepository;

public class CategoriaProdutoController {
    private CategoriaProdutoRepository categoriaProdutoRepository;

    public CategoriaProdutoController(CategoriaProdutoRepository categoriaProdutoRepository) {
        this.categoriaProdutoRepository = categoriaProdutoRepository;
    }

    public void createCategoriaProduto(CategoriaProduto categoriaProduto) {
        categoriaProdutoRepository.create(categoriaProduto);
    }

    public List<CategoriaProduto> readCategoriaProduto() {
        return categoriaProdutoRepository.read();
    }

    public CategoriaProduto findByName(String nome) {
        return categoriaProdutoRepository.findByName(nome);
    }
    public CategoriaProduto findById(int id) {
        return categoriaProdutoRepository.findById(id);
    }

    public void updateCategoriaProduto(CategoriaProduto categoriaProduto) {
        categoriaProdutoRepository.update(categoriaProduto);
    }

    public void deleteCategoriaProduto(int id) {
        categoriaProdutoRepository.delete(id);
    }
}