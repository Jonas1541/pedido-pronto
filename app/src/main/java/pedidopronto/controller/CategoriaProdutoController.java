package pedidopronto.controller;

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

    

    

    

    

}
