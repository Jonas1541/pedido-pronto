package pedidopronto.view;

import java.util.List;
import java.util.Scanner;

import pedidopronto.controller.CategoriaProdutoController;
import pedidopronto.controller.ProdutoController;
import pedidopronto.model.CategoriaProduto;
import pedidopronto.model.Produto;
import pedidopronto.repository.CategoriaProdutoRepository;
import pedidopronto.repository.ProdutoRepository;

public class ProdutoView {

    private ProdutoController produtoController;
    private CategoriaProdutoController categoriaProdutoController;

    public ProdutoView() {
        produtoController = new ProdutoController(new ProdutoRepository());
        categoriaProdutoController = new CategoriaProdutoController(new CategoriaProdutoRepository());
    }

    public void cadastrarProduto() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### CADASTRO DE PRODUTOS ###");
            System.out.print("Nome do produto: ");
            String nome = scanner.nextLine();
            System.out.print("Descrição do produto: ");
            String descricao = scanner.nextLine();
            System.out.print("Preço do produto: ");
            double preco = scanner.nextDouble();
            scanner.nextLine(); // Limpar buffer

            CategoriaProduto categoriaProduto = selecionarOuCriarCategoria(scanner);

            Produto novoProduto = new Produto(nome, descricao, preco, categoriaProduto);
            produtoController.createProduto(novoProduto);
            System.out.println("Produto cadastrado com sucesso!");
        }
    }

    public void lerProduto() {
        System.out.println("### LISTA DE PRODUTOS ###");
        List<Produto> produtos = produtoController.readProduto();
        produtos.forEach(produto -> System.out.println(produto.getId() + " - " + produto.getNome() + " - " + produto.getPreco()));
    }

    public void editarProduto() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### EDITAR PRODUTO ###");
            System.out.print("ID do produto para editar: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            System.out.print("Nome do produto: ");
            String nome = scanner.nextLine();
            System.out.print("Descrição do produto: ");
            String descricao = scanner.nextLine();
            System.out.print("Preço do produto: ");
            double preco = scanner.nextDouble();
            scanner.nextLine(); // Limpar buffer

            CategoriaProduto categoriaProduto = selecionarOuCriarCategoria(scanner);

            Produto produto = new Produto(id, nome, descricao, preco, categoriaProduto);
            produtoController.updateProduto(produto);
            System.out.println("Produto atualizado com sucesso!");
        }
    }

    public void deletarProduto() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("### DELETAR PRODUTO ###");
            System.out.print("ID do produto para deletar: ");
            int id = scanner.nextInt();

            produtoController.deleteProduto(id);
            System.out.println("Produto deletado com sucesso!");
        }
    }

    private CategoriaProduto selecionarOuCriarCategoria(Scanner scanner) {
        System.out.print("Categoria do Produto: ");
        String categoriaNome = scanner.next();

        CategoriaProduto categoriaProduto = categoriaProdutoController.findByName(categoriaNome);

        if (categoriaProduto == null) {
            System.out.println("Categoria não encontrada. Deseja criar uma nova categoria? (S/N)");
            if (scanner.next().equalsIgnoreCase("S")) {
                CategoriaProdutoView categoriaView = new CategoriaProdutoView();
                categoriaView.cadastrarCategoria();
                categoriaProduto = categoriaProdutoController.findByName(categoriaNome);
            }
        }
        return categoriaProduto;
    }
}
