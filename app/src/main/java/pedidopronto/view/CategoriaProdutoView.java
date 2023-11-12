package pedidopronto.view;

import java.util.Scanner;

import pedidopronto.controller.CategoriaProdutoController;
import pedidopronto.model.CategoriaProduto;
import pedidopronto.repository.CategoriaProdutoRepository;

public class CategoriaProdutoView {

    public static void main(String[] args) {
        CategoriaProdutoView categoriaView = new CategoriaProdutoView();
        categoriaView.cadastrarCategoria();
    }

    public void cadastrarCategoria() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("### CADASTRO DE CATEGORIA ###");

        System.out.print("Nome da categoria: ");
        String nomeCategoria = scanner.nextLine();

        // Aqui você precisaria chamar métodos para interagir com a camada de modelo e persistência (Model e Repository)
        // Por exemplo, criar um objeto CategoriaProduto e enviá-lo para o repository

        CategoriaProduto novaCategoria = new CategoriaProduto(2, nomeCategoria);

        try {
            CategoriaProdutoController categoriaController = new CategoriaProdutoController(new CategoriaProdutoRepository());
            categoriaController.createCategoriaProduto(novaCategoria);
            System.out.println("Categoria cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("ERRO DETECTADO>>>>>>>: " + e);
        }
    }
}
