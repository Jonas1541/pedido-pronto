package pedidopronto.view;

import java.util.Scanner;

import pedidopronto.controller.CategoriaProdutoController;
import pedidopronto.controller.ProdutoController;
import pedidopronto.model.CategoriaProduto;
import pedidopronto.model.Produto;
import pedidopronto.repository.CategoriaProdutoRepository;
import pedidopronto.repository.ProdutoRepository;

public class ProdutoView {

    public static void main(String[] args) {
        ProdutoView produtoView = new ProdutoView();
        produtoView.cadastrarProduto();
    }

    public void cadastrarProduto() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("### CADASTRO DE PRODUTOS ###");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição do produto: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço do produto: ");
        double preco = scanner.nextDouble();

        CategoriaProduto categoriaProduto = selecionarOuCriarCategoria();

        // Aqui você precisaria chamar métodos para interagir com a camada de modelo e persistência (Model e Repository)
        // Por exemplo, criar um objeto Produto e enviá-lo para o repository

        Produto novoProduto = new Produto(nome, descricao, preco, categoriaProduto);

        try {
            ProdutoController produtoController = new ProdutoController(new ProdutoRepository());
            produtoController.createProduto(novoProduto);
            System.out.println("Produto cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("ERRO DETECTADO>>>>>>>: " + e);
        }
    }

    private CategoriaProduto selecionarOuCriarCategoria() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Categoria do Produto: ");
        String categoriaNome = scanner.next();

        CategoriaProdutoController categoriaController = new CategoriaProdutoController(new CategoriaProdutoRepository());

        CategoriaProduto categoriaProduto = categoriaController.findByName(categoriaNome);

        if (categoriaProduto == null) {
            // Se a categoria não existir, ofereça a opção de criar uma nova categoria
            System.out.println("Categoria não encontrada. Deseja criar uma nova categoria? (S/N)");

            if (scanner.next().equalsIgnoreCase("S")) {
                CategoriaProdutoView categoriaView = new CategoriaProdutoView();
                categoriaView.cadastrarCategoria();
                // Após cadastrar a nova categoria, recupere a instância da categoria criada
                categoriaProduto = categoriaController.findByName(categoriaNome);
            } else {
                // Lógica para lidar com a situação em que o usuário decide não criar uma nova categoria
                // Pode ser uma exceção, uma mensagem de erro, etc.
            }
        }

        return categoriaProduto;
    }
}
